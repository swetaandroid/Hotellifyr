package com.xongolab.hotellifyr.view.activity.home.search.hotel

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.xongolab.hotellifyr.R
import com.xongolab.hotellifyr.core.CoreActivity
import com.xongolab.hotellifyr.databinding.ActivityHotelDetailBinding
import com.xongolab.hotellifyr.databinding.DialogConfirmationBinding
import com.xongolab.hotellifyr.databinding.DialogDirectionBinding
import com.xongolab.hotellifyr.databinding.DialogGalleryImageListBinding
import com.xongolab.hotellifyr.databinding.DialogGalleryImageViewBinding
import com.xongolab.hotellifyr.model.HotelModel
import com.xongolab.hotellifyr.model.SearchHotel
import com.xongolab.hotellifyr.utils.Constants
import com.xongolab.hotellifyr.utils.Pref
import com.xongolab.hotellifyr.utils.Util
import com.xongolab.hotellifyr.utils.Util.createPartFromString
import com.xongolab.hotellifyr.utils.makeGone
import com.xongolab.hotellifyr.utils.makeVisible
import com.xongolab.hotellifyr.view.activity.MainActivity
import com.xongolab.hotellifyr.view.activity.home.RoomReservationActivity
import com.xongolab.hotellifyr.view.adapter.HotelAmenitiesTabAdapter
import com.xongolab.hotellifyr.view.adapter.HotelDetailViewPager
import com.xongolab.hotellifyr.view.adapter.SmallImagesAdapter
import com.xongolab.hotellifyr.view.adapter.hotel.DrinkingDiningAdapter
import com.xongolab.hotellifyr.view.adapter.hotel.HotelDetailActivitiesAdapter
import com.xongolab.hotellifyr.view.adapter.hotel.HotelDetailAmenitiesAdapter
import com.xongolab.hotellifyr.view.adapter.hotel.HotelOfferAdapter
import com.xongolab.hotellifyr.view.adapter.hotel.HowtoReachAdapter
import com.xongolab.hotellifyr.view.adapter.hotel.NearestDestinationsAdapter
import com.xongolab.hotellifyr.view.adapter.hotel.WeddingEventAdapter
import com.xongolab.hotellifyr.view.adapter.hotel.WhatWeOfferAdapter
import com.xongolab.hotellifyr.view.adapter.hotel.gallery.GalleryAdapter
import com.xongolab.hotellifyr.view.adapter.hotel.gallery.GalleryImageAdapter
import com.xongolab.hotellifyr.view.adapter.hotel.gallery.GalleryViewImageAdapter
import com.xongolab.hotellifyr.viewModel.HotelViewModel
import com.xongolab.hotellifyr.viewModel.ViewModelFactory

class HotelDetailActivity : CoreActivity() {

    private lateinit var binding: ActivityHotelDetailBinding
    private lateinit var whatWeOfferAdapter: WhatWeOfferAdapter
    private lateinit var drinkingDiningAdapter: DrinkingDiningAdapter
    private lateinit var weddingEventAdapter: WeddingEventAdapter
    private lateinit var hotelOfferAdapter: HotelOfferAdapter
    private lateinit var hotelDetailActivitiesAdapter: HotelDetailActivitiesAdapter
    private lateinit var nearestDestinationsAdapter: NearestDestinationsAdapter
    private lateinit var howtoReachAdapter: HowtoReachAdapter
    private lateinit var hotelDetailAmenitiesAdapter: HotelDetailAmenitiesAdapter
    private lateinit var hotelAmenitiesTabAdapter: HotelAmenitiesTabAdapter
    private lateinit var galleryAdapter: GalleryAdapter
    private lateinit var galleryImageAdapter: GalleryImageAdapter
    private lateinit var galleryViewImageAdapter: GalleryViewImageAdapter
    private lateinit var hotelDetailViewPager: HotelDetailViewPager
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private lateinit var hotelViewModel: HotelViewModel
    private lateinit var hotelModel: HotelModel
    private var searchHotel = SearchHotel()
    private var hotelName: String = ""
    private var paymentOption: String = ""
    private var hotelMobile: String = ""
    private var shareHotelLink: String = ""
    private var isFavorite: Boolean = false
    private var hotelLocation: LatLng = LatLng(0.0, 0.0)

