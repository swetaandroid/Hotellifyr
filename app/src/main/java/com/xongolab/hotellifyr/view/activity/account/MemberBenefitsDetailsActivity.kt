package com.xongolab.hotellifyr.view.activity.account

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.google.android.material.tabs.TabLayoutMediator
import com.xongolab.hotellifyr.R
import com.xongolab.hotellifyr.core.CoreActivity
import com.xongolab.hotellifyr.custom.ViewPagerExtensions.addCarouselEffect
import com.xongolab.hotellifyr.databinding.ActivityMemberBenefitsDetailsBinding
import com.xongolab.hotellifyr.model.ResortModel
import com.xongolab.hotellifyr.view.activity.account.subscription.SubscriptionPlanActivity
import com.xongolab.hotellifyr.view.adapter.MemberBenefitsCarouselSliderAdapter
import com.xongolab.hotellifyr.view.adapter.SubscriptionAdapter

class MemberBenefitsDetailsActivity : CoreActivity() {
    private lateinit var binding: ActivityMemberBenefitsDetailsBinding
    private lateinit var sliderAdapter: MemberBenefitsCarouselSliderAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMemberBenefitsDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setFullScreenMode(window)

        initView()
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.btnBack -> finish()
            R.id.btnSubscribeNow -> startActivity(Intent(this, SubscriptionPlanActivity::class.java))
        }
    }

    private fun initView() {
        binding.toolBar.btnBack.setOnClickListener(this)
        binding.toolBar.toolbarTitle.text = ""

        binding.btnSubscribeNow.setOnClickListener(this)

        sliderAdapter =
            MemberBenefitsCarouselSliderAdapter(this@MemberBenefitsDetailsActivity)

        binding.viewPager.adapter = sliderAdapter
        binding.viewPager.currentItem = 0

        binding.viewPager.addCarouselEffect()

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { _, _ -> }.attach()

        imageList()
    }

    private fun imageList() {
        sliderAdapter.objList = ArrayList()

        var model = ResortModel()
        model.profileImg = R.drawable.ic_benefits_1
        sliderAdapter.objList.add(model)

        model = ResortModel()
        model.profileImg = R.drawable.ic_benefits_2
        sliderAdapter.objList.add(model)

        model = ResortModel()
        model.profileImg = R.drawable.ic_benefits_3
        sliderAdapter.objList.add(model)

        model = ResortModel()
        model.profileImg = R.drawable.ic_benefits_4
        sliderAdapter.objList.add(model)

        model = ResortModel()
        model.profileImg = R.drawable.ic_benefits_5
        sliderAdapter.objList.add(model)

        sliderAdapter.addData(sliderAdapter.objList)
    }

}