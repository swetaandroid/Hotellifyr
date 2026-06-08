package com.xongolab.hotellifyr.view.activity.home.search.hotel

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.google.gson.Gson
import com.xongolab.hotellifyr.R
import com.xongolab.hotellifyr.core.CoreActivity
import com.xongolab.hotellifyr.databinding.ActivityWeddingEventBinding
import com.xongolab.hotellifyr.model.HotelModel
import com.xongolab.hotellifyr.view.adapter.HotelDetailViewPager

class WeddingEventActivity : CoreActivity() {
    private lateinit var binding: ActivityWeddingEventBinding
    private lateinit var hotelDetailViewPager: HotelDetailViewPager

    private var venueData: HotelModel.VenueData = HotelModel.VenueData()
    private var hotelID = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityWeddingEventBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setFullScreenMode(window)
        initView()
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btnBack -> finish()
            R.id.btnEnquiryNow -> {
                val intent = Intent(this, BookEventBanquetActivity::class.java)
                intent.putExtra("hotelID", hotelID)
                intent.putExtra("id", venueData.id)
                startActivity(intent)
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun initView() {
        if (intent.hasExtra("response")) {
            venueData =
                Gson().fromJson(intent.getStringExtra("response"), HotelModel.VenueData::class.java)
            hotelID = intent.getStringExtra("hotelID")!!
        }

        binding.apply {
            hotelDetailViewPager = HotelDetailViewPager(this@WeddingEventActivity)

            viewPager.adapter = hotelDetailViewPager
            viewPager.currentItem = 0
            indicator.isSaveFromParentEnabled = false
            indicator.attachTo(viewPager)

            hotelDetailViewPager.addData(venueData.images)

            tvTitle.text = venueData.title
            tvGuest.text = "${venueData.area} Sq m • ${venueData.guestsCapacity}"
            tvDescription.text = venueData.description

            tvArea.text = venueData.area + " Sq m"
            tvHall.text = venueData.hall
            tvGuestsCapacity.text = venueData.guestsCapacity
        }

        binding.btnBack.setOnClickListener(this)
        binding.btnEnquiryNow.setOnClickListener(this)
    }
}