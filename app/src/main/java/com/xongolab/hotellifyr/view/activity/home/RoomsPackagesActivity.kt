package com.xongolab.hotellifyr.view.activity.home

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.xongolab.hotellifyr.R
import com.xongolab.hotellifyr.core.CoreActivity
import com.xongolab.hotellifyr.databinding.ActivityRoomsPackagesBinding
import com.xongolab.hotellifyr.databinding.DialogRateDetailsBinding
import com.xongolab.hotellifyr.databinding.DialogRoomDetailsBinding
import com.xongolab.hotellifyr.model.BookRoomRequest
import com.xongolab.hotellifyr.model.OfferRoomPriceModel
import com.xongolab.hotellifyr.model.SearchHotel
import com.xongolab.hotellifyr.utils.Constants
import com.xongolab.hotellifyr.utils.Pref
import com.xongolab.hotellifyr.utils.Util
import com.xongolab.hotellifyr.view.activity.auth.LoginActivity
import com.xongolab.hotellifyr.view.adapter.HotelAmenitiesAdapter
import com.xongolab.hotellifyr.view.adapter.RateDescriptionAdapter
import com.xongolab.hotellifyr.view.adapter.RoomDetailsListAdapter
import com.xongolab.hotellifyr.view.adapter.SmallImagesAdapter
import com.xongolab.hotellifyr.viewModel.HotelViewModel
import com.xongolab.hotellifyr.viewModel.ViewModelFactory

class RoomsPackagesActivity : CoreActivity() {
    private lateinit var binding: ActivityRoomsPackagesBinding
    private lateinit var hotelViewModel: HotelViewModel

    private lateinit var roomDetailsListAdapter: RoomDetailsListAdapter
    private var searchHotel = SearchHotel()
    var hotelName: String = ""
    var paymentOption: String = ""
    var bookRoomRequest = BookRoomRequest()
    var taxPercentage: Int = 0
    var clickIndex: Int = 0
    var bookRoomList: ArrayList<BookRoomRequest> = arrayListOf()

    var children: Int = 0
    var adults: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRoomsPackagesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setFullScreenMode(window)

