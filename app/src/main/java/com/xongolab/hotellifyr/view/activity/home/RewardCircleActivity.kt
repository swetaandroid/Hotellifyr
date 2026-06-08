package com.xongolab.hotellifyr.view.activity.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.xongolab.hotellifyr.R
import com.xongolab.hotellifyr.core.CoreActivity
import com.xongolab.hotellifyr.databinding.ActivityRewardCircleBinding
import com.xongolab.hotellifyr.model.ResortModel
import com.xongolab.hotellifyr.view.adapter.ImageAdapter
import com.xongolab.hotellifyr.view.adapter.RewardsCircleVerticalAdapter

class RewardCircleActivity : CoreActivity() {
    private lateinit var binding: ActivityRewardCircleBinding
    private lateinit var imageAdapter: ImageAdapter
    private lateinit var rewardsCircleVerticalAdapter: RewardsCircleVerticalAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRewardCircleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setFullScreenMode(window)

        initView()
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.btnBack -> finish()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun initView() {
        binding.toolbar.toolbarTitle.text = getString(R.string.rewards_circle)
        binding.toolbar.btnBack.setOnClickListener(this)

        imageAdapter = ImageAdapter(this)

        binding.viewPager.adapter = imageAdapter
        binding.viewPager.currentItem = 0
        binding.indicator.isSaveFromParentEnabled = false
        binding.indicator.attachTo(binding.viewPager)

        imageList()

        val images = arrayOf(
            R.drawable.ic_amazon, R.drawable.ic_flipkart, R.drawable.ic_bluestone,
            R.drawable.ic_body_shop, R.drawable.ic_pvr, R.drawable.ic_croma,
            R.drawable.ic_westside, R.drawable.ic_nykaa, R.drawable.ic_bata,
            R.drawable.ic_urban, R.drawable.ic_puma, R.drawable.ic_fastrack,
            R.drawable.ic_jockey, R.drawable.ic_myntra, R.drawable.ic_tata,
        )

        rewardsCircleVerticalAdapter = RewardsCircleVerticalAdapter(this, images)
        binding.rvRewardsCircle.layoutManager = GridLayoutManager(this, 3)
        binding.rvRewardsCircle.adapter = rewardsCircleVerticalAdapter
    }

    private fun imageList() {
        val imageList = arrayListOf<Int>().apply {
            add(R.drawable.ic_reward_offer)
            add(R.drawable.ic_reward_offer)
            add(R.drawable.ic_reward_offer)
        }

        imageAdapter.addDrawableData(imageList)
    }
}