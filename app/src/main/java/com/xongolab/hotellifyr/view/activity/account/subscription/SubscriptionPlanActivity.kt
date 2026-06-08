package com.xongolab.hotellifyr.view.activity.account.subscription

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.xongolab.hotellifyr.R
import com.xongolab.hotellifyr.core.CoreActivity
import com.xongolab.hotellifyr.databinding.ActivitySubscriptionPlanBinding

class SubscriptionPlanActivity : CoreActivity() {

    private lateinit var binding: ActivitySubscriptionPlanBinding
    private var isSelected = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySubscriptionPlanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setFullScreenMode(window)

        initView()
    }

    override fun onClick(v: View) {
        binding.apply {
            when (v.id) {
                R.id.btnBack -> finish()

                R.id.rlPerMonth -> {
                    isSelected = !isSelected

                    rlPerMonth.setBackgroundResource( if (isSelected) R.drawable.dr_gradient_secondary_corner_25 else R.drawable.dr_black_bg_with_light_white_border_25)
                }

                R.id.rlPerYear -> {
                    isSelected = !isSelected

                    rlPerYear.setBackgroundResource( if (isSelected) R.drawable.dr_gradient_secondary_corner_25 else R.drawable.dr_black_bg_with_light_white_border_25)
                }

                R.id.btnContinue -> {
                    isSelected = !isSelected

                    btnContinue.setBackgroundResource( if (isSelected) R.drawable.dr_primary_bg_25 else R.drawable.dr_light_gray_bg_25)

                    if (isSelected) {
                        startActivity(Intent(this@SubscriptionPlanActivity, SubscriptionBuyActivity::class.java))
                    }
                }
            }
        }

    }

    private fun initView() {
        binding.btnBack.setOnClickListener(this)

        binding.rlPerMonth.setOnClickListener(this)
        binding.rlPerMonth.setBackgroundResource(R.drawable.dr_black_bg_with_light_white_border_25)

        binding.rlPerYear.setOnClickListener(this)
        binding.rlPerYear.setBackgroundResource(R.drawable.dr_black_bg_with_light_white_border_25)

        binding.btnContinue.setOnClickListener(this)
        binding.btnContinue.setBackgroundResource(R.drawable.dr_light_gray_bg_25)
    }
}