package com.xongolab.hotellifyr.view.activity.home.search

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import com.archit.calendardaterangepicker.customviews.CalendarListener
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.xongolab.hotellifyr.R
import com.xongolab.hotellifyr.core.CoreActivity
import com.xongolab.hotellifyr.databinding.ActivitySearchLocationDetailBinding
import com.xongolab.hotellifyr.databinding.DialogChooseRangeDateBinding
import com.xongolab.hotellifyr.model.SearchHotel
import com.xongolab.hotellifyr.utils.Constants
import com.xongolab.hotellifyr.utils.Util
import com.xongolab.hotellifyr.view.activity.home.search.hotel.HotelDetailActivity
import com.xongolab.hotellifyr.view.activity.home.search.hotel.HotelListViewActivity
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class SearchLocationDetailActivity : CoreActivity() {

    private lateinit var binding: ActivitySearchLocationDetailBinding

    private var searchHotel = SearchHotel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySearchLocationDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setFullScreenMode(window)
        initView()
    }

    @SuppressLint("SetTextI18n")
    private fun initView() {
        if (intent != null) {
            searchHotel = Gson().fromJson(intent.getStringExtra(Constants.SEARCH_HOTEL), SearchHotel::class.java)

            println("SEARCH_HOTEL...JSON..." + Gson().toJson(searchHotel))

            val json = GsonBuilder().setPrettyPrinting().create().toJson(searchHotel)
            println("SEARCH_HOTEL...setPrettyPrinting JSON..." + json)

        }

        searchHotel.rooms = 1
        searchHotel.adults = 1
        searchHotel.children = 0

        binding.toolbar.btnBack.setOnClickListener(this)
        binding.toolbar.toolbarTitle.text = "Find Hotel"

        binding.llDateView.setOnClickListener(this)
        binding.llGuestAndRooms.setOnClickListener(this)
        binding.btnDiscoverHotels.setOnClickListener(this)

        binding.tvLocation.text = searchHotel.location
        binding.tvCheckInDate.text = Util.formatTimestamp(searchHotel.checkIn!!, "EEE MMM dd HH:mm:ss zzz yyyy", "EEE, dd MMM")
        binding.tvCheckOutDate.text = Util.formatTimestamp(searchHotel.checkOut!!, "EEE MMM dd HH:mm:ss zzz yyyy", "EEE, dd MMM")
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btnBack -> finish()
            R.id.llDateView -> chooseRangeDateDialog()
            R.id.llGuestAndRooms -> {
                val intent = Intent(this, SelectRoomAndGuestsActivity::class.java)
                intent.putExtra("rooms", searchHotel.rooms)
                intent.putExtra("adults", searchHotel.adults)
                intent.putExtra("children", searchHotel.children)
                resultLauncher.launch(intent)
            }

            R.id.btnDiscoverHotels -> {
                if (searchHotel.type == Constants.HOTEL) {
                    val intent = Intent(this, HotelDetailActivity::class.java)
                    intent.putExtra(Constants.SEARCH_HOTEL, Gson().toJson(searchHotel))
                    startActivity(intent)
                } else {
                    val intent = Intent(this, HotelListViewActivity::class.java)
                    intent.putExtra(Constants.SEARCH_HOTEL, Gson().toJson(searchHotel))
                    startActivity(intent)
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                searchHotel.rooms = data?.getIntExtra("rooms", 1)
                searchHotel.adults = data?.getIntExtra("adults", 1)
                searchHotel.children = data?.getIntExtra("children", 0)

                val totalRooms = searchHotel.rooms
                val totalGuest = searchHotel.adults!! + searchHotel.children!!

                binding.tvGuestAndRooms.text = "$totalRooms ${getString(R.string.room)}, $totalGuest ${getString(R.string.guest)}"
            }
        }

    @SuppressLint("DefaultLocale", "SetTextI18n")
    private fun chooseRangeDateDialog() {
        var checkInDate: String
        var checkOutDate: String

        val dateFormat = SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH)

        val dateDialog = Dialog(this)
        val dialogBinding = DialogChooseRangeDateBinding.inflate(layoutInflater)
        dateDialog.setContentView(dialogBinding.root)
        dateDialog.setCanceledOnTouchOutside(true)
        dateDialog.setCancelable(false)

        dialogBinding.apply {

            val typeface = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                resources.getFont(R.font.product_sans_regular)
            } else {
                Typeface.createFromAsset(assets, "font/product_sans_regular.ttf")
            }

            calendarDateRangePicker.setFonts(typeface)

            val startMonth = Calendar.getInstance()
            val endMonth = startMonth.clone() as Calendar
            endMonth.add(Calendar.YEAR, 5)
            calendarDateRangePicker.setVisibleMonthRange(startMonth, endMonth)
            calendarDateRangePicker.setSelectableDateRange(startMonth, endMonth)
            calendarDateRangePicker.setCurrentMonth(startMonth)

            val startCalendar = Calendar.getInstance().apply {
                time = dateFormat.parse(searchHotel.checkIn!!)!!
            }

            val endCalendar = Calendar.getInstance().apply {
                time = dateFormat.parse(searchHotel.checkOut!!)!!
            }

            checkInDate = startCalendar.time.toString()
            checkOutDate = endCalendar.time.toString()

            calendarDateRangePicker.setSelectedDateRange(startCalendar, endCalendar)

            val differenceInMillis = endCalendar.timeInMillis - startCalendar.timeInMillis
            val daysSelected = (differenceInMillis / (1000 * 60 * 60 * 24)).toInt()

            btnBookForToNight.text = "Continue with $daysSelected nights"

            calendarDateRangePicker.setCalendarListener(object : CalendarListener {
                override fun onFirstDateSelected(startDate: Calendar) {

                }

                @SuppressLint("SetTextI18n")
                override fun onDateRangeSelected(startDate: Calendar, endDate: Calendar) {
                    dialogBinding.apply {
                        checkInDate = startDate.time.toString()
                        checkOutDate = endDate.time.toString()

                        // Calculate the number of days selected
                        val newDifferenceInMillis = endDate.timeInMillis - startDate.timeInMillis
                        val newDaysSelected = (newDifferenceInMillis / (1000 * 60 * 60 * 24)).toInt()
                        if (newDaysSelected < 1) {
                            btnBookForToNight.text = getString(R.string.please_select_at_least_1_night)
                            btnBookForToNight.isEnabled = false
                            btnBookForToNight.alpha = 0.4F
                        } else {
                            checkInDate = startDate.time.toString()
                            checkOutDate = endDate.time.toString()
                            btnBookForToNight.text = "${getString(R.string.continue_with)} $newDaysSelected ${getString(R.string.nights)}"
                            btnBookForToNight.isEnabled = true
                            btnBookForToNight.alpha = 1.0F
                        }
                        //  btnBookForToNight.text = "Continue with $newDaysSelected nights"
                    }
                }
            })

            btnBookForToNight.setOnClickListener {
                if (checkInDate.isEmpty() && checkOutDate.isEmpty()) {
                    msgDialog(getString(R.string.please_select_check_in_check_out_date))
                } else if (checkInDate.isEmpty()) {
                    msgDialog(getString(R.string.please_select_check_in_date))
                } else if (checkOutDate.isEmpty()) {
                    msgDialog(getString(R.string.please_select_check_out_date))
                } else {
                    dateDialog.dismiss()
                    searchHotel.checkIn = checkInDate
                    searchHotel.checkOut = checkOutDate

                    binding.tvCheckInDate.text = Util.formatTimestamp(
                        checkInDate,
                        "EEE MMM dd HH:mm:ss zzz yyyy",
                        "EEE, dd MMM"
                    )
                    binding.tvCheckOutDate.text = Util.formatTimestamp(
                        checkOutDate,
                        "EEE MMM dd HH:mm:ss zzz yyyy",
                        "EEE, dd MMM"
                    )
                }
            }
        }

        val window = dateDialog.window
        val params = window?.attributes
        params?.width = ((resources.displayMetrics.widthPixels * 0.95).toInt())
        window?.attributes = params
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dateDialog.show()
    }
}