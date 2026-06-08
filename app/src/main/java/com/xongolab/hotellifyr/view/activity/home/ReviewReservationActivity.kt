package com.xongolab.hotellifyr.view.activity.home

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.webkit.WebView
import android.widget.RadioButton
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.payu.base.models.ErrorResponse
import com.payu.base.models.PayUPaymentParams
import com.payu.checkoutpro.PayUCheckoutPro
import com.payu.checkoutpro.utils.PayUCheckoutProConstants.CP_HASH_NAME
import com.payu.checkoutpro.utils.PayUCheckoutProConstants.CP_HASH_STRING
import com.payu.checkoutpro.utils.PayUCheckoutProConstants.CP_MERCHANT_RESPONSE
import com.payu.checkoutpro.utils.PayUCheckoutProConstants.CP_PAYU_RESPONSE
import com.payu.ui.model.listeners.PayUCheckoutProListener
import com.payu.ui.model.listeners.PayUHashGenerationListener
import com.stripe.android.paymentsheet.PaymentSheet
import com.stripe.android.paymentsheet.PaymentSheetResult
import com.xongolab.hotellifyr.R
import com.xongolab.hotellifyr.core.CoreActivity
import com.xongolab.hotellifyr.custom.SpaceItemDecoration
import com.xongolab.hotellifyr.databinding.ActivityReviewReservationBinding
import com.xongolab.hotellifyr.idrequest.IDRequestBuilder
import com.xongolab.hotellifyr.model.AddOnExperienceRequest
import com.xongolab.hotellifyr.model.AddPromoCodeRequest
import com.xongolab.hotellifyr.model.AirportPickupDetailRequest
import com.xongolab.hotellifyr.model.BookingHotelRequest
import com.xongolab.hotellifyr.model.PaymentMethod
import com.xongolab.hotellifyr.model.PayuResponseModel
import com.xongolab.hotellifyr.utils.Constants
import com.xongolab.hotellifyr.utils.DialogManager
import com.xongolab.hotellifyr.utils.Pref
import com.xongolab.hotellifyr.utils.Util
import com.xongolab.hotellifyr.utils.makeGone
import com.xongolab.hotellifyr.utils.makeVisible
import com.xongolab.hotellifyr.view.activity.MainActivity
import com.xongolab.hotellifyr.view.activity.auth.WebActivity
import com.xongolab.hotellifyr.view.adapter.AddOnsExperienceAdapter
import com.xongolab.hotellifyr.view.adapter.AirportPickAdapter
import com.xongolab.hotellifyr.view.adapter.PaymentMethodAdapter
import com.xongolab.hotellifyr.view.adapter.ReviewReservationRoomAdapter
import com.xongolab.hotellifyr.view.adapter.SelectedAddOnExperienceAdapter
import com.xongolab.hotellifyr.view.adapter.hotel.AddOnAdapter
import com.xongolab.hotellifyr.viewModel.HotelViewModel
import com.xongolab.hotellifyr.viewModel.UserViewModel
import com.xongolab.hotellifyr.viewModel.ViewModelFactory
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException
import java.security.MessageDigest
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlin.math.floor
import kotlin.random.Random

class ReviewReservationActivity : CoreActivity() {
    private lateinit var binding: ActivityReviewReservationBinding
    private lateinit var addOnsExperienceAdapter: AddOnsExperienceAdapter
    private lateinit var selectedAddOnExperienceAdapter: SelectedAddOnExperienceAdapter
    private lateinit var paymentMethodAdapter: PaymentMethodAdapter
    private lateinit var airportPickAdapter: AirportPickAdapter
    private lateinit var reviewReservationRoomAdapter: ReviewReservationRoomAdapter
    private lateinit var addOnPriceAdapter: AddOnAdapter

    private var bookingHotelRequest = BookingHotelRequest()
    private lateinit var userViewModel: UserViewModel
    private lateinit var hotelViewModel: HotelViewModel
    private var isAgree = false
    private var generatedHash: String = ""
    private lateinit var callHashGenerationListener: PayUHashGenerationListener
    private lateinit var hashName: String
    private lateinit var dialogManager: DialogManager

    private var finalCost: Double = 0.0
    private val client = OkHttpClient()
    var customerPaymentId: String = ""
    var ephemeralKey: String = ""
    lateinit var paymentSheet: PaymentSheet
    var paymentIntentClientSecret: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReviewReservationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setFullScreenMode(window)

