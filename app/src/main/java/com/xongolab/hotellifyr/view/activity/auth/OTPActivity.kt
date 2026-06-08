package com.xongolab.hotellifyr.view.activity.auth

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.provider.Settings
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.xongolab.hotellifyr.R
import com.xongolab.hotellifyr.core.CoreActivity
import com.xongolab.hotellifyr.databinding.ActivityOtpBinding
import com.xongolab.hotellifyr.model.DeviceDetails
import com.xongolab.hotellifyr.model.VerifyOTPRequest
import com.xongolab.hotellifyr.utils.Constants
import com.xongolab.hotellifyr.utils.Pref
import com.xongolab.hotellifyr.utils.Util
import com.xongolab.hotellifyr.view.activity.MainActivity
import com.xongolab.hotellifyr.viewModel.UserViewModel
import com.xongolab.hotellifyr.viewModel.ViewModelFactory

class OTPActivity : CoreActivity() {
    private lateinit var binding: ActivityOtpBinding

    private lateinit var userViewModel: UserViewModel

    private var resendCodeTimer: CountDownTimer? = null

    private var mobile: String = ""
    private var mobileCountryCode: String = ""
    private var mobileCountryName: String = ""

    private var _otpID: String = ""
    private var _otp: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOtpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setFullScreenMode(window)

