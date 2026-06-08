package com.xongolab.hotellifyr.view.activity.intro

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import com.xongolab.hotellifyr.core.CoreActivity
import com.xongolab.hotellifyr.databinding.ActivitySplash1Binding
import com.xongolab.hotellifyr.utils.Pref
import com.xongolab.hotellifyr.view.activity.MainActivity

class Splash1Activity : CoreActivity() {
    private lateinit var binding: ActivitySplash1Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplash1Binding.inflate(layoutInflater)
        setContentView(binding.root)

        setFullScreenMode(window)

        initView()
    }

    override fun onClick(v: View?) {
    }

    private fun initView() {
        Handler(Looper.getMainLooper()).postDelayed({
            if (Pref.getBooleanValue(Pref.PREF_IS_LOGIN, false)) {
                startActivity(Intent(this@Splash1Activity, MainActivity::class.java))
            } else {
                startActivity(Intent(this@Splash1Activity, Splash2Activity::class.java))
            }
            finish()
        }, 2000)
    }
}