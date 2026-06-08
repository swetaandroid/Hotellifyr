package com.xongolab.hotellifyr.view.activity.home.search.hotel

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.lifecycle.ViewModelProvider
import com.xongolab.hotellifyr.R
import com.xongolab.hotellifyr.core.CoreActivity
import com.xongolab.hotellifyr.databinding.ActivityBookEventBanquetBinding
import com.xongolab.hotellifyr.utils.Pref
import com.xongolab.hotellifyr.utils.Util
import com.xongolab.hotellifyr.utils.Util.createPartFromString
import com.xongolab.hotellifyr.view.activity.MainActivity
import com.xongolab.hotellifyr.view.activity.home.search.hotel.ReservationSuccessActivity
import com.xongolab.hotellifyr.view.activity.intro.Splash2Activity
import com.xongolab.hotellifyr.viewModel.HotelViewModel
import com.xongolab.hotellifyr.viewModel.ViewModelFactory
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class BookEventBanquetActivity : CoreActivity() {
    private lateinit var binding: ActivityBookEventBanquetBinding
    private lateinit var hotelViewModel: HotelViewModel

    private var dinningTypeList = ArrayList<String>()

    private var hotelID = ""
    private var id = ""
    private var dinningType = ""
    private var bookingDate = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBookEventBanquetBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setFullScreenMode(window)
        initView()
    }

    @SuppressLint("SetTextI18n")
    private fun initView() {
        if (intent != null) {
            hotelID = intent.getStringExtra("hotelID")!!
            id = intent.getStringExtra("id")!!
        }

        initViewModel()

        binding.toolbar.toolbarTitle.text = getString(R.string.book_a_banquet_hall)

        if (Pref.getBooleanValue(Pref.PREF_IS_LOGIN,false)) {
            binding.edtFirstName.setText(Pref.getStringValue(Pref.PREF_FIRST_NAME, ""))
            binding.edtLastName.setText(Pref.getStringValue(Pref.PREF_LAST_NAME, ""))
            binding.edtEmailId.setText(Pref.getStringValue(Pref.PREF_EMAIL, ""))
            binding.ccp.setCountryForPhoneCode(Pref.getStringValue(Pref.PREF_COUNTRY_CODE, "").toInt())
            binding.edtPhoneNumber.setText(Pref.getStringValue(Pref.PREF_MOBILE_NO, ""))
        }

        binding.toolbar.btnBack.setOnClickListener(this)
        binding.llDinningType.setOnClickListener(this)
        binding.tvBookingDate.setOnClickListener(this)
        binding.btnSubmit.setOnClickListener(this)

        setDinningTypeSpinnerData()
    }

    private fun initViewModel() {
        hotelViewModel =
            ViewModelProvider(this, ViewModelFactory(mainRepository))[HotelViewModel::class.java]
        observeViewModel()
    }

    private fun observeViewModel() {
        hotelViewModel.getBookEventBanquetApiResponse.observe(this) { response ->
            response?.let {
                msgDialog(getString(R.string.your_enquiry_has_been_successfully_submitted))
                Handler(Looper.getMainLooper()).postDelayed({
                    startActivity(Intent(this@BookEventBanquetActivity, MainActivity::class.java))
                    finish()
                }, 1000)
            }
        }
    }

    @SuppressLint("HardwareIds")
    private fun getBookEventBanquetApi() {
        if (isInternetConnected()) {
            val type = createPartFromString("event", "type")
            val id = createPartFromString(id, "ID")
            val hotelID = createPartFromString(hotelID, "hotelID")
            val bookingDate = createPartFromString(bookingDate, "bookingDate")
            val firstName =
                createPartFromString(Util.getTextValue(binding.edtFirstName), "FirstName")
            val lastName = createPartFromString(Util.getTextValue(binding.edtLastName), "LastName")
            val email = createPartFromString(Util.getTextValue(binding.edtEmailId), "Email")
            val mobileCountryCode =
                createPartFromString(binding.ccp.selectedCountryCode, "MobileCountryCode")
            val mobile = createPartFromString(Util.getTextValue(binding.edtPhoneNumber), "Mobile")
            val diningType = createPartFromString(dinningType, "diningType")
            val numberOfGuests =
                createPartFromString(Util.getTextValue(binding.edtNumberOfGuests), "numberOfGuests")
            hotelViewModel.getBookEventBanquetApi(
                this,
                type,
                id,
                hotelID,
                bookingDate,
                firstName,
                lastName,
                email,
                mobileCountryCode,
                mobile,
                diningType,
                numberOfGuests,
            )
        } else {
            msgDialog(resources.getString(R.string.check_internet))
        }
    }

    private fun setDinningTypeSpinnerData() {
        dinningTypeList.addAll(listOf("Lunch", "Dinner"))

        val adapter = ArrayAdapter(
            this, R.layout.spinner_dropdown_item, dinningTypeList
        )

        binding.spDinningType.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                @SuppressLint("SetTextI18n")
                override fun onItemSelected(
                    parent: AdapterView<*>, view: View?, position: Int, id: Long
                ) {
                    if (view != null) {
                        binding.tvDinningType.text = dinningTypeList[position]

                        dinningType = dinningTypeList[position]
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }

        binding.spDinningType.setPopupBackgroundResource(R.drawable.dr_spinner_bg)

        binding.spDinningType.adapter = adapter
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
            R.id.llDinningType -> binding.spDinningType.performClick()
            R.id.tvBookingDate -> datePickerDialog()
            R.id.btnSubmit -> {
                if (isValidate())
                    getBookEventBanquetApi()
            }
        }
    }

    private fun datePickerDialog() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val view = currentFocus ?: View(this)
        imm.hideSoftInputFromWindow(view.windowToken, 0)

        val currentDateTime = Calendar.getInstance()
        val startYear = currentDateTime.get(Calendar.YEAR)
        val startMonth = currentDateTime.get(Calendar.MONTH)
        val startDay = currentDateTime.get(Calendar.DAY_OF_MONTH)

        // Set minimum date to today
        val datePickerDialog = DatePickerDialog(this, { _, year, month, day ->
            // If the selected day is today, restrict the time to current or future times
            val pickedDateTime = Calendar.getInstance()
            pickedDateTime.set(year, month, day)

            val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            bookingDate = outputFormat.format(pickedDateTime.time)

            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val formattedDateTime = dateFormat.format(pickedDateTime.time)
            binding.tvBookingDate.text = formattedDateTime
        }, startYear, startMonth, startDay)

        // Set the minimum date to today
        datePickerDialog.datePicker.minDate = currentDateTime.timeInMillis
        datePickerDialog.show()
    }
}