        initView()

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                showExitConfirmationDialog()
            }
        })
    }

    @SuppressLint("SetTextI18n", "NotifyDataSetChanged")
    private fun initView() {

        initViewModel()

        paymentSheet = PaymentSheet(this, ::onPaymentSheetResult)

        if (intent != null) {
            bookingHotelRequest = intent.getSerializableExtra("bookingHotelRequest") as BookingHotelRequest
        }

        dialogManager = DialogManager(this)

        binding.toolbarTitle.text = getString(R.string.review_reservation)

        getAirportPickApi()

        hotelDataSet()
        binding.btnBack.setOnClickListener(this)
        binding.llAddOnsExperience.setOnClickListener(this)
        binding.llPaymentOption.setOnClickListener(this)
        binding.llPromoCode.setOnClickListener(this)
        binding.tvAddCard.setOnClickListener(this)
        binding.llFlightDateTime.setOnClickListener(this)
        binding.btnSave.setOnClickListener(this)
        binding.btnBookNow.setOnClickListener(this)
        binding.btnPromoAdd.setOnClickListener(this)
        binding.btnDeletePromo.setOnClickListener(this)
        binding.ivAgree.setOnClickListener(this)

        reviewReservationRoomAdapter = ReviewReservationRoomAdapter(this)
        binding.rvReservationRoom.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        binding.rvReservationRoom.adapter = reviewReservationRoomAdapter

        reviewReservationRoomAdapter.addData(bookingHotelRequest.bookRoom)
        reviewReservationRoomAdapter.notifyDataSetChanged()


        addOnPriceAdapter = AddOnAdapter(this)
        binding.rvAddOnPrice.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        binding.rvAddOnPrice.adapter = addOnPriceAdapter


        addOnsExperienceAdapter = AddOnsExperienceAdapter(this)
        binding.rvAddOnsExperience.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        binding.rvAddOnsExperience.adapter = addOnsExperienceAdapter

        val spacingInPixels = resources.getDimensionPixelSize(com.intuit.sdp.R.dimen._5sdp)
        binding.rvAddOnsExperience.addItemDecoration(SpaceItemDecoration(0, spacingInPixels))

        addOnsExperienceAdapter.onItemClick = { _, item ->
            val intent = Intent(this@ReviewReservationActivity, AddOnsExperienceDetailsActivity::class.java)
            intent.putExtra("checkIn", bookingHotelRequest.checkIn)
            intent.putExtra("checkOut", bookingHotelRequest.checkOut)
            intent.putExtra("item", Gson().toJson(item))
            resultLauncher.launch(intent)
        }

        getAddOnExperienceListApi()

        selectedAddOnExperienceAdapter = SelectedAddOnExperienceAdapter(this)
        binding.rvSelectedAddOnsExperience.layoutManager =
            LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        binding.rvSelectedAddOnsExperience.adapter = selectedAddOnExperienceAdapter

        selectedAddOnExperienceAdapter.onItemDeleteClick = { position, _ ->
            bookingHotelRequest.addOnExperience.removeAt(position)
            selectedAddOnExperienceAdapter.removeItem(position)
            hotelDataSet()
        }

        paymentMethodAdapter = PaymentMethodAdapter(this)
        binding.rvPaymentMethod.layoutManager =
            LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        binding.rvPaymentMethod.adapter = paymentMethodAdapter

        binding.rvPaymentMethod.addItemDecoration(
            SpaceItemDecoration(
                resources.getDimensionPixelSize(
                    com.intuit.sdp.R.dimen._8sdp
                ), 0
            )
        )

        paymentMethodList()

        makeLinks(
            binding.tvAgreeTerms,
            "other",
            Pair(getString(R.string.privacy_policy), View.OnClickListener {
                startActivity(Intent(this@ReviewReservationActivity, WebActivity::class.java).apply {
                    putExtra(Constants.TITLE, getString(R.string.privacy_policy))
                    putExtra(Constants.WEB_URL, Pref.getStringValue(Constants.PRIVACY_POLICY, ""))
                })
            }),
            Pair(getString(R.string.terms_conditions), View.OnClickListener {
                startActivity(Intent(this@ReviewReservationActivity, WebActivity::class.java).apply {
                    putExtra(Constants.TITLE, getString(R.string.terms_conditions))
                    putExtra(Constants.WEB_URL, Pref.getStringValue(Constants.TERMS_CONDITION, ""))
                })
            }),
        )

        airportPickAdapter = AirportPickAdapter(this)
        binding.rvAirportPick.layoutManager =
            LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        binding.rvAirportPick.adapter = airportPickAdapter

        binding.rvAirportPick.addItemDecoration(SpaceItemDecoration(0, spacingInPixels))

        airportPickAdapter.onItemClick = { _, item ->
            for (obj in airportPickAdapter.objList) {
                obj.isSelect = false
            }
            item.isSelect = true

            binding.llAirportPickupInfo.makeVisible()
            airportPickAdapter.notifyDataSetChanged()
        }

        //  airportPickList()

        binding.switchAirportPick.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                bookingHotelRequest.airportPickupDetail = AirportPickupDetailRequest()
                binding.expandAirportPick.makeVisible()
            } else {
                bookingHotelRequest.airportPickupDetail = null
                binding.expandAirportPick.makeGone()
            }
            updateAirportPickUI()   // 👈 CLEAN CONTROL
            hotelDataSet()
        }

        updatePromoCodeUI()
        updateAddOnsExperienceClick()

    }

    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val resultData = Gson().fromJson(
                    result.data?.getStringExtra("AddOnExperienceRequest"),
                    AddOnExperienceRequest::class.java
                )

                val existingIndex = bookingHotelRequest.addOnExperience.indexOfFirst {
                    it.packageID == resultData.packageID
                }

                if (existingIndex != -1) {
                    // Replace existing item
                    bookingHotelRequest.addOnExperience[existingIndex] = resultData
                } else {
                    // Add new item
                    bookingHotelRequest.addOnExperience.add(resultData)
                }

                selectedAddOnExperienceAdapter.addData(bookingHotelRequest.addOnExperience)

                hotelDataSet()
            }
        }

    private fun initViewModel() {
        userViewModel = ViewModelProvider(this, ViewModelFactory(mainRepository))[UserViewModel::class.java]
        hotelViewModel = ViewModelProvider(this, ViewModelFactory(mainRepository))[HotelViewModel::class.java]
        observeViewModel()
    }

    @SuppressLint("SetTextI18n")
    private fun observeViewModel() {
        userViewModel.getSendBookOTPApiResponse.observe(this) { response ->
            response?.let {

                val intent = Intent(this@ReviewReservationActivity, HotelBookOtpActivity::class.java)
                intent.putExtra(Constants.MOBILE, bookingHotelRequest.primaryGuestDetail.Mobile)
                intent.putExtra(Constants.MOBILE_COUNTRY_CODE, bookingHotelRequest.primaryGuestDetail.MobileCountryCode)
                intent.putExtra(Constants.OTP_ID, it.payload?.otpID)
                intent.putExtra(Constants.OTP, it.payload?.otp.toString())
                intent.putExtra("bookingHotelRequest", bookingHotelRequest)
                startActivity(intent)
            }
        }

        hotelViewModel.getAirportPickApiResponse.observe(this) { response ->
            response?.let {
                airportPickAdapter.addData(it.payload)
            }
        }

        hotelViewModel.getAddOnExperienceListApiResponse.observe(this) { response ->
            response?.let {
                addOnsExperienceAdapter.addData(it.payload)
                updateAddOnsExperienceClick()
            }
        }

        hotelViewModel.generateHashApiResponse.observe(this) { response ->
            response?.let {
                generatedHash = it.payload!!.hash

                val hash: String? = generatedHash /*+ "tCjZjihVszOBfwyp4XS67lii8USuMDjf"*/
                Log.e(TAG, "generateHash: ${hash}")
                if (!TextUtils.isEmpty(hash)) {
                    val dataMap = HashMap<String, String>()
                    dataMap[hashName] = hash!!
                    callHashGenerationListener.onHashGenerated(dataMap as HashMap<String, String?>)
                }
                //  startPayUCheckout()
            }

        }

        hotelViewModel.callAddPromoCodeApiResponse.observe(this) { response ->
            response?.let {
                binding.llPromoConfirm.makeVisible()
                binding.llPromoApplied.makeVisible()
                binding.tvPromo.text = "${getString(R.string.promo_code)}(${it.payload!!.couponCode})"
                binding.tvPromoCode.text = " (${it.payload!!.couponCode})"
                binding.tvPromoPrice.text = setPriceWithUnit(it.payload!!.discountValue)
                binding.tvPromoCodePrice.text = setPriceWithUnit(it.payload!!.discountValue)
                binding.btnPromoAdd.makeGone()
                bookingHotelRequest.couponDetail = it.payload!!
                hotelDataSet()
            }
        }

        hotelViewModel.callBookingHotelApiResponse.observe(this) { response ->
            response?.let {
                startActivity(
                    Intent(this@ReviewReservationActivity, BookingConfirmedActivity::class.java)
                        .putExtra("bookingId", it.payload!!.bookingId)
                        .putExtra("hotelName", bookingHotelRequest.hotel)
                        .putExtra("checkIn", bookingHotelRequest.checkIn)
                        .putExtra("checkOut", bookingHotelRequest.checkOut)
                )
                finishAffinity()
            }
        }

        userViewModel.getPaymentApiResponse.observe(this) { response ->
            response?.let {
                paymentIntentClientSecret = it.payload!!.paymentIntent
                ephemeralKey = it.payload!!.ephemeralKey
                customerPaymentId = it.payload!!.paymentCustomerId
                presentPaymentSheet()
            }
        }
    }

    /*private fun startPayUCheckout() {
        val randomStr = Random.nextInt(100000, 999999)

        val payUPaymentParams = PayUPaymentParams.Builder()
            .setAmount(bookingHotelRequest.totalPayableAmount.toString())
            .setTransactionId("TXN_${System.currentTimeMillis()}_$randomStr")
            .setKey(Constants.PAYU_KEY)
            .setProductInfo("Booking for ${bookingHotelRequest.hotel}")
            .setFirstName(bookingHotelRequest.primaryGuestDetail.FirstName)
            .setEmail(bookingHotelRequest.primaryGuestDetail.Email)
            .setPhone(bookingHotelRequest.primaryGuestDetail.Mobile)
            .setSurl(Constants.PAYU_SURL)
            .setFurl(Constants.PAYU_FURL)
            .setIsProduction(true)
            .setUserCredential(Constants.PAYU_KEY + ":${bookingHotelRequest.primaryGuestDetail.Email}")
            .setUserToken(Pref.getStringValue(Pref.PREF_AUTH_TOKEN, ""))
            .build()

        PayUCheckoutPro.open(this, payUPaymentParams, object : PayUCheckoutProListener {
            override fun onPaymentSuccess(response: Any) {
                response as HashMap<*, *>
                val payUResponse = response[CP_PAYU_RESPONSE]
                val merchantResponse = response[CP_MERCHANT_RESPONSE]

                val gson = Gson()
                val jsonString = payUResponse.toString()
                val payuResponse = gson.fromJson(jsonString, PayuResponseModel::class.java)

                if (payuResponse != null) {
                    bookingHotelRequest.paymentStatus = "paid"
                    bookingHotelRequest.paymentDetails = jsonString

                    callBookingHotelApi()
                }
                Log.e(TAG, "onPaymentSuccess: ")
            }

            override fun setWebViewProperties(webView: WebView?, bank: Any?) {

            }

            override fun generateHash(
                map: HashMap<String, String?>,
                hashGenerationListener: PayUHashGenerationListener
            ) {

                val hashName: String? = map.get(CP_HASH_NAME)
                val hashData: String? = map.get(CP_HASH_STRING)

                callAPIRetrofitAPI(hashName, hashData, hashGenerationListener)

            }

            override fun onError(errorResponse: ErrorResponse) {
                Log.e(TAG, "onError: ${errorResponse.errorMessage}")
                Log.e(TAG, "onError: ${errorResponse.errorCode}")
                msgDialog(errorResponse.errorMessage!!)
            }

            override fun onPaymentCancel(isTxnInitiated: Boolean) {
                Log.e(TAG, "onPaymentCancel: ")
            }

            override fun onPaymentFailure(response: Any) {
                Log.e(TAG, "onPaymentFailure: ")
            }
        })
    }*/

    private fun getPaymentApi(amount: String) {
        if (Util.isOnline(this)) {

            userViewModel.getPaymentApi(this, amount)
        } else {
            msgDialog(resources.getString(R.string.check_internet))
        }
    }

    private fun callBookingHotelApi() {
        if (isInternetConnected()) {
            hotelViewModel.callBookingHotelApi(this, bookingHotelRequest)
        } else {
            msgDialog(resources.getString(R.string.check_internet))
        }
    }

    fun callAPIRetrofitAPI(
        hashName: String?,
        hashData: String?,
        hashGenerationListener: PayUHashGenerationListener
    ) {
        val params = JSONObject()
        try {
            params.put("hashString", hashData)
        } catch (e: Exception) {
        }
        val JSON: MediaType? = "application/json; charset=utf-8".toMediaTypeOrNull()
        val requestBody: RequestBody? = RequestBody.create(JSON, params.toString())

        val request = Request.Builder()
            .url(Constants.BASE_URL + IDRequestBuilder.GENERATE_HASH_API)  // Replace with your API URL
            .post(requestBody!!)
            .addHeader("Authorization", "${Pref.getStringValue(Pref.PREF_AUTH_TOKEN, "")}")
            .addHeader("Content-Type", "application/json")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                response.body?.string()?.let { responseBody ->
                    try {
                        val json = JSONObject(responseBody)
                        val hash = json.getJSONObject("payload").getString("hash")
                        println("Extracted Hash: $hash")

                        Log.e(TAG, "generateHash: ${hash}")
                        if (!TextUtils.isEmpty(hash)) {
                            val dataMap: HashMap<String?, String?> = HashMap()
                            dataMap.put(hashName, hash)
                            hashGenerationListener.onHashGenerated(dataMap as HashMap<String, String?>)

                        }

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        })
    }


    private fun hotelDataSet() {
        binding.apply {
            toolbarDesc.text = bookingHotelRequest.hotel
            tvStandardRateTitle.text = "${getString(R.string.standard_rate)} (${bookingHotelRequest.numberOfDays} ${getString(R.string.night)})"
            tvStandardRate.text = setPriceWithUnit(bookingHotelRequest.totalRoomPrice)
            tvTotalTax.text = setPriceWithUnit(bookingHotelRequest.taxAmount)

            var total =
                (bookingHotelRequest.totalPayableAmount + bookingHotelRequest.addOnExperience.sumOf { it.price } + if (bookingHotelRequest.airportPickupDetail != null) bookingHotelRequest.airportPickupDetail!!.price.toDouble() else 0.0) - if (bookingHotelRequest.couponDetail != null) floor(
                    bookingHotelRequest.couponDetail!!.discountValue.toDouble()
                ).toInt().toDouble() else 0.0
            tvTotalCostOfStay.text = setPriceWithUnit(total)
            tvTotalPayAmount.text = setPriceWithUnit(total)

            if (bookingHotelRequest.paymentOption == "payNow") {
                rbPayNow.makeVisible()
                rbPayAtHotel.makeGone()
                cvPaymentOption.makeGone()
            } else if (bookingHotelRequest.paymentOption == "payAtHotel") {
                rbPayNow.makeGone()
                rbPayAtHotel.makeVisible()
                cvPaymentOption.makeGone()
            } else {
                cvPaymentOption.makeVisible()
                rbPayNow.makeVisible()
                rbPayAtHotel.makeVisible()
            }

            finalCost = total
            bookingHotelRequest.totalAddOnPrice = bookingHotelRequest.addOnExperience.sumOf { it.price }

            if (bookingHotelRequest.addOnExperience.size > 0) {
                addOnPriceAdapter.addData(bookingHotelRequest.addOnExperience)
                binding.rvAddOnPrice.visibility = View.VISIBLE
            } else {
                binding.rvAddOnPrice.visibility = View.GONE
            }

            if (bookingHotelRequest.couponDetail != null) {
                binding.llPromoConfirm.makeVisible()
                binding.tvPromoCodePrice.text = setPriceWithUnit(bookingHotelRequest.couponDetail!!.discountValue)
            } else {
                binding.llPromoConfirm.makeGone()
            }

            if (bookingHotelRequest.airportPickupDetail != null) {
                llAirportConfirm.visibility = View.VISIBLE
                tvAirPort.text = "${getString(R.string.airport_pickup)} (${bookingHotelRequest.airportPickupDetail!!.seater} ${getString(R.string.seater)})"
                tvAirportPrice.text = setPriceWithUnit(bookingHotelRequest.airportPickupDetail!!.price.toDouble())
            } else {
                llAirportConfirm.visibility = View.GONE
            }
        }
    }

    override fun onClick(view: View) {
        binding.apply {
            when (view.id) {
                R.id.btnBack -> {
                    onBackPressedDispatcher.onBackPressed()
                    // showExitConfirmationDialog()
                }

                R.id.llAddOnsExperience -> {
                    if (expandAddOnsExperience.visibility == View.VISIBLE) {
                        expandAddOnsExperience.makeGone()
                        ivExperienceArrow.rotation = 0F
                    } else {
                        expandAddOnsExperience.makeVisible()
                        ivExperienceArrow.rotation = 180F
                    }
                }

                R.id.llPaymentOption -> {
                    if (expandPaymentOption.visibility == View.VISIBLE) {
                        expandPaymentOption.makeGone()
                        ivPaymentArrow.rotation = 0F
                    } else {
                        expandPaymentOption.makeVisible()
                        ivPaymentArrow.rotation = 180F
                    }
                }

                R.id.llPromoCode -> {
                    if (expandPromoCode.visibility == View.VISIBLE) {
                        expandPromoCode.makeGone()
                        ivPromoCodeArrow.rotation = 0F
                    } else {
                        expandPromoCode.makeVisible()
                        ivPaymentArrow.rotation = 180F
                    }
                }

                R.id.tvAddCard -> {
                    startActivity(Intent(this@ReviewReservationActivity, AddCardActivity::class.java))
                }

                R.id.llFlightDateTime -> pickDateTime()

                R.id.btnSave -> {
                    if (isValidate()) {
                        expandAirportPick.makeGone()
                        val airportPickupDetail = AirportPickupDetailRequest()

                        val selectedItem = airportPickAdapter.objList.find { it.isSelect }
                        selectedItem?.let {
                            airportPickupDetail.seater = it.seater
                            airportPickupDetail.vehicleID = it.vehicleID
                            airportPickupDetail.from = Util.getTextValue(edtFromDestination)
                            airportPickupDetail.flightNumber = Util.getTextValue(edtFlightNumber)
                            airportPickupDetail.vehicle = it.vehicle
                            airportPickupDetail.price = it.price
                            airportPickupDetail.flightDate = Util.formatTimestamp(Util.getTextValue(tvFlightDateTime), "dd/MM/yyyy hh:mm a", "yyyy-MM-dd hh:mm:ss")
                        }

                        bookingHotelRequest.airportPickupDetail = airportPickupDetail

                        edtNoOfPassenger.setText("")
                        edtFromDestination.setText("")
                        edtFlightNumber.setText("")
                        tvFlightDateTime.text = ""

                        hotelDataSet()
                    }
                }

                R.id.btnBookNow -> {
                    if (bookingHotelRequest.paymentOption == "payNow") {
                        bookingHotelRequest.totalPayableAmount = finalCost

                        bookingHotelRequest.paymentOption = "payNow"

                        if (switchAirportPick.isChecked && bookingHotelRequest.airportPickupDetail!!.vehicleID.isEmpty()) {
                            Util.msgDialog(
                                this@ReviewReservationActivity,
                                getString(R.string.please_select_the_vehicle_and_fill_in_the_pickup_information_then_save)
                            )
                        } else if (!isAgree) {
                            Util.msgDialog(this@ReviewReservationActivity, getString(R.string.terms_and_condition_validation))
                        } else {
//                            startPayUCheckout()
                            getPaymentApi(bookingHotelRequest.totalPayableAmount.toString())
                        }
                    } else if (bookingHotelRequest.paymentOption == "payAtHotel") {
                        bookingHotelRequest.totalPayableAmount = finalCost
                        bookingHotelRequest.paymentOption = "payAtHotel"
                        bookingHotelRequest.paymentStatus = "unpaid"

                        if (switchAirportPick.isChecked && bookingHotelRequest.airportPickupDetail!!.vehicleID.isEmpty()) {
                            Util.msgDialog(
                                this@ReviewReservationActivity,
                                getString(R.string.please_select_the_vehicle_and_fill_in_the_pickup_information_then_save)
                            )
                        } else if (!isAgree) {
                            Util.msgDialog(this@ReviewReservationActivity, getString(R.string.terms_and_condition_validation))
                        } else {
                            sendOTPApi()
                        }
                    } else {
                        val selectedRadioButtonId = paymentRadioGroup.checkedRadioButtonId

                        if (selectedRadioButtonId != -1) {
                            val selectedRadioButton = findViewById<RadioButton>(selectedRadioButtonId)
                            val selectedText = selectedRadioButton.text.toString()


                            bookingHotelRequest.totalPayableAmount = finalCost

                            if (selectedText == "Pay At Hotel") {
                                bookingHotelRequest.paymentOption = "payAtHotel"
                                bookingHotelRequest.paymentStatus = "unpaid"
                            } else {
                                bookingHotelRequest.paymentOption = "payNow"
                            }

                            if (switchAirportPick.isChecked && bookingHotelRequest.airportPickupDetail!!.vehicleID.isEmpty()) {
                                Util.msgDialog(
                                    this@ReviewReservationActivity,
                                    getString(R.string.please_select_the_vehicle_and_fill_in_the_pickup_information_then_save)
                                )
                            } else if (!isAgree) {
                                Util.msgDialog(this@ReviewReservationActivity, getString(R.string.terms_and_condition_validation))
                            } else {
                                if (selectedText == "Pay At Hotel") {
                                    sendOTPApi()
                                } else {
                                    //  generateHashApi()
                                    getPaymentApi(bookingHotelRequest.totalPayableAmount.toString())
                                }
                            }

                        } else {
                            msgDialog(getString(R.string.please_select_a_payment_method))
                        }
                    }

                }

                R.id.ivAgree -> {
                    isAgree = !isAgree
                    ivAgree.setImageResource(if (isAgree) R.drawable.ic_radio_select_primary else R.drawable.ic_radio_unselect)
                }

                R.id.btnPromoAdd -> {
                    if (isPromoValidate()) {
                        callAddPromoCodeApi()
                    }
                }

                R.id.btnDeletePromo -> {
                    bookingHotelRequest.couponDetail = null
                    binding.edtPromoCode.setText("")
                    binding.llPromoApplied.makeGone()
                    binding.btnPromoAdd.makeVisible()
                    hotelDataSet()
                }
            }
        }
    }

    private fun updateAddOnsExperienceClick() {
        val hasData = addOnsExperienceAdapter.itemCount > 0

        binding.llAddOnsExperience.isClickable = hasData
        binding.llAddOnsExperience.isEnabled = hasData
        binding.ivExperienceArrow.alpha = if (hasData) 1f else 0.3f

        if (!hasData) {
            binding.expandAddOnsExperience.makeGone()
        }
    }

    private fun updatePromoCodeUI() {
        val hasPromoApplied = bookingHotelRequest.couponDetail != null

        binding.llPromoCode.isClickable = true   // keep clickable for input
        binding.llPromoCode.isEnabled = true

        // Optional UX
        binding.ivPromoCodeArrow.alpha = 1f

        if (!hasPromoApplied) {
            binding.expandPromoCode.makeGone()
        }
    }

    private fun updateAirportPickUI() {
        val isEnabled = binding.switchAirportPick.isChecked
        binding.expandAirportPick.visibility = if (isEnabled) View.VISIBLE else View.GONE

        // Optional: fade UI when OFF
        binding.expandAirportPick.alpha = if (isEnabled) 1f else 0.5f
    }

    @SuppressLint("HardwareIds")
    private fun sendOTPApi() {
        if (isInternetConnected()) {
            val countryCode =
                Util.createPartFromString(bookingHotelRequest.primaryGuestDetail.MobileCountryCode, "MobileCountryCode")
            val mobile =
                Util.createPartFromString(bookingHotelRequest.primaryGuestDetail.Mobile, "Mobile")
            val type = Util.createPartFromString("EN", "lang_code")

            userViewModel.getSendBookOTPApi(this, mobile, countryCode, type)
        } else {
            msgDialog(resources.getString(R.string.check_internet))
        }
    }

    @SuppressLint("HardwareIds")
    private fun callAddPromoCodeApi() {
        if (isInternetConnected()) {
            val addPromoCodeRequest = AddPromoCodeRequest()
            addPromoCodeRequest.couponCode = Util.getTextValue(binding.edtPromoCode)
            addPromoCodeRequest.hotelID = bookingHotelRequest.hotelID
            addPromoCodeRequest.mobile = bookingHotelRequest.primaryGuestDetail.Mobile
            addPromoCodeRequest.customerID = Pref.getStringValue(Pref.PREF_USER_ID, "")
            addPromoCodeRequest.amount = bookingHotelRequest.totalRoomPrice
            hotelViewModel.callAddPromoCodeApi(this, addPromoCodeRequest)
        } else {
            msgDialog(resources.getString(R.string.check_internet))
        }
    }

    @SuppressLint("HardwareIds")
    private fun getAirportPickApi() {
        if (isInternetConnected()) {
            hotelViewModel.getAirportPickApi(this, bookingHotelRequest.hotelID)
        } else {
            msgDialog(resources.getString(R.string.check_internet))
        }
    }

    @SuppressLint("HardwareIds")
    private fun getAddOnExperienceListApi() {
        if (isInternetConnected()) {
            hotelViewModel.getAddOnExperienceListApi(this, bookingHotelRequest.hotelID)
        } else {
            msgDialog(resources.getString(R.string.check_internet))
        }
    }

    private fun paymentMethodList() {
        paymentMethodAdapter.objList = ArrayList()

        paymentMethodAdapter.objList.add(PaymentMethod(R.drawable.ic_upi, "UPI Payment"))
        paymentMethodAdapter.objList.add(PaymentMethod(R.drawable.ic_net_banking, "Net Banking"))

        paymentMethodAdapter.addData(paymentMethodAdapter.objList)
    }

    fun presentPaymentSheet() {
        paymentSheet.presentWithPaymentIntent(
            paymentIntentClientSecret,
            PaymentSheet.Configuration(
                merchantDisplayName = "My merchant name",
                customer = PaymentSheet.CustomerConfiguration(customerPaymentId, ephemeralKey),
                allowsDelayedPaymentMethods = true
            )
        )
    }

    fun onPaymentSheetResult(paymentSheetResult: PaymentSheetResult) {
        when(paymentSheetResult) {
            is PaymentSheetResult.Canceled -> {
                Log.e("TAG", "onPaymentSheetResult: Canceled", )
                msgDialog("Transaction canceled. Please try again later.")
            }
            is PaymentSheetResult.Failed -> {
                msgDialog("Transaction failed. Please try again later.")
                Log.e("TAG", "onPaymentSheetResult: Failed", )
            }
            is PaymentSheetResult.Completed -> {
                print("Completed")
                Log.e("TAG", "onPaymentSheetResult: Completed", )

                val paymentIntentId = paymentIntentClientSecret.substringBefore("_secret_")

                val paymentData = JsonObject().apply {
                    addProperty("payment_intent_id", paymentIntentId)
                    addProperty("client_secret", paymentIntentClientSecret)
                    addProperty("payment_status", "paid")
                    addProperty("payment_gateway", "stripe")
                }

                bookingHotelRequest.paymentStatus = "paid"
                bookingHotelRequest.paymentDetails = paymentData.toString()

                Log.e("TAG", "paymentDetails: ${bookingHotelRequest.paymentDetails}")
                callBookingHotelApi()
//                payNowApi(paymentIntentClientSecret.substringBefore("_secret_"), paymentIntentClientSecret.substringBefore("_secret_"))
            }
        }
    }


    private fun isValidate(): Boolean {
        binding.apply {
            return when {
                edtNoOfPassenger.text.toString().trim().isEmpty() -> {
                    Util.msgDialog(this@ReviewReservationActivity, getString(R.string.please_enter_number_of_passengers))
                    false
                }

                edtFromDestination.text.toString().trim().isEmpty() -> {
                    Util.msgDialog(this@ReviewReservationActivity, getString(R.string.please_enter_from_destination))
                    false
                }

                edtFlightNumber.text.toString().trim().isEmpty() -> {
                    Util.msgDialog(this@ReviewReservationActivity, getString(R.string.please_enter_flight_number))
                    false
                }

                else -> true
            }
        }
    }

    private fun isPromoValidate(): Boolean {
        binding.apply {
            return when {
                edtPromoCode.text.toString().trim().isEmpty() -> {
                    Util.msgDialog(this@ReviewReservationActivity, getString(R.string.please_enter_promo_code))
                    false
                }

                else -> true
            }
        }
    }

    private fun pickDateTime() {
        val currentDateTime = Calendar.getInstance()
        val startYear = currentDateTime.get(Calendar.YEAR)
        val startMonth = currentDateTime.get(Calendar.MONTH)
        val startDay = currentDateTime.get(Calendar.DAY_OF_MONTH)
        val startHour = currentDateTime.get(Calendar.HOUR_OF_DAY)
        val startMinute = currentDateTime.get(Calendar.MINUTE)

        val datePickerDialog = DatePickerDialog(this, { _, year, month, day ->

                val isToday =
                    year == startYear &&
                            month == startMonth &&
                            day == startDay

                val timePickerDialog = TimePickerDialog(
                    this,
                    { _, hour, minute ->

                        val pickedDateTime = Calendar.getInstance()
                        pickedDateTime.set(year, month, day, hour, minute, 0)

                        // Prevent previous time selection
                        if (isToday && pickedDateTime.before(currentDateTime)) {

                            Util.msgDialog(
                                this@ReviewReservationActivity,
                                "Please select proper flight pickup time"
                            )

                            return@TimePickerDialog
                        }

                        // Format date time
                        val dateFormat = SimpleDateFormat(
                            "dd/MM/yyyy hh:mm a",
                            Locale.getDefault()
                        )

                        val formattedDateTime =
                            dateFormat.format(pickedDateTime.time)

                        binding.tvFlightDateTime.text = formattedDateTime

                        println("Formatted pickedDateTime: $formattedDateTime")

                    },
                    if (isToday) startHour else 0,
                    if (isToday) startMinute else 0,
                    false
                )

                timePickerDialog.show()

            },
            startYear,
            startMonth,
            startDay
        )

        // Minimum date = today
        datePickerDialog.datePicker.minDate =
            currentDateTime.timeInMillis

        // Maximum date = checkout date
        if (bookingHotelRequest.checkOut.isNotEmpty()) {

            val maxDateCalendar = Calendar.getInstance().apply {
                time = SimpleDateFormat(
                    "yyyy-MM-dd",
                    Locale.getDefault()
                ).parse(bookingHotelRequest.checkOut)!!
            }

            datePickerDialog.datePicker.maxDate =
                maxDateCalendar.timeInMillis
        }

        datePickerDialog.show()
    }

    private fun showExitConfirmationDialog() {

        dialogManager.showBackConfirmationBottomSheetDialog(
            onButtonClick = {
                startActivity(Intent(this@ReviewReservationActivity, MainActivity::class.java))
                finish()
            }
        )

    }
}