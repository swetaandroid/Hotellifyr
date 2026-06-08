package com.xongolab.hotellifyr.view.activity.home

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.archit.calendardaterangepicker.customviews.CalendarListener
import com.google.gson.Gson
import com.xongolab.hotellifyr.R
import com.xongolab.hotellifyr.core.CoreActivity
import com.xongolab.hotellifyr.databinding.ActivityRoomReservationBinding
import com.xongolab.hotellifyr.databinding.DialogChooseRangeDateBinding
import com.xongolab.hotellifyr.databinding.DialogUpdateGuestBinding
import com.xongolab.hotellifyr.databinding.PrimaryGuestDetailsDialogBinding
import com.xongolab.hotellifyr.model.BookRoomRequest
import com.xongolab.hotellifyr.model.BookingHotelRequest
import com.xongolab.hotellifyr.model.RoomWiseGuest
import com.xongolab.hotellifyr.model.SearchHotel
import com.xongolab.hotellifyr.utils.Constants
import com.xongolab.hotellifyr.utils.Pref
import com.xongolab.hotellifyr.utils.Util
import com.xongolab.hotellifyr.utils.Util.getTimeInMillis
import com.xongolab.hotellifyr.utils.makeGone
import com.xongolab.hotellifyr.utils.makeVisible
import com.xongolab.hotellifyr.view.activity.home.search.hotel.HotelDetailActivity
import com.xongolab.hotellifyr.view.adapter.SelectChildAgeAdapter
import com.xongolab.hotellifyr.view.adapter.hotel.DayWisePriceAdapter
import com.xongolab.hotellifyr.view.adapter.hotel.DayWisePriceSecondAdapter
import com.xongolab.hotellifyr.view.adapter.hotel.DayWisePriceThirdAdapter
import com.xongolab.hotellifyr.view.adapter.hotel.DayWiseTaxFirstAdapter
import com.xongolab.hotellifyr.view.adapter.hotel.DayWiseTaxSecondAdapter
import com.xongolab.hotellifyr.view.adapter.hotel.DayWiseTaxThirdAdapter
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlin.math.floor

class RoomReservationActivity : CoreActivity() {
    private lateinit var binding: ActivityRoomReservationBinding

    private var searchHotel = SearchHotel()
    private var hotelName: String = ""
    private var payOption: String = ""
    private var isFirstRoomSelected: Boolean = false
    private var isSecondRoomSelected: Boolean = false
    private var isThirdRoomSelected: Boolean = false
    private var isAgree = false
    private var bookRoomList: ArrayList<BookRoomRequest> = arrayListOf()
    private var roomWiseGuestList: ArrayList<RoomWiseGuest> = arrayListOf()

    private lateinit var dayWisePriceAdapter: DayWisePriceAdapter
    private lateinit var dayWisePriceSecondAdapter: DayWisePriceSecondAdapter
    private lateinit var dayWisePriceThirdAdapter: DayWisePriceThirdAdapter

    private lateinit var dayWiseTaxFirstAdapter: DayWiseTaxFirstAdapter
    private lateinit var dayWiseTaxSecondAdapter: DayWiseTaxSecondAdapter
    private lateinit var dayWiseTaxThirdAdapter: DayWiseTaxThirdAdapter

    private lateinit var selectChildAgeAdapter: SelectChildAgeAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRoomReservationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setFullScreenMode(window)

