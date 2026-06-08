package com.xongolab.hotellifyr.view.activity.home.search.hotel

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import com.google.gson.Gson
import com.xongolab.hotellifyr.R
import com.xongolab.hotellifyr.core.CoreActivity
import com.xongolab.hotellifyr.databinding.ActivityHotelOfferDetailBinding
import com.xongolab.hotellifyr.model.HotelModel

class HotelOfferDetailActivity : CoreActivity() {

    private lateinit var binding: ActivityHotelOfferDetailBinding

    private var hotelOffersSection: HotelModel.HotelOffersSection = HotelModel.HotelOffersSection()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHotelOfferDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setFullScreenMode(window)
        initView()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onClick(v: View) {
        when (v.id) {
            R.id.btnBack -> finish()
        }
    }

    private fun initView(){
        if (intent.hasExtra("response")) {
            hotelOffersSection = Gson().fromJson(
                intent.getStringExtra("response"),
                HotelModel.HotelOffersSection::class.java
            )
        }

        binding.apply {
            tvTitle.text = hotelOffersSection.title
            tvDescription.text = hotelOffersSection.description

//            tvHowToBook.text = hotelOffersSection.subtitle
            tvValidity.text = hotelOffersSection.validity

            if (hotelOffersSection.images.isNotEmpty())
                ivIcon.setImageURI(hotelOffersSection.images.first())
        }

        binding.btnBack.setOnClickListener(this)
    }
}