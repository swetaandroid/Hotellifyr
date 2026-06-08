package com.xongolab.hotellifyr.view.activity.home.search.hotel

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.xongolab.hotellifyr.R
import com.xongolab.hotellifyr.core.CoreActivity
import com.xongolab.hotellifyr.databinding.ActivityHotelMapBinding
import com.xongolab.hotellifyr.databinding.BottomSheetHotelMapNavigationBinding
import com.xongolab.hotellifyr.databinding.CustomMarkerBinding
import com.xongolab.hotellifyr.databinding.DialogDirectionBinding
import com.xongolab.hotellifyr.view.adapter.hotel.map.MapNavigationAdapter
import com.xongolab.hotellifyr.view.adapter.hotel.map.MapNavigationTitleAdapter
import com.xongolab.hotellifyr.viewModel.HotelViewModel
import com.xongolab.hotellifyr.viewModel.ViewModelFactory

class HotelMapActivity : CoreActivity(), OnMapReadyCallback {

    private lateinit var binding: ActivityHotelMapBinding
    private lateinit var mapNavigationTitleAdapter: MapNavigationTitleAdapter
    private lateinit var mapNavigationAdapter: MapNavigationAdapter
    private lateinit var hotelViewModel: HotelViewModel

    private lateinit var nearByHotelBinding: BottomSheetHotelMapNavigationBinding

    private var hotelID: String = ""
    private var name: String = ""
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0

    private var sheetBehavior: BottomSheetBehavior<*>? = null

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var googleMap: GoogleMap

    private var mCenterLatLong: LatLng = LatLng(0.0, 0.0)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHotelMapBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setFullScreenMode(window)

        nearByHotelBinding = binding.bsHotelMapNavigation

        initView()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onClick(v: View) {
        when (v.id) {
            R.id.btnNavigation -> locationDialog()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initView() {
        initViewModel()

        if (intent.hasExtra("hotelID")) {
            hotelID = intent.getStringExtra("hotelID")!!
            name = intent.getStringExtra("name")!!
            latitude = intent.getDoubleExtra("latitude", 0.0)
            longitude = intent.getDoubleExtra("longitude", 0.0)

            mCenterLatLong = LatLng(latitude, longitude)
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        val mapFragment =
            supportFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)

        bottomSheet()

        mapNavigationTitleAdapter = MapNavigationTitleAdapter(this)
        nearByHotelBinding.rvNavigationTitle.layoutManager =
            LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        nearByHotelBinding.rvNavigationTitle.adapter = mapNavigationTitleAdapter

        mapNavigationTitleAdapter.onItemClick = { _, item ->
            for (it in mapNavigationTitleAdapter.objList) {
                it.isSelected = false
            }

            item.isSelected = true
            mapNavigationAdapter.addData(item.mapItems, item.icon, mCenterLatLong)

            mapNavigationTitleAdapter.notifyDataSetChanged()
        }

        // Navigation
        mapNavigationAdapter = MapNavigationAdapter(this)
        nearByHotelBinding.rvNavigation.layoutManager =
            LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        nearByHotelBinding.rvNavigation.adapter = mapNavigationAdapter

        mapNavigationAdapter.onItemClick = { _, item ->
            chooseDirectionDialog(item.latitude, item.longitude)

            println("mapNavigationAdapter..." + item)
        }

        getNearByPlaceListApi()
    }

    private fun initViewModel() {
        hotelViewModel =
            ViewModelProvider(this, ViewModelFactory(mainRepository))[HotelViewModel::class.java]
        observeViewModel()
    }

    @SuppressLint("SetTextI18n")
    private fun observeViewModel() {
        hotelViewModel.getNearByPlaceListApiResponse.observe(this) { response ->
            response?.let {

                if (it.payload.isNotEmpty()) {
                    val model = it.payload.first()
                    model.isSelected = true

                    mapNavigationTitleAdapter.addData(it.payload)
                    mapNavigationAdapter.addData(model.mapItems, model.icon, mCenterLatLong)

                    nearByHotelBinding.tvNoDataFound.visibility = View.GONE
                    nearByHotelBinding.rvNavigation.visibility = View.VISIBLE
                    nearByHotelBinding.rvNavigationTitle.visibility = View.VISIBLE

                } else {
                    nearByHotelBinding.tvNoDataFound.visibility = View.VISIBLE
                    nearByHotelBinding.rvNavigation.visibility = View.GONE
                    nearByHotelBinding.rvNavigationTitle.visibility = View.GONE
                    nearByHotelBinding.viewBorder.visibility = View.GONE
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun getNearByPlaceListApi() {
        if (isInternetConnected()) {

            hotelViewModel.getNearByPlaceListApi(
                this,
                hotelID
            )
        } else {
            msgDialog(getString(R.string.check_internet))
        }
    }

    private fun chooseDirectionDialog(latitude: Double, longitude: Double) {
        val dialogBinding = DialogDirectionBinding.inflate(LayoutInflater.from(this))

        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setView(dialogBinding.root)

        val alertDialog: AlertDialog = alertDialogBuilder.create()
        alertDialog.show()

        alertDialog.setCancelable(false)

        dialogBinding.apply {
            tvOpenGoogleMap.setOnClickListener {
                openGoogleMap(latitude, longitude)
                alertDialog.dismiss()
            }

            btnCancel.setOnClickListener {
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

    private fun openGoogleMap(latitude: Double, longitude: Double) {
        val uri = Uri.parse("geo:$latitude,$longitude?q=$latitude,$longitude")
        val mapIntent = Intent(Intent.ACTION_VIEW, uri)
        mapIntent.setPackage("com.google.android.apps.maps")
        startActivity(mapIntent)
    }

    private fun bottomSheet() {
        sheetBehavior = BottomSheetBehavior.from(nearByHotelBinding.bsHotelMapNavigation)

        val maxHeight = resources.displayMetrics.heightPixels * 0.75 // 75% of screen height
        val peekHeight = resources.getDimension(com.intuit.sdp.R.dimen._150sdp).toInt()

        sheetBehavior?.peekHeight = peekHeight
        sheetBehavior?.maxHeight = maxHeight.toInt()
        sheetBehavior?.state = BottomSheetBehavior.STATE_EXPANDED
        sheetBehavior?.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {}

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })
    }

    @SuppressLint("PotentialBehaviorOverride")
    override fun onMapReady(map: GoogleMap) {
        googleMap = map

        setHotelMarker(mCenterLatLong, name)
    }

    private fun setHotelMarker(position: LatLng, name: String) {
        val bitmapDescriptor = getBitmapDescriptorFromVector(name)
        googleMap.addMarker(MarkerOptions().position(position).icon(bitmapDescriptor))

        val cameraUpdate = CameraUpdateFactory.newLatLngZoom(position, 12.0f)
        googleMap.animateCamera(cameraUpdate, 1000, object : GoogleMap.CancelableCallback {
            override fun onFinish() {
            }

            override fun onCancel() {
            }
        })
    }

    @SuppressLint("SetTextI18n", "DefaultLocale")
    private fun getBitmapDescriptorFromVector(name: String): BitmapDescriptor {
        val binding = CustomMarkerBinding.inflate(LayoutInflater.from(this))

        // Set price text
        binding.markerText.text = name

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
}