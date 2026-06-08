package com.xongolab.hotellifyr.view.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.SettingsClient
import com.google.gson.Gson
import com.xongolab.hotellifyr.R
import com.xongolab.hotellifyr.core.CoreFragment
import com.xongolab.hotellifyr.databinding.FragmentHome1Binding
import com.xongolab.hotellifyr.model.ResortModel
import com.xongolab.hotellifyr.model.SearchHotel
import com.xongolab.hotellifyr.utils.Constants
import com.xongolab.hotellifyr.utils.Pref
import com.xongolab.hotellifyr.utils.Util
import com.xongolab.hotellifyr.utils.Util.msgDialog
import com.xongolab.hotellifyr.view.activity.account.PersonalInformationActivity
import com.xongolab.hotellifyr.view.activity.home.CurrentOffersActivity
import com.xongolab.hotellifyr.view.activity.home.MostRelevantActivity
import com.xongolab.hotellifyr.view.activity.home.NewlyLaunchedActivity
import com.xongolab.hotellifyr.view.activity.home.NewlyOpenedActivity
import com.xongolab.hotellifyr.view.activity.home.ReviewReservationActivity
import com.xongolab.hotellifyr.view.activity.home.RewardCircleActivity
import com.xongolab.hotellifyr.view.activity.home.search.SearchActivity
import com.xongolab.hotellifyr.view.activity.home.search.hotel.HotelDetailActivity
import com.xongolab.hotellifyr.view.activity.notification.NotificationActivity
import com.xongolab.hotellifyr.view.adapter.BestOffersAdapter
import com.xongolab.hotellifyr.view.adapter.CurrentOfferAdapter
import com.xongolab.hotellifyr.view.adapter.RewardsCircleHorizontalAdapter
import com.xongolab.hotellifyr.view.adapter.hotel.HotelTagAdapter
import com.xongolab.hotellifyr.viewModel.HotelViewModel
import com.xongolab.hotellifyr.viewModel.UserViewModel
import com.xongolab.hotellifyr.viewModel.ViewModelFactory
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone


class Home1Fragment : CoreFragment() {
    private lateinit var binding: FragmentHome1Binding
    private lateinit var userViewModel: UserViewModel
    private lateinit var hotelViewModel: HotelViewModel

    private lateinit var currentOfferAdapter: CurrentOfferAdapter
    private lateinit var rewardsCircleHorizontalAdapter: RewardsCircleHorizontalAdapter
    private lateinit var bestOffersAdapter: BestOffersAdapter
    private lateinit var hotelTagAdapter: HotelTagAdapter

    private lateinit var fusedLocationClient: FusedLocationProviderClient


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHome1Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initViewModel() {
        userViewModel = ViewModelProvider(
            this,
            ViewModelFactory(coreActivity!!.mainRepository)
        )[UserViewModel::class.java]
        hotelViewModel = ViewModelProvider(
            this,
            ViewModelFactory(coreActivity!!.mainRepository)
        )[HotelViewModel::class.java]
        observeViewModel()
    }

    @SuppressLint("SetTextI18n")
    private fun observeViewModel() {
        userViewModel.getCustomerInfoApiResponse.observe(coreActivity!!) { response ->
            response?.let {
                val payload = it.payload!!
                binding.apply {

                    ivProfile.setImageURI(payload.avatar)

                    tvGreeting.text = "${getString(R.string.hello_2)}, ${payload.firstName} ${getString(R.string.tell_us_where_you_want_to_go)}"

                    Pref.setStringValue(Pref.PREF_PROFILE_PIC, payload.avatar ?: "")
                    Pref.setStringValue(Pref.PREF_FIRST_NAME, payload.firstName)
                    Pref.setStringValue(Pref.PREF_LAST_NAME, payload.lastName)
                    Pref.setStringValue(Pref.PREF_EMAIL, payload.email)
                    Pref.setStringValue(Pref.PREF_COUNTRY_CODE, payload.mobileCountryCode)
                }
            }
        }

        hotelViewModel.getCurrentOfferListApiResponse.observe(coreActivity!!) { response ->
            response?.let {
                currentOfferAdapter.addData(it.payload)
            }
        }

        hotelViewModel.getHotelTagListApiResponse.observe(coreActivity!!) { response ->
            response?.let {
                hotelTagAdapter.addData(it.payload)
            }
        }

    }

