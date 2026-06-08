package com.xongolab.hotellifyr.view.activity.account

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import com.xongolab.hotellifyr.R
import com.xongolab.hotellifyr.core.CoreActivity
import com.xongolab.hotellifyr.databinding.ActivityConfirmIdentityBinding
import com.xongolab.hotellifyr.utils.makeVisible


class VerificationCodeActivity : CoreActivity() {

    private lateinit var binding: ActivityConfirmIdentityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityConfirmIdentityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btnBack -> finish()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun initView() {
        binding.layoutToolBar.toolbarTitle.text = getString(R.string.verification_code)
        binding.layoutToolBar.btnBack.setOnClickListener(this)

        binding.btnSendCode.text = getString(R.string.verify)
        binding.tvResendCode.makeVisible()
    }
}