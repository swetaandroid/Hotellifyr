package com.xongolab.hotellifyr.view.activity.intro

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.xongolab.hotellifyr.R
import com.xongolab.hotellifyr.core.CoreActivity
import com.xongolab.hotellifyr.databinding.ActivityIntroBinding
import com.xongolab.hotellifyr.utils.Constants
import com.xongolab.hotellifyr.utils.Pref
import com.xongolab.hotellifyr.utils.Util
import com.xongolab.hotellifyr.utils.makeGone
import com.xongolab.hotellifyr.utils.makeVisible
import com.xongolab.hotellifyr.view.activity.MainActivity
import com.xongolab.hotellifyr.view.activity.auth.LoginActivity
import com.xongolab.hotellifyr.view.adapter.IntroPagerAdapter
import com.xongolab.hotellifyr.viewModel.UserViewModel
import com.xongolab.hotellifyr.viewModel.ViewModelFactory


class IntroActivity : CoreActivity() {
    private lateinit var binding: ActivityIntroBinding
    private lateinit var userViewModel: UserViewModel

    private lateinit var introPagerAdapter: IntroPagerAdapter

    val images =
        intArrayOf(R.drawable.ic_intro_one, R.drawable.ic_intro_two, R.drawable.ic_intro_three)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIntroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setFullScreenMode(window)

        initView()
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.btnNext -> {
                if (binding.introViewPager.currentItem == images.size - 1) {
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                } else {
                    binding.introViewPager.currentItem++
                }
            }

            R.id.btnContinueAsGuest -> {
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("fromContinueAsGuest", "true")
                startActivity(intent)
                finish()
            }
        }
    }

    private fun initView() {
        initViewModel()

        binding.btnContinueAsGuest.makeGone()
        binding.btnNext.setOnClickListener(this)
        binding.btnContinueAsGuest.setOnClickListener(this)

        setUpViewPager()

        makeStyledText(
            binding.tvIntroTitle,
            "onboarding",
            links =  arrayOf(getString(R.string.lbl_bookings))
        )

        getCMSApi()

    }

    private fun initViewModel() {
        userViewModel =
            ViewModelProvider(this, ViewModelFactory(mainRepository))[UserViewModel::class.java]
        observeViewModel()
    }

    private fun observeViewModel() {
        userViewModel.getCMSApiResponse.observe(this) { response ->
            response?.let {
                if (it.payload.isNotEmpty()) {
                    for (item in it.payload) {
                        if (item.templateCode == "TERMS_AND_CONITIONS") {
                            Pref.setStringValue(Constants.TERMS_CONDITION, item.link)
                        } else if (item.templateCode == "PRIVACY_POLICY") {
                            Pref.setStringValue(Constants.PRIVACY_POLICY, item.link)
                        }
                    }
                } else {
                    // Handle the case where payload is empty
                }
            }
        }

    }

    private fun getCMSApi() {
        if (isInternetConnected()) {
            userViewModel.getCMSApi(this, "active")
        } else {
            msgDialog(getString(R.string.check_internet))
        }
    }

    private fun setUpViewPager() {
        introPagerAdapter = IntroPagerAdapter(images)
        binding.introViewPager.adapter = introPagerAdapter
        binding.introViewPager.currentItem = 0
        binding.indicator.isSaveFromParentEnabled = false
        binding.indicator.attachTo(binding.introViewPager)

        binding.introViewPager.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if (position == images.size - 1) {
                    binding.btnNext.text =
                        ContextCompat.getString(this@IntroActivity, R.string.login_or_signup)
                    binding.btnContinueAsGuest.makeVisible()
                } else {
                    binding.btnNext.text =
                        ContextCompat.getString(this@IntroActivity, R.string.next)
                    binding.btnContinueAsGuest.makeGone()
                }

            }
        })
    }
}