        initView()

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val intent = Intent(this@RoomReservationActivity, HotelDetailActivity::class.java)
                intent.putExtra(Constants.SEARCH_HOTEL, Gson().toJson(searchHotel))
                intent.putExtra("hotelName", hotelName)
                intent.putExtra("paymentOption", payOption)
                startActivity(intent)
                finishAffinity()
            }
        })
    }

    @SuppressLint("SetTextI18n")
    private fun initView() {

        if (intent != null) {
            searchHotel = Gson().fromJson(
                intent.getStringExtra(Constants.SEARCH_HOTEL),
                SearchHotel::class.java
            )
            hotelName = intent.getStringExtra("hotelName").toString()
            payOption = intent.getStringExtra("paymentOption").toString()

            if (intent.hasExtra("bookRoomList")) {
                bookRoomList = intent.getSerializableExtra("bookRoomList") as ArrayList<BookRoomRequest>
            }
        }

        binding.tvToolBarTitle.text = hotelName


        val checkIn = Util.formatTimestamp(
            searchHotel.checkIn!!,
            "EEE MMM dd HH:mm:ss zzz yyyy",
            "dd"
        )
        val checkOut = Util.formatTimestamp(
            searchHotel.checkOut!!,
            "EEE MMM dd HH:mm:ss zzz yyyy",
            "dd"
        )

        binding.tvCheckInDate.text = checkIn
        binding.tvCheckOutDate.text = checkOut
        binding.tvCheckInWeek.text = Util.formatTimestamp(
            searchHotel.checkIn!!,
            "EEE MMM dd HH:mm:ss zzz yyyy",
            "EEE"
        )
        binding.tvCheckOutWeek.text = Util.formatTimestamp(
            searchHotel.checkOut!!,
            "EEE MMM dd HH:mm:ss zzz yyyy",
            "EEE"
        )
        binding.tvCheckInMonth.text = Util.formatTimestamp(
            searchHotel.checkIn!!,
            "EEE MMM dd HH:mm:ss zzz yyyy",
            "MMM"
        )
        binding.tvCheckOutMonth.text = Util.formatTimestamp(
            searchHotel.checkOut!!,
            "EEE MMM dd HH:mm:ss zzz yyyy",
            "MMM"
        )

        dayWisePriceAdapter = DayWisePriceAdapter(this)
        binding.rvRoomPriceDayWiseFirst.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        binding.rvRoomPriceDayWiseFirst.adapter = dayWisePriceAdapter

        dayWisePriceSecondAdapter = DayWisePriceSecondAdapter(this)
        binding.rvRoomPriceDayWiseSecond.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        binding.rvRoomPriceDayWiseSecond.adapter = dayWisePriceSecondAdapter

        dayWisePriceThirdAdapter = DayWisePriceThirdAdapter(this)
        binding.rvRoomPriceDayWiseThird.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        binding.rvRoomPriceDayWiseThird.adapter = dayWisePriceThirdAdapter

        dayWiseTaxFirstAdapter = DayWiseTaxFirstAdapter(this)
        binding.rvRoomTaxDayWiseFirst.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        binding.rvRoomTaxDayWiseFirst.adapter = dayWiseTaxFirstAdapter

        dayWiseTaxSecondAdapter = DayWiseTaxSecondAdapter(this)
        binding.rvRoomTaxDayWiseSecond.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        binding.rvRoomTaxDayWiseSecond.adapter = dayWiseTaxSecondAdapter

        dayWiseTaxThirdAdapter = DayWiseTaxThirdAdapter(this)
        binding.rvRoomTaxDayWiseThird.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        binding.rvRoomTaxDayWiseThird.adapter = dayWiseTaxThirdAdapter

        val endDateTimeInMillis = getTimeInMillis(searchHotel.checkOut.toString(), "EEE MMM dd HH:mm:ss zzz yyyy")
        val startDateTimeInMillis = getTimeInMillis(searchHotel.checkIn.toString(), "EEE MMM dd HH:mm:ss zzz yyyy")

        val differenceInMillis = endDateTimeInMillis - startDateTimeInMillis
        val daysSelected = (differenceInMillis / (1000 * 60 * 60 * 24)).toInt()

        binding.tvNoOfNights.text = "$daysSelected ${getString(R.string.night)}"

        binding.tvTotalGuestsFirst.text = searchHotel.adults.toString() + getString(R.string.adults) + searchHotel.children?.let {
            if (it > 0) {
                searchHotel.children.toString() + getString(R.string.child)
            } else ""
        }
        binding.llFirstHotel.makeVisible()

        if (searchHotel.rooms == 3) {
            val roomWiseGuest = RoomWiseGuest()
            roomWiseGuest.adults = searchHotel.adults
            roomWiseGuest.children = searchHotel.children
            roomWiseGuestList.clear()
            roomWiseGuestList.add(roomWiseGuest)
            roomWiseGuestList.add(roomWiseGuest)
            roomWiseGuestList.add(roomWiseGuest)

            binding.tvTotalGuestsFirst.text = roomWiseGuestList[0].adults.toString() + getString(R.string.adults) + roomWiseGuestList[0].children?.let {
                if (it > 0) {
                    roomWiseGuestList[0].children.toString() + getString(R.string.child)
                } else ""
            }
            binding.llFirstHotel.makeVisible()

            binding.tvTotalGuestsSecond.text = roomWiseGuestList[1].adults.toString() + getString(R.string.adults) + roomWiseGuestList[1].children?.let {
                if (it > 0) {
                    roomWiseGuestList[1].children.toString() + getString(R.string.child)
                } else ""
            }
            binding.llSecondHotel.makeVisible()

            binding.tvTotalGuestsThird.text = roomWiseGuestList[2].adults.toString() + getString(R.string.adults) + roomWiseGuestList[2].children?.let {
                if (it > 0) {
                    roomWiseGuestList[2].children.toString() + getString(R.string.child)
                } else ""
            }
            binding.llThirdHotel.makeVisible()

        } else if (searchHotel.rooms == 2) {
            val roomWiseGuest = RoomWiseGuest()
            roomWiseGuest.adults = searchHotel.adults
            roomWiseGuest.children = searchHotel.children
            roomWiseGuestList.clear()
            roomWiseGuestList.add(roomWiseGuest)
            roomWiseGuestList.add(roomWiseGuest)

            binding.tvTotalGuestsFirst.text = roomWiseGuestList[0].adults.toString() + getString(R.string.adults) + roomWiseGuestList[0].children?.let {
                if (it > 0) {
                    roomWiseGuestList[0].children.toString() + getString(R.string.child)
                } else ""
            }
            binding.llFirstHotel.makeVisible()

            binding.tvTotalGuestsSecond.text = roomWiseGuestList[1].adults.toString() + getString(R.string.adults) + roomWiseGuestList[1].children?.let {
                if (it > 0) {
                    roomWiseGuestList[1].children.toString() + getString(R.string.child)
                } else ""
            }
            binding.llSecondHotel.makeVisible()


        } else {
            val roomWiseGuest = RoomWiseGuest()
            roomWiseGuest.adults = searchHotel.adults
            roomWiseGuest.children = searchHotel.children
            roomWiseGuestList.clear()
            roomWiseGuestList.add(roomWiseGuest)

            binding.tvTotalGuestsFirst.text = roomWiseGuestList[0].adults.toString() + getString(R.string.adults) + roomWiseGuestList[0].children?.let {
                if (it > 0) {
                    roomWiseGuestList[0].children.toString() +getString(R.string.child)
                } else ""
            }
            binding.llFirstHotel.makeVisible()

        }

        searchHotel.roomWiseGuest = roomWiseGuestList

        setUIData()


        binding.btnBack.setOnClickListener(this)
        binding.btnRoom.setOnClickListener(this)
        binding.icCloseSecond.setOnClickListener(this)
        binding.icCloseThird.setOnClickListener(this)
        binding.icClose.setOnClickListener(this)

        binding.btnSelectRoom.setOnClickListener(this)
        binding.btnSelectRoomSecond.setOnClickListener(this)
        binding.btnSelectRoomThird.setOnClickListener(this)

        binding.llTotalGuestsFirst.setOnClickListener(this)
        binding.llTotalGuestsSecond.setOnClickListener(this)
        binding.llTotalGuestsThird.setOnClickListener(this)

        binding.llTaxInfoView.setOnClickListener(this)
        binding.llPriceInfoView.setOnClickListener(this)
        binding.llDateRange.setOnClickListener(this)
        binding.btnJoinNow.setOnClickListener(this)
    }

    @SuppressLint("SetTextI18n")
    private fun setUIData() {
        if (bookRoomList.isNotEmpty()) {
            binding.cvRoomWisePrice.makeVisible()
            binding.tvTotalPrice.text = setPriceWithUnit(bookRoomList.sumOf { it.totalPrice })
            binding.tvTotalTax.text = setPriceWithUnit(bookRoomList.sumOf { it.taxAmount })

            val totalAmount = bookRoomList.sumOf { it.totalPrice } + bookRoomList.sumOf { it.taxAmount }
            val roundedTotalAmount = floor(totalAmount).toInt()
            val roundOffAmount = totalAmount - roundedTotalAmount

            binding.tvRoundOffPrice.text = setPriceWithUnit(roundOffAmount)
            binding.tvTotalCostOfStay.text = setPriceWithUnit(roundedTotalAmount.toDouble())

            binding.btnJoinNow.makeVisible()
            binding.llEarnPoint.makeGone()
        } else {
            binding.cvRoomWisePrice.makeGone()
            binding.btnJoinNow.makeGone()
            binding.llEarnPoint.makeGone()
            binding.tvTotalPrice.text = setPriceWithUnit(bookRoomList.sumOf { it.totalPrice })
            binding.tvTotalTax.text = setPriceWithUnit(bookRoomList.sumOf { it.taxAmount })
            val totalAmount = bookRoomList.sumOf { it.totalPrice } + bookRoomList.sumOf { it.taxAmount }
            val roundedTotalAmount = floor(totalAmount).toInt()
            val roundOffAmount = totalAmount - roundedTotalAmount
            binding.tvRoundOffPrice.text = setPriceWithUnit(roundOffAmount)
            binding.tvTotalCostOfStay.text = setPriceWithUnit(roundedTotalAmount.toDouble())
        }
        when (searchHotel.rooms) {
            3 -> {
                binding.llThirdHotel.makeVisible()
                binding.llSecondHotel.makeVisible()
                binding.llFirstHotel.makeVisible()
                binding.btnRoom.makeGone()
            }

            2 -> {
                binding.llThirdHotel.makeGone()
                binding.llSecondHotel.makeVisible()
                binding.llFirstHotel.makeVisible()
            }

            1 -> {
                binding.llThirdHotel.makeGone()
                binding.llSecondHotel.makeGone()
                binding.llFirstHotel.makeVisible()
            }
        }
        when (bookRoomList.size) {
            0 -> {
                isFirstRoomSelected = false
                isSecondRoomSelected = false
                isThirdRoomSelected = false

                binding.tvRoomNameFirst.text = getString(R.string.room_1_not_selected)
                binding.tvRoomRatesFirst.text = getString(R.string.str_0_inr)
                binding.ivHotel.setImageURI("")

                binding.tvRoomNameSecond.text = getString(R.string.room_2_not_selected)
                binding.tvRoomRatesSecond.text = getString(R.string.str_0_inr)
                binding.ivHotelSecond.setImageURI("")

                binding.tvRoomNameThird.text = getString(R.string.room_3_not_selected)
                binding.tvRoomRatesThird.text = getString(R.string.str_0_inr)
                binding.ivHotelThird.setImageURI("")
            }

            1 -> {
                if (bookRoomList[0].rateCode != "") {
                    isFirstRoomSelected = true
                }
                binding.tvRoomNameFirst.text = bookRoomList[0].roomTitle
                binding.tvTotalGuestsFirst.text = "${bookRoomList[0].adults + bookRoomList[0].children} ${getString(R.string.guest)}"
                binding.tvRoomRatesFirst.text = setPriceWithUnit(bookRoomList[0].price)

                binding.tvRoomNameFirstPrice.text = bookRoomList[0].roomTitle
                binding.tvRoomPricePerDayFirst.text = setPriceWithUnit(bookRoomList[0].roomRatePerDay) + " ${getString(R.string.night_2)}"
                binding.ivHotel.setImageURI(bookRoomList[0].hotelImageURL)

                println("SEARCH_HOTEL... 0.0 ..." + bookRoomList[0].hotelImageURL)

                binding.tvTaxRoomNameFirst.text = bookRoomList[0].roomTitle

                dayWisePriceAdapter.addData(bookRoomList[0].dayPrice)
                dayWiseTaxFirstAdapter.addData(bookRoomList[0].dayPrice)

                binding.llTaxFirst.makeVisible()
                binding.llTaxSecond.makeGone()
                binding.llTaxThird.makeGone()
                binding.llFirstPrice.makeVisible()
                binding.rvRoomPriceDayWiseFirst.makeVisible()
                binding.rvRoomTaxDayWiseFirst.makeVisible()
                binding.rvRoomPriceDayWiseSecond.makeGone()
                binding.rvRoomPriceDayWiseThird.makeGone()
                binding.rvRoomTaxDayWiseSecond.makeGone()
                binding.rvRoomTaxDayWiseThird.makeGone()
                binding.llSecondPrice.makeGone()
                binding.llThirdPrice.makeGone()
            }

            2 -> {
                if (bookRoomList[0].rateCode != "") {
                    isFirstRoomSelected = true
                }
                if (bookRoomList[1].rateCode != "") {
                    isSecondRoomSelected = true
                }
                binding.tvRoomNameFirst.text = bookRoomList[0].roomTitle
                binding.tvTotalGuestsFirst.text = "${bookRoomList[0].adults + bookRoomList[0].children} ${getString(R.string.guest)}"
                binding.tvRoomRatesFirst.text = setPriceWithUnit(bookRoomList[0].price)

                binding.tvRoomNameSecond.text = bookRoomList[1].roomTitle
                binding.tvTotalGuestsSecond.text = "${bookRoomList[1].adults + bookRoomList[1].children} ${getString(R.string.guest)}"
                binding.tvRoomRatesSecond.text = setPriceWithUnit(bookRoomList[1].price)

                binding.tvRoomNameFirstPrice.text = bookRoomList[0].roomTitle
                binding.tvRoomPricePerDayFirst.text = setPriceWithUnit(bookRoomList[0].roomRatePerDay) + " ${getString(R.string.night_2)}"
                binding.ivHotel.setImageURI(bookRoomList[0].hotelImageURL)
                println("SEARCH_HOTEL... 0.1 ..." + bookRoomList[0].hotelImageURL)

                binding.tvRoomNameSecondPrice.text = bookRoomList[1].roomTitle
                binding.tvRoomPricePerDaySecond.text = setPriceWithUnit(bookRoomList[1].roomRatePerDay) + " ${getString(R.string.night_2)}"
                binding.ivHotelSecond.setImageURI(bookRoomList[1].hotelImageURL)
                println("SEARCH_HOTEL... 1.0 ..." + bookRoomList[1].hotelImageURL)

                binding.tvTaxRoomNameFirst.text = bookRoomList[0].roomTitle
                binding.tvTaxRoomNameSecond.text = bookRoomList[1].roomTitle

                dayWisePriceAdapter.addData(bookRoomList[0].dayPrice)
                dayWisePriceSecondAdapter.addData(bookRoomList[1].dayPrice)
                dayWiseTaxFirstAdapter.addData(bookRoomList[0].dayPrice)
                dayWiseTaxSecondAdapter.addData(bookRoomList[1].dayPrice)

                binding.llTaxFirst.makeVisible()
                binding.llTaxSecond.makeVisible()
                binding.llTaxThird.makeGone()
                binding.llFirstPrice.makeVisible()
                binding.rvRoomPriceDayWiseFirst.makeVisible()
                binding.rvRoomTaxDayWiseFirst.makeVisible()
                binding.llSecondPrice.makeVisible()
                binding.rvRoomPriceDayWiseSecond.makeVisible()
                binding.rvRoomTaxDayWiseSecond.makeVisible()
                binding.llThirdPrice.makeGone()
                binding.rvRoomPriceDayWiseThird.makeGone()
                binding.rvRoomTaxDayWiseThird.makeGone()
            }

            3 -> {
                if (bookRoomList[0].rateCode != "") {
                    isFirstRoomSelected = true
                }
                if (bookRoomList[1].rateCode != "") {
                    isSecondRoomSelected = true
                }
                if (bookRoomList[2].rateCode != "") {
                    isThirdRoomSelected = true
                }

                binding.tvRoomNameFirst.text = bookRoomList[0].roomTitle
                binding.tvTotalGuestsFirst.text = "${bookRoomList[0].adults + bookRoomList[0].children} ${getString(R.string.guest)}"
                binding.tvRoomRatesFirst.text = setPriceWithUnit(bookRoomList[0].price)

                binding.tvRoomNameSecond.text = bookRoomList[1].roomTitle
                binding.tvTotalGuestsSecond.text = "${bookRoomList[1].adults + bookRoomList[1].children} ${getString(R.string.guest)}"
                binding.tvRoomRatesSecond.text = setPriceWithUnit(bookRoomList[1].price)

                binding.tvRoomNameThird.text = bookRoomList[2].roomTitle
                binding.tvTotalGuestsThird.text = "${bookRoomList[2].adults + bookRoomList[2].children} ${getString(R.string.guest)}"
                binding.tvRoomRatesThird.text = setPriceWithUnit(bookRoomList[2].price)

                binding.tvRoomNameFirstPrice.text = bookRoomList[0].roomTitle
                binding.tvRoomPricePerDayFirst.text = setPriceWithUnit(bookRoomList[0].roomRatePerDay) + getString(R.string.night_2)
                binding.ivHotel.setImageURI(bookRoomList[0].hotelImageURL)
                println("SEARCH_HOTEL... 0.3 ..." + bookRoomList[0].hotelImageURL)

                binding.tvRoomNameSecondPrice.text = bookRoomList[1].roomTitle
                binding.tvRoomPricePerDaySecond.text = setPriceWithUnit(bookRoomList[1].roomRatePerDay) + " ${getString(R.string.night_2)}"
                binding.ivHotelSecond.setImageURI(bookRoomList[1].hotelImageURL)

                println("SEARCH_HOTEL... 1.1 ..." + bookRoomList[1].hotelImageURL)

                binding.tvRoomNameThirdPrice.text = bookRoomList[2].roomTitle
                binding.tvRoomPricePerDayThird.text = setPriceWithUnit(bookRoomList[2].roomRatePerDay) + " ${getString(R.string.night_2)}"
                binding.ivHotelThird.setImageURI(bookRoomList[2].hotelImageURL)
                println("SEARCH_HOTEL... 2.0 ..." + bookRoomList[2].hotelImageURL)

                binding.tvTaxRoomNameFirst.text = bookRoomList[0].roomTitle
                binding.tvTaxRoomNameSecond.text = bookRoomList[1].roomTitle
                binding.tvTaxRoomNameSecond.text = bookRoomList[2].roomTitle

                dayWisePriceAdapter.addData(bookRoomList[0].dayPrice)
                dayWisePriceSecondAdapter.addData(bookRoomList[1].dayPrice)
                dayWisePriceThirdAdapter.addData(bookRoomList[2].dayPrice)
                dayWiseTaxFirstAdapter.addData(bookRoomList[0].dayPrice)
                dayWiseTaxSecondAdapter.addData(bookRoomList[1].dayPrice)
                dayWiseTaxThirdAdapter.addData(bookRoomList[2].dayPrice)

                binding.llTaxFirst.makeVisible()
                binding.llTaxSecond.makeVisible()
                binding.llTaxThird.makeVisible()
                binding.llFirstPrice.makeVisible()
                binding.rvRoomPriceDayWiseFirst.makeVisible()
                binding.rvRoomTaxDayWiseFirst.makeVisible()
                binding.llSecondPrice.makeVisible()
                binding.rvRoomPriceDayWiseSecond.makeVisible()
                binding.rvRoomTaxDayWiseSecond.makeVisible()
                binding.llThirdPrice.makeVisible()
                binding.rvRoomPriceDayWiseThird.makeVisible()
                binding.rvRoomTaxDayWiseThird.makeVisible()
                binding.icClose.makeGone()
                binding.icCloseSecond.makeGone()
                binding.icCloseThird.makeGone()
            }
        }

        if (isFirstRoomSelected) {
            binding.btnSelectRoom.makeGone()
        } else {
            binding.btnSelectRoom.makeVisible()
        }
        if (isSecondRoomSelected) {
            binding.btnSelectRoomSecond.makeGone()
        } else {
            binding.btnSelectRoomSecond.makeVisible()
        }
        if (isThirdRoomSelected) {
            binding.btnSelectRoomThird.makeGone()
        } else {
            binding.btnSelectRoomThird.makeVisible()
        }
    }


    @SuppressLint("SetTextI18n")
    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.btnBack -> onBackPressedDispatcher.onBackPressed()
            R.id.btnRoom -> {
                if (binding.llSecondHotel.isVisible) {
                    binding.llThirdHotel.makeVisible()
                    binding.btnRoom.makeGone()
                    val guest = RoomWiseGuest()
                    guest.adults = searchHotel.adults
                    guest.children = searchHotel.children

                    roomWiseGuestList.clear()
                    roomWiseGuestList.add(guest)
                    roomWiseGuestList.add(guest)
                    roomWiseGuestList.add(guest)

                    searchHotel.rooms = roomWiseGuestList.size
                    binding.tvTotalGuestsFirst.text = roomWiseGuestList[0].adults.toString() + getString(R.string.adults) + roomWiseGuestList[0].children?.let {
                        if (it > 0) {
                            roomWiseGuestList[0].children.toString() + getString(R.string.child)
                        } else ""
                    }
                    binding.tvTotalGuestsSecond.text = roomWiseGuestList[1].adults.toString() + getString(R.string.adults) + roomWiseGuestList[1].children?.let {
                        if (it > 0) {
                            roomWiseGuestList[1].children.toString() + getString(R.string.child)
                        } else ""
                    }
                    binding.tvTotalGuestsThird.text = roomWiseGuestList[2].adults.toString() + getString(R.string.adults) + roomWiseGuestList[2].children?.let {
                        if (it > 0) {
                            roomWiseGuestList[2].children.toString() + getString(R.string.child)
                        } else ""
                    }

                } else {
                    binding.llSecondHotel.makeVisible()
                    binding.llThirdHotel.makeGone()
                    binding.icClose.makeVisible()
                    val guest = RoomWiseGuest()
                    guest.adults = searchHotel.adults
                    guest.children = searchHotel.children
                    roomWiseGuestList.clear()
                    roomWiseGuestList.add(guest)
                    roomWiseGuestList.add(guest)
                    searchHotel.rooms = roomWiseGuestList.size
                    binding.tvTotalGuestsFirst.text = roomWiseGuestList[0].adults.toString() + getString(R.string.adults) + roomWiseGuestList[0].children?.let {
                        if (it > 0) {
                            roomWiseGuestList[0].children.toString() + getString(R.string.child)
                        } else ""
                    }
                    binding.tvTotalGuestsSecond.text = roomWiseGuestList[1].adults.toString() + getString(R.string.adults) + roomWiseGuestList[1].children?.let {
                        if (it > 0) {
                            roomWiseGuestList[1].children.toString() + getString(R.string.child)
                        } else ""
                    }

                }
            }

            R.id.icClose -> {
                if (binding.llThirdHotel.isVisible) {
                    binding.llThirdHotel.makeGone()
                    roomWiseGuestList.removeAt(2)
                    searchHotel.rooms = roomWiseGuestList.size
                } else if (binding.llSecondHotel.isVisible && !binding.llThirdHotel.isVisible) {
                    binding.llSecondHotel.makeGone()
                    roomWiseGuestList.removeAt(1)
                    searchHotel.rooms = roomWiseGuestList.size
                } else {
                    binding.icClose.makeGone()
                    if (!binding.btnSelectRoom.isVisible) {
                        binding.btnSelectRoom.makeVisible()
                    }
                    // roomWiseGuestList.removeAt(0)
                    binding.tvRoomNameFirst.text = getString(R.string.room_1_not_selected)
                    binding.tvRoomRatesFirst.text = getString(R.string.str_0_inr)
                    binding.ivHotel.setImageURI("")
                }
                if (bookRoomList.size > 0) {
                    bookRoomList.removeAt(0)
                    setUIData()
                }
                binding.btnRoom.makeVisible()
            }

            R.id.icCloseSecond -> {
                binding.llSecondHotel.makeGone()
                binding.btnRoom.makeVisible()
                if (roomWiseGuestList.size > 1) {
                    roomWiseGuestList.removeAt(1)
                }
                if (bookRoomList.size > 1) {
                    bookRoomList.removeAt(1)
                    setUIData()
                }
                searchHotel.rooms = roomWiseGuestList.size
                binding.tvRoomNameSecond.text = getString(R.string.room_2_not_selected)
                binding.tvRoomRatesSecond.text = getString(R.string.str_0_inr)
                binding.ivHotelSecond.setImageURI("")
                if (!binding.btnSelectRoomSecond.isVisible) {
                    binding.btnSelectRoomSecond.makeVisible()
                }
            }

            R.id.icCloseThird -> {
                binding.llThirdHotel.makeGone()
                binding.btnRoom.makeVisible()
                if (roomWiseGuestList.size > 2) {
                    roomWiseGuestList.removeAt(2)
                }
                if (bookRoomList.size > 2) {
                    bookRoomList.removeAt(2)
                    setUIData()
                }
                searchHotel.rooms = roomWiseGuestList.size
                binding.tvRoomNameThird.text = getString(R.string.room_3_not_selected)
                binding.tvRoomRatesThird.text = getString(R.string.str_0_inr)
                binding.ivHotelThird.setImageURI("")
                if (!binding.btnSelectRoomThird.isVisible) {
                    binding.btnSelectRoomThird.makeVisible()
                }
            }

            R.id.btnSelectRoom -> {
                val intent = Intent(this@RoomReservationActivity, RoomsPackagesActivity::class.java)
                intent.putExtra(Constants.SEARCH_HOTEL, Gson().toJson(searchHotel))
                intent.putExtra("hotelName", hotelName)
                intent.putExtra("paymentOption", payOption)
                intent.putExtra("bookRoomList", bookRoomList)
                intent.putExtra("clickIndex", 1)
                startActivity(intent)
            }

            R.id.btnSelectRoomSecond -> {
                val intent = Intent(this@RoomReservationActivity, RoomsPackagesActivity::class.java)
                intent.putExtra(Constants.SEARCH_HOTEL, Gson().toJson(searchHotel))
                intent.putExtra("hotelName", hotelName)
                intent.putExtra("paymentOption", payOption)
                intent.putExtra("bookRoomList", bookRoomList)
                intent.putExtra("clickIndex", 2)
                startActivity(intent)
            }

            R.id.btnSelectRoomThird -> {
                val intent = Intent(this@RoomReservationActivity, RoomsPackagesActivity::class.java)
                intent.putExtra(Constants.SEARCH_HOTEL, Gson().toJson(searchHotel))
                intent.putExtra("hotelName", hotelName)
                intent.putExtra("paymentOption", payOption)
                intent.putExtra("bookRoomList", bookRoomList)
                intent.putExtra("clickIndex", 3)
                startActivity(intent)
            }

            R.id.btnJoinNow -> {
                if (roomWiseGuestList.size >= 1 && !isFirstRoomSelected) {
                    msgDialog(getString(R.string.please_select_room_1))
                    return
                }

                if (roomWiseGuestList.size >= 2 && !isSecondRoomSelected) {
                    msgDialog(getString(R.string.please_select_room_2))
                    return
                }

                if (roomWiseGuestList.size >= 3 && !isThirdRoomSelected) {
                    msgDialog(getString(R.string.please_select_room_3))
                    return
                }
                openPrimaryGuestDetailsDialog()
            }

            R.id.llTotalGuestsFirst -> {
                openIncreaseDecreaseGuestDialog(1)
            }

            R.id.llTotalGuestsSecond -> {
                openIncreaseDecreaseGuestDialog(2)
            }

            R.id.llTotalGuestsThird -> {
                openIncreaseDecreaseGuestDialog(3)
            }

            R.id.llTaxInfoView -> {
                if (binding.llTaxInfo.isVisible) {
                    binding.llTaxInfo.makeGone()
                } else {
                    binding.llTaxInfo.makeVisible()
                }
            }

            R.id.llPriceInfoView -> {
                if (binding.llPriceInfo.isVisible) {
                    binding.llPriceInfo.makeGone()
                } else {
                    binding.llPriceInfo.makeVisible()
                }
            }

            R.id.llDateRange -> {
                chooseRangeDateDialog()
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun openIncreaseDecreaseGuestDialog(i: Int) {
        val dialogBinding = DialogUpdateGuestBinding.inflate(LayoutInflater.from(this))

        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setView(dialogBinding.root)

        val alertDialog: AlertDialog = alertDialogBuilder.create()
        alertDialog.show()

        var totalAdults = 1
        var totalChildren = 0
        val maxAdults = 4
        val maxChildren = 2

        alertDialog.setCancelable(false)

        dialogBinding.apply {

            selectChildAgeAdapter = SelectChildAgeAdapter(this@RoomReservationActivity, this@RoomReservationActivity)
            rvSelectChildAge.layoutManager =
                LinearLayoutManager(this@RoomReservationActivity, RecyclerView.VERTICAL, false)
            rvSelectChildAge.adapter = selectChildAgeAdapter

            when (i) {
                1 -> {
                    tvAdults.text = "${roomWiseGuestList[0].adults}"
                    tvChildren.text = "${roomWiseGuestList[0].children}"
                    totalAdults = roomWiseGuestList[0].adults!!
                    totalChildren = roomWiseGuestList[0].children!!
                }

                2 -> {
                    tvAdults.text = "${roomWiseGuestList[1].adults}"
                    tvChildren.text = "${roomWiseGuestList[1].children}"
                    totalAdults = roomWiseGuestList[1].adults!!
                    totalChildren = roomWiseGuestList[1].children!!
                }

                3 -> {
                    tvAdults.text = "${roomWiseGuestList[2].adults}"
                    tvChildren.text = "${roomWiseGuestList[2].children}"
                    totalAdults = roomWiseGuestList[2].adults!!
                    totalChildren = roomWiseGuestList[2].children!!
                }
            }


            ivPlusAdults.setOnClickListener {
                if (totalAdults >= maxAdults)
                    return@setOnClickListener

                totalAdults++

                tvAdults.text = "$totalAdults"
            }
            ivMinusAdults.setOnClickListener {
                if (totalAdults == 1)
                    return@setOnClickListener

                totalAdults--
                tvAdults.text = "$totalAdults"
            }
            ivPlusChildren.setOnClickListener {
                if (totalChildren >= maxChildren)
                    return@setOnClickListener

                totalChildren++
                tvChildren.text = "$totalChildren"
                selectChildAgeAdapter.addChild()
            }
            ivMinusChildren.setOnClickListener {
                if (totalChildren == 0)
                    return@setOnClickListener

                totalChildren--
                tvChildren.text = "$totalChildren"
                selectChildAgeAdapter.removeChild()
            }

            tvApply.setOnClickListener {
                // Update only the selected room's data
                roomWiseGuestList[i - 1].adults = totalAdults
                roomWiseGuestList[i - 1].children = totalChildren

                // Update UI for the selected position
                when (i) {
                    1 -> {
                        binding.tvTotalGuestsFirst.text = "$totalAdults ${getString(R.string.adults)}${if (totalChildren > 0) ", $totalChildren Child" else ""}"

                        if (bookRoomList.size > 0) {
                            val bookRoom = bookRoomList[0]
                            bookRoom.adults = totalAdults
                            bookRoom.children = totalChildren
                            bookRoom.roomID = ""
                            bookRoom.roomCode = ""
                            bookRoom.rateTitle = ""
                            bookRoom.rateCode = ""
                            bookRoom.roomTitle = getString(R.string.room_1_not_selected)
                            bookRoom.selectedRate = ""
                            bookRoom.price = 0.0
                            bookRoom.totalPrice = 0.0
                            bookRoom.taxPercentage = 0
                            bookRoom.taxAmount = 0.0
                            bookRoom.roomRatePerDay = 0.0
                            bookRoom.hotelImageURL = ""
                            bookRoom.dayPrice.clear()
                        }
                        isFirstRoomSelected = false
                        if (!binding.btnSelectRoom.isVisible) {
                            binding.btnSelectRoom.makeVisible()
                        }
                        searchHotel.rooms = roomWiseGuestList.size
                        binding.tvRoomNameFirst.text = getString(R.string.room_1_not_selected)
                        binding.tvRoomRatesFirst.text = getString(R.string.str_0_inr)
                        binding.ivHotel.setImageURI("")
                        setUIData()
                    }

                    2 -> {
                        binding.tvTotalGuestsSecond.text = "$totalAdults ${getString(R.string.adults)}${if (totalChildren > 0) ", $totalChildren Child" else ""}"

                        if (bookRoomList.size > 1) {
                            val bookRoom = bookRoomList[1]
                            bookRoom.adults = totalAdults
                            bookRoom.children = totalChildren
                            bookRoom.roomID = ""
                            bookRoom.roomCode = ""
                            bookRoom.rateTitle = ""
                            bookRoom.rateCode = ""
                            bookRoom.roomTitle = getString(R.string.room_2_not_selected)
                            bookRoom.selectedRate = ""
                            bookRoom.price = 0.0
                            bookRoom.totalPrice = 0.0
                            bookRoom.taxPercentage = 0
                            bookRoom.taxAmount = 0.0
                            bookRoom.roomRatePerDay = 0.0
                            bookRoom.hotelImageURL = ""
                            bookRoom.dayPrice.clear()
                        }
                        isSecondRoomSelected = false
                        if (!binding.btnSelectRoomSecond.isVisible) {
                            binding.btnSelectRoomSecond.makeVisible()
                        }

                        searchHotel.rooms = roomWiseGuestList.size
                        binding.tvRoomNameSecond.text = getString(R.string.room_2_not_selected)
                        binding.tvRoomRatesSecond.text = getString(R.string.str_0_inr)
                        binding.ivHotelSecond.setImageURI("")
                        setUIData()
                    }

                    3 -> {
                        binding.tvTotalGuestsThird.text = "$totalAdults ${getString(R.string.adults)}${if (totalChildren > 0) ", $totalChildren ${getString(R.string.child)}" else ""}"

                        if (bookRoomList.size > 2) {
                            val bookRoom = bookRoomList[2]
                            bookRoom.adults = totalAdults
                            bookRoom.children = totalChildren
                            bookRoom.roomID = ""
                            bookRoom.roomCode = ""
                            bookRoom.rateTitle = ""
                            bookRoom.rateCode = ""
                            bookRoom.roomTitle = getString(R.string.room_3_not_selected)
                            bookRoom.selectedRate = ""
                            bookRoom.price = 0.0
                            bookRoom.totalPrice = 0.0
                            bookRoom.taxPercentage = 0
                            bookRoom.taxAmount = 0.0
                            bookRoom.roomRatePerDay = 0.0
                            bookRoom.hotelImageURL = ""
                            bookRoom.dayPrice.clear()
                        }
                        isThirdRoomSelected = false
                        if (!binding.btnSelectRoomThird.isVisible) {
                            binding.btnSelectRoomThird.makeVisible()
                        }
                        searchHotel.rooms = roomWiseGuestList.size
                        binding.tvRoomNameThird.text = getString(R.string.room_3_not_selected)
                        binding.tvRoomRatesThird.text = getString(R.string.str_0_inr)
                        binding.ivHotelThird.setImageURI("")
                        setUIData()
                    }
                }

                alertDialog.dismiss()
            }

        }

        alertDialog.window!!.setLayout(
            (resources.displayMetrics.widthPixels * 0.9).toInt(),
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        alertDialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)

        val window = alertDialog.window!!
        val layoutParams = window.attributes
        layoutParams.gravity = Gravity.BOTTOM
        layoutParams.y = 0
        window.attributes = layoutParams
    }

    private fun openPrimaryGuestDetailsDialog() {

        val endDateTimeInMillis = getTimeInMillis(searchHotel.checkOut.toString(), "EEE MMM dd HH:mm:ss zzz yyyy")
        val startDateTimeInMillis = getTimeInMillis(searchHotel.checkIn.toString(), "EEE MMM dd HH:mm:ss zzz yyyy")
        val differenceInMillis = endDateTimeInMillis - startDateTimeInMillis
        val daysSelected = (differenceInMillis / (1000 * 60 * 60 * 24)).toInt()

        val totalAmount = bookRoomList.sumOf { it.totalPrice } + bookRoomList.sumOf { it.taxAmount }
        val roundedTotalAmount = floor(totalAmount).toInt()
        val roundOffAmount = totalAmount - roundedTotalAmount

        val bookingHotelRequest = BookingHotelRequest()
        bookingHotelRequest.bookRoom = bookRoomList
        bookingHotelRequest.customerID = Pref.getStringValue(Pref.PREF_USER_ID, "")
        bookingHotelRequest.memberType = "customer"
        bookingHotelRequest.hotelID = searchHotel.hotelID.toString()
        bookingHotelRequest.hotel = hotelName
        bookingHotelRequest.checkIn = Util.formatTimestamp(
            searchHotel.checkIn!!,
            "EEE MMM dd HH:mm:ss zzz yyyy",
            "yyyy-MM-dd"
        )
        bookingHotelRequest.checkOut = Util.formatTimestamp(
            searchHotel.checkOut!!,
            "EEE MMM dd HH:mm:ss zzz yyyy",
            "yyyy-MM-dd"
        )
        bookingHotelRequest.numberOfDays = daysSelected
        bookingHotelRequest.totalRoomPrice = bookRoomList.sumOf { it.totalPrice }
        bookingHotelRequest.taxAmount = bookRoomList.sumOf { it.taxAmount }
        bookingHotelRequest.totalPayableAmount = roundedTotalAmount.toDouble()
        bookingHotelRequest.bookingBy = "android"
        bookingHotelRequest.paymentOption = payOption

        val bottomSheet = BottomSheetPrimaryGuestDialogFragment(
            bookingHotelRequest,
            onApplyClick = { data: BookingHotelRequest ->
                val intent = Intent(this@RoomReservationActivity, ReviewReservationActivity::class.java)
                intent.putExtra("bookingHotelRequest", data)
                startActivity(intent)
                finish()
            })
        bottomSheet.show(supportFragmentManager, "PrimaryGuestBottomSheet")
    }


    private fun PrimaryGuestDetailsDialogBinding.validateFields(): Boolean {
        return edtFirstName.text.toString().trim().isNotEmpty() &&
                edtLastName.text.toString().trim().isNotEmpty() &&
                edtEmailId.text.toString().trim().isNotEmpty() &&
                edtPhoneNumber.text.toString().trim().isNotEmpty() &&
                Util.isValidEmail(edtEmailId.text.toString().trim()) &&
                edtPhoneNumber.text.toString().trim().length == 10 &&
                isAgree
    }

    private fun PrimaryGuestDetailsDialogBinding.checkFieldsAndEnableButton() {
        btnSubmit.isEnabled = validateFields()
        btnSubmit.alpha = if (validateFields()) 1F else 0.4F
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

            btnBookForToNight.text = "${getString(R.string.continue_with)} $daysSelected ${getString(R.string.nights_small)}"

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
                            btnBookForToNight.text = "${getString(R.string.continue_with)} $newDaysSelected ${getString(R.string.nights_small)}"
                            btnBookForToNight.isEnabled = true
                            btnBookForToNight.alpha = 1.0F
                        }
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

                    bookRoomList.clear()
                    roomWiseGuestList.clear()

                    val checkIn = Util.formatTimestamp(
                        searchHotel.checkIn!!,
                        "EEE MMM dd HH:mm:ss zzz yyyy",
                        "dd"
                    )
                    val checkOut = Util.formatTimestamp(
                        searchHotel.checkOut!!,
                        "EEE MMM dd HH:mm:ss zzz yyyy",
                        "dd"
                    )

                    binding.tvCheckInDate.text = checkIn
                    binding.tvCheckOutDate.text = checkOut
                    binding.tvCheckInWeek.text = Util.formatTimestamp(
                        searchHotel.checkIn!!,
                        "EEE MMM dd HH:mm:ss zzz yyyy",
                        "EEE"
                    )
                    binding.tvCheckOutWeek.text = Util.formatTimestamp(
                        searchHotel.checkOut!!,
                        "EEE MMM dd HH:mm:ss zzz yyyy",
                        "EEE"
                    )
                    binding.tvCheckInMonth.text = Util.formatTimestamp(
                        searchHotel.checkIn!!,
                        "EEE MMM dd HH:mm:ss zzz yyyy",
                        "MMM"
                    )
                    binding.tvCheckOutMonth.text = Util.formatTimestamp(
                        searchHotel.checkOut!!,
                        "EEE MMM dd HH:mm:ss zzz yyyy",
                        "MMM"
                    )

                    val endDateTimeInMillis = getTimeInMillis(searchHotel.checkOut.toString(), "EEE MMM dd HH:mm:ss zzz yyyy")
                    val startDateTimeInMillis = getTimeInMillis(searchHotel.checkIn.toString(), "EEE MMM dd HH:mm:ss zzz yyyy")

                    val differenceInMillis = endDateTimeInMillis - startDateTimeInMillis
                    val daysSelected = (differenceInMillis / (1000 * 60 * 60 * 24)).toInt()

                    binding.tvNoOfNights.text = "$daysSelected ${getString(R.string.nights)}"

                    binding.tvTotalGuestsFirst.text = searchHotel.adults.toString() + getString(R.string.adults) + searchHotel.children?.let {
                        if (it > 0) {
                            searchHotel.children.toString() + getString(R.string.child)
                        } else ""
                    }
                    binding.llFirstHotel.makeVisible()

                    if (searchHotel.rooms == 3) {
                        val roomWiseGuest = RoomWiseGuest()
                        roomWiseGuest.adults = searchHotel.adults
                        roomWiseGuest.children = searchHotel.children
                        roomWiseGuestList.clear()
                        roomWiseGuestList.add(roomWiseGuest)
                        roomWiseGuestList.add(roomWiseGuest)
                        roomWiseGuestList.add(roomWiseGuest)

                        binding.tvTotalGuestsFirst.text = roomWiseGuestList[0].adults.toString() + getString(R.string.adults) + roomWiseGuestList[0].children?.let {
                            if (it > 0) {
                                roomWiseGuestList[0].children.toString() + getString(R.string.child)
                            } else ""
                        }
                        binding.llFirstHotel.makeVisible()

                        binding.tvTotalGuestsSecond.text = roomWiseGuestList[1].adults.toString() + getString(R.string.adults) + roomWiseGuestList[1].children?.let {
                            if (it > 0) {
                                roomWiseGuestList[1].children.toString() + getString(R.string.child)
                            } else ""
                        }
                        binding.llSecondHotel.makeVisible()

                        binding.tvTotalGuestsThird.text = roomWiseGuestList[2].adults.toString() + getString(R.string.adults) + roomWiseGuestList[2].children?.let {
                            if (it > 0) {
                                roomWiseGuestList[2].children.toString() +getString(R.string.child)
                            } else ""
                        }
                        binding.llThirdHotel.makeVisible()

                    } else if (searchHotel.rooms == 2) {
                        val roomWiseGuest = RoomWiseGuest()
                        roomWiseGuest.adults = searchHotel.adults
                        roomWiseGuest.children = searchHotel.children
                        roomWiseGuestList.clear()
                        roomWiseGuestList.add(roomWiseGuest)
                        roomWiseGuestList.add(roomWiseGuest)

                        binding.tvTotalGuestsFirst.text = roomWiseGuestList[0].adults.toString() + getString(R.string.adults) + roomWiseGuestList[0].children?.let {
                            if (it > 0) {
                                roomWiseGuestList[0].children.toString() + getString(R.string.child)
                            } else ""
                        }
                        binding.llFirstHotel.makeVisible()

                        binding.tvTotalGuestsSecond.text = roomWiseGuestList[1].adults.toString() + getString(R.string.adults) + roomWiseGuestList[1].children?.let {
                            if (it > 0) {
                                roomWiseGuestList[1].children.toString() +getString(R.string.child)
                            } else ""
                        }
                        binding.llSecondHotel.makeVisible()


                    } else {
                        val roomWiseGuest = RoomWiseGuest()
                        roomWiseGuest.adults = searchHotel.adults
                        roomWiseGuest.children = searchHotel.children
                        roomWiseGuestList.clear()
                        roomWiseGuestList.add(roomWiseGuest)

                        binding.tvTotalGuestsFirst.text = roomWiseGuestList[0].adults.toString() + getString(R.string.adults) + roomWiseGuestList[0].children?.let {
                            if (it > 0) {
                                roomWiseGuestList[0].children.toString() + getString(R.string.child)
                            } else ""
                        }
                        binding.llFirstHotel.makeVisible()

                    }

                    searchHotel.roomWiseGuest = roomWiseGuestList

                    setUIData()
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