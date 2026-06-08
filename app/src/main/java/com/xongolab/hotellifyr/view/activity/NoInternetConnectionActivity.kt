package com.xongolab.hotellifyr.view.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.xongolab.hotellifyr.R
import com.xongolab.hotellifyr.core.CoreActivity
import com.xongolab.hotellifyr.databinding.ActivityNoInternetConnectionBinding
import com.xongolab.hotellifyr.utils.Pref
import com.xongolab.hotellifyr.utils.Util
import com.xongolab.hotellifyr.view.activity.auth.LoginActivity

class NoInternetConnectionActivity : CoreActivity(), View.OnClickListener {

    private lateinit var binding: ActivityNoInternetConnectionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNoInternetConnectionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
    }

    private fun initView() {


        binding.btnTryAgain.setOnClickListener(this)

    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.btnTryAgain -> {
                if (isInternetConnected()) {
                    if (Pref.getBooleanValue(Pref.PREF_IS_LOGIN, false)) {
                        startActivity(Intent(this@NoInternetConnectionActivity, MainActivity::class.java))
                        finishAffinity()
                    } else {
                        startActivity(Intent(this@NoInternetConnectionActivity, LoginActivity::class.java))
                        finishAffinity()
                    }
                } else {
                    Toast.makeText(this@NoInternetConnectionActivity, "Check your internet connection", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

}