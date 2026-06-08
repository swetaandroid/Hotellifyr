package com.xongolab.hotellifyr.view.activity.intro

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import com.xongolab.hotellifyr.core.CoreActivity
import com.xongolab.hotellifyr.databinding.ActivitySplash2Binding

class Splash2Activity : CoreActivity() {
    private lateinit var binding: ActivitySplash2Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplash2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        setFullScreenMode(window)

        initView()
    }

    override fun onClick(v: View?) {
    }

    private fun initView() {
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this@Splash2Activity, IntroActivity::class.java))
            finish()
        }, 2000)
    }
}