        initView()
    }


    @SuppressLint("SetTextI18n")
    private fun initView() {
        initViewModel()

        if (intent != null) {
            searchHotel = Gson().fromJson(
                intent.getStringExtra(Constants.SEARCH_HOTEL),
                SearchHotel::class.java
            )
            hotelName = intent.getStringExtra("hotelName").toString()
            paymentOption = intent.getStringExtra("paymentOption").toString()
            bookRoomList = intent.getSerializableExtra("bookRoomList") as ArrayList<BookRoomRequest>
            clickIndex = intent.getIntExtra("clickIndex", 0)
        }
        binding.toolbarTitle.text = hotelName

        val checkIn = Util.formatTimestamp(
            searchHotel.checkIn!!,
            "EEE MMM dd HH:mm:ss zzz yyyy",
            "dd MMM"
        )
        val checkOut = Util.formatTimestamp(
            searchHotel.checkOut!!,
            "EEE MMM dd HH:mm:ss zzz yyyy",
            "dd MMM"
        )

        when (clickIndex) {
            1 -> {
                searchHotel.roomWiseGuest?.get(0)?.adults?.let { adults = it.toInt() }
                searchHotel.roomWiseGuest?.get(0)?.children?.let { children = it.toInt() }
            }

            2 -> {
                searchHotel.roomWiseGuest?.get(1)?.adults?.let { adults = it.toInt() }
                searchHotel.roomWiseGuest?.get(1)?.children?.let { children = it.toInt() }
            }

            3 -> {
                searchHotel.roomWiseGuest?.get(2)?.adults?.let { adults = it.toInt() }
                searchHotel.roomWiseGuest?.get(2)?.children?.let { children = it.toInt() }
            }
        }

        binding.tvDateRange.text = "$checkIn - $checkOut"
        binding.btnBack.setOnClickListener(this)

        roomDetailsListAdapter = RoomDetailsListAdapter(this)
        binding.rvRoomDetailsList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvRoomDetailsList.adapter = roomDetailsListAdapter

        roomDetailsListAdapter.apply {
            onItemRoomDetailsClick = { _, item ->
                roomDetailsDialog(item)
            }

            onItemSelectRoomClick = { _, getBookRoomRequest, roomPrice, isStandardRate ->
                if (!Pref.getBooleanValue(Pref.PREF_IS_LOGIN, false) && !isStandardRate) {
                    val intent = Intent(this@RoomsPackagesActivity, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    bookRoomRequest = getBookRoomRequest
                    bookRoomRequest.adults = adults
                    bookRoomRequest.children = children

                    when (bookRoomList.size) {
                        0 -> {
                            bookRoomRequest.roomIndex = 1
                        }

                        1 -> {
                            bookRoomRequest.roomIndex = 2
                        }

                        2 -> {
                            bookRoomRequest.roomIndex = 3
                        }
                    }
                    when (clickIndex) {
                        1 -> {
                            if (bookRoomList.size > 0) {
                                bookRoomList[0] = bookRoomRequest
                            } else {
                                bookRoomList.add(bookRoomRequest)
                            }
                        }

                        2 -> {
                            if (bookRoomList.size > 1) {
                                bookRoomList[1] = bookRoomRequest
                            } else {
                                bookRoomList.add(bookRoomRequest)
                            }
                        }

                        3 -> {
                            if (bookRoomList.size > 2) {
                                bookRoomList[2] = bookRoomRequest
                            } else {
                                bookRoomList.add(bookRoomRequest)
                            }
                        }
                    }
                    // bookRoomList.add(bookRoomRequest)
                    val intent = Intent(this@RoomsPackagesActivity, RoomReservationActivity::class.java)
                    intent.putExtra("bookRoomList", bookRoomList)
                    intent.putExtra(Constants.SEARCH_HOTEL, Gson().toJson(searchHotel))
                    intent.putExtra("hotelName", hotelName)
                    intent.putExtra("paymentOption", paymentOption)
                    startActivity(intent)
                    finish()
                }
            }

            onItemRateDetailsClick = { _, item ->
                rateDetailsDialog(item)
            }
        }

        getOfferRoomPriceApi()

        binding.switchAvailabilityStatus.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                roomDetailsListAdapter.isStandard()
            } else {
                roomDetailsListAdapter.isUnStandard()
            }
        }
    }

    private fun initViewModel() {
        hotelViewModel =
            ViewModelProvider(this, ViewModelFactory(mainRepository))[HotelViewModel::class.java]
        observeViewModel()
    }

    @SuppressLint("SetTextI18n")
    private fun observeViewModel() {
        hotelViewModel.getOfferRoomPriceApiResponse.observe(this) { response ->
            response?.let {
                roomDetailsListAdapter.addData(it.payload)
            }
        }
        hotelViewModel.getTaxPercentageApiResponse.observe(this) { response ->
            response?.let {
                taxPercentage = it.payload!!.taxPercentage
                val taxPrice = (bookRoomRequest.price * taxPercentage) / 100
                bookRoomRequest.taxPercentage = taxPercentage
                bookRoomRequest.taxAmount = taxPrice
                bookRoomRequest.adults = adults
                bookRoomRequest.children = children

                when (bookRoomList.size) {
                    0 -> {
                        bookRoomRequest.roomIndex = 1
                    }

                    1 -> {
                        bookRoomRequest.roomIndex = 2
                    }

                    2 -> {
                        bookRoomRequest.roomIndex = 3
                    }
                }
                bookRoomList.add(bookRoomRequest)
                val intent = Intent(this@RoomsPackagesActivity, RoomReservationActivity::class.java)
                intent.putExtra("bookRoomList", bookRoomList)
                intent.putExtra(Constants.SEARCH_HOTEL, Gson().toJson(searchHotel))
                intent.putExtra("hotelName", hotelName)
                intent.putExtra("paymentOption", paymentOption)
                startActivity(intent)
                finish()
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun getOfferRoomPriceApi() {
        if (isInternetConnected()) {
            val checkIn = Util.formatTimestamp(
                searchHotel.checkIn!!,
                "EEE MMM dd HH:mm:ss zzz yyyy",
                "YYYY-MM-dd"
            )
            val checkOut = Util.formatTimestamp(
                searchHotel.checkOut!!,
                "EEE MMM dd HH:mm:ss zzz yyyy",
                "YYYY-MM-dd"
            )

            hotelViewModel.getOfferRoomPriceApi(
                this,
                searchHotel.hotelID!!,
                checkIn,
                checkOut,
                adults,
                children
            )
        } else {
            msgDialog(getString(R.string.check_internet))
        }
    }

    @SuppressLint("SetTextI18n")
    private fun getTaxPercentageApi(roomPrice: Double) {
        if (isInternetConnected()) {
            hotelViewModel.getTaxPercentageApi(this, roomPrice)
        } else {
            msgDialog(getString(R.string.check_internet))
        }
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.btnBack -> finish()
        }
    }

    private fun rateDetailsDialog(item: OfferRoomPriceModel.OfferPrice) {
        val rateDetailsBinding = Dialog(this)
        val dialogBinding = DialogRateDetailsBinding.inflate(layoutInflater)
        rateDetailsBinding.setContentView(dialogBinding.root)
        rateDetailsBinding.setCanceledOnTouchOutside(false)
        rateDetailsBinding.setCancelable(false)

        dialogBinding.apply {
            ivClose.setOnClickListener {
                rateDetailsBinding.dismiss()
            }

            tvName.text = item.title
            tvCancellationPolicy.text = item.rateDetails.cancellationPolicy
            tvGuaranteePolicy.text = item.rateDetails.guaranteePolicy

            val rateDescriptionAdapter =
                RateDescriptionAdapter(this@RoomsPackagesActivity)
            dialogBinding.rvDescription.layoutManager =
                LinearLayoutManager(this@RoomsPackagesActivity, RecyclerView.VERTICAL, false)
            dialogBinding.rvDescription.adapter = rateDescriptionAdapter

            rateDescriptionAdapter.addData(item.rateDetails.rateDescription)
        }

        val displayMetrics = resources.displayMetrics
        val width = (displayMetrics.widthPixels * 0.95).toInt()

        val window = rateDetailsBinding.window
        val params = window?.attributes
        params?.width = width
        window?.attributes = params

        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        rateDetailsBinding.show()
    }

    @SuppressLint("SetTextI18n")
    private fun roomDetailsDialog(item: OfferRoomPriceModel) {
        val roomDetailsDialog = Dialog(this)
        val dialogBinding = DialogRoomDetailsBinding.inflate(layoutInflater)
        roomDetailsDialog.setContentView(dialogBinding.root)
        roomDetailsDialog.setCanceledOnTouchOutside(false)
        roomDetailsDialog.setCancelable(false)

        dialogBinding.apply {
            ivClose.setOnClickListener {
                roomDetailsDialog.dismiss()
            }

            // 👉 Default first image should show in imgMain
            if (item.images.isNotEmpty()) {
                Glide.with(this@RoomsPackagesActivity)
                    .load(item.images[0])
                    .placeholder(R.drawable.ic_placeholder_rectangle)
                    .into(imgMain)
            }

            // Setup small image list with click callback
            val imgAdapter = SmallImagesAdapter(this@RoomsPackagesActivity) { selectedImage ->
                Glide.with(this@RoomsPackagesActivity)
                    .load(selectedImage)
                    .placeholder(R.drawable.ic_placeholder_rectangle)
                    .into(imgMain)
            }

            rvImage.layoutManager = LinearLayoutManager(this@RoomsPackagesActivity, LinearLayoutManager.HORIZONTAL, false)
            rvImage.adapter = imgAdapter
            imgAdapter.setData(item.images)

            rvImage.visibility = if (item.images.size > 1) View.VISIBLE else View.GONE

            tvName.text = item.title
            tvArea.text = "${item.area} m2"
            tvGuests.text = "${item.guest} Guests"
            tvBeds.text = "${item.beds} Beds"
            tvBathroom.text = "${item.bathroom} Bathroom"

            val hotelAmenitiesAdapter = HotelAmenitiesAdapter(this@RoomsPackagesActivity)
            dialogBinding.rvAmenities.layoutManager =
                GridLayoutManager(this@RoomsPackagesActivity, 2)
            dialogBinding.rvAmenities.adapter = hotelAmenitiesAdapter

            hotelAmenitiesAdapter.addData(item.amenities)

            val included = arrayListOf(
                "Private balcony",
                "140x200 cm Elite bed",
                "Upholstered seat beside the panoramic window",
                "TV-UHD screen for watching mountaineering films",
                "Writing desk with USB ports for documenting your adventures",
                "Room safe for your top mountain photos",
                "Service station with Lavazza coffee machine, kettle and tea",
                "Bathroom with rain shower",
                "Comfortable terry towels and bathrobes"
            )

            val includeAdapter = RateDescriptionAdapter(this@RoomsPackagesActivity)
            dialogBinding.rvIncluded.layoutManager =
                LinearLayoutManager(this@RoomsPackagesActivity, RecyclerView.VERTICAL, false)
            dialogBinding.rvIncluded.adapter = includeAdapter

            includeAdapter.addData(included)
        }

        val displayMetrics = resources.displayMetrics
        val width = (displayMetrics.widthPixels * 0.95).toInt()
        val height = (displayMetrics.heightPixels * 0.95).toInt()

        val window = roomDetailsDialog.window
        val params = window?.attributes
        params?.width = width
        //  params?.height = height
        window?.attributes = params

        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        roomDetailsDialog.show()
    }

}