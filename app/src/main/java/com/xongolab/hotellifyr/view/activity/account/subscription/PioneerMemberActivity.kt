package com.xongolab.hotellifyr.view.activity.account.subscription

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.xongolab.hotellifyr.R
import com.xongolab.hotellifyr.core.CoreActivity
import com.xongolab.hotellifyr.databinding.ActivityPioneerMemberBinding
import com.xongolab.hotellifyr.model.ResortModel
import com.xongolab.hotellifyr.view.activity.account.SettingActivity
import com.xongolab.hotellifyr.view.activity.home.NewlyOpenedActivity
import com.xongolab.hotellifyr.view.adapter.RewardsCircleHorizontalAdapter
import com.xongolab.hotellifyr.view.adapter.account.NewlyOpenedPioneerMemberAdapter
import com.xongolab.hotellifyr.view.adapter.account.PioneerMemberDiscountAdapter

class PioneerMemberActivity : CoreActivity() {

    private lateinit var binding: ActivityPioneerMemberBinding
    private lateinit var rewardsCircleHorizontalAdapter: RewardsCircleHorizontalAdapter
    private lateinit var newlyOpenedPioneerMemberAdapter: NewlyOpenedPioneerMemberAdapter
    private lateinit var pioneerMemberDiscountAdapter: PioneerMemberDiscountAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPioneerMemberBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setFullScreenMode(window)

        initView()
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btnBack -> {
                finish()
            }

            R.id.cvNewlyOpened -> {
                startActivity(Intent(this, NewlyOpenedActivity::class.java))
            }

            R.id.tvNewlyOpenedSeeAll -> {
                startActivity(Intent(this, NewlyOpenedActivity::class.java))
            }

            R.id.icSetting -> {
                startActivity(Intent(this, SettingActivity::class.java))
            }
        }
    }

    private fun initView() {

        binding.tvNewlyOpenedSeeAll.setOnClickListener(this)
        binding.btnBack.setOnClickListener(this)
        binding.icSetting.setOnClickListener(this)

        rewardsCircleHorizontalAdapter = RewardsCircleHorizontalAdapter(this)
        binding.llRewardCircle.rvRewardsCircle.layoutManager =
            LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        binding.llRewardCircle.rvRewardsCircle.adapter = rewardsCircleHorizontalAdapter

        rewardsCircleHorizontalAdapter.onItemClick = {

        }

        rewardsCircleList()

        // Newly Offer
        newlyOpenedPioneerMemberAdapter = NewlyOpenedPioneerMemberAdapter(this, this)
        binding.rvNewlyOpened.layoutManager =
            LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        binding.rvNewlyOpened.adapter = newlyOpenedPioneerMemberAdapter

        newlyOpenedList()

        // Pioneer Member Discount
        pioneerMemberDiscountAdapter = PioneerMemberDiscountAdapter(this, this)
        binding.rvMemberDiscount.layoutManager =
            LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        binding.rvMemberDiscount.adapter = pioneerMemberDiscountAdapter

        pioneerMemberDiscountList()
    }

    private fun rewardsCircleList() {
        rewardsCircleHorizontalAdapter.objList = ArrayList()

        var model = ResortModel()
        model.profileImg = R.drawable.ic_rewards_circle_1
        rewardsCircleHorizontalAdapter.objList.add(model)

        model = ResortModel()
        model.profileImg = R.drawable.ic_rewards_circle_2
        rewardsCircleHorizontalAdapter.objList.add(model)

        model = ResortModel()
        model.profileImg = R.drawable.ic_rewards_circle_3
        rewardsCircleHorizontalAdapter.objList.add(model)

        model = ResortModel()
        model.profileImg = R.drawable.ic_rewards_circle_4
        rewardsCircleHorizontalAdapter.objList.add(model)

        model = ResortModel()
        model.profileImg = R.drawable.ic_rewards_circle_5
        rewardsCircleHorizontalAdapter.objList.add(model)

        model = ResortModel()
        model.profileImg = R.drawable.ic_rewards_circle_6
        rewardsCircleHorizontalAdapter.objList.add(model)

        rewardsCircleHorizontalAdapter.addData(rewardsCircleHorizontalAdapter.objList)
    }

    private fun newlyOpenedList() {
        newlyOpenedPioneerMemberAdapter.objList = ArrayList()

        var model = ResortModel()
        model.title = "Royal Orchid Beach Resort & Spa, Jaipur"
        model.name = "Jaipur"
        model.profileImg = R.drawable.img_hotel_detail
        newlyOpenedPioneerMemberAdapter.objList.add(model)

        model = ResortModel()
        model.title = "Royal Orchid Beach Resort & Spa, Jaipur"
        model.name = "Delhi"
        model.profileImg = R.drawable.img_hotel_detail
        newlyOpenedPioneerMemberAdapter.objList.add(model)

        model = ResortModel()
        model.title = "Royal Orchid Beach Resort & Spa, Jaipur"
        model.name = "Kerala"
        model.profileImg = R.drawable.img_hotel_detail
        newlyOpenedPioneerMemberAdapter.objList.add(model)

        newlyOpenedPioneerMemberAdapter.addData(newlyOpenedPioneerMemberAdapter.objList)
    }

    private fun pioneerMemberDiscountList() {
        pioneerMemberDiscountAdapter.objList = ArrayList()

        var model = ResortModel()
        model.title = "50%"
        model.name = "Food & Beverages"
        model.profileImg = R.drawable.ic_complimentary
        pioneerMemberDiscountAdapter.objList.add(model)

        model = ResortModel()
        model.title = "30%"
        model.name = "alcoholic beverages"
        model.profileImg = R.drawable.ic_members_event
        pioneerMemberDiscountAdapter.objList.add(model)

        model = ResortModel()
        model.title = "20%"
        model.name = "Hotel Openings"
        model.profileImg = R.drawable.ic_bottle
        pioneerMemberDiscountAdapter.objList.add(model)

        model = ResortModel()
        model.title = "70%"
        model.name = "Hotel Openings"
        model.profileImg = R.drawable.ic_bottle
        pioneerMemberDiscountAdapter.objList.add(model)

        pioneerMemberDiscountAdapter.addData(pioneerMemberDiscountAdapter.objList)
    }
}