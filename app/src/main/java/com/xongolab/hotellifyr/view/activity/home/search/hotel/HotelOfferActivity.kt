package com.xongolab.hotellifyr.view.activity.home.search.hotel

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.xongolab.hotellifyr.R
import com.xongolab.hotellifyr.core.CoreActivity
import com.xongolab.hotellifyr.databinding.ActivityHotelOfferBinding
import com.xongolab.hotellifyr.model.HotelModel
import com.xongolab.hotellifyr.utils.makeGone
import com.xongolab.hotellifyr.utils.makeVisible
import com.xongolab.hotellifyr.view.adapter.hotel.HotelOfferAdapter

class HotelOfferActivity : CoreActivity() {

    private lateinit var binding: ActivityHotelOfferBinding

    private lateinit var hotelOfferAdapter: HotelOfferAdapter

    private var hotelModel: HotelModel = HotelModel()

    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHotelOfferBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btnBack -> finish()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun initView() {
        binding.layoutToolBar.toolbarTitle.text = "Hotel Offers"
        binding.layoutToolBar.btnBack.setOnClickListener(this)

        if (intent.hasExtra("response")) {
            hotelModel = Gson().fromJson(intent.getStringExtra("response"), HotelModel::class.java)
        }

        hotelOfferAdapter = HotelOfferAdapter(this, isDetailActivity = false)
        binding.rvHotelOffer.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        binding.rvHotelOffer.adapter = hotelOfferAdapter


        if (hotelModel.hotelOffersSections.isEmpty()) {
            binding.tvNoDataFound.makeVisible()
        } else {
            binding.tvNoDataFound.makeGone()
            hotelOfferAdapter.addData(hotelModel.hotelOffersSections)
        }

        hotelOfferAdapter.onItemClick = { selectedItem ->
            val intent = Intent(this, HotelOfferDetailActivity::class.java)
            intent.putExtra("response", Gson().toJson(selectedItem))
            startActivity(intent)
        }
    }
}