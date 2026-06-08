package com.xongolab.hotellifyr.view.activity.account.subscription

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import com.xongolab.hotellifyr.R
import com.xongolab.hotellifyr.core.CoreActivity
import com.xongolab.hotellifyr.databinding.ActivityMemberDiscountBinding

class MemberDiscountActivity : CoreActivity() {
    private lateinit var binding: ActivityMemberDiscountBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMemberDiscountBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setFullScreenMode(window)

        initView()
    }

    override fun onClick(v: View) {
        when (v.id) {

            R.id.btnBack -> {
                finish()
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun initView() {
        binding.btnBack.setOnClickListener(this)

        val selectedPosition = intent.getIntExtra("selected_position", -1)

        val selectedGradient = when (selectedPosition) {
            0 -> R.drawable.img_member_1_bg
            1 -> R.drawable.img_member_2_bg
            2 -> R.drawable.img_member_3_bg
            3 -> R.drawable.img_member_4_bg
            else -> R.drawable.img_member_1_bg
        }

        binding.apply {
            llTopLayout.background = ContextCompat.getDrawable(this@MemberDiscountActivity, selectedGradient)

            when (selectedPosition) {
                0 -> {
                    tvDiscount.text = "50% Discount on"
                    imgDiscount.setImageResource(R.drawable.ic_member_discount_1)
                }
                1 -> {
                    tvDiscount.text = "30% Discount on"
                    imgDiscount.setImageResource(R.drawable.ic_priority_access)
                }
                2 -> {
                    tvDiscount.text = "50% OFF on"
                    imgDiscount.setImageResource(R.drawable.ic_members_event)
                }
                3 -> {
                    tvDiscount.text = "50% OFF on"
                    imgDiscount.setImageResource(R.drawable.ic_complimentary)
                }
                else -> {
                    tvDiscount.text = "50% Discount on"
                    imgDiscount.setImageResource(R.drawable.ic_member_discount_1)
                }
            }
        }

    }
}