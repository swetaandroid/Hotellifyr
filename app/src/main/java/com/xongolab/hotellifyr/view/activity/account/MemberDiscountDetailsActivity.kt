package com.xongolab.hotellifyr.view.activity.account

import android.os.Bundle
import android.view.View
import com.google.android.material.tabs.TabLayoutMediator
import com.xongolab.hotellifyr.R
import com.xongolab.hotellifyr.core.CoreActivity
import com.xongolab.hotellifyr.custom.ViewPagerExtensions.addCarouselEffect
import com.xongolab.hotellifyr.databinding.ActivityMemberDiscountDetailsBinding
import com.xongolab.hotellifyr.model.ResortModel
import com.xongolab.hotellifyr.view.adapter.MemberDiscountCarouselSliderAdapter

class MemberDiscountDetailsActivity : CoreActivity() {
    private lateinit var binding: ActivityMemberDiscountDetailsBinding
    private lateinit var sliderAdapter: MemberDiscountCarouselSliderAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMemberDiscountDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setFullScreenMode(window)

        initView()
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.btnBack -> finish()
        }
    }

    private fun initView() {
        binding.toolBar.btnBack.setOnClickListener(this)
        binding.toolBar.toolbarTitle.text = ""

        sliderAdapter =
            MemberDiscountCarouselSliderAdapter(this@MemberDiscountDetailsActivity)

        binding.viewPager.adapter = sliderAdapter
        binding.viewPager.currentItem = 0

        binding.viewPager.addCarouselEffect()

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { _, _ -> }.attach()

        imageList()
    }

    private fun imageList() {
        sliderAdapter.objList = ArrayList()

        var model = ResortModel()
        model.profileImg = R.drawable.ic_newly_opened_hotels
        sliderAdapter.objList.add(model)

        model = ResortModel()
        model.profileImg = R.drawable.ic_newly_opened_hotels
        sliderAdapter.objList.add(model)

        model = ResortModel()
        model.profileImg = R.drawable.ic_newly_opened_hotels
        sliderAdapter.objList.add(model)

        model = ResortModel()
        model.profileImg = R.drawable.ic_newly_opened_hotels
        sliderAdapter.objList.add(model)

        model = ResortModel()
        model.profileImg = R.drawable.ic_newly_opened_hotels
        sliderAdapter.objList.add(model)

        sliderAdapter.addData(sliderAdapter.objList)
    }
}