package com.xongolab.hotellifyr.view.activity.account

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.xongolab.hotellifyr.R
import com.xongolab.hotellifyr.core.CoreActivity
import com.xongolab.hotellifyr.databinding.ActivityAccountBinding
import com.xongolab.hotellifyr.model.MemberBenefits
import com.xongolab.hotellifyr.model.MemberDiscount
import com.xongolab.hotellifyr.view.adapter.MemberBenefitsAdapter
import com.xongolab.hotellifyr.view.adapter.MemberDiscountAdapter

class AccountActivity : CoreActivity() {
    private lateinit var binding: ActivityAccountBinding
    private lateinit var discountAdapter: MemberDiscountAdapter
    private lateinit var benefitsAdapter: MemberBenefitsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setFullScreenMode(window)

        initView()
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.btnBack -> finish()
            R.id.icSetting -> startActivity(Intent(this, SettingActivity::class.java))
        }
    }

    private fun initView() {
        binding.btnBack.setOnClickListener(this)
        binding.icSetting.setOnClickListener(this)

        discountAdapter = MemberDiscountAdapter(this)
        binding.rvDiscount.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        binding.rvDiscount.adapter = discountAdapter

        discountAdapter.onItemClick = {
            startActivity(Intent(this, MemberDiscountDetailsActivity::class.java))
        }

        memberDiscountList()

        benefitsAdapter = MemberBenefitsAdapter(this)
        binding.rvBenefits.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        binding.rvBenefits.adapter = benefitsAdapter

        benefitsAdapter.onItemClick = {
            startActivity(Intent(this, MemberBenefitsDetailsActivity::class.java))
        }

        memberBenefitsList()
    }

    private fun memberDiscountList() {
        discountAdapter.objList = ArrayList()

        discountAdapter.objList.add(
            MemberDiscount(
                R.drawable.dr_gradient_member_discount_1,
                R.drawable.ic_food_discount,
                "50% Discount",
                "on Food & Beverages"
            )
        )
        discountAdapter.objList.add(
            MemberDiscount(
                R.drawable.dr_gradient_member_discount_2,
                R.drawable.ic_alcoholic_discount,
                "30% Discount",
                "on alcoholic beverages"
            )
        )
        discountAdapter.objList.add(
            MemberDiscount(
                R.drawable.dr_gradient_member_discount_3,
                R.drawable.ic_openings_discount,
                "50% Off",
                "on Stay at New Hotel Openings"
            )
        )
        discountAdapter.objList.add(
            MemberDiscount(
                R.drawable.dr_gradient_member_discount_4,
                R.drawable.ic_rooms_discount,
                "50% Discount",
                "on Rooms (Best Available Rate)"
            )
        )

        discountAdapter.addData(discountAdapter.objList)
    }

    private fun memberBenefitsList() {
        benefitsAdapter.objList = ArrayList()

        benefitsAdapter.objList.add(
            MemberBenefits(
                R.drawable.ic_benefits_1,
                "1 complimentary night stay",
            )
        )
        benefitsAdapter.objList.add(
            MemberBenefits(
                R.drawable.ic_benefits_2,
                "Free Tea/Coffee daily",
            )
        )
        benefitsAdapter.objList.add(
            MemberBenefits(
                R.drawable.ic_benefits_3,
                "Members-only events Pass",
            )
        )
        benefitsAdapter.objList.add(
            MemberBenefits(
                R.drawable.ic_benefits_4,
                "Priority access to events",
            )
        )
        benefitsAdapter.objList.add(
            MemberBenefits(
                R.drawable.ic_benefits_5,
                "1 bottle of wine during your stay",
            )
        )

        benefitsAdapter.addData(benefitsAdapter.objList)
    }


}