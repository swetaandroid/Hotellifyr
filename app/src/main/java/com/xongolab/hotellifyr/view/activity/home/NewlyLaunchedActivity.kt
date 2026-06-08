package com.xongolab.hotellifyr.view.activity.home

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.xongolab.hotellifyr.R
import com.xongolab.hotellifyr.core.CoreActivity
import com.xongolab.hotellifyr.databinding.ActivityNewlyLaunchedBinding
import com.xongolab.hotellifyr.model.HotelModel
import com.xongolab.hotellifyr.model.SearchHotel
import com.xongolab.hotellifyr.utils.Constants
import com.xongolab.hotellifyr.view.activity.home.search.hotel.HotelDetailActivity
import com.xongolab.hotellifyr.view.adapter.ImageAdapter
import com.xongolab.hotellifyr.view.adapter.NewlyLaunchedVerticalAdapter
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

class NewlyLaunchedActivity : CoreActivity() {
    private lateinit var binding: ActivityNewlyLaunchedBinding
    private lateinit var imageAdapter: ImageAdapter
    private lateinit var adapter: NewlyLaunchedVerticalAdapter

    private var hotelModel: HotelModel = HotelModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewlyLaunchedBinding.inflate(layoutInflater)
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

        imageAdapter = ImageAdapter(this)

        binding.viewPager.adapter = imageAdapter
        binding.viewPager.currentItem = 0
        binding.indicator.isSaveFromParentEnabled = false
        binding.indicator.attachTo(binding.viewPager)

        imageList()

        adapter = NewlyLaunchedVerticalAdapter(this)
        binding.rvNewlyLaunched.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvNewlyLaunched.adapter = adapter

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
            val intent = Intent(this@NewlyLaunchedActivity, HotelDetailActivity::class.java)
            intent.putExtra(Constants.SEARCH_HOTEL, Gson().toJson(searchHotel))
            startActivity(intent)
        }
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