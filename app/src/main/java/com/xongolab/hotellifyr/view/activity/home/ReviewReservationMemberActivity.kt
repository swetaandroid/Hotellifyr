package com.xongolab.hotellifyr.view.activity.home

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.xongolab.hotellifyr.R
import com.xongolab.hotellifyr.core.CoreActivity
import com.xongolab.hotellifyr.custom.SpaceItemDecoration
import com.xongolab.hotellifyr.databinding.ActivityReviewReservationMemberBinding
import com.xongolab.hotellifyr.model.PaymentMethod
import com.xongolab.hotellifyr.model.ResortModel
import com.xongolab.hotellifyr.utils.makeGone
import com.xongolab.hotellifyr.utils.makeVisible
import com.xongolab.hotellifyr.view.adapter.AddOnsExperienceAdapter
import com.xongolab.hotellifyr.view.adapter.PaymentMethodAdapter

class ReviewReservationMemberActivity : CoreActivity() {
    private lateinit var binding: ActivityReviewReservationMemberBinding
    private lateinit var addOnsExperienceAdapter: AddOnsExperienceAdapter
    private lateinit var paymentMethodAdapter: PaymentMethodAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReviewReservationMemberBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setFullScreenMode(window)

        initView()
    }

    override fun onClick(view: View) {
        binding.apply {
            when (view.id) {
                R.id.btnBack -> finish()

                R.id.llAddOnsExperience -> {
                    if (expandAddOnsExperience.visibility == View.VISIBLE) {
                        expandAddOnsExperience.makeGone()
                        ivExperienceArrow.rotation = 0F
                    } else {
                        expandAddOnsExperience.makeVisible()
                        ivExperienceArrow.rotation = 180F
                    }
                }

                R.id.llRedeemPoints -> {
                    if (expandRedeemPoints.visibility == View.VISIBLE) {
                        expandRedeemPoints.makeGone()
                        ivRedeemPointsArrow.rotation = 0F
                    } else {
                        expandRedeemPoints.makeVisible()
                        ivRedeemPointsArrow.rotation = 180F
                    }
                }

                R.id.llPaymentOption -> {
                    if (expandPaymentOption.visibility == View.VISIBLE) {
                        expandPaymentOption.makeGone()
                        ivPaymentArrow.rotation = 0F
                    } else {
                        expandPaymentOption.makeVisible()
                        ivPaymentArrow.rotation = 180F
                    }
                }

                R.id.llPromoCode -> {
                    if (expandPromoCode.visibility == View.VISIBLE) {
                        expandPromoCode.makeGone()
                        ivPromoCodeArrow.rotation = 0F
                    } else {
                        expandPromoCode.makeVisible()
                        ivPaymentArrow.rotation = 180F
                    }
                }

                R.id.tvAddCard -> {
                    startActivity(Intent(this@ReviewReservationMemberActivity, AddCardActivity::class.java))
                }

                R.id.ivDeleteExperience -> {
                    rvAddOnsExperience.makeVisible()
                    llOnsExperienceAdded.makeGone()
                }

                R.id.btnBookNow -> {
                    startActivity(Intent(this@ReviewReservationMemberActivity, ConfirmPayActivity::class.java))
                }
            }
        }
    }


    @SuppressLint("SetTextI18n", "NotifyDataSetChanged")
    private fun initView() {
        binding.toolbarTitle.text = getString(R.string.review_reservation)
        binding.toolbarDesc.text = "Royal Orchid Beach Resort & Spa, Goa"
        binding.btnBack.setOnClickListener(this)
        binding.llAddOnsExperience.setOnClickListener(this)
        binding.llRedeemPoints.setOnClickListener(this)
        binding.llPaymentOption.setOnClickListener(this)
        binding.llPromoCode.setOnClickListener(this)
        binding.tvAddCard.setOnClickListener(this)
        binding.ivDeleteExperience.setOnClickListener(this)
        binding.btnBookNow.setOnClickListener(this)

        addOnsExperienceAdapter = AddOnsExperienceAdapter(this)
        binding.rvAddOnsExperience.layoutManager =
            LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        binding.rvAddOnsExperience.adapter = addOnsExperienceAdapter

        val spacingInPixels = resources.getDimensionPixelSize(com.intuit.sdp.R.dimen._5sdp)
        binding.rvAddOnsExperience.addItemDecoration(SpaceItemDecoration(0, spacingInPixels))

        addOnsExperienceAdapter.onItemClick = { position, item ->
            startActivity(Intent(this@ReviewReservationMemberActivity, AddOnsExperienceDetailsActivity::class.java))

            binding.rvAddOnsExperience.makeGone()
            binding.llOnsExperienceAdded.makeVisible()
        }

        //addOnsExperienceList()

        paymentMethodAdapter = PaymentMethodAdapter(this)
        binding.rvPaymentMethod.layoutManager =
            LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        binding.rvPaymentMethod.adapter = paymentMethodAdapter

        binding.rvPaymentMethod.addItemDecoration(
            SpaceItemDecoration(
                resources.getDimensionPixelSize(
                    com.intuit.sdp.R.dimen._8sdp
                ), 0
            )
        )

        paymentMethodList()

        binding.radioGroup.setOnCheckedChangeListener { _, checkedId ->
            if (checkedId == R.id.rbPayNow) {
                binding.llPaymentMethod.makeVisible()
            } else {
                binding.llPaymentMethod.makeGone()
            }
        }

        makeLinks(
            binding.tvAgreeTerms,
            "other",
            Pair(getString(R.string.privacy_policy), View.OnClickListener {
            }),
            Pair(getString(R.string.terms_and_conditions), View.OnClickListener {
            }),
        )
    }


    private fun paymentMethodList() {
        paymentMethodAdapter.objList = ArrayList()

        paymentMethodAdapter.objList.add(PaymentMethod(R.drawable.ic_upi, "UPI Payment"))
        paymentMethodAdapter.objList.add(PaymentMethod(R.drawable.ic_net_banking, "Net Banking"))

        paymentMethodAdapter.addData(paymentMethodAdapter.objList)
    }
}