    @SuppressLint("HardwareIds")
    private fun getCustomerInfoApi() {
        if (isInternetConnected()) {
            userViewModel.getCustomerInfoApi(coreActivity!!)
        } else {
            msgDialog(coreActivity!!, getString(R.string.check_internet))
        }
    }


    @SuppressLint("SetTextI18n")
    private fun getHotelTagListApi() {
        if (isInternetConnected()) {
            hotelViewModel.getHotelTagListApi(coreActivity!!)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun getCurrentOfferListApi() {
        if (isInternetConnected()) {
            hotelViewModel.getCurrentOfferListApi(coreActivity!!)
        }
    }

    override fun onResume() {
        super.onResume()
        getCustomerInfoApi()
        requestLocationPermission()
    }

    @SuppressLint("SetTextI18n")
    private fun initView() {
        initViewModel()

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(coreActivity!!)

        binding.ivNotification.setOnClickListener(this)
        binding.ivProfile.setOnClickListener(this)
        binding.ivNotification.setOnClickListener(this)
        binding.ivProfile.setOnClickListener(this)
        binding.cvSearchBar.setOnClickListener(this)
        binding.tvRewardsCircleSeeAll.setOnClickListener(this)

        binding.tvGreeting.text = "${getString(R.string.hello_2)}, " + Pref.getStringValue(
            Pref.PREF_FIRST_NAME,
            ""
        ) + "! ${getString(R.string.tell_us_where_you_want_to_go)}"

        binding.ivProfile.setImageURI(Pref.getStringValue(Pref.PREF_PROFILE_PIC,""))

        hotelTagAdapter = HotelTagAdapter(coreActivity!!)
        binding.rvHotelTag.layoutManager =
            LinearLayoutManager(coreActivity!!, RecyclerView.VERTICAL, false)
        binding.rvHotelTag.adapter = hotelTagAdapter
        getHotelTagListApi()

        hotelTagAdapter.onItemClick = { code, item ->
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
            val intent = Intent(coreActivity!!, HotelDetailActivity::class.java)
            intent.putExtra(Constants.SEARCH_HOTEL, Gson().toJson(searchHotel))
            startActivity(intent)
        }

        hotelTagAdapter.onViewAllItemClick = { code, item ->
            when (code) {
                "NEWLY_OPENED" -> startActivity(Intent(coreActivity!!, NewlyOpenedActivity::class.java).putExtra(Constants.HOTELS, Gson().toJson(item)))
                "POPULAR_CHOICE" -> startActivity(Intent(coreActivity!!, NewlyLaunchedActivity::class.java).putExtra(Constants.HOTELS, Gson().toJson(item)))
                "HOT_SELLING" -> startActivity(Intent(coreActivity!!, MostRelevantActivity::class.java).putExtra(Constants.HOTELS, Gson().toJson(item)))
                else -> startActivity(Intent(coreActivity!!, MostRelevantActivity::class.java).putExtra(Constants.HOTELS, Gson().toJson(item)))
            }
        }


        currentOfferAdapter = CurrentOfferAdapter(coreActivity!!)
        binding.llCurrentOffers.rvCurrentOffer.layoutManager =
            LinearLayoutManager(coreActivity!!, RecyclerView.HORIZONTAL, false)
        binding.llCurrentOffers.rvCurrentOffer.adapter = currentOfferAdapter
        getCurrentOfferListApi()

        currentOfferAdapter.onItemClick = {
            startActivity(Intent(coreActivity!!, CurrentOffersActivity::class.java))
        }

        rewardsCircleHorizontalAdapter = RewardsCircleHorizontalAdapter(coreActivity!!)
        binding.llRewardCircle.rvRewardsCircle.layoutManager =
            LinearLayoutManager(coreActivity!!, RecyclerView.HORIZONTAL, false)
        binding.llRewardCircle.rvRewardsCircle.adapter = rewardsCircleHorizontalAdapter

        rewardsCircleHorizontalAdapter.onItemClick = {
        }

        rewardsCircleList()

        bestOffersAdapter = BestOffersAdapter(coreActivity!!)
        binding.llBestOffers.rvBestOffers.layoutManager =
            LinearLayoutManager(coreActivity!!, RecyclerView.HORIZONTAL, false)
        binding.llBestOffers.rvBestOffers.adapter = bestOffersAdapter

        bestOffersList()
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.tvRewardsCircleSeeAll -> {
                startActivity(Intent(coreActivity!!, RewardCircleActivity::class.java))
            }

            R.id.cvSearchBar -> {
                startActivity(Intent(coreActivity!!, SearchActivity::class.java))
            }

            R.id.tvNewlyOpenedSeeAll -> {
                startActivity(Intent(coreActivity!!, NewlyOpenedActivity::class.java))
            }

            R.id.ivNotification -> {
                startActivity(Intent(coreActivity!!, NotificationActivity::class.java))
            }

            R.id.ivProfile -> {
                startActivity(Intent(coreActivity!!, PersonalInformationActivity::class.java))
            }

            R.id.tvBookNow -> {
                startActivity(Intent(coreActivity!!, ReviewReservationActivity::class.java))
            }
        }
    }

    private fun rewardsCircleList() {
        rewardsCircleHorizontalAdapter.objList = ArrayList()

        var model = ResortModel()
        model.profileImg = R.drawable.ic_rewards_circle_1
        rewardsCircleHorizontalAdapter.objList.add(model)

        model = ResortModel()
        model.profileImg = R.drawable.ic_rewards_circle_2
        rewardsCircleHorizontalAdapter.objList.add(model)

        model = ResortModel()
        model.profileImg = R.drawable.ic_rewards_circle_3
        rewardsCircleHorizontalAdapter.objList.add(model)

        model = ResortModel()
        model.profileImg = R.drawable.ic_rewards_circle_4
        rewardsCircleHorizontalAdapter.objList.add(model)

        model = ResortModel()
        model.profileImg = R.drawable.ic_rewards_circle_5
        rewardsCircleHorizontalAdapter.objList.add(model)

        model = ResortModel()
        model.profileImg = R.drawable.ic_rewards_circle_6
        rewardsCircleHorizontalAdapter.objList.add(model)

        rewardsCircleHorizontalAdapter.addData(rewardsCircleHorizontalAdapter.objList)
    }

    private fun bestOffersList() {
        bestOffersAdapter.objList = ArrayList()

        var model = ResortModel()
        model.profileImg = R.drawable.ic_best_offer_1
        model.title = "Royal Orchid Beach Resort & Spa, Goa"
        bestOffersAdapter.objList.add(model)

        model = ResortModel()
        model.profileImg = R.drawable.ic_best_offer_2
        model.title = "Royal Orchid Beach Resort & Spa, Goa"
        bestOffersAdapter.objList.add(model)

        model = ResortModel()
        model.profileImg = R.drawable.ic_best_offer_3
        model.title = "Royal Orchid Beach Resort & Spa, Goa"
        bestOffersAdapter.objList.add(model)

        model = ResortModel()
        model.profileImg = R.drawable.ic_best_offer_4
        model.title = "Royal Orchid Beach Resort & Spa, Goa"
        bestOffersAdapter.objList.add(model)

        bestOffersAdapter.addData(bestOffersAdapter.objList)
    }

    private fun requestLocationPermission() {
        if (ActivityCompat.checkSelfPermission(
                coreActivity!!,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            getCurrentLocation()
        } else {
            requestLocationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
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
        fusedLocationClient.lastLocation.addOnSuccessListener(coreActivity!!) { location ->
            if (location != null) {
                if (Geocoder.isPresent()) {
                    val geocoder = Geocoder(coreActivity!!, Locale.getDefault())
                    val addresses: List<Address> =
                        geocoder.getFromLocation(location.latitude, location.longitude, 1)!!

                    if (addresses.isNotEmpty()) {
                        val cityName = addresses[0].locality // Extract city name
                        if (cityName != null) {
                            binding.tvLocation.text = cityName
                        } else {
                            binding.tvLocation.text = ""
                        }
                    }
                } else {
                    Log.e("Geocoder", "Geocoder service is unavailable on this device.")
                }
            } else {
                isLocationEnabled()
            }
        }
    }

    private fun isLocationEnabled() {

        val locationRequest = LocationRequest.create()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)

        val client: SettingsClient = LocationServices.getSettingsClient(coreActivity!!)
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
}