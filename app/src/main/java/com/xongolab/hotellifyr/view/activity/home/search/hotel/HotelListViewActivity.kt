package com.xongolab.hotellifyr.view.activity.home.search.hotel

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.SettingsClient
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import com.google.maps.android.clustering.ClusterManager
import com.xongolab.hotellifyr.R
import com.xongolab.hotellifyr.core.CoreActivity
import com.xongolab.hotellifyr.custom.HotelClusterItem
import com.xongolab.hotellifyr.databinding.ActivityHotelListViewBinding
import com.xongolab.hotellifyr.databinding.BottomsheetHotelDetailBinding
import com.xongolab.hotellifyr.databinding.CustomMarkerBinding
import com.xongolab.hotellifyr.databinding.DialogConfirmationBinding
import com.xongolab.hotellifyr.databinding.DialogSortBinding
import com.xongolab.hotellifyr.model.HotelModel
import com.xongolab.hotellifyr.model.SearchHotel
import com.xongolab.hotellifyr.model.SortHotel
import com.xongolab.hotellifyr.utils.BottomSheetFilterDialogFragment
import com.xongolab.hotellifyr.utils.Constants
import com.xongolab.hotellifyr.utils.Pref
import com.xongolab.hotellifyr.utils.Util
import com.xongolab.hotellifyr.utils.Util.createPartFromString
import com.xongolab.hotellifyr.utils.makeInvisible
import com.xongolab.hotellifyr.utils.makeVisible
import com.xongolab.hotellifyr.view.adapter.ImageAdapter
import com.xongolab.hotellifyr.view.adapter.SmallImagesAdapter
import com.xongolab.hotellifyr.view.adapter.SortAdapter
import com.xongolab.hotellifyr.view.adapter.hotel.HotelListViewAdapter
import com.xongolab.hotellifyr.viewModel.HotelViewModel
import com.xongolab.hotellifyr.viewModel.ViewModelFactory

class HotelListViewActivity : CoreActivity(), OnMapReadyCallback {

    private lateinit var binding: ActivityHotelListViewBinding
    private lateinit var wishListAdapter: HotelListViewAdapter
    private lateinit var sortAdapter: SortAdapter

    private lateinit var viewPagerAdapter: ImageAdapter
    private lateinit var hotelViewModel: HotelViewModel

    private var searchHotel = SearchHotel()

    private var currentView = Param.LIST

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var googleMap: GoogleMap

    private var mCenterLatLong: LatLng = LatLng(0.0, 0.0)

    private lateinit var dialogBinding: DialogConfirmationBinding
    private lateinit var alertDialog: AlertDialog
    private var lastPos: Int = -1
    private lateinit var clusterManager: ClusterManager<HotelClusterItem>

    private var clickStatus = ""

