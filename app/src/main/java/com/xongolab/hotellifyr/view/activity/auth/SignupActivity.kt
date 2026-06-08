package com.xongolab.hotellifyr.view.activity.auth

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.xongolab.hotellifyr.R
import com.xongolab.hotellifyr.core.CoreActivity
import com.xongolab.hotellifyr.databinding.ActivitySignupBinding
import com.xongolab.hotellifyr.model.DeviceDetails
import com.xongolab.hotellifyr.model.SignUpRequest
import com.xongolab.hotellifyr.utils.Constants
import com.xongolab.hotellifyr.utils.Pref
import com.xongolab.hotellifyr.utils.Util
import com.xongolab.hotellifyr.view.activity.MainActivity
import com.xongolab.hotellifyr.viewModel.UserViewModel
import com.xongolab.hotellifyr.viewModel.ViewModelFactory

class SignupActivity : CoreActivity() {
    private lateinit var binding: ActivitySignupBinding
    private var isAgree = false

    private lateinit var userViewModel: UserViewModel

    private var mobile: String = ""
    private var mobileCountryCode: String = ""
    private var mobileCountryName: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setFullScreenMode(window)

        initView()
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.btnRegister -> {
                if (isValidate()) {
                    signupApi()
                }
            }

            R.id.ivAgree -> {
                isAgree = !isAgree

                binding.ivAgree.setImageResource(if (isAgree) R.drawable.cb_check else R.drawable.cb_uncheck)
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
        }

        binding.ccp.setTypeFace(typeface)

        binding.ccp.setCountryForNameCode(mobileCountryName)
        binding.edtPhoneNumber.text = mobile

        makeLinks(
            binding.tvAgreeTerms,
            "singup",
            Pair(getString(R.string.terms_conditions), View.OnClickListener {
                startActivity(Intent(this@SignupActivity, WebActivity::class.java).apply {
                    putExtra(Constants.TITLE, getString(R.string.terms_conditions))
                    putExtra(Constants.WEB_URL, Pref.getStringValue(Constants.TERMS_CONDITION, ""))
                })
            }),
        )

        binding.ivAgree.setOnClickListener(this)
        binding.btnRegister.setOnClickListener(this)
    }

    private fun initViewModel() {
        userViewModel =
            ViewModelProvider(this, ViewModelFactory(mainRepository))[UserViewModel::class.java]
        observeViewModel()
    }

    private fun observeViewModel() {
        userViewModel.getSignupApiResponse.observe(this) { response ->
            response?.let {
                val payload = it.payload!!

                Pref.setBooleanValue(Pref.PREF_IS_LOGIN, true)
                Pref.setStringValue(Pref.PREF_USER_ID, payload.id)
                Pref.setStringValue(Pref.PREF_FIRST_NAME, payload.firstName)
                Pref.setStringValue(Pref.PREF_LAST_NAME, payload.lastName)
                Pref.setStringValue(Pref.PREF_EMAIL, payload.email)
                Pref.setStringValue(Pref.PREF_COUNTRY_CODE, payload.mobileCountryCode)
                Pref.setStringValue(Pref.PREF_MOBILE_NO, payload.mobile)
                Pref.setStringValue(Pref.PREF_AUTH_TOKEN, payload.token)

                val intent = Intent(this@SignupActivity, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
            }
        }
    }

    @SuppressLint("HardwareIds")
    private fun signupApi() {
        if (isInternetConnected()) {
            val deviceDetail = DeviceDetails()
            deviceDetail.deviceName = Build.MODEL
            deviceDetail.deviceType = "android"
            deviceDetail.deviceToken = Pref.getStringValue(Pref.PREF_DEVICE_TOKEN, "")
            deviceDetail.deviceID =
                Settings.Secure.getString(this.contentResolver, Settings.Secure.ANDROID_ID)
            deviceDetail.appVersion = getAppVersionName()

            val signupRequest = SignUpRequest()
            signupRequest.firstName = Util.getTextValue(binding.edtFirstName)
            signupRequest.lastName = Util.getTextValue(binding.edtLastName)
            signupRequest.mobileCountryCode = binding.ccp.selectedCountryCode
            signupRequest.mobile = Util.getTextValue(binding.edtPhoneNumber)
            signupRequest.email = Util.getTextValue(binding.edtEmailId)
            signupRequest.deviceDetails = deviceDetail

            userViewModel.getSignUpApi(this, signupRequest)
        } else {
            msgDialog(resources.getString(R.string.check_internet))
        }
    }


    private fun isValidate(): Boolean {
        var isError = true
        when {
            Util.isEmptyText(binding.edtFirstName) -> {
                isError = false
                binding.edtFirstName.requestFocus()
                Util.msgDialog(this, getString(R.string.first_name_validation))
            }

            Util.isEmptyText(binding.edtLastName) -> {
                isError = false
                binding.edtLastName.requestFocus()
                Util.msgDialog(this, getString(R.string.last_name_validation))
            }

            Util.isEmptyText(binding.edtEmailId) -> {
                isError = false
                binding.edtEmailId.requestFocus()
                Util.msgDialog(this, getString(R.string.email_validation))
            }

            !Util.isValidEmail(binding.edtEmailId.text.toString().trim()) -> {
                isError = false
                binding.edtEmailId.requestFocus()
                Util.msgDialog(this, getString(R.string.email_invalid))
            }

            Util.isEmptyText(binding.edtPhoneNumber) -> {
                isError = false
                binding.edtPhoneNumber.requestFocus()
                Util.msgDialog(this, getString(R.string.mobile_validation))
            }

            binding.edtPhoneNumber.text.toString().trim().length != 10 -> {
                isError = false
                binding.edtPhoneNumber.requestFocus()
                Util.msgDialog(this, getString(R.string.mobile_invalid))
            }

            !isAgree -> {
                isError = false
                Util.msgDialog(this, getString(R.string.terms_and_condition_validation))
            }
        }
        return isError
    }
}