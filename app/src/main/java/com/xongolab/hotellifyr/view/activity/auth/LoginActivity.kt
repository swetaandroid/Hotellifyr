package com.xongolab.hotellifyr.view.activity.auth

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.xongolab.hotellifyr.R
import com.xongolab.hotellifyr.core.CoreActivity
import com.xongolab.hotellifyr.databinding.ActivityLoginBinding
import com.xongolab.hotellifyr.utils.Constants
import com.xongolab.hotellifyr.utils.Util
import com.xongolab.hotellifyr.utils.Util.msgDialog
import com.xongolab.hotellifyr.viewModel.UserViewModel
import com.xongolab.hotellifyr.viewModel.ViewModelFactory

class LoginActivity : CoreActivity() {
    private lateinit var binding: ActivityLoginBinding

    private lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setFullScreenMode(window)

        initView()
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.btnRequestOTP -> {
                if (isValidate()) {
                    sendOTPApi()
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

        binding.ccp.setTypeFace(typeface)
        binding.btnRequestOTP.setOnClickListener(this)
    }

    private fun initViewModel() {
        userViewModel =
            ViewModelProvider(this, ViewModelFactory(mainRepository))[UserViewModel::class.java]
        observeViewModel()
    }

    private fun observeViewModel() {
        userViewModel.getSendOTPApiResponse.observe(this) { response ->
            response?.let {

                val intent = Intent(this@LoginActivity, OTPActivity::class.java)
                intent.putExtra(Constants.MOBILE, Util.getTextValue(binding.edtPhoneNumber))
                intent.putExtra(Constants.MOBILE_COUNTRY_CODE, binding.ccp.selectedCountryCode)
                intent.putExtra(Constants.MOBILE_COUNTRY_NAME, binding.ccp.selectedCountryNameCode)
                intent.putExtra(Constants.OTP_ID, it.payload?.otpID)
                intent.putExtra(Constants.OTP, it.payload?.otp.toString())
                startActivity(intent)
            }
        }
    }

    @SuppressLint("HardwareIds")
    private fun sendOTPApi() {
        if (isInternetConnected()) {
            val countryCode =
                Util.createPartFromString(binding.ccp.selectedCountryCode, "MobileCountryCode")
            val mobile =
                Util.createPartFromString(Util.getTextValue(binding.edtPhoneNumber), "Mobile")
            val type = Util.createPartFromString("login", "type")

            userViewModel.getSendOTPApi(this, mobile, countryCode, type)
        } else {
            msgDialog(resources.getString(R.string.check_internet))
        }
    }

    private fun isValidate(): Boolean {
        var isError = true
        when {
            Util.isEmptyText(binding.edtPhoneNumber) -> {
                isError = false
                binding.edtPhoneNumber.requestFocus()
                msgDialog(this, getString(R.string.mobile_validation))
            }

            binding.edtPhoneNumber.text.toString().trim().length != 10 -> {
                isError = false
                binding.edtPhoneNumber.requestFocus()
                msgDialog(this, getString(R.string.mobile_invalid))
            }
        }
        return isError
    }
}