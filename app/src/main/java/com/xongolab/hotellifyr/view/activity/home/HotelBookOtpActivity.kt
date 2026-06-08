package com.xongolab.hotellifyr.view.activity.home

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import com.xongolab.hotellifyr.R
import com.xongolab.hotellifyr.core.CoreActivity
import com.xongolab.hotellifyr.databinding.ActivityHotelBookOtpBinding
import com.xongolab.hotellifyr.model.BookingHotelRequest
import com.xongolab.hotellifyr.model.VerifyBookOTPRequest
import com.xongolab.hotellifyr.utils.Constants
import com.xongolab.hotellifyr.utils.Util
import com.xongolab.hotellifyr.viewModel.HotelViewModel
import com.xongolab.hotellifyr.viewModel.UserViewModel
import com.xongolab.hotellifyr.viewModel.ViewModelFactory

class HotelBookOtpActivity : CoreActivity() {
    private lateinit var binding: ActivityHotelBookOtpBinding

    private lateinit var userViewModel: UserViewModel
    private lateinit var hotelViewModel: HotelViewModel
    private var resendCodeTimer: CountDownTimer? = null

    private var mobile: String = ""
    private var mobileCountryCode: String = ""

    private var _otpID: String = ""
    private var _otp: String = ""

    var bookingHotelRequest = BookingHotelRequest()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHotelBookOtpBinding.inflate(layoutInflater)
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
            bookingHotelRequest = intent.getSerializableExtra("bookingHotelRequest") as BookingHotelRequest

            println("BookingHotelRequest...." + Gson().toJson(bookingHotelRequest))

            mobile = intent.getStringExtra(Constants.MOBILE).toString()
            mobileCountryCode = intent.getStringExtra(Constants.MOBILE_COUNTRY_CODE).toString()
            _otpID = intent.getStringExtra(Constants.OTP_ID).toString()
            _otp = intent.getStringExtra(Constants.OTP).toString()
        }

        binding.otpView.typeface = typeface
        binding.otpView.setText(_otp)

        dataSet()
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

    private fun dataSet() {
        binding.apply {
            tvHotelName.text = bookingHotelRequest.hotel
            tvDateRange.text = Util.formatTimestamp(bookingHotelRequest.checkIn, "yyyy-MM-dd", "EEE, dd MMM") + " - " + Util.formatTimestamp(bookingHotelRequest.checkOut, "yyyy-MM-dd", "EEE, dd MMM")
        }
    }

    private fun initViewModel() {
        userViewModel = ViewModelProvider(this, ViewModelFactory(mainRepository))[UserViewModel::class.java]
        hotelViewModel = ViewModelProvider(this, ViewModelFactory(mainRepository))[HotelViewModel::class.java]
        observeViewModel()
    }

    private fun observeViewModel() {
        userViewModel.getSendBookOTPApiResponse.observe(this) { response ->
            response?.let {
                _otpID = it.payload?.otpID!!
                _otp = it.payload?.otp.toString()
                binding.otpView.setText(_otp)
                startResendCodeTimer()
            }
        }

        userViewModel.getVerifyBookOTPApiResponse.observe(this) { response ->
            response?.let {
                callBookingHotelApi()
            }
        }

        hotelViewModel.callBookingHotelApiResponse.observe(this) { response ->
            response?.let {
                startActivity(
                    Intent(this@HotelBookOtpActivity, BookingConfirmedActivity::class.java)
                        .putExtra("bookingId", it.payload!!.bookingId)
                        .putExtra("hotelName", bookingHotelRequest.hotel)
                        .putExtra("checkIn", bookingHotelRequest.checkIn)
                        .putExtra("checkOut", bookingHotelRequest.checkOut)
                        .putExtra("numberOfDays", bookingHotelRequest.numberOfDays)
                        .putExtra("adults", bookingHotelRequest.bookRoom.firstOrNull()?.adults)
                        .putExtra("children", bookingHotelRequest.bookRoom.firstOrNull()?.children)
                )
                finishAffinity()
            }
        }
    }

    private fun callBookingHotelApi() {
        if (isInternetConnected()) {
            hotelViewModel.callBookingHotelApi(this, bookingHotelRequest)
        } else {
            msgDialog(resources.getString(R.string.check_internet))
        }
    }

    @SuppressLint("HardwareIds")
    private fun sendOTPApi() {
        if (isInternetConnected()) {
            val countryCode =
                Util.createPartFromString(mobileCountryCode, "MobileCountryCode")
            val mobile =
                Util.createPartFromString(mobile, "Mobile")
            val type = Util.createPartFromString("EN", "lang_code")

            userViewModel.getSendBookOTPApi(this, mobile, countryCode, type)
        } else {
            msgDialog(resources.getString(R.string.check_internet))
        }
    }

    @SuppressLint("HardwareIds")
    private fun verifyOTPApi() {
        if (isInternetConnected()) {
            val verifyOTPRequest = VerifyBookOTPRequest()
            verifyOTPRequest.otpID = _otpID
            verifyOTPRequest.otp = Util.getTextValue(binding.otpView)

            userViewModel.getVerifyBookOTPApi(this, verifyOTPRequest)
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
                    ForegroundColorSpan(ContextCompat.getColor(this@HotelBookOtpActivity, R.color.colorSecondary)), // Change this to your desired color
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