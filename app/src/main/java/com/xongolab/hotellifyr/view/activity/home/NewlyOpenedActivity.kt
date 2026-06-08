package com.xongolab.hotellifyr.view.activity.home

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.xongolab.hotellifyr.R
import com.xongolab.hotellifyr.core.CoreActivity
import com.xongolab.hotellifyr.databinding.ActivityNewlyOpenedBinding
import com.xongolab.hotellifyr.model.HotelModel
import com.xongolab.hotellifyr.model.ResortModel
import com.xongolab.hotellifyr.model.SearchHotel
import com.xongolab.hotellifyr.utils.Constants
import com.xongolab.hotellifyr.view.activity.home.NewlyLaunchedActivity
import com.xongolab.hotellifyr.view.activity.home.search.hotel.HotelDetailActivity
import com.xongolab.hotellifyr.view.adapter.NewlyOpenedVerticalAdapter
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

class NewlyOpenedActivity : CoreActivity() {
    private lateinit var binding: ActivityNewlyOpenedBinding
    private lateinit var adapter: NewlyOpenedVerticalAdapter

    private var hotelModel: HotelModel = HotelModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewlyOpenedBinding.inflate(layoutInflater)
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
        if (intent.hasExtra(Constants.HOTELS)) {
            hotelModel = Gson().fromJson(intent.getStringExtra(Constants.HOTELS), HotelModel::class.java)
        }

        binding.toolbar.toolbarTitle.text = hotelModel.title
        binding.toolbar.btnBack.setOnClickListener(this)

        adapter = NewlyOpenedVerticalAdapter(this)
        binding.rvNewlyOpened.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
        binding.rvNewlyOpened.adapter = adapter

        adapter.addData(hotelModel.hotelDetails)

        adapter.onItemClick = { item ->
            val dateFormat = SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.getDefault())
            dateFormat.timeZone = TimeZone.getDefault()

            // Get current calendar instance
            val calendar = Calendar.getInstance()

            // Format today's date
            val today = dateFormat.format(calendar.time)

            calendar.add(Calendar.DAY_OF_YEAR, 1)
            val tomorrow = dateFormat.format(calendar.time)

            val searchHotel = SearchHotel()
            searchHotel.apply {
                rooms = 1
                adults = 1
                children = 0
                hotelID = item.id
                location = item.address
                type = Constants.HOTEL
                checkIn = today
                checkOut = tomorrow
            }
            val intent = Intent(this@NewlyOpenedActivity, HotelDetailActivity::class.java)
            intent.putExtra(Constants.SEARCH_HOTEL, Gson().toJson(searchHotel))
            startActivity(intent)
        }
    }

}