    private var clickStatus = ""
    private var lastPos: Int = -1

    private lateinit var dialogBinding: DialogConfirmationBinding
    private lateinit var alertDialog: android.app.AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHotelDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setFullScreenMode(window)
        initView()

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val intent = Intent(this@HotelDetailActivity, MainActivity::class.java)
                startActivity(intent)
                finishAffinity()
            }
        })
    }

    override fun onClick(v: View) {
        binding.apply {
            when (v.id) {
                R.id.btnBack -> onBackPressedDispatcher.onBackPressed()

                R.id.btnShare -> {
                    val name = hotelModel.name
                    val description = hotelModel.description
                    val url = hotelModel.name

                    val shareText =
                        "$name\n\n$description\n\nVisit now: $shareHotelLink"

                    val shareIntent = Intent()
                    shareIntent.setAction(Intent.ACTION_SEND)
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareText)
                    shareIntent.setType("text/plain")

                    startActivity(Intent.createChooser(shareIntent, "Share via"))
                }

                R.id.btnCall -> {
                    val callIntent = Intent(Intent.ACTION_DIAL)
                    callIntent.data = Uri.parse("tel:$hotelMobile")
                    startActivity(callIntent)
                }

                R.id.llHotelAmenitiesView -> {
                    lastPos = Integer.parseInt(v.tag.toString())
                    for (i in 0 until hotelAmenitiesTabAdapter.objList.size) {
                        hotelAmenitiesTabAdapter.objList[i].isSelected = false
                    }
                    hotelAmenitiesTabAdapter.objList[lastPos].isSelected = true

                    lblHotelFacility.text = hotelAmenitiesTabAdapter.objList[lastPos].title

                    setHotelFacilityView(lastPos)

                    hotelAmenitiesTabAdapter.notifyDataSetChanged()
                }

                R.id.ivLocation -> chooseDirectionDialog()

                R.id.icLocationDirection -> {
                    val intent = Intent(this@HotelDetailActivity, HotelMapActivity::class.java)
                        .putExtra("hotelID", searchHotel.hotelID)
                        .putExtra("name", hotelName)
                        .putExtra("latitude", hotelLocation.latitude)
                        .putExtra("longitude", hotelLocation.longitude)
                    startActivity(intent)
                }

                R.id.rlSelectRoom -> {
                    val intent =
                        Intent(this@HotelDetailActivity, RoomReservationActivity::class.java)
                    intent.putExtra(Constants.SEARCH_HOTEL, Gson().toJson(searchHotel))
                    intent.putExtra("hotelName", hotelName)
                    intent.putExtra("paymentOption", paymentOption)
                    startActivity(intent)
                }

                R.id.tvViewAllHotelOffer -> {
                    val intent = Intent(this@HotelDetailActivity, HotelOfferActivity::class.java)
                    intent.putExtra("response", Gson().toJson(hotelModel))
                    startActivity(intent)
                }

                R.id.btnWish -> {
                    showConfirmationDialog()
                }
            }
        }
    }

    @SuppressLint("ServiceCast")
    private fun chooseDirectionDialog() {
        val dialogBinding = DialogDirectionBinding.inflate(LayoutInflater.from(this))

        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setView(dialogBinding.root)

        val alertDialog: AlertDialog = alertDialogBuilder.create()
        alertDialog.show()

        alertDialog.setCancelable(false)

        dialogBinding.apply {
            tvOpenGoogleMap.setOnClickListener {
                openGoogleMap(hotelLocation.latitude, hotelLocation.longitude)
                alertDialog.dismiss()
            }

            tvCopyAddress.setOnClickListener {
                val addressToCopy = binding.tvHotelAddress.text.toString().trim()
                if (addressToCopy.isNotEmpty()) {
                    val clipboard =
                        getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    val clip = ClipData.newPlainText("Hotel Address", addressToCopy)
                    clipboard.setPrimaryClip(clip)
                    Toast.makeText(
                        this@HotelDetailActivity,
                        getString(R.string.address_copied_to_clipboard),
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        this@HotelDetailActivity,
                        getString(R.string.address_not_available),
                        Toast.LENGTH_SHORT
                    ).show()
                }
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

    fun showConfirmationDialog() {
        dialogBinding = DialogConfirmationBinding.inflate(LayoutInflater.from(this))
        val alertDialogBuilder = android.app.AlertDialog.Builder(this)
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
            resources.getString(if (isFavorite) R.string.are_you_sure_you_want_to_remove_this_hotel_from_your_wishlist else R.string.are_you_sure_you_want_to_add_this_hotel_in_your_wishlist)
        dialogBinding.tvConfirm.text = resources.getString(R.string.str_yes)
        dialogBinding.tvCancel.text = resources.getString(R.string.str_no)
        dialogBinding.apply {
            tvConfirm.setOnClickListener {
                alertDialog.dismiss()
                if (isFavorite) {
                    clickStatus = "Remove"
                    deleteWishListApi()
                } else {
                    clickStatus = "Add"
                    addWishListApi()
                }
            }
            tvCancel.setOnClickListener { alertDialog.dismiss() }
        }

        alertDialog.setCancelable(false)
    }

    private fun openGoogleMap(latitude: Double, longitude: Double) {
        val uri = Uri.parse("geo:$latitude,$longitude?q=$latitude,$longitude")
        val mapIntent = Intent(Intent.ACTION_VIEW, uri)
        mapIntent.setPackage("com.google.android.apps.maps")
        startActivity(mapIntent)
    }

    private fun initView() {
        initViewModel()

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        if (intent != null) {
            searchHotel = Gson().fromJson(
                intent.getStringExtra(Constants.SEARCH_HOTEL),
                SearchHotel::class.java
            )
        }


        val json = GsonBuilder().setPrettyPrinting().create().toJson(searchHotel)
        println("SEARCH_HOTEL...setPrettyPrinting JSON..." + json)

        binding.btnBack.setOnClickListener(this)
        binding.btnShare.setOnClickListener(this)
        binding.btnCall.setOnClickListener(this)
        binding.ivLocation.setOnClickListener(this)
        binding.icLocationDirection.setOnClickListener(this)
        binding.rlSelectRoom.setOnClickListener(this)
        binding.tvViewAllHotelOffer.setOnClickListener(this)
        binding.btnWish.setOnClickListener(this)

        setUpRecyclerviewWhatWeOffer()
        setUpRecyclerviewGallery()
        setUpRecyclerviewDrinkingDining()
        setUpRecyclerviewWeddingEvent()
        setUpRecyclerviewHotelOffer()
        setUpRecyclerviewAmenities()
        setUpRecyclerviewHotelAmenitiesTab()
        setUpRecyclerviewHowToReach()
        setUpRecyclerviewNearestDestination()
        setUpRecyclerviewActivities()

        getHotelDetailApi()
    }

    private fun initViewModel() {
        hotelViewModel =
            ViewModelProvider(this, ViewModelFactory(mainRepository))[HotelViewModel::class.java]
        observeViewModel()
    }

    @SuppressLint("SetTextI18n")
    private fun observeViewModel() {
        hotelViewModel.getHotelDetailApiResponse.observe(this) { response ->
            response?.let {
                val payload = it.payload!!
                hotelModel = it.payload!!
                binding.apply {
                    tvRating.text = payload.starRating.toString()

                    if (payload.tags.isNotEmpty())
                        tvTag.makeVisible()

                    tvTag.text = payload.tags.joinToString(", ") { it.title }
                    tvHotelName.text = payload.name
                    tvHotelAddress.text = payload.address

                    searchHotel.location = payload.address

                    hotelName = payload.name
                    hotelMobile = payload.mobile
                    hotelLocation = LatLng(payload.latitude, payload.longitude)

                    btnWish.setImageResource(if (payload.isFavorite) R.drawable.ic_favourite else R.drawable.ic_wish)

                    isFavorite = payload.isFavorite
                    paymentOption = payload.paymentOption

                    shareHotelLink = "https://demo.hotellifyr.com/hotel/" + payload.urlName


                    if (payload.overview.eN.title.isEmpty() && payload.overview.eN.description.isEmpty()) {
                        tvExperience.makeGone()
                        tvDescription.makeGone()
                        view1.makeGone()
                    } else {
                        tvExperience.makeVisible()
                        tvDescription.makeVisible()
                        view1.makeVisible()
                        tvExperience.text = payload.overview.eN.title
                        tvDescription.text = payload.overview.eN.description
                    }

                    tvPrice.text = "${payload.price}"


                    if (payload.images.isNotEmpty()) {
                        Glide.with(this@HotelDetailActivity)
                            .load(payload.images[0])
                            .placeholder(R.drawable.ic_placeholder_rectangle)
                            .into(imgMain)
                    }

                    val imgAdapter = SmallImagesAdapter(this@HotelDetailActivity) { selectedImage ->
                        Glide.with(this@HotelDetailActivity)
                            .load(selectedImage)
                            .placeholder(R.drawable.ic_placeholder_rectangle)
                            .into(imgMain)
                    }

                    rvImage.layoutManager = LinearLayoutManager(
                        this@HotelDetailActivity,
                        LinearLayoutManager.HORIZONTAL,
                        false
                    )
                    rvImage.adapter = imgAdapter
                    imgAdapter.setData(payload.images)

                    rvImage.visibility = if (payload.images.size > 1) View.VISIBLE else View.GONE

                    // What We Offer
                    if (payload.services.isNotEmpty())
                        whatWeOfferAdapter.addData(payload.services)

                    // Gallery
                    if (payload.galleryImages.isNotEmpty())
                        galleryAdapter.addData(payload.galleryImages)

                    // Drinking & Dining
                    if (payload.diningData.isNotEmpty())
                        drinkingDiningAdapter.addData(payload.diningData)

                    // Wedding & Event
                    if (payload.venueData.isNotEmpty())
                        weddingEventAdapter.addData(payload.venueData)

                    // Hotel Offers
                    if (payload.hotelOffersSections.isNotEmpty())
                        hotelOfferAdapter.addData(payload.hotelOffersSections)


                    hotelDetailAmenitiesAdapter.addData(payload.amenities)
                    howtoReachAdapter.addData(payload.howToReach)
                    nearestDestinationsAdapter.addData(payload.nearByHotel)
                    hotelDetailActivitiesAdapter.addData(payload.experienceData)

                    setHotelFacilityView(0)
                }
            }
        }

        hotelViewModel.deleteWishListApiResponse.observe(this) { response ->
            response?.let {
                if (clickStatus == "Add") {
                    Util.msgDialog(this, getString(R.string.you_ve_successfully_added_this_hotel_to_your_wishlist))
                } else if (clickStatus == "Remove") {
                    Util.msgDialog(
                        this,
                        getString(R.string.you_ve_successfully_removed_this_hotel_from_your_wishlist)
                    )
                }
                getHotelDetailApi()
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun addWishListApi() {
        if (isInternetConnected()) {
            val actionType = createPartFromString("add", "actionType")
            val hotelID = createPartFromString(searchHotel.hotelID.toString(), "hotelID")

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
    private fun deleteWishListApi() {
        if (isInternetConnected()) {
            val actionType = createPartFromString("delete", "actionType")
            val hotelID = createPartFromString(searchHotel.hotelID.toString(), "hotelID")

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
    private fun getHotelDetailApi() {
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

            hotelViewModel.getHotelDetailApi(
                this,
                Pref.getStringValue(Pref.PREF_USER_ID, ""),
                searchHotel.hotelID!!,
                checkIn,
                checkOut,
                searchHotel.adults,
                searchHotel.children
            )
        } else {
            msgDialog(getString(R.string.check_internet))
        }
    }

    private fun setUpRecyclerviewWhatWeOffer() {
        whatWeOfferAdapter = WhatWeOfferAdapter(this, this)
        binding.rvWhatWeOffer.layoutManager =
            LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        binding.rvWhatWeOffer.adapter = whatWeOfferAdapter
    }

    private fun setUpRecyclerviewWeddingEvent() {
        weddingEventAdapter = WeddingEventAdapter(this)
        binding.rvWeddingEvent.layoutManager =
            LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        binding.rvWeddingEvent.adapter = weddingEventAdapter

        weddingEventAdapter.onItemClick = { selectedItem ->
            val intent = Intent(this, WeddingEventActivity::class.java)
            intent.putExtra("hotelID", searchHotel.hotelID)
            intent.putExtra("response", Gson().toJson(selectedItem))
            startActivity(intent)
        }
    }

    private fun setUpRecyclerviewGallery() {
        galleryAdapter = GalleryAdapter(this)
        binding.rvGallery.layoutManager = GridLayoutManager(this, 4)
        binding.rvGallery.adapter = galleryAdapter

        galleryAdapter.onItemClick = { position, _ ->
            openGalleryImageView(galleryAdapter.objList, position)
        }

        galleryAdapter.onMoreItemClick = {
            openGalleryImageList(galleryAdapter.objList)
        }
    }

    private fun setUpRecyclerviewDrinkingDining() {
        drinkingDiningAdapter = DrinkingDiningAdapter(this)
        binding.rvDrinkAndDinning.layoutManager =
            LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        binding.rvDrinkAndDinning.adapter = drinkingDiningAdapter

        drinkingDiningAdapter.onItemClick = { selectedItem ->
            val intent = Intent(this, RestaurantDetailActivity::class.java)
            intent.putExtra("response", Gson().toJson(selectedItem))
            startActivity(intent)
        }
    }

    private fun setUpRecyclerviewHotelOffer() {
        hotelOfferAdapter = HotelOfferAdapter(this, isDetailActivity = true)
        binding.rvHotelOffer.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        binding.rvHotelOffer.adapter = hotelOfferAdapter

        hotelOfferAdapter.onItemClick = { selectedItem ->
            val intent = Intent(this, HotelOfferDetailActivity::class.java)
            intent.putExtra("response", Gson().toJson(selectedItem))
            startActivity(intent)
        }
    }

    private fun setUpRecyclerviewHotelAmenitiesTab() {
        hotelAmenitiesTabAdapter = HotelAmenitiesTabAdapter(this, this)
        binding.rvHotelFacility.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        binding.rvHotelFacility.adapter = hotelAmenitiesTabAdapter
        hotelFacilityList()
    }

    private fun setUpRecyclerviewHowToReach() {
        howtoReachAdapter = HowtoReachAdapter(this, this)
        binding.rvHowToReach.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        binding.rvHowToReach.adapter = howtoReachAdapter
    }

    private fun setUpRecyclerviewAmenities() {
        hotelDetailAmenitiesAdapter = HotelDetailAmenitiesAdapter(this, this)
        binding.rvAmenities.layoutManager = GridLayoutManager(this, 2)
        binding.rvAmenities.adapter = hotelDetailAmenitiesAdapter
    }

    private fun setUpRecyclerviewNearestDestination() {
        nearestDestinationsAdapter = NearestDestinationsAdapter(this, this)
        binding.rvNearestDestinations.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        binding.rvNearestDestinations.adapter = nearestDestinationsAdapter
    }

    private fun setUpRecyclerviewActivities() {
        hotelDetailActivitiesAdapter = HotelDetailActivitiesAdapter(this, this)
        binding.rvActivities.layoutManager = GridLayoutManager(this, 2)
        binding.rvActivities.adapter = hotelDetailActivitiesAdapter
    }


    @SuppressLint("SetTextI18n")
    private fun openGalleryImageList(galleryImages: List<String>) {
        val addressDialog = Dialog(this)
        val dialogBinding = DialogGalleryImageListBinding.inflate(layoutInflater)
        addressDialog.setContentView(dialogBinding.root)
        addressDialog.setCanceledOnTouchOutside(false)
        addressDialog.setCancelable(false)

        dialogBinding.rvImageGallery.visibility = View.VISIBLE

        // Set up full gallery RecyclerView
        galleryImageAdapter = GalleryImageAdapter(galleryImages)
        dialogBinding.rvImageGallery.layoutManager = GridLayoutManager(this, 2)
        dialogBinding.rvImageGallery.adapter = galleryImageAdapter

        galleryImageAdapter.onItemClick = { position, _ ->
            addressDialog.dismiss()
            openGalleryImageView(galleryImages, position)
        }

        dialogBinding.ivClose.setOnClickListener {
            addressDialog.dismiss()
        }

        val window = addressDialog.window
        val params = window?.attributes
        params?.width = ((resources.displayMetrics.widthPixels * 0.9).toInt())
        window?.attributes = params
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        addressDialog.show()
    }

    @SuppressLint("SetTextI18n")
    private fun openGalleryImageView(galleryImages: List<String>, currentPosition: Int) {
        val addressDialog = Dialog(this)
        val dialogBinding = DialogGalleryImageViewBinding.inflate(layoutInflater)
        addressDialog.setContentView(dialogBinding.root)
        addressDialog.setCanceledOnTouchOutside(false)
        addressDialog.setCancelable(false)

        dialogBinding.imgViewImage.setImageURI(galleryImages[currentPosition])

        dialogBinding.tvCount.text = "${currentPosition + 1}/${galleryImages.size}"

        // Set up horizontal image view RecyclerView
        galleryViewImageAdapter = GalleryViewImageAdapter(this, currentPosition)
        dialogBinding.rvViewImage.layoutManager =
            LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        dialogBinding.rvViewImage.adapter = galleryViewImageAdapter
        galleryViewImageAdapter.setData(galleryImages)

        galleryViewImageAdapter.onItemClick = { position, imageUrl ->
            dialogBinding.imgViewImage.setImageURI(imageUrl)

            dialogBinding.tvCount.text = "${position + 1}/${galleryImages.size}"
        }

        dialogBinding.ivClose.setOnClickListener {
            addressDialog.dismiss()
        }

        // Configure dialog appearance and display
        val window = addressDialog.window
        val params = window?.attributes
        params?.width = ((resources.displayMetrics.widthPixels * 0.9).toInt())
        window?.attributes = params
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        addressDialog.show()
    }


    private val facilityViews by lazy {
        listOf(
            binding.rvWhatWeOffer,
            binding.rvGallery,
            binding.rvDrinkAndDinning,
            binding.rvWeddingEvent,
            binding.rvHotelOffer,
            binding.rvAmenities,
            binding.rvHowToReach,
            binding.rvNearestDestinations,
            binding.rvActivities
        )
    }

    private val facilityDataCounts by lazy {
        listOf(
            hotelModel.services.size,
            hotelModel.galleryImages.size,
            hotelModel.diningData.size,
            hotelModel.venueData.size,
            hotelModel.hotelOffersSections?.size ?: 0,
            hotelModel.amenities.size,
            hotelModel.howToReach.size,
            hotelModel.nearByHotel.size,
            hotelModel.experienceData.size
        )
    }

    private fun setHotelFacilityView(position: Int) {

        val selectedRv = facilityViews[position]
        val dataCount = facilityDataCounts[position]

        facilityViews.forEach { it.visibility = View.GONE }

        if (dataCount == 0) {
            binding.tvNoDataFound.visibility = View.VISIBLE
            binding.tvViewAllHotelOffer.visibility = View.GONE
        } else {
            binding.tvNoDataFound.visibility = View.GONE
            selectedRv.visibility = View.VISIBLE

            binding.tvViewAllHotelOffer.visibility =
                if (position == 4) View.VISIBLE else View.GONE
        }
    }


    private fun hotelFacilityList() {
        hotelAmenitiesTabAdapter.objList = ArrayList()

        var model = HotelModel()
        model.title = getString(R.string.what_we_offer)
        model.isSelected = true
        hotelAmenitiesTabAdapter.objList.add(model)

        model = HotelModel()
        model.title = getString(R.string.gallery)
        hotelAmenitiesTabAdapter.objList.add(model)

        model = HotelModel()
        model.title = getString(R.string.drinking_dining)
        hotelAmenitiesTabAdapter.objList.add(model)

        model = HotelModel()
        model.title = getString(R.string.wedding_event)
        hotelAmenitiesTabAdapter.objList.add(model)

        model = HotelModel()
        model.title = getString(R.string.hotel_offers)
        hotelAmenitiesTabAdapter.objList.add(model)

        model = HotelModel()
        model.title = getString(R.string.amenities)
        hotelAmenitiesTabAdapter.objList.add(model)

        model = HotelModel()
        model.title = getString(R.string.how_to_reach)
        hotelAmenitiesTabAdapter.objList.add(model)

        model = HotelModel()
        model.title = getString(R.string.nearest_destinations)
        hotelAmenitiesTabAdapter.objList.add(model)

        model = HotelModel()
        model.title = getString(R.string.activities)
        hotelAmenitiesTabAdapter.objList.add(model)

        hotelAmenitiesTabAdapter.notifyDataSetChanged()
    }
}
