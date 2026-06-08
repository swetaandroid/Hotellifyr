package com.xongolab.hotellifyr.view.activity.home

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.xongolab.hotellifyr.R
import com.xongolab.hotellifyr.core.CoreActivity
import com.xongolab.hotellifyr.databinding.ActivityConfirmPayBinding
import com.xongolab.hotellifyr.view.activity.MainActivity

class ConfirmPayActivity : CoreActivity() {
    private lateinit var binding: ActivityConfirmPayBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConfirmPayBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setFullScreenMode(window)

        initView()
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.btnBack -> finish()
            R.id.btnConfirmPay ->  startActivity(Intent(this, MainActivity::class.java))
        }
    }

    @SuppressLint("SetTextI18n")
    private fun initView() {
        binding.toolbar.toolbarTitle.text = getString(R.string.confirm_pay)
        binding.toolbar.btnBack.setOnClickListener(this)
        binding.btnConfirmPay.setOnClickListener(this)
    }
}