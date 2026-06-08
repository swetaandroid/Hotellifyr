package com.xongolab.hotellifyr.view.activity.account.subscription

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import com.xongolab.hotellifyr.R
import com.xongolab.hotellifyr.core.CoreActivity
import com.xongolab.hotellifyr.databinding.ActivitySubscriptionBuyBinding
import com.xongolab.hotellifyr.model.ResortModel
import com.xongolab.hotellifyr.view.adapter.SubscriptionAdapter

class SubscriptionBuyActivity : CoreActivity() {

    private lateinit var binding: ActivitySubscriptionBuyBinding
    private var isSelected = false
    private lateinit var subscriptionAdapter: SubscriptionAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySubscriptionBuyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setFullScreenMode(window)

        initView()
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btnBack -> finish()

            R.id.llPerMonth -> {
                isSelected = !isSelected

                binding.llPerMonth.setBackgroundResource(if (isSelected) R.drawable.dr_gradient_secondary_corner_10 else R.drawable.dr_black_bg_with_light_white_border_10)
                binding.radioMonth.setImageDrawable(ContextCompat.getDrawable(this, if (isSelected) R.drawable.ic_radio_select_white else R.drawable.ic_radio_unselect_white))
            }

            R.id.llPerYear -> {
                isSelected = !isSelected

                binding.llPerYear.setBackgroundResource(if (isSelected) R.drawable.dr_gradient_secondary_corner_10 else R.drawable.dr_black_bg_with_light_white_border_10)
                binding.radioYear.setImageDrawable(ContextCompat.getDrawable(this, if (isSelected) R.drawable.ic_radio_select_white else R.drawable.ic_radio_unselect_white))
            }

            R.id.btnContinue -> {
                isSelected = !isSelected
                if (isSelected) {
                    binding.btnContinue.setBackgroundResource(R.drawable.dr_primary_bg_25)
                    binding.btnContinue.setTextColor(
                        ContextCompat.getColor(
                            this,
                            R.color.colorWhite
                        )
                    )

                    startActivity(Intent(this, PioneerMemberActivity::class.java))

                } else {
                    binding.btnContinue.setBackgroundResource(R.drawable.dr_light_gray_bg_25)
                    binding.btnContinue.setTextColor(
                        ContextCompat.getColor(
                            this,
                            R.color.colorBlack
                        )
                    )
                }
            }

        }

    }

    private fun initView() {
        binding.btnBack.setOnClickListener(this)

        binding.llPerMonth.setOnClickListener(this)
        binding.llPerMonth.setBackgroundResource(R.drawable.dr_black_bg_with_light_white_border_10)

        binding.llPerYear.setOnClickListener(this)
        binding.llPerYear.setBackgroundResource(R.drawable.dr_black_bg_with_light_white_border_10)

        binding.btnContinue.setOnClickListener(this)
        binding.btnContinue.setBackgroundResource(R.drawable.dr_light_gray_bg_25)

        setViewPager()
    }

    private fun setViewPager() {
        val resortList = arrayListOf<ResortModel>().apply {
            add(ResortModel().apply {
                profileImg = R.drawable.ic_complimentary
                title = "1 complimentary night stay"
            })

            add(ResortModel().apply {
                profileImg = R.drawable.ic_tea_coffee
                title = "Free Tea/Coffee daily"
            })

            add(ResortModel().apply {
                profileImg = R.drawable.ic_bottle
                title = "1 bottle of wine during your stay"
            })
        }

        subscriptionAdapter = SubscriptionAdapter(this)
        subscriptionAdapter.addData(resortList)

        binding.viewPager.adapter = subscriptionAdapter
        binding.indicator.attachTo(binding.viewPager)
    }
}