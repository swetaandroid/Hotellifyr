package com.xongolab.hotellifyr.view.activity.home

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.xongolab.hotellifyr.R
import com.xongolab.hotellifyr.core.CoreActivity
import com.xongolab.hotellifyr.custom.SpaceItemDecoration
import com.xongolab.hotellifyr.databinding.ActivityCurrentOffersBinding
import com.xongolab.hotellifyr.model.HotelModel
import com.xongolab.hotellifyr.model.OffersModel
import com.xongolab.hotellifyr.utils.Constants
import com.xongolab.hotellifyr.view.activity.home.search.hotel.HotelOfferDetailActivity
import com.xongolab.hotellifyr.view.adapter.CurrentOfferAdapter
import com.xongolab.hotellifyr.view.adapter.ImageAdapter


class CurrentOffersActivity : CoreActivity() {
    private lateinit var binding: ActivityCurrentOffersBinding
    private lateinit var imageAdapter: ImageAdapter
    private lateinit var currentOfferAdapter: CurrentOfferAdapter

    private var offersList: ArrayList<OffersModel>? = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCurrentOffersBinding.inflate(layoutInflater)
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
        if (intent.hasExtra(Constants.OFFERS)) {
            offersList = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                intent.getParcelableArrayListExtra(Constants.OFFERS, OffersModel::class.java)
            } else {
                @Suppress("DEPRECATION")
                intent.getParcelableArrayListExtra(Constants.OFFERS)
            }
        }

        binding.toolbar.toolbarTitle.text = getString(R.string.current_offers)
        binding.toolbar.btnBack.setOnClickListener(this)

        imageAdapter = ImageAdapter(this)

        binding.viewPager.adapter = imageAdapter
        binding.viewPager.currentItem = 0
        binding.indicator.isSaveFromParentEnabled = false
        binding.indicator.attachTo(binding.viewPager)

        imageList()

        currentOfferAdapter = CurrentOfferAdapter(this)
        binding.rvCurrentOffer.layoutManager =
            LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        binding.rvCurrentOffer.adapter = currentOfferAdapter

        if (!offersList.isNullOrEmpty()) {
            currentOfferAdapter.objList.addAll(offersList!!)
        }

        currentOfferAdapter.onItemClick = { item ->
            val model = HotelModel.HotelOffersSection().apply {
                title = item.title
                description = item.description
                details = item.details
                id = item.id
                images.add(item.images)
                validity = item.validity
                subtitle = item.subtitle
            }

            val intent = Intent(this@CurrentOffersActivity, HotelOfferDetailActivity::class.java)
            intent.putExtra("response", Gson().toJson(model))
            startActivity(intent)
        }

        val spacingInPixels = resources.getDimensionPixelSize(com.intuit.sdp.R.dimen._15sdp)
        binding.rvCurrentOffer.addItemDecoration(SpaceItemDecoration(spacingInPixels, 0))
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