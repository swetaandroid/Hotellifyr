package com.xongolab.hotellifyr.view.activity.home.search

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.view.View
import com.archit.calendardaterangepicker.customviews.CalendarListener
import com.google.gson.Gson
import com.xongolab.hotellifyr.R
import com.xongolab.hotellifyr.core.CoreActivity
import com.xongolab.hotellifyr.databinding.ActivitySearchCalendarBinding
import com.xongolab.hotellifyr.model.SearchHotel
import com.xongolab.hotellifyr.utils.Constants
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


class SearchCalendarActivity : CoreActivity() {

    private lateinit var binding: ActivitySearchCalendarBinding

    val inputFormat = SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.getDefault())
    val dateFormatMonth = SimpleDateFormat("MMM", Locale.getDefault())
    val dateFormatDayOfWeek = SimpleDateFormat("EEE", Locale.getDefault())
    val dateFormatDayOfMonth = SimpleDateFormat("dd", Locale.getDefault())

    var checkInDate: String = ""
    var checkOutDate: String = ""

    private var searchHotel = SearchHotel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySearchCalendarBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setFullScreenMode(window)
        initView()
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btnBack -> finish()

            R.id.btnBookForToNight -> {
               // throw RuntimeException("Test Crash") // Force a crash
                if (checkInDate.isEmpty() && checkOutDate.isEmpty()) {
                    msgDialog(getString(R.string.please_select_check_in_check_out_date))
                } else if (checkInDate.isEmpty()) {
                    msgDialog(getString(R.string.please_select_check_in_date))
                } else if (checkOutDate.isEmpty()) {
                    msgDialog(getString(R.string.please_select_check_out_date))
                } else {
                    searchHotel.checkIn = checkInDate
                    searchHotel.checkOut = checkOutDate
                    val intent = Intent(this, SearchLocationDetailActivity::class.java)
                    intent.putExtra(Constants.SEARCH_HOTEL, Gson().toJson(searchHotel))
                    startActivity(intent)
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun initView() {
        try {
            if (intent != null) {
                //   searchHotel = Gson().fromJson(intent.getStringExtra(Constants.SEARCH_HOTEL), SearchHotel::class.java)
                intent.getStringExtra(Constants.SEARCH_HOTEL)?.let {
                    searchHotel = Gson().fromJson(it, SearchHotel::class.java)
                }

            }

            binding.btnBack.setOnClickListener(this)
            binding.btnBookForToNight.setOnClickListener(this)

            binding.apply {

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

                calendarDateRangePicker.setCalendarListener(calendarListener)

                val startCalendar = Calendar.getInstance() // Today
                val endCalendar = Calendar.getInstance().apply {
                    add(Calendar.DAY_OF_YEAR, 1) // Tomorrow
                }

                calendarDateRangePicker.setSelectedDateRange(startCalendar, endCalendar)

                checkInDate = startCalendar.time.toString()
                checkOutDate = endCalendar.time.toString()

                val newStartDate: Date = inputFormat.parse(startCalendar.time.toString()) ?: Date()

                tvCheckInDate.text = dateFormatDayOfMonth.format(newStartDate)
                tvCheckInWeek.text = dateFormatDayOfWeek.format(newStartDate)
                tvCheckInMonth.text = dateFormatMonth.format(newStartDate)

                val newEndDate: Date = inputFormat.parse(endCalendar.time.toString()) ?: Date()

                tvCheckOutDate.text = dateFormatDayOfMonth.format(newEndDate)
                tvCheckOutWeek.text = dateFormatDayOfWeek.format(newEndDate)
                tvCheckOutMonth.text = dateFormatMonth.format(newEndDate)

                btnBookForToNight.text = getString(R.string.continue_with_1_nights)
                tvNoOfNights.text = getString(R.string.str_1_night)
            }
        }catch (e: Exception){
            e.printStackTrace()
        }

    }

    private val calendarListener: CalendarListener = object : CalendarListener {
        @SuppressLint("SetTextI18n")
        override fun onFirstDateSelected(startDate: Calendar) {
            try {
                binding.apply {
                    checkInDate = startDate.time.toString()
                    checkOutDate = ""

                    val newStartDate: Date = inputFormat.parse(startDate.time.toString()) ?: Date()

                    tvCheckInDate.text = dateFormatDayOfMonth.format(newStartDate)
                    tvCheckInWeek.text = dateFormatDayOfWeek.format(newStartDate)
                    tvCheckInMonth.text = dateFormatMonth.format(newStartDate)

                    tvCheckOutDate.text = ""
                    tvCheckOutWeek.text = ""
                    tvCheckOutMonth.text = ""

                    btnBookForToNight.text = getString(R.string.book_for_tonight)
                }
            }catch (e: Exception){
                e.printStackTrace()
            }

        }

        @SuppressLint("SetTextI18n")
        override fun onDateRangeSelected(startDate: Calendar, endDate: Calendar) {
            try {
                binding.apply {
                    checkInDate = startDate.time.toString()
                    checkOutDate = endDate.time.toString()

                    val newStartDate: Date = inputFormat.parse(startDate.time.toString()) ?: Date()

                    tvCheckInDate.text = dateFormatDayOfMonth.format(newStartDate)
                    tvCheckInWeek.text = dateFormatDayOfWeek.format(newStartDate)
                    tvCheckInMonth.text = dateFormatMonth.format(newStartDate)

                    val newEndDate: Date = inputFormat.parse(endDate.time.toString()) ?: Date()

                    tvCheckOutDate.text = dateFormatDayOfMonth.format(newEndDate)
                    tvCheckOutWeek.text = dateFormatDayOfWeek.format(newEndDate)
                    tvCheckOutMonth.text = dateFormatMonth.format(newEndDate)

                    // Calculate the number of days selected
                    val differenceInMillis = endDate.timeInMillis - startDate.timeInMillis
                    val daysSelected = (differenceInMillis / (1000 * 60 * 60 * 24)).toInt()

                    if (daysSelected < 1) {
                        btnBookForToNight.text = getString(R.string.please_select_at_least_1_night)
                        btnBookForToNight.isEnabled = false
                        btnBookForToNight.alpha = 0.4F
                        tvNoOfNights.text = "$daysSelected ${getString(R.string.str_night)}"
                    } else {
                        btnBookForToNight.text = "${getString(R.string.continue_with)} $daysSelected ${getString(R.string.nights_small)}"
                        btnBookForToNight.isEnabled = true
                        btnBookForToNight.alpha = 1.0F
                        tvNoOfNights.text = "$daysSelected ${getString(R.string.str_night)}"
                    }
                }
            }catch (e: Exception){
                e.printStackTrace()
            }
        }
    }

}