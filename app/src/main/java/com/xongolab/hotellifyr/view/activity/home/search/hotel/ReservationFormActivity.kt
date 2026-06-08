package com.xongolab.hotellifyr.view.activity.home.search.hotel

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import com.xongolab.hotellifyr.R
import com.xongolab.hotellifyr.core.CoreActivity
import com.xongolab.hotellifyr.databinding.ActivityReservationFormBinding
import com.xongolab.hotellifyr.model.BookTableRequest
import com.xongolab.hotellifyr.model.HotelModel
import com.xongolab.hotellifyr.utils.Pref
import com.xongolab.hotellifyr.utils.Util
import com.xongolab.hotellifyr.utils.Util.createPartFromString
import com.xongolab.hotellifyr.viewModel.HotelViewModel
import com.xongolab.hotellifyr.viewModel.ViewModelFactory
import java.text.SimpleDateFormat
import java.util.Locale

class ReservationFormActivity : CoreActivity() {

    private lateinit var binding: ActivityReservationFormBinding
    private lateinit var hotelViewModel: HotelViewModel

    private var diningData: HotelModel.DiningData = HotelModel.DiningData()
    private var bookingDate = ""
    private var bookingTime = ""
    private var diningType = ""
    private var numberOfGuests = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityReservationFormBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setFullScreenMode(window)
        initView()
    }

    @SuppressLint("SetTextI18n")
    private fun initView() {
        if (intent.hasExtra("response")) {
            diningData = Gson().fromJson(intent.getStringExtra("response"), HotelModel.DiningData::class.java)
            bookingDate = intent.getStringExtra("bookingDate")!!
            bookingTime = intent.getStringExtra("bookingTime")!!
            diningType = intent.getStringExtra("diningType")!!
            numberOfGuests = intent.getStringExtra("numberOfGuests")?.replace(" Guests", "") ?: ""
        }

        initViewModel()

        binding.toolbar.toolbarTitle.text = "Guest Details"

        if (Pref.getBooleanValue(Pref.PREF_IS_LOGIN,false)) {
            binding.edtFirstName.setText(Pref.getStringValue(Pref.PREF_FIRST_NAME, ""))
            binding.edtLastName.setText(Pref.getStringValue(Pref.PREF_LAST_NAME, ""))
            binding.edtEmailId.setText(Pref.getStringValue(Pref.PREF_EMAIL, ""))

//            binding.ccp.setCountryForPhoneCode(Pref.getStringValue(Pref.PREF_COUNTRY_CODE, "").toInt())

            val countryCode = Pref.getStringValue(Pref.PREF_COUNTRY_CODE, "")
            countryCode.toIntOrNull()?.let { binding.ccp.setCountryForPhoneCode(it) }

            println("PREF_COUNTRY_CODE form ..." + Pref.getStringValue(Pref.PREF_COUNTRY_CODE, ""))

            binding.edtPhoneNumber.setText(Pref.getStringValue(Pref.PREF_MOBILE_NO, ""))
        }

        binding.toolbar.btnBack.setOnClickListener(this)
        binding.btnConfirmReservation.setOnClickListener(this)
    }

    private fun initViewModel() {
        hotelViewModel =
            ViewModelProvider(this, ViewModelFactory(mainRepository))[HotelViewModel::class.java]
        observeViewModel()
    }

    private fun observeViewModel() {
        hotelViewModel.getBookTableApiResponse.observe(this) { response ->
            response?.let {
                val intent = Intent(this, ReservationSuccessActivity::class.java)
                intent.putExtra("response", Gson().toJson(diningData))
                intent.putExtra("bookingDate", bookingDate)
                intent.putExtra("bookingTime", bookingTime)
                intent.putExtra("diningType", diningType)
                intent.putExtra("numberOfGuests", numberOfGuests)
                startActivity(intent)
                finish()
            }
        }
    }

    @SuppressLint("HardwareIds")
    private fun getBookTableApi() {
        if (isInternetConnected()) {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd hh:mm a", Locale.getDefault())
            val outputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())

            val dateTime = inputFormat.parse("$bookingDate $bookingTime")
            val formattedDateTime = outputFormat.format(dateTime!!)

            val restaurantID = createPartFromString(diningData.id, "restaurantID")
            val hotelID = createPartFromString(diningData.hotelID, "hotelID")
            val bookingDate = createPartFromString(formattedDateTime, "bookingDate")
            val firstName = createPartFromString(Util.getTextValue(binding.edtFirstName), "FirstName")
            val lastName = createPartFromString(Util.getTextValue(binding.edtLastName), "LastName")
            val email = createPartFromString(Util.getTextValue(binding.edtEmailId), "Email")
            val mobileCountryCode = createPartFromString(binding.ccp.selectedCountryCode, "MobileCountryCode")
            val mobile = createPartFromString(Util.getTextValue(binding.edtPhoneNumber), "Mobile")
            val diningType = createPartFromString(diningType, "diningType")
            val numberOfGuests = createPartFromString(numberOfGuests, "numberOfGuests")

            hotelViewModel.getBookTableApi(
                this,
                restaurantID,
                hotelID,
                bookingDate,
                firstName,
                lastName,
                email,
                mobileCountryCode,
                mobile,
                diningType,
                numberOfGuests
            )
        }
        else {
            msgDialog(resources.getString(R.string.check_internet))
        }
    }

    private fun isValidate(): Boolean {
        var isError = true
        when {
            Util.isEmptyText(binding.edtFirstName) -> {
                isError = false
                binding.edtFirstName.requestFocus()
                Util.msgDialog(this, getString(R.string.first_name_validation))
            }

            Util.isEmptyText(binding.edtLastName) -> {
                isError = false
                binding.edtLastName.requestFocus()
                Util.msgDialog(this, getString(R.string.last_name_validation))
            }

            Util.isEmptyText(binding.edtEmailId) -> {
                isError = false
                binding.edtEmailId.requestFocus()
                Util.msgDialog(this, getString(R.string.email_validation))
            }

            !Util.isValidEmail(binding.edtEmailId.text.toString().trim()) -> {
                isError = false
                binding.edtEmailId.requestFocus()
                Util.msgDialog(this, getString(R.string.email_invalid))
            }

            Util.isEmptyText(binding.edtPhoneNumber) -> {
                isError = false
                binding.edtPhoneNumber.requestFocus()
                Util.msgDialog(this, getString(R.string.mobile_validation))
            }

            binding.edtPhoneNumber.text.toString().trim().length != 10 -> {
                isError = false
                binding.edtPhoneNumber.requestFocus()
                Util.msgDialog(this, getString(R.string.mobile_invalid))
            }
        }
        return isError
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btnBack -> finish()
            R.id.btnConfirmReservation -> {
                if (isValidate())
                    getBookTableApi()
            }
        }
    }
}