        initView()
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.btnContinue -> {
                if (isValidate()) {
                    verifyOTPApi()
                }
            }
        }
    }

    private fun initView() {
        val typeface = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            resources.getFont(R.font.product_sans_regular)
        } else {
            Typeface.createFromAsset(assets, "font/product_sans_regular.ttf")
        }

        initViewModel()

        if (intent != null) {
            mobile = intent.getStringExtra(Constants.MOBILE).toString()
            mobileCountryCode = intent.getStringExtra(Constants.MOBILE_COUNTRY_CODE).toString()
            mobileCountryName = intent.getStringExtra(Constants.MOBILE_COUNTRY_NAME).toString()
            _otpID = intent.getStringExtra(Constants.OTP_ID).toString()
            _otp = intent.getStringExtra(Constants.OTP).toString()
        }

        binding.otpView.typeface = typeface
        binding.otpView.setText(_otp)

        startResendCodeTimer()

        binding.btnContinue.setOnClickListener(this)

        makeLinks(
            binding.tvResendOtp,
            "otp",
            Pair(getString(R.string.resend_it), View.OnClickListener {
                sendOTPApi()
            }),
        )
    }

    private fun initViewModel() {
        userViewModel =
            ViewModelProvider(this, ViewModelFactory(mainRepository))[UserViewModel::class.java]
        observeViewModel()
    }

    private fun observeViewModel() {
        userViewModel.getSendOTPApiResponse.observe(this) { response ->
            response?.let {

                _otpID = it.payload?.otpID!!
                _otp = it.payload?.otp.toString()

               binding.otpView.setText(_otp)

                startResendCodeTimer()
            }
        }

        userViewModel.getVerifyOTPApiResponse.observe(this) { response ->
            response?.let {

                if (it.payload!!.isRegistered) {
                    val payload = it.payload!!.userInfo

                    Pref.setBooleanValue(Pref.PREF_IS_LOGIN, true)
                    Pref.setStringValue(Pref.PREF_USER_ID, payload.id)
                    Pref.setStringValue(Pref.PREF_FIRST_NAME, payload.firstName)
                    Pref.setStringValue(Pref.PREF_LAST_NAME, payload.lastName)
                    Pref.setStringValue(Pref.PREF_EMAIL, payload.email)
                    Pref.setStringValue(Pref.PREF_COUNTRY_CODE, payload.mobileCountryCode)
                    Pref.setStringValue(Pref.PREF_MOBILE_NO, payload.mobile)
                    Pref.setStringValue(Pref.PREF_AUTH_TOKEN, it.payload!!.token)
                    Pref.setStringValue(Pref.PREF_PROFILE_PIC, payload.avatar ?: "")

                    val intent = Intent(this@OTPActivity, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                    finish()
                } else {
                    val intent = Intent(
                        this@OTPActivity,
                        SignupActivity::class.java
                    )
                    intent.putExtra(Constants.MOBILE, mobile)
                    intent.putExtra(Constants.MOBILE_COUNTRY_CODE, mobileCountryCode)
                    intent.putExtra(Constants.MOBILE_COUNTRY_NAME, mobileCountryName)
                    startActivity(intent)
                }
            }
        }
    }

    @SuppressLint("HardwareIds")
    private fun sendOTPApi() {
        if (isInternetConnected()) {
            val countryCode =
                Util.createPartFromString(mobileCountryCode, "MobileCountryCode")
            val mobile =
                Util.createPartFromString(mobile, "Mobile")
            val type = Util.createPartFromString("login", "type")

            userViewModel.getSendOTPApi(this, mobile, countryCode, type)
        } else {
            msgDialog(resources.getString(R.string.check_internet))
        }
    }

    @SuppressLint("HardwareIds")
    private fun verifyOTPApi() {
        if (isInternetConnected()) {
            val deviceDetails = DeviceDetails()
            deviceDetails.deviceName = Build.MODEL
            deviceDetails.deviceType = "android"
            deviceDetails.deviceToken = Pref.getStringValue(Pref.PREF_DEVICE_TOKEN, "")
            deviceDetails.deviceID =
                Settings.Secure.getString(this.contentResolver, Settings.Secure.ANDROID_ID)
            deviceDetails.appVersion = getAppVersionName()

            val verifyOTPRequest = VerifyOTPRequest()
            verifyOTPRequest.otpID = _otpID
            verifyOTPRequest.otp = Util.getTextValue(binding.otpView)

            verifyOTPRequest.deviceDetails = deviceDetails


            userViewModel.getVerifyOTPApi(this, verifyOTPRequest)
        } else {
            msgDialog(resources.getString(R.string.check_internet))
        }
    }

    private fun startResendCodeTimer() {
        //  binding.tvResendTime.visibility = View.VISIBLE

        resendCodeTimer?.cancel()

        resendCodeTimer = object : CountDownTimer(30000, 1000) {
            @SuppressLint("SetTextI18n")
            override fun onTick(millisUntilFinished: Long) {
                binding.tvResendOtp.isEnabled = false

                val seconds = (millisUntilFinished / 1000) % 60
                val minutes = (millisUntilFinished / 1000) / 60
                val countdownText = "%02d:%02d".format(minutes, seconds)

                val fullText = "Didn’t get the code? $countdownText"
                val spannable = SpannableString(fullText)

                // Set color only to the countdown part
                val startIndex = fullText.indexOf(countdownText)
                val endIndex = startIndex + countdownText.length
                spannable.setSpan(
                    ForegroundColorSpan(ContextCompat.getColor(this@OTPActivity, R.color.colorSecondary)), // Change this to your desired color
                    startIndex,
                    endIndex,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )

                binding.tvResendOtp.text = spannable
            }

            @SuppressLint("SetTextI18n")
            override fun onFinish() {
                binding.tvResendOtp.isEnabled = true

                binding.tvResendOtp.text = "Didn't get the code? Resend it"

                makeLinks(
                    binding.tvResendOtp,
                    "otp",
                    Pair("Resend it", View.OnClickListener {
                        sendOTPApi()
                    }),
                )
            }
        }.start()
    }

    private fun isValidate(): Boolean {
        var isError = true
        when {
            Util.isEmptyText(binding.otpView) -> {
                isError = false
                binding.otpView.requestFocus()
                Util.msgDialog(this, getString(R.string.otp_validation))
            }

            binding.otpView.text.toString().trim().length != 6 -> {
                isError = false
                binding.otpView.requestFocus()
                Util.msgDialog(this, getString(R.string.otp_invalid))
            }
        }
        return isError
    }
}