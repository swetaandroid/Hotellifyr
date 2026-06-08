package com.xongolab.hotellifyr.view.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.activity.OnBackPressedCallback
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
import com.google.gson.GsonBuilder
import com.xongolab.hotellifyr.R
import com.xongolab.hotellifyr.core.CoreFragment
import com.xongolab.hotellifyr.databinding.DialogConfirmationBinding
import com.xongolab.hotellifyr.databinding.FragmentWishListBinding
import com.xongolab.hotellifyr.model.SearchHotel
import com.xongolab.hotellifyr.utils.Constants
import com.xongolab.hotellifyr.utils.DialogManager
import com.xongolab.hotellifyr.utils.Pref
import com.xongolab.hotellifyr.utils.Util
import com.xongolab.hotellifyr.utils.Util.createPartFromString
import com.xongolab.hotellifyr.utils.Util.msgDialog
import com.xongolab.hotellifyr.utils.makeGone
import com.xongolab.hotellifyr.utils.makeVisible
import com.xongolab.hotellifyr.view.activity.MainActivity
import com.xongolab.hotellifyr.view.activity.home.search.hotel.HotelDetailActivity
import com.xongolab.hotellifyr.view.adapter.WishListAdapter
import com.xongolab.hotellifyr.viewModel.HotelViewModel
import com.xongolab.hotellifyr.viewModel.ViewModelFactory
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone


class WishListFragment : CoreFragment() {
    private lateinit var binding: FragmentWishListBinding

    private lateinit var wishListAdapter: WishListAdapter
    private lateinit var dialogManager: DialogManager
    private lateinit var hotelViewModel: HotelViewModel
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    var currentLatitude: Double = 0.0
    var currentLongitude: Double = 0.0
    private lateinit var dialogBinding: DialogConfirmationBinding
    private lateinit var alertDialog: AlertDialog
    private var lastPos: Int = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWishListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    // Navigate back to Home1Fragment when back button is pressed
                    (requireActivity() as MainActivity).changeFragment(0)
                }
            })

        initView()
    }


    override fun onClick(view: View) {
        when (view.id) {
            R.id.btnBack -> {
                (requireActivity() as MainActivity).changeFragment(0)
            }

            R.id.btnCreateWishList -> {
                showBottomSheet()
            }

            R.id.btnWishlist -> {
                lastPos = Integer.parseInt(view.tag.toString())
                showConfirmationDialog()
            }

            R.id.btnViewHotel -> {
                lastPos = Integer.parseInt(view.tag.toString())

                val selectedHotel = wishListAdapter.objList[lastPos]

                val json = GsonBuilder().setPrettyPrinting().create().toJson(selectedHotel)
                println("SEARCH_HOTEL...setPrettyPrinting JSON wishlist..." + Gson().toJson(selectedHotel))
                println("SEARCH_HOTEL...setPrettyPrinting JSON json..." + json)

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
                    hotelID = wishListAdapter.objList[lastPos]._id
                    location = wishListAdapter.objList[lastPos].address
                    type = Constants.HOTEL
                    checkIn = today
                    checkOut = tomorrow
                }
                val intent = Intent(coreActivity!!, HotelDetailActivity::class.java)
                intent.putExtra(Constants.SEARCH_HOTEL, Gson().toJson(searchHotel))
                startActivity(intent)

            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun initView() {

        initViewModel()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(coreActivity!!)
        dialogManager = DialogManager(requireContext())

        binding.toolbar.toolbarTitle.text = "Wish List"
        binding.toolbar.btnBack.setOnClickListener(this)
        binding.btnCreateWishList.setOnClickListener(this)

        wishListAdapter = WishListAdapter(coreActivity!!, this)
        binding.rvWishList.layoutManager =
            LinearLayoutManager(coreActivity!!, RecyclerView.VERTICAL, false)
        binding.rvWishList.adapter = wishListAdapter

        if (wishListAdapter.objList.isEmpty()) {
            binding.llNoData.makeVisible()
        } else {
            binding.rvWishList.makeVisible()
        }
    }

    private fun showBottomSheet() {
        dialogManager.showCustomBottomSheetDialog(
            imageResId = R.drawable.ic_create_wishlist,
            title = getString(R.string.create_your_wishlist),
            description = getString(R.string.as_your_search_tap_heart_icon_to_save_your_favorite_hotels_to_a_wishlist),
            buttonText = getString(R.string.get_started),
            showNotNow = false,
            onButtonClick = {
                (requireActivity() as MainActivity).changeFragment(0)

            }
        )
    }

    private fun initViewModel() {
        hotelViewModel = ViewModelProvider(this, ViewModelFactory(coreActivity!!.mainRepository))[HotelViewModel::class.java]
        observeViewModel()
    }

    private fun observeViewModel() {
        hotelViewModel.getWishListApiResponse.observe(coreActivity!!) { response ->
            response?.let {
                val payload = it.payload!!
                if (payload.favHotels.size > 0) {
                    wishListAdapter.clearData()
                    wishListAdapter.addData(payload.favHotels)
                    binding.rvWishList.makeVisible()
                    binding.llNoData.makeGone()
                } else {
                    wishListAdapter.clearData()
                    binding.rvWishList.makeGone()
                    binding.llNoData.makeVisible()
                }
            }
        }
        hotelViewModel.deleteWishListApiResponse.observe(coreActivity!!) { response ->
            response?.let {
                getWishListApi()
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun getWishListApi() {
        if (isInternetConnected()) {
            val actionType = createPartFromString("list", "actionType")

            hotelViewModel.getWishListApi(
                coreActivity!!,
                actionType
            )
        } else {
            msgDialog(coreActivity!!, getString(R.string.check_internet))
        }
    }

    @SuppressLint("SetTextI18n")
    private fun deleteWishListApi() {
        if (isInternetConnected()) {
            val actionType = createPartFromString("delete", "actionType")
            val hotelID = createPartFromString(wishListAdapter.objList[lastPos]._id, "hotelID")

            hotelViewModel.deleteWishListApi(
                coreActivity!!,
                actionType,
                hotelID
            )
        } else {
            msgDialog(coreActivity!!, getString(R.string.check_internet))
        }
    }

    override fun onResume() {
        super.onResume()
        requestLocationPermission()
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
                currentLatitude = location.latitude
                currentLongitude = location.longitude
                Pref.setStringValue(Pref.PREF_CURRENT_LAT, currentLatitude.toString())
                Pref.setStringValue(Pref.PREF_CURRENT_LONG, currentLongitude.toString())
                getWishListApi()

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

    fun showConfirmationDialog() {
        dialogBinding = DialogConfirmationBinding.inflate(LayoutInflater.from(coreActivity!!))
        val alertDialogBuilder = AlertDialog.Builder(coreActivity!!)
        alertDialogBuilder.setView(dialogBinding.root)

        alertDialog = alertDialogBuilder.create()
        alertDialog.show()

        alertDialog.window!!.attributes.width =
            ((resources.displayMetrics.widthPixels * 0.9).toInt())
        alertDialog.window?.apply {
            setDimAmount(0.4f) // Adjust dim intensity (0.0f = no dim, 1.0f = full dim)
            addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        }
        alertDialog.window!!.setBackgroundDrawableResource(R.drawable.dr_white_bg_15)

        dialogBinding.tvMessage.text = resources.getString(R.string.are_you_sure_you_want_to_remove_this_hotel_from_your_wishlist)
        dialogBinding.tvConfirm.text = resources.getString(R.string.str_yes)
        dialogBinding.tvCancel.text = resources.getString(R.string.str_no)
        dialogBinding.apply {
            tvConfirm.setOnClickListener {
                alertDialog.dismiss()
                deleteWishListApi()
            }
            tvCancel.setOnClickListener { alertDialog.dismiss() }
        }

        alertDialog.setCancelable(false)
    }
}