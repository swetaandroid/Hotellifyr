package com.xongolab.hotellifyr.view.activity.home.search

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.archit.calendardaterangepicker.customviews.CalendarListener
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.SettingsClient
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.xongolab.hotellifyr.R
import com.xongolab.hotellifyr.core.CoreActivity
import com.xongolab.hotellifyr.databinding.ActivitySearchBinding
import com.xongolab.hotellifyr.databinding.BottomSearchCalendarBinding
import com.xongolab.hotellifyr.databinding.BottomSelectRoomGuestBinding
import com.xongolab.hotellifyr.model.HotelModel
import com.xongolab.hotellifyr.model.ResortModel
import com.xongolab.hotellifyr.model.SearchHotel
import com.xongolab.hotellifyr.utils.Constants
import com.xongolab.hotellifyr.utils.DialogManager
import com.xongolab.hotellifyr.utils.makeGone
import com.xongolab.hotellifyr.utils.makeVisible
import com.xongolab.hotellifyr.view.activity.home.search.hotel.HotelDetailActivity
import com.xongolab.hotellifyr.view.activity.home.search.hotel.HotelListViewActivity
import com.xongolab.hotellifyr.view.adapter.GuestsPerRoomAdapter
import com.xongolab.hotellifyr.view.adapter.NewlyOpenedAdapter
import com.xongolab.hotellifyr.view.adapter.PopularHotelAdapter
import com.xongolab.hotellifyr.view.adapter.RecentlySearchAdapter
import com.xongolab.hotellifyr.viewModel.HotelViewModel
import com.xongolab.hotellifyr.viewModel.ViewModelFactory
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


class SearchActivity : CoreActivity() {

    private lateinit var binding: ActivitySearchBinding

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private lateinit var dialogManager: DialogManager
    private lateinit var newlyOpenedAdapter: NewlyOpenedAdapter
    private lateinit var recentlySearchAdapter: RecentlySearchAdapter

    private lateinit var hotelViewModel: HotelViewModel

    private lateinit var citySearchAdapter: PopularHotelAdapter
    private lateinit var hotelSearchAdapter: PopularHotelAdapter

    private var hotelResultsFound = false
    private var cityResultsFound = false
    private var searchHotel = SearchHotel()

    // TODO : Bottom Search Calendar
    private lateinit var bottomCalendarBinding: BottomSearchCalendarBinding
    private var calendarDialog: BottomSheetDialog? = null

    private val displayFormat = SimpleDateFormat("MMM dd", Locale.getDefault())

    val inputFormat = SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.getDefault())
    val dateFormatMonth = SimpleDateFormat("MMM", Locale.getDefault())
    val dateFormatDayOfWeek = SimpleDateFormat("EEE", Locale.getDefault())
    val dateFormatDayOfMonth = SimpleDateFormat("dd", Locale.getDefault())
    var checkInDate: String = ""
    var checkOutDate: String = ""


    // TODO : Bottom select room and guest
    private lateinit var guestsPerRoomAdapter: GuestsPerRoomAdapter
    private var totalRooms = 1
    private var totalAdults = 1
    private var totalChildren = 0
    private var maxAdults = 4
    private var maxChildren = 2
    private lateinit var bottomRoomGuestBinding: BottomSelectRoomGuestBinding
    private var roomGuestDialog: BottomSheetDialog? = null

    private var isItemSelected = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setFullScreenMode(window)
        initView()
    }

    private fun initView() {
        initViewModel()

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        dialogManager = DialogManager(this)

        binding.btnBack.setOnClickListener(this)
        binding.llNearby.setOnClickListener(this)
        binding.icLocationSearch.setOnClickListener(this)
        binding.llFromToDate.setOnClickListener(this)
        binding.llGuest.setOnClickListener(this)
        binding.btnDiscoverHotels.setOnClickListener(this)

        // Recent Search
        recentlySearchAdapter = RecentlySearchAdapter(this) {
            binding.rvRecentSearch.visibility = View.GONE
            binding.tvNoData.visibility = View.VISIBLE
        }
        binding.rvRecentSearch.layoutManager =
            LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        binding.rvRecentSearch.adapter = recentlySearchAdapter

        recentlySearchAdapter.onItemClick = { _, item ->
            isItemSelected = true   // 👈 important

            binding.edtSearch.setText(item.title)
//            val searchHotel = SearchHotel()
            searchHotel.cityID = item.id
            searchHotel.location = item.title
            searchHotel.type = Constants.CITY

            val json = GsonBuilder().setPrettyPrinting().create().toJson(searchHotel)
            println("SEARCH_HOTEL...setPrettyPrinting JSON 1..." + json)

            /*val intent = Intent(this, SearchCalendarActivity::class.java)
            intent.putExtra(Constants.SEARCH_HOTEL, Gson().toJson(searchHotel))
            startActivity(intent)*/

            // 👉 Hide search UI & restore default
            binding.llRecently.makeVisible()
            binding.rlSearch.makeGone()

            // 👉 Optional: clear filter
            hotelSearchAdapter.filter.filter("")
            citySearchAdapter.filter.filter("")

            // 👉 Remove focus (important)
            binding.edtSearch.clearFocus()

            bottomSearchCalendar()
        }

        binding.edtSearch.requestFocus()

        // Recently Viewed
        newlyOpenedAdapter = NewlyOpenedAdapter(this)
        binding.rvNewlyOpened.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        binding.rvNewlyOpened.adapter = newlyOpenedAdapter

        newlyOpenedAdapter.onItemClick = { _, item ->
            isItemSelected = true   // 👈 important

            binding.edtSearch.setText(item.name)
//            val searchHotel = SearchHotel()
            searchHotel.hotelID = item.id
            searchHotel.location = item.address
            searchHotel.type = Constants.HOTEL

            val json = GsonBuilder().setPrettyPrinting().create().toJson(searchHotel)
            println("SEARCH_HOTEL...setPrettyPrinting JSON 2..." + json)

            /*val intent = Intent(this, SearchCalendarActivity::class.java)
            intent.putExtra(Constants.SEARCH_HOTEL, Gson().toJson(searchHotel))
            startActivity(intent)*/

            // 👉 Hide search UI & restore default
            binding.llRecently.makeVisible()
            binding.rlSearch.makeGone()

            // 👉 Optional: clear filter
            hotelSearchAdapter.filter.filter("")
            citySearchAdapter.filter.filter("")

            // 👉 Remove focus (important)
            binding.edtSearch.clearFocus()

            bottomSearchCalendar()
        }

        citySearchAdapter = PopularHotelAdapter(this)
        binding.rvPopularCities.layoutManager =
            LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        binding.rvPopularCities.adapter = citySearchAdapter

        citySearchAdapter.onItemClick = { _, item ->
            isItemSelected = true   // 👈 important

            binding.edtSearch.setText(item.title)
//            val searchHotel = SearchHotel()
            searchHotel.cityID = item.id
            searchHotel.location = item.title
            searchHotel.type = Constants.CITY

            val json = GsonBuilder().setPrettyPrinting().create().toJson(searchHotel)
            println("SEARCH_HOTEL...setPrettyPrinting JSON 3..." + json)

            /*val intent = Intent(this, SearchCalendarActivity::class.java)
            intent.putExtra(Constants.SEARCH_HOTEL, Gson().toJson(searchHotel))
            startActivity(intent)*/

            // 👉 Hide search UI & restore default
            binding.llRecently.makeVisible()
            binding.rlSearch.makeGone()

            // 👉 Optional: clear filter
            hotelSearchAdapter.filter.filter("")
            citySearchAdapter.filter.filter("")

            // 👉 Remove focus (important)
            binding.edtSearch.clearFocus()

            bottomSearchCalendar()
        }

        hotelSearchAdapter = PopularHotelAdapter(this)
        binding.rvPopularHotels.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        binding.rvPopularHotels.adapter = hotelSearchAdapter

        hotelSearchAdapter.onItemClick = { _, item ->
            isItemSelected = true   // 👈 important

            binding.edtSearch.setText(item.name)
//            val searchHotel = SearchHotel()
            searchHotel.hotelID = item.id
            searchHotel.location = item.address
            searchHotel.type = Constants.HOTEL

            val json = GsonBuilder().setPrettyPrinting().create().toJson(searchHotel)
            println("SEARCH_HOTEL...setPrettyPrinting JSON 4..." + json)

            /*val intent = Intent(this, SearchCalendarActivity::class.java)
            intent.putExtra(Constants.SEARCH_HOTEL, Gson().toJson(searchHotel))
            startActivity(intent)*/

            // 👉 Hide search UI & restore default
            binding.llRecently.makeVisible()
            binding.rlSearch.makeGone()

            // 👉 Optional: clear filter
            hotelSearchAdapter.filter.filter("")
            citySearchAdapter.filter.filter("")

            // 👉 Remove focus (important)
            binding.edtSearch.clearFocus()


            bottomSearchCalendar()
        }

        binding.edtSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            @SuppressLint("SetTextI18n")
            override fun afterTextChanged(s: Editable?) {

                if (isItemSelected) {
                    isItemSelected = false
                    return   // 👈 skip unwanted filtering
                }

                val searchText = s.toString()

                if (searchText.length > 1) {
                    binding.llRecently.makeGone()
                    binding.rlSearch.makeVisible()

                    binding.tvPopularCities.text = "Cities"
                    binding.tvPopularHotels.text = "Hotels"

                    citySearchAdapter.filter.filter(searchText)
                    hotelSearchAdapter.filter.filter(searchText)
                } else if (searchText.length == 1) {
                    binding.llRecently.makeGone()
                    binding.rlSearch.makeVisible()

                    binding.tvPopularCities.text = "Popular Cities"
                    binding.tvPopularHotels.text = "Popular Hotels"
                } else {
                    binding.llRecently.makeVisible()
                    binding.rlSearch.makeGone()

                    citySearchAdapter.filter.filter("")
                    hotelSearchAdapter.filter.filter("")
                }
            }
        })

        binding.edtSearch.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                binding.edtSearch.hint = "" // Hide hint on focus
            } else {
                if (binding.edtSearch.text.isNullOrEmpty()) {
                    binding.edtSearch.hint = getString(R.string.search_places)
                }
            }
        }

        hotelSearchAdapter.onFilterResultsListener = object : PopularHotelAdapter.OnFilterResultsListener {
            override fun onNoResultsFound() {
                hotelResultsFound = false
                updateVisibility()
            }

            override fun onResultsFound() {
                hotelResultsFound = true
                updateVisibility()
            }
        }

        citySearchAdapter.onFilterResultsListener = object : PopularHotelAdapter.OnFilterResultsListener {
            override fun onNoResultsFound() {
                cityResultsFound = false
                updateVisibility()
            }

            override fun onResultsFound() {
                cityResultsFound = true
                updateVisibility()
            }
        }

        getHotelListApi()


        val json = GsonBuilder().setPrettyPrinting().create().toJson(searchHotel)
        println("SEARCH_HOTEL...setPrettyPrinting JSON init..." + json)
    }

    @SuppressLint("SetTextI18n")
    override fun onClick(v: View) {
        when (v.id) {
            R.id.btnBack -> finish()
            R.id.llNearby -> requestLocationPermission()
            R.id.icLocationSearch -> requestLocationPermission()
            R.id.llFromToDate -> bottomSearchCalendar()
            R.id.llGuest -> bottomSelectRoomAndGuest()

            R.id.btnDiscoverHotels -> {
                if (binding.tvFromToDate.text.toString().trim().isEmpty()) {
                    msgDialog("Please select check in and check out date")
                } else if (binding.tvGuestNo.text.toString().trim().isEmpty()) {
                    msgDialog("Please select room and guest")
                } else {
                    if (searchHotel.type == Constants.HOTEL) {
                        val intent = Intent(this, HotelDetailActivity::class.java)
                        intent.putExtra(Constants.SEARCH_HOTEL, Gson().toJson(searchHotel))
                        startActivity(intent)
                    } else {
                        val intent = Intent(this, HotelListViewActivity::class.java)
                        intent.putExtra(Constants.SEARCH_HOTEL, Gson().toJson(searchHotel))
                        startActivity(intent)
                    }

                    val json = GsonBuilder().setPrettyPrinting().create().toJson(searchHotel)
                    println("SEARCH_HOTEL...setPrettyPrinting JSON on click..." + json)
                }
            }

            // TODO : Bottom Search Calendar
            R.id.btnBookForToNight -> {
                // throw RuntimeException("Test Crash") // Force a crash
                if (checkInDate.isEmpty() && checkOutDate.isEmpty()) {
                    msgDialog("Please select check in & check out date")
                } else if (checkInDate.isEmpty()) {
                    msgDialog("Please select check in date")
                } else if (checkOutDate.isEmpty()) {
                    msgDialog("Please select check out date")
                } else {
                    searchHotel.checkIn = checkInDate
                    searchHotel.checkOut = checkOutDate

                    /*val intent = Intent(this, SearchLocationDetailActivity::class.java)
                    intent.putExtra(Constants.SEARCH_HOTEL, Gson().toJson(searchHotel))
                    startActivity(intent)*/

                    try {
                        val checkIn: Date = inputFormat.parse(checkInDate)!!
                        val checkOut: Date = inputFormat.parse(checkOutDate)!!

                        val formattedCheckIn = displayFormat.format(checkIn)   // Jan 18
                        val formattedCheckOut = displayFormat.format(checkOut) // Jan 28

                        binding.tvFromToDate.text = "$formattedCheckIn to $formattedCheckOut"

                        calendarDialog?.dismiss()

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }

            // TODO : Select room guest
            R.id.tvClear -> {
                totalRooms = 1
                totalAdults = 1
                totalChildren = 0

                maxAdults = 4
                maxChildren = 2

                bottomRoomGuestBinding.tvRooms.text = "$totalRooms"
                bottomRoomGuestBinding.tvAdults.text = "$totalAdults"
                bottomRoomGuestBinding.tvChildren.text = "$totalChildren"
                bottomRoomGuestBinding.tvTotal.text = "${totalRooms} Room, ${totalAdults + totalChildren} Guest"
            }

            R.id.ivPlusRooms -> {
                if (totalRooms >= 3)
                    return

                totalRooms++

                maxAdults = totalRooms * 4
                maxChildren = totalRooms * 2

                bottomRoomGuestBinding.tvRooms.text = "$totalRooms"
                bottomRoomGuestBinding.tvTotal.text = "${totalRooms} Room, ${totalAdults + totalChildren} Guest"
            }

            R.id.ivMinusRooms -> {
                if (totalRooms == 1)
                    return

                totalRooms--

                maxAdults = totalRooms * 4
                maxChildren = totalRooms * 2

                if (totalAdults > maxAdults) {
                    totalAdults = maxAdults
                    bottomRoomGuestBinding.tvAdults.text = "$totalAdults"
                }

                if (totalChildren > maxChildren) {
                    totalChildren = maxChildren
                    bottomRoomGuestBinding.tvChildren.text = "$totalChildren"
                }

                bottomRoomGuestBinding.tvRooms.text = "$totalRooms"
                bottomRoomGuestBinding.tvTotal.text = "${totalRooms} Room, ${totalAdults + totalChildren} Guest"
            }

            R.id.ivPlusAdults -> {
                if (totalAdults >= maxAdults)
                    return

                totalAdults++

                bottomRoomGuestBinding.tvAdults.text = "$totalAdults"
                bottomRoomGuestBinding.tvTotal.text = "${totalRooms} Room, ${totalAdults + totalChildren} Guest"
            }

            R.id.ivMinusAdults -> {
                if (totalAdults == 1)
                    return

                totalAdults--
                bottomRoomGuestBinding.tvAdults.text = "$totalAdults"
                bottomRoomGuestBinding.tvTotal.text = "${totalRooms} Room, ${totalAdults + totalChildren} Guest"
            }

            R.id.ivPlusChildren -> {
                if (totalChildren >= maxChildren)
                    return

                totalChildren++
                bottomRoomGuestBinding.tvChildren.text = "$totalChildren"
                bottomRoomGuestBinding.tvTotal.text = "${totalRooms} Room, ${totalAdults + totalChildren} Guest"
            }

            R.id.ivMinusChildren -> {
                if (totalChildren == 0)
                    return

                totalChildren--
                bottomRoomGuestBinding.tvChildren.text = "$totalChildren"
                bottomRoomGuestBinding.tvTotal.text = "${totalRooms} Room, ${totalAdults + totalChildren} Guest"
            }

            R.id.btnApply -> {
                searchHotel.rooms = totalRooms
                searchHotel.adults = totalAdults
                searchHotel.children = totalChildren

                binding.tvGuestNo.text = "$totalAdults Adults $totalChildren Kids"
                roomGuestDialog?.dismiss()

            }
        }
    }

    private fun updateVisibility() {
        binding.apply {
            if (!hotelResultsFound && !cityResultsFound) {
                // Both have no results
                llSearchDataNotFound.makeVisible()
                llPopular.makeGone()
                /*tvPopularHotels.makeGone()
                rvPopularHotels.makeGone()
                tvPopularCities.makeGone()
                rvPopularCities.makeGone()*/
            } else {
                llSearchDataNotFound.makeGone()
                llPopular.makeVisible()
                // Handle the visibility based on each adapter's results
                if (hotelResultsFound) {
                    tvPopularHotels.makeVisible()
                    rvPopularHotels.makeVisible()
                } else {
                    tvPopularHotels.makeGone()
                    rvPopularHotels.makeGone()
                }

                if (cityResultsFound) {
                    tvPopularCities.makeVisible()
                    rvPopularCities.makeVisible()
                } else {
                    tvPopularCities.makeGone()
                    rvPopularCities.makeGone()
                }
            }
        }

    }

    private fun openLocationPermissionBottomSheet() {
        dialogManager.showCustomBottomSheetDialog(
            imageResId = R.drawable.ic_search_location,
            title = getString(R.string.turn_on_location_services),
            description = getString(R.string.share_your_location_to_discover_the_amazing_hotels_nearby),
            buttonText = getString(R.string.enable_now),
            showNotNow = true,
            onButtonClick = {
                requestLocationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
        )
    }

    private fun initViewModel() {
        hotelViewModel =
            ViewModelProvider(this, ViewModelFactory(mainRepository))[HotelViewModel::class.java]
        observeViewModel()
    }


    private fun observeViewModel() {
        hotelViewModel.getCityListApiResponse.observe(this) { response ->
            response?.let {
                if (it.payload.isNotEmpty()) {
                    val popularObjList: ArrayList<HotelModel> = ArrayList()
                    for (i in 0 until it.payload.size) {
                        if (it.payload[i].isPopular) {
                            popularObjList.add(it.payload[i])
                        }
                        it.payload[i].name = "Hotels in " + it.payload[i].title
                    }
                    citySearchAdapter.addData(it.payload)
                    citySearchAdapter.addPopularData(popularObjList)
                    recentlySearchAdapter.addData(popularObjList)
                } else {
                    // Handle the case where payload is empty
                }
            }
        }

        hotelViewModel.getHotelListApiResponse.observe(this) { response ->
            response?.let {
                if (it.payload.isNotEmpty()) {
                    val popularObjList: ArrayList<HotelModel> = ArrayList()
                    for (i in 0 until it.payload.size) {
                        if (it.payload[i].isPopular) {
                            popularObjList.add(it.payload[i])
                        }
                    }
                    hotelSearchAdapter.addData(it.payload)
                    hotelSearchAdapter.addPopularData(popularObjList)
                    newlyOpenedAdapter.addData(popularObjList)
                } else {
                    // Handle the case where payload is empty
                }
            }
        }
    }

    private fun getHotelListApi() {
        if (isInternetConnected()) {
            hotelViewModel.getCityListApi(this, false, "")
            hotelViewModel.getHotelListApi(this, false, "", "")
        } else {
            msgDialog(getString(R.string.check_internet))
        }
    }

    private fun requestLocationPermission() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            getCurrentLocation()
        } else {
            openLocationPermissionBottomSheet()
        }
    }

    private val requestLocationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            getCurrentLocation()
        }
    }

    @SuppressLint("MissingPermission")
    private fun getCurrentLocation() {
        fusedLocationClient.lastLocation.addOnSuccessListener(this) { location ->
            if (location != null) {
                isItemSelected = true   // 👈 important

                binding.edtSearch.setText("Current Location")
//                val searchHotel = SearchHotel()
                searchHotel.location = "Current Location"
                searchHotel.latitude = location.latitude
                searchHotel.longitude = location.longitude
                searchHotel.type = Constants.CITY

                val json = GsonBuilder().setPrettyPrinting().create().toJson(searchHotel)
                println("SEARCH_HOTEL...setPrettyPrinting JSON 5..." + json)

                /*val intent = Intent(this, SearchCalendarActivity::class.java)
                intent.putExtra(Constants.SEARCH_HOTEL, Gson().toJson(searchHotel))
                startActivity(intent)*/

                // 👉 Hide search UI & restore default
                binding.llRecently.makeVisible()
                binding.rlSearch.makeGone()

                // 👉 Optional: clear filter
                hotelSearchAdapter.filter.filter("")
                citySearchAdapter.filter.filter("")

                // 👉 Remove focus (important)
                binding.edtSearch.clearFocus()

                bottomSearchCalendar()


            } else {
                isLocationEnabled()
            }
        }
    }

    private fun isLocationEnabled() {

        val locationRequest = LocationRequest.create()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)

        val client: SettingsClient = LocationServices.getSettingsClient(this)
        val task = client.checkLocationSettings(builder.build())

        task.addOnSuccessListener {
            getCurrentLocation()
        }

        task.addOnFailureListener { exception ->
            if (exception is ResolvableApiException) {
                try {
                    val intentSenderRequest =
                        IntentSenderRequest.Builder(exception.resolution).build()
                    resolutionResultLauncher.launch(intentSenderRequest)
                } catch (sendEx: IntentSender.SendIntentException) {
                    // Ignore the error.
                }
            }
        }
    }

    private val resolutionResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                Log.d("Resolution", "Resolution succeeded!")
                getCurrentLocation() // Proceed after success
            } else {
                Log.e("Resolution", "Resolution failed or was canceled.")
            }
        }


    fun bottomSearchCalendar() {
        if (calendarDialog?.isShowing == true) return

        val dialog = BottomSheetDialog(this)
        calendarDialog = dialog

        bottomCalendarBinding = BottomSearchCalendarBinding.inflate(LayoutInflater.from(this))
        val view = bottomCalendarBinding.root

        bottomCalendarBinding.apply {
            ivClose.setOnClickListener {
                dialog.dismiss()
            }

            btnBookForToNight.setOnClickListener(this@SearchActivity)

            // initView set data
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

        calendarDialog?.behavior.apply {
            this?.isFitToContents = true
            this?.state = BottomSheetBehavior.STATE_EXPANDED
        }

        calendarDialog?.setCancelable(false)
        calendarDialog?.setContentView(view)
        calendarDialog?.show()
    }


    private val calendarListener: CalendarListener = object : CalendarListener {
        @SuppressLint("SetTextI18n")
        override fun onFirstDateSelected(startDate: Calendar) {
            try {
                bottomCalendarBinding.apply {
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
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }

        @SuppressLint("SetTextI18n")
        override fun onDateRangeSelected(startDate: Calendar, endDate: Calendar) {
            try {
                bottomCalendarBinding.apply {
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
                        tvNoOfNights.text = "$daysSelected ${getString(R.string.nights)}"
                    } else {
                        btnBookForToNight.text = "${getString(R.string.continue_with)} $daysSelected ${getString(R.string.nights_small)}"
                        btnBookForToNight.isEnabled = true
                        btnBookForToNight.alpha = 1.0F
                        tvNoOfNights.text = "$daysSelected ${getString(R.string.nights)}"
                    }

                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


    // TODO : Bottom select room and guest

    fun bottomSelectRoomAndGuest() {
        if (roomGuestDialog?.isShowing == true) return

        val dialog = BottomSheetDialog(this)
        roomGuestDialog = dialog

        bottomRoomGuestBinding = BottomSelectRoomGuestBinding.inflate(LayoutInflater.from(this))
        val view = bottomRoomGuestBinding.root

        bottomRoomGuestBinding.apply {
            ivClose.setOnClickListener { dialog.dismiss() }

            // initView data
            tvRooms.text = "$totalRooms"
            tvAdults.text = "$totalAdults"
            tvChildren.text = "$totalChildren"

            tvClear.setOnClickListener(this@SearchActivity)
            ivMinusRooms.setOnClickListener(this@SearchActivity)
            ivPlusRooms.setOnClickListener(this@SearchActivity)
            ivMinusAdults.setOnClickListener(this@SearchActivity)
            ivPlusAdults.setOnClickListener(this@SearchActivity)
            ivMinusChildren.setOnClickListener(this@SearchActivity)
            ivPlusChildren.setOnClickListener(this@SearchActivity)
            btnApply.setOnClickListener(this@SearchActivity)

            guestsPerRoomAdapter = GuestsPerRoomAdapter(this@SearchActivity, this@SearchActivity)
            rvGuestPerRoom.layoutManager = LinearLayoutManager(this@SearchActivity, RecyclerView.VERTICAL, false)
            rvGuestPerRoom.adapter = guestsPerRoomAdapter

            guestPerRoomList()

            tvTotal.text = "${totalRooms} ${getString(R.string.room)}, ${totalAdults + totalChildren} ${getString(R.string.guest)}"

        }

        roomGuestDialog?.behavior.apply {
            this?.isFitToContents = true
            this?.state = BottomSheetBehavior.STATE_EXPANDED
        }

        roomGuestDialog?.setCancelable(false)
        roomGuestDialog?.setContentView(view)
        roomGuestDialog?.show()
    }

    private fun guestPerRoomList() {
        guestsPerRoomAdapter.objList = ArrayList()

        var model = ResortModel()
        model.title = getString(R.string.room)
        guestsPerRoomAdapter.objList.add(model)

        model = ResortModel()
        model.title = getString(R.string.adults_per_room)
        guestsPerRoomAdapter.objList.add(model)

        model = ResortModel()
        model.title = getString(R.string.children_per_room)
        guestsPerRoomAdapter.objList.add(model)

        guestsPerRoomAdapter.addData(guestsPerRoomAdapter.objList)
    }
}