    object Param {
        const val MAP = "MAP"
        const val LIST = "LIST"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHotelListViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setFullScreenMode(window)
        initView()
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onClick(v: View) {
        when (v.id) {
            R.id.btnBack -> finish()

            R.id.llView -> {
                binding.apply {
                    currentView = if (currentView == Param.LIST) Param.MAP else Param.LIST

                    ivView.setImageResource(if (currentView == Param.LIST) R.drawable.ic_map_black else R.drawable.ic_list)
                    tvView.text = if (currentView == Param.LIST) Param.MAP else Param.LIST

                    rlMap.isVisible = currentView == Param.MAP
                    //cvHotel.isVisible = currentView == Param.MAP
                    rvHotels.isVisible = currentView == Param.LIST

                    if (currentView == Param.MAP) {
                        binding.rlFilterLayout.setBackgroundResource(R.drawable.bottom_round_corner_white_30)
                    } else {
                        binding.rlFilterLayout.setBackgroundColor(ContextCompat.getColor(this@HotelListViewActivity, R.color.colorWhite))
                    }

                    if (currentView == Param.MAP) {
                        val cameraPosition = CameraPosition.fromLatLngZoom(mCenterLatLong, 10.0f)
                        val cu = CameraUpdateFactory.newCameraPosition(cameraPosition)
                        googleMap.animateCamera(cu)
                    }
                }
            }

            R.id.llSort -> sortDialog()

            R.id.llFilter -> {
                val bottomSheet = BottomSheetFilterDialogFragment(
                    searchHotel,
                    onApplyClick = { data: SearchHotel ->
                        searchHotel = data

                        getHotelListingApi()
                    })
                bottomSheet.show(supportFragmentManager, "FilterBottomSheet")
            }

            R.id.btnWishlist -> {
                lastPos = Integer.parseInt(v.tag.toString())
                showConfirmationDialog()
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun initView() {
        initViewModel()

        if (intent != null) {
            searchHotel = Gson().fromJson(
                intent.getStringExtra(Constants.SEARCH_HOTEL),
                SearchHotel::class.java
            )
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)

        binding.btnBack.setOnClickListener(this)
        binding.llSort.setOnClickListener(this)
        binding.llView.setOnClickListener(this)
        binding.llFilter.setOnClickListener(this)

        val imageList = arrayListOf<Int>().apply {
            add(R.drawable.ic_wish_list_1)
            add(R.drawable.ic_wish_list_2)
            add(R.drawable.ic_wish_list_3)
        }

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

        val totalRooms = searchHotel.rooms
        val totalGuest = searchHotel.adults!! + searchHotel.children!!

        binding.tvGuest.text = "$checkIn - $checkOut, $totalRooms Room, $totalGuest Guest"
        binding.tvLocation.text = searchHotel.location.ifEmpty { "Current Location" }

        viewPagerAdapter = ImageAdapter(this)
        viewPagerAdapter.addDrawableData(imageList)

        binding.viewPager.adapter = viewPagerAdapter
        binding.indicator.attachTo(binding.viewPager)

        // Hotel List View
        wishListAdapter = HotelListViewAdapter(this, this)
        binding.rvHotels.layoutManager =
            LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        binding.rvHotels.adapter = wishListAdapter

        wishListAdapter.onItemClick = { item ->
            val hotel = SearchHotel()
            hotel.hotelID = item.id
            hotel.location = item.address
            hotel.cityID = searchHotel.cityID
            hotel.checkIn = searchHotel.checkIn
            hotel.checkOut = searchHotel.checkOut
            hotel.adults = searchHotel.adults
            hotel.children = searchHotel.children
            hotel.rooms = searchHotel.rooms
            hotel.roomWiseGuest = searchHotel.roomWiseGuest

            val intent = Intent(this, HotelDetailActivity::class.java)
            intent.putExtra(Constants.SEARCH_HOTEL, Gson().toJson(hotel))
            startActivity(intent)
        }

        getHotelListingApi()
    }

    private fun initViewModel() {
        hotelViewModel = ViewModelProvider(this, ViewModelFactory(mainRepository))[HotelViewModel::class.java]
        observeViewModel()
    }


    @SuppressLint("SetTextI18n")
    private fun observeViewModel() {
        hotelViewModel.getHotelListingApiResponse.observe(this) { response ->
            response?.let {
                googleMap.clear()
                val filteredHotels: ArrayList<HotelModel> = it.payload.filter { hotel ->
                    hotel.price in searchHotel.minPrice..searchHotel.maxPrice
                } as ArrayList<HotelModel>

                binding.tvResult.text = "${filteredHotels.size} Results"

                if (filteredHotels.isEmpty()) {
                    binding.tvNoData.visibility = View.VISIBLE
                } else {
                    binding.tvNoData.visibility = View.GONE
                }

                wishListAdapter.addData(filteredHotels)

                filteredHotels.forEachIndexed { index, it1 -> // Use forEachIndexed to get index
                    setHotelMarker(LatLng(it1.position.lat, it1.position.lng), it1.price, index)
                }

                filteredHotels.firstOrNull()?.let {
                    mCenterLatLong = LatLng(it.position.lat, it.position.lng)
                }

                //    clusterManager.cluster()
                val cameraPosition = CameraPosition.fromLatLngZoom(mCenterLatLong, 10.0f)
                val cu = CameraUpdateFactory.newCameraPosition(cameraPosition)
                googleMap.animateCamera(cu)
            }
        }
        hotelViewModel.deleteWishListApiResponse.observe(this) { response ->
            response?.let {
                if (clickStatus == "Add") {
                    Util.msgDialog(this, getString(R.string.you_ve_successfully_added_this_hotel_to_your_wishlist))
                } else if (clickStatus == "Remove") {
                    Util.msgDialog(this, getString(R.string.you_ve_successfully_removed_this_hotel_from_your_wishlist))
                }
                getHotelListingApi()
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun addWishListApi(hotelID: String) {
        if (isInternetConnected()) {
            val actionType = createPartFromString("add", "actionType")
            val hotelID = createPartFromString(hotelID, "hotelID")

            hotelViewModel.deleteWishListApi(
                this,
                actionType,
                hotelID
            )
        } else {
            Util.msgDialog(this, getString(R.string.check_internet))
        }
    }

    @SuppressLint("SetTextI18n")
    private fun deleteWishListApi(hotelID: String) {
        if (isInternetConnected()) {
            val actionType = createPartFromString("delete", "actionType")
            val hotelID = createPartFromString(hotelID, "hotelID")

            hotelViewModel.deleteWishListApi(
                this,
                actionType,
                hotelID
            )
        } else {
            Util.msgDialog(this, getString(R.string.check_internet))
        }
    }

    private fun getHotelListingApi() {
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

            val selectedBrands = searchHotel.brandIDs!!
                .filter { it.isChecked }.map { it.id }

            val selectedDestination = searchHotel.destinationIDs!!
                .filter { it.isChecked }.map { it.id }


            val selectedAmenities = searchHotel.amenitiesIDs!!
                .filter { it.isChecked }.map { it.id }

            hotelViewModel.getHotelListingApi(
                this,
                Pref.getStringValue(Pref.PREF_USER_ID, ""),
                adults = searchHotel.adults,
                children = searchHotel.children,
                checkIn = checkIn,
                checkOut = checkOut,
                hotelType = searchHotel.hotelType,
                starRating = searchHotel.starRating,
                cityID = searchHotel.cityID,
                stateID = searchHotel.stateID,
                brandID = searchHotel.brandID,
                longitude = searchHotel.longitude,
                latitude = searchHotel.latitude,
                hotelIds = if (searchHotel.hotelIds!!.isNotEmpty()) searchHotel.hotelIds!!.joinToString(",") else null,
                amenitiesIDs = if (selectedAmenities.isNotEmpty()) selectedAmenities.joinToString(",") else null,
                brandIDs = if (selectedBrands.isNotEmpty()) selectedBrands.joinToString(",") else null,
                destinationIDs = if (selectedDestination.isNotEmpty()) selectedDestination.joinToString(",") else null,
                sortBy = searchHotel.sortBy,
            )
        } else {
            msgDialog(getString(R.string.check_internet))
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun sortDialog() {
        var sortBy: String? = null
        val addressDialog = Dialog(this)
        val dialogBinding = DialogSortBinding.inflate(layoutInflater)
        addressDialog.setContentView(dialogBinding.root)
        addressDialog.setCanceledOnTouchOutside(false)
        addressDialog.setCancelable(false)

        dialogBinding.apply {
            ivClose.setOnClickListener {
                addressDialog.dismiss()
            }
            // Sort List
            sortAdapter = SortAdapter(this@HotelListViewActivity)
            dialogBinding.rvSort.layoutManager =
                LinearLayoutManager(this@HotelListViewActivity, RecyclerView.VERTICAL, false)
            dialogBinding.rvSort.adapter = sortAdapter

            sortAdapter.onItemClick = { item: SortHotel ->
                for (model in sortAdapter.objList) {
                    model.isChecked = false
                }

                sortBy = item.sortBy
                item.isChecked = true
                sortAdapter.notifyDataSetChanged()
            }

            sortList()

            tvClear.setOnClickListener {
                for (model in sortAdapter.objList) {
                    model.isChecked = false
                }
                sortAdapter.notifyDataSetChanged()
            }

            btnApply.setOnClickListener {
                searchHotel.sortBy = sortBy
                getHotelListingApi()
                addressDialog.dismiss()
            }

        }

        val window = addressDialog.window
        val params = window?.attributes
        params?.width = ((resources.displayMetrics.widthPixels * 0.9).toInt())
        window?.attributes = params

        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        addressDialog.show()
    }

    private fun sortList() {
        val sortList = ArrayList<SortHotel>()

        sortList.add(SortHotel("Distance", "distance"))
        sortList.add(SortHotel("Lowest Price", "lowPrice"))
        sortList.add(SortHotel("Highest Price", "highPrice"))
        sortList.add(SortHotel("Guest Rating", "starRating"))

        sortList.firstOrNull { it.sortBy == searchHotel.sortBy }?.isChecked = true

        sortAdapter.addData(sortList)
    }

    @SuppressLint("PotentialBehaviorOverride")
    override fun onMapReady(map: GoogleMap) {
        googleMap = map

        googleMap.setOnMarkerClickListener { marker ->
            val tag = marker.tag  // Retrieve the tag value
            if (tag is Int) {  // Ensure the tag is of type Int (or change as needed)
                showHotelDetailDialog(wishListAdapter.objList[tag])
            }
            false
        }

        requestLocationPermission()
    }

    private fun requestLocationPermission() {
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION
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
        fusedLocationClient.lastLocation.addOnSuccessListener(this) { location ->
            if (location != null) {
                mCenterLatLong = LatLng(location.latitude, location.longitude)


                val cameraUpdate = CameraUpdateFactory.newLatLngZoom(mCenterLatLong, 12.0f)
                googleMap.animateCamera(cameraUpdate, 1000, object : GoogleMap.CancelableCallback {
                    override fun onFinish() {
                    }

                    override fun onCancel() {
                    }
                })
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

    private fun setHotelMarker(position: LatLng, price: Double, index: Int) {
        val bitmapDescriptor = getBitmapDescriptorFromVector(price)
        val marker = googleMap.addMarker(MarkerOptions().position(position).icon(bitmapDescriptor))
        marker?.tag = index
    }

    @SuppressLint("SetTextI18n", "DefaultLocale")
    private fun getBitmapDescriptorFromVector(price: Double): BitmapDescriptor {
        val binding = CustomMarkerBinding.inflate(LayoutInflater.from(this))

        // Set price text
        binding.markerText.text = setPriceWithUnit(price)

        // Convert view to bitmap
        binding.root.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
        val bitmap = Bitmap.createBitmap(
            binding.root.measuredWidth, binding.root.measuredHeight, Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        binding.root.layout(0, 0, binding.root.measuredWidth, binding.root.measuredHeight)
        binding.root.draw(canvas)

        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }

    @SuppressLint("DefaultLocale", "SetTextI18n")
    private fun showHotelDetailDialog(item: HotelModel) {
        val dialog = BottomSheetDialog(this, R.style.TransparentBottomSheetDialog)

        val dialogBinding = BottomsheetHotelDetailBinding.inflate(layoutInflater)

        dialogBinding.apply {
            if (item.images.isNotEmpty()) {
                Glide.with(this@HotelListViewActivity)
                    .load(item.images[0])
                    .placeholder(R.drawable.ic_placeholder_rectangle)
                    .into(imgMain)
            }

            val imgAdapter = SmallImagesAdapter(this@HotelListViewActivity) { selectedImage ->
                Glide.with(this@HotelListViewActivity)
                    .load(selectedImage)
                    .placeholder(R.drawable.ic_placeholder_rectangle)
                    .into(imgMain)
            }

            rvImage.layoutManager = LinearLayoutManager(this@HotelListViewActivity, LinearLayoutManager.HORIZONTAL, false)
            rvImage.adapter = imgAdapter
            imgAdapter.setData(item.images)

            rvImage.visibility = if (item.images.size > 1) View.VISIBLE else View.GONE

            tvTitle.text = item.name
            tvStar.text = "" + item.starRating
            tvPrice.text = setPriceWithUnit(item.price)

            ivWish.setImageResource(if (item.isFavorite) R.drawable.ic_favourite else R.drawable.ic_wish)

            if (item.distanceInKm != null) {
                tvKm.text = String.format("%.2f km", item.distanceInKm)
                llDistance.makeVisible()
            } else {
                llDistance.makeInvisible()
            }

            btnWishlist.setOnClickListener {
                if (item.isFavorite) {
                    clickStatus = "Remove"
                    deleteWishListApi(item.id)
                } else {
                    clickStatus = "Add"
                    addWishListApi(item.id)
                }
                dialog.dismiss()
            }

            btnViewHotel.setOnClickListener {
                val hotel = SearchHotel()
                hotel.hotelID = item.id
                hotel.location = item.address
                hotel.cityID = searchHotel.cityID
                hotel.checkIn = searchHotel.checkIn
                hotel.checkOut = searchHotel.checkOut
                hotel.adults = searchHotel.adults
                hotel.children = searchHotel.children
                hotel.rooms = searchHotel.rooms
                hotel.roomWiseGuest = searchHotel.roomWiseGuest

                val intent = Intent(this@HotelListViewActivity, HotelDetailActivity::class.java)
                intent.putExtra(Constants.SEARCH_HOTEL, Gson().toJson(hotel))
                startActivity(intent)
            }
        }

        dialog.behavior.apply {
            isFitToContents = true
            state = BottomSheetBehavior.STATE_EXPANDED
        }

        dialog.setCancelable(true)
        dialog.setCanceledOnTouchOutside(false)
        dialog.setContentView(dialogBinding.root)
        dialog.show()
    }

    fun showConfirmationDialog() {
        dialogBinding = DialogConfirmationBinding.inflate(LayoutInflater.from(this))
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setView(dialogBinding.root)

        alertDialog = alertDialogBuilder.create()
        alertDialog.show()

        alertDialog.window!!.attributes.width =
            ((resources.displayMetrics.widthPixels * 0.9).toInt())
        alertDialog.window?.apply {
            setDimAmount(0.8f) // Adjust dim intensity (0.0f = no dim, 1.0f = full dim)
            addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND)
        }
        alertDialog.window!!.setBackgroundDrawableResource(R.drawable.dr_white_bg_15)

        dialogBinding.tvMessage.text =
            resources.getString(if (wishListAdapter.objList[lastPos].isFavorite) R.string.are_you_sure_you_want_to_remove_this_hotel_from_your_wishlist else R.string.are_you_sure_you_want_to_add_this_hotel_in_your_wishlist)
        dialogBinding.tvConfirm.text = resources.getString(R.string.str_yes)
        dialogBinding.tvCancel.text = resources.getString(R.string.str_no)
        dialogBinding.apply {
            tvConfirm.setOnClickListener {
                alertDialog.dismiss()
                if (wishListAdapter.objList[lastPos].isFavorite) {
                    clickStatus = "Remove"
                    deleteWishListApi(wishListAdapter.objList[lastPos].id)
                } else {
                    clickStatus = "Add"
                    addWishListApi(wishListAdapter.objList[lastPos].id)
                }
            }
            tvCancel.setOnClickListener { alertDialog.dismiss() }
        }

        alertDialog.setCancelable(false)
    }
}