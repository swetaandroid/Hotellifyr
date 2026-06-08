package com.xongolab.hotellifyr.view.activity.account

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.xongolab.hotellifyr.R
import com.xongolab.hotellifyr.core.CoreActivity
import com.xongolab.hotellifyr.databinding.ActivityPersonalInformationBinding
import com.xongolab.hotellifyr.model.UserModel
import com.xongolab.hotellifyr.utils.ImagePicker
import com.xongolab.hotellifyr.utils.Pref
import com.xongolab.hotellifyr.utils.Util
import com.xongolab.hotellifyr.utils.Util.createPartFromString
import com.xongolab.hotellifyr.viewModel.UserViewModel
import com.xongolab.hotellifyr.viewModel.ViewModelFactory
import com.yalantis.ucrop.UCrop
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class PersonalInformationActivity : CoreActivity() {

    private lateinit var binding: ActivityPersonalInformationBinding

    private lateinit var userViewModel: UserViewModel

    private var countryID: String = ""
    private var countryList = ArrayList<UserModel>()
    private var arrayListCountry = ArrayList<String>()

    private var stateID: String = ""
    private var stateList = ArrayList<UserModel>()
    private var arrayListState = ArrayList<String>()

    private var cityID: String = ""
    private var cityList = ArrayList<UserModel>()
    private var arrayListCity = ArrayList<String>()

    private var picker: ImagePicker? = null
    private var selectImagePath: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPersonalInformationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
    }

    @SuppressLint("SetTextI18n")
    private fun initView() {
        initViewModel()

        if (picker == null) {
            picker = ImagePicker(this@PersonalInformationActivity)
            picker!!.listener = { _, uri ->
                callUCCrop(uri)
            }
        }

        binding.layoutToolBar.toolbarTitle.text = getString(R.string.personal_information)
        binding.layoutToolBar.btnBack.setOnClickListener(this)
        binding.rlUploadProfile.setOnClickListener(this)
        binding.llCountry.setOnClickListener(this)
        binding.llState.setOnClickListener(this)
        binding.llCity.setOnClickListener(this)
        binding.edtDateOfBirth.setOnClickListener(this)
        binding.edtAnniversary.setOnClickListener(this)
        binding.btnSave.setOnClickListener(this)

        getCustomerInfoApi()

        getCountryListApi()
    }

    private fun initViewModel() {
        userViewModel =
            ViewModelProvider(this, ViewModelFactory(mainRepository))[UserViewModel::class.java]
        observeViewModel()
    }

    private fun observeViewModel() {
        userViewModel.getCustomerInfoApiResponse.observe(this) { response ->
            response?.let {
                val payload = it.payload!!
                binding.apply {

                    Pref.setStringValue(Pref.PREF_FIRST_NAME, payload.firstName)
                    Pref.setStringValue(Pref.PREF_LAST_NAME, payload.lastName)
                    Pref.setStringValue(Pref.PREF_EMAIL, payload.email)
                    Pref.setStringValue(Pref.PREF_COUNTRY_CODE, payload.mobileCountryCode)
                    Pref.setStringValue(Pref.PREF_PROFILE_PIC, payload.avatar ?: "")

                        ivProfile.setImageURI(payload.avatar)
                        edtFirstName.setText(payload.firstName)
                        edtLastName.setText(payload.lastName)
                        edtMobile.text = payload.mobile
                        edtEmailId.setText(payload.email)
                        edtAddress.setText(payload.address)
                        if (!payload.dateOfBirth.isNullOrEmpty()) {
                            edtDateOfBirth.text =
                                Util.formatTimestamp(payload.dateOfBirth, parseFormat = "dd/MM/yyyy")
                        }
                        if (!payload.anniversaryDate.isNullOrEmpty()) {
                            edtAnniversary.text = Util.formatTimestamp(
                                payload.anniversaryDate,
                                parseFormat = "dd/MM/yyyy"
                            )
                        }

                        ccp.setCountryForPhoneCode(payload.mobileCountryCode.toInt())

                        countryID = payload.countryID
                        stateID = payload.stateID
                        cityID = payload.cityID

                        if (!TextUtils.isEmpty(countryID)) {
                            for (i in arrayListCountry.indices) {
                                if (countryList[i].id == countryID) {
                                    binding.spCountry.setSelection(i, true)
                                    break
                                }
                            }
                        }
                }
            }
        }

        userViewModel.getCountryListApiResponse.observe(this) { response ->
            response?.let {
                countryList.clear()
                arrayListCountry.clear()
                countryList.add(UserModel(id = "", title = "Select Country"))
                countryList.addAll(it.payload)

                for (country in countryList) {
                    arrayListCountry.add(country.title)
                }

                setCountrySpinnerAdapter()

                if (!TextUtils.isEmpty(countryID)) {
                    for (i in arrayListCountry.indices) {
                        if (countryList[i].id == countryID) {
                            binding.spCountry.setSelection(i, true)
                            break
                        }
                    }
                }
            }
        }

        userViewModel.getStateListApiResponse.observe(this) { response ->
            response?.let {
                stateList.clear()
                arrayListState.clear()
                stateList.add(UserModel(id = "", title = "Select State"))
                stateList.addAll(it.payload)

                for (state in stateList) {
                    arrayListState.add(state.title)
                }

                setStateSpinnerAdapter()

                if (!TextUtils.isEmpty(stateID)) {
                    for (i in stateList.indices) {
                        if (stateList[i].id == stateID) {
                            binding.spState.setSelection(i, true)
                            break
                        }
                    }
                }
            }
        }

        userViewModel.getCityListApiResponse.observe(this) { response ->
            response?.let {
                cityList.clear()
                arrayListCity.clear()
                cityList.add(UserModel(id = "", title = "Select City"))
                cityList.addAll(it.payload)

                for (city in cityList) {
                    arrayListCity.add(city.title)
                }

                setCitySpinnerAdapter()

                if (!TextUtils.isEmpty(cityID)) {
                    for (i in cityList.indices) {
                        if (cityList[i].id == cityID) {
                            binding.spCity.setSelection(i, true)
                            break
                        }
                    }
                }
            }
        }

        userViewModel.updateProfileApiResponse.observe(this) { response ->
            response?.let {
                msgDialog(getString(R.string.your_profile_has_been_updated_successfully))
                val payload = it.payload!!

                Handler(Looper.getMainLooper()).postDelayed({
                    finish()
                }, 1000)
            }
        }

        userViewModel.updateProfilePhotoApiResponse.observe(this) { response ->
            response?.let {
                msgDialog(getString(R.string.your_profile_picture_has_been_updated_successfully))

                Pref.setStringValue(Pref.PREF_PROFILE_PIC, it.payload!!.avatar ?: "")
            }
        }
    }

    @SuppressLint("HardwareIds")
    private fun getCustomerInfoApi() {
        if (isInternetConnected()) {
            userViewModel.getCustomerInfoApi(this)
        } else {
            msgDialog(resources.getString(R.string.check_internet))
        }
    }

    @SuppressLint("HardwareIds")
    private fun getCountryListApi() {
        if (isInternetConnected()) {
            userViewModel.getCountryListApi(this)
        } else {
            msgDialog(resources.getString(R.string.check_internet))
        }
    }

    @SuppressLint("HardwareIds")
    private fun getStateListApi(countryID: String) {
        if (isInternetConnected()) {
            userViewModel.getStateListApi(this, countryID)
        } else {
            msgDialog(resources.getString(R.string.check_internet))
        }
    }

    @SuppressLint("HardwareIds")
    private fun getCityListApi(stateID: String) {
        if (isInternetConnected()) {
            userViewModel.getCityListApi(this, stateID)
        } else {
            msgDialog(resources.getString(R.string.check_internet))
        }
    }

    @SuppressLint("HardwareIds")
    private fun updateProfileApi() {
        if (isInternetConnected()) {
            val firstName = createPartFromString(Util.getTextValue(binding.edtFirstName), "FirstName")
            val lastName = createPartFromString(Util.getTextValue(binding.edtLastName), "LastName")
            val email = createPartFromString(Util.getTextValue(binding.edtEmailId), "Email")
            val mobile = createPartFromString(Util.getTextValue(binding.edtMobile), "Mobile")
            val mobileCountryCode = createPartFromString(binding.ccp.selectedCountryCode, "MobileCountryCode")
            val address = createPartFromString(Util.getTextValue(binding.edtAddress), "address")
            val countryID = createPartFromString(countryList[binding.spCountry.selectedItemPosition].id, "countryID")
            val stateID = createPartFromString(stateList[binding.spState.selectedItemPosition].id, "stateID")
            val cityID = createPartFromString(cityList[binding.spCity.selectedItemPosition].id, "cityID")
            val anniversaryDate = if (Util.getTextValue(binding.edtAnniversary).isNullOrBlank()) {
                createPartFromString("", "anniversaryDate")
            } else {
                createPartFromString(
                    Util.formatTimestamp(
                        Util.getTextValue(binding.edtAnniversary),
                        "dd/MM/yyyy",
                        "yyyy-MM-dd"
                    ),
                    "anniversaryDate"
                )
            }
            val dateOfBirth = if (Util.getTextValue(binding.edtDateOfBirth).isNullOrBlank()) {
                createPartFromString("", "DateOfBirth")
            } else {
                createPartFromString(Util.formatTimestamp(Util.getTextValue(binding.edtDateOfBirth), "dd/MM/yyyy", "yyyy-MM-dd"), "DateOfBirth")
            }

            //    val anniversaryDate = createPartFromString(Util.formatTimestamp(Util.getTextValue(binding.edtAnniversary), "dd/MM/yyyy","yyyy-MM-dd"), "anniversaryDate")
            //   val dateOfBirth = createPartFromString(Util.formatTimestamp(Util.getTextValue(binding.edtDateOfBirth), "dd/MM/yyyy","yyyy-MM-dd"), "DateOfBirth")
            userViewModel.updateProfileApi(this, firstName, lastName, email, mobile, mobileCountryCode, address, countryID, stateID, cityID, anniversaryDate, dateOfBirth)
        } else {
            msgDialog(resources.getString(R.string.check_internet))
        }
    }

    private fun updateProfilePhotoApi() {
        if (isInternetConnected()) {
            val file = File(selectImagePath)
            val filePart = MultipartBody.Part.createFormData(
                "avatar", file.name, file.asRequestBody(
                    contentResolver.getType(Uri.fromFile(file)).toString().toMediaTypeOrNull()
                )
            )
            userViewModel.updateProfilePhotoApi(this, filePart)
        } else {
            msgDialog(resources.getString(R.string.check_internet))
        }
    }

    private fun setCountrySpinnerAdapter() {
        val adapter = ArrayAdapter(
            this, R.layout.spinner_dropdown_item, arrayListCountry
        )

        binding.spCountry.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>, view: View?, position: Int, id: Long
                ) {
                    if (view != null) {
                        binding.tvCountry.text = arrayListCountry[position]
                    }

                    if (position > 0) {
                        getStateListApi(countryList[position].id)
                    } else {
                        stateList.clear()
                        arrayListState.clear()
                        stateList.add(UserModel(id = "", title = "Select State"))

                        for (state in stateList) {
                            arrayListState.add(state.title)
                        }

                        setStateSpinnerAdapter()
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
        binding.spCountry.setPopupBackgroundResource(R.drawable.dr_spinner_bg)

        binding.spCountry.adapter = adapter
    }

    private fun setStateSpinnerAdapter() {

        val adapter = ArrayAdapter(
            this, R.layout.spinner_dropdown_item, arrayListState
        )

        binding.spState.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>, view: View?, position: Int, id: Long
            ) {

                if (view != null) {
                    binding.tvState.text = arrayListState[position]
                }

                if (position > 0) {
                    getCityListApi(stateList[position].id)
                } else {
                    cityList.clear()
                    arrayListCity.clear()
                    cityList.add(UserModel(id = "", title = "Select City"))

                    for (city in cityList) {
                        arrayListCity.add(city.title)
                    }

                    setCitySpinnerAdapter()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
        binding.spState.setPopupBackgroundResource(R.drawable.dr_spinner_bg)

        binding.spState.adapter = adapter
    }

    private fun setCitySpinnerAdapter() {
        val adapter = ArrayAdapter(
            this, R.layout.spinner_dropdown_item, arrayListCity
        )

        binding.spCity.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>, view: View?, position: Int, id: Long
            ) {
                if (view != null) {
                    binding.tvCity.text = arrayListCity[position]
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
        binding.spCity.setPopupBackgroundResource(R.drawable.dr_spinner_bg)
        binding.spCity.setSelection(0, true)

        binding.spCity.adapter = adapter
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btnBack -> finish()
            R.id.rlUploadProfile -> {
                if (picker != null && picker!!.isAdded) {
                    return
                }
                picker!!.show(supportFragmentManager, "")
            }

            R.id.llCountry -> binding.spCountry.performClick()
            R.id.llState -> binding.spState.performClick()
            R.id.llCity -> binding.spCity.performClick()
            R.id.edtDateOfBirth -> {
                val currentDate = Calendar.getInstance()
                val mYear = currentDate[Calendar.YEAR]
                val mMonth = currentDate[Calendar.MONTH]
                val mDay = currentDate[Calendar.DAY_OF_MONTH]

                val mDatePicker = DatePickerDialog(
                    this@PersonalInformationActivity, { _, year, month, day ->
                        currentDate[Calendar.YEAR] = year
                        currentDate[Calendar.MONTH] = month
                        currentDate[Calendar.DAY_OF_MONTH] = day
                        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.US)
                        binding.edtDateOfBirth.text = sdf.format(currentDate.time)

                    }, mYear - 18, mMonth, mDay
                )
                currentDate[mYear - 18, mMonth] = mDay
                val value = currentDate.timeInMillis
                mDatePicker.datePicker.maxDate = value
                mDatePicker.show()
            }

            R.id.edtAnniversary -> {
                val currentDate = Calendar.getInstance()
                val mYear = currentDate[Calendar.YEAR]
                val mMonth = currentDate[Calendar.MONTH]
                val mDay = currentDate[Calendar.DAY_OF_MONTH]

                val mDatePicker = DatePickerDialog(
                    this@PersonalInformationActivity, { _, year, month, day ->
                        currentDate[Calendar.YEAR] = year
                        currentDate[Calendar.MONTH] = month
                        currentDate[Calendar.DAY_OF_MONTH] = day
                        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.US)
                        binding.edtAnniversary.text = sdf.format(currentDate.time)

                    }, mYear, mMonth, mDay
                )
                currentDate[mYear, mMonth] = mDay
                val value = currentDate.timeInMillis
                mDatePicker.datePicker.maxDate = value
                mDatePicker.show()
            }

            R.id.btnSave -> if (isValidate()) updateProfileApi()
        }
    }

    private fun callUCCrop(uri: Uri?) {
        val options = UCrop.Options()
        options.setLogoColor(
            ContextCompat.getColor(
                this,
                R.color.colorBlack
            )
        )
        options.setStatusBarColor(
            ContextCompat.getColor(
                this,
                R.color.colorWhite
            )
        )
        options.setToolbarColor(
            ContextCompat.getColor(
                this,
                R.color.colorWhite
            )
        )
        options.setActiveControlsWidgetColor(
            ContextCompat.getColor(
                this,
                R.color.colorBlack
            )
        )
        val uCrop = UCrop.of(
            uri!!,
            Uri.fromFile(
                File(
                    cacheDir,
                    "tempPic" + System.currentTimeMillis() + ".png"
                )
            )
        )
        uCrop.withOptions(options)
        uCrop.withMaxResultSize(
            resources.getDimensionPixelSize(com.intuit.sdp.R.dimen._60sdp),
            resources.getDimensionPixelSize(com.intuit.sdp.R.dimen._60sdp)
        )
        uCrop.withAspectRatio(1f, 1f)

        cropImageLauncher.launch(uCrop.getIntent(this))
    }

    private val cropImageLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val resultUri = UCrop.getOutput(result.data!!)
                if (resultUri != null) {
                    selectImagePath = resultUri.path!!
                    binding.ivProfile.setImageURI(resultUri)
                    updateProfilePhotoApi()
                } else {
                    Toast.makeText(
                        this,
                        R.string.toast_cannot_retrieve_cropped_image,
                        Toast.LENGTH_SHORT
                    ).show()
                }
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


            binding.spCountry.selectedItemPosition <= 0 -> {
                isError = false
                Util.msgDialog(this, getString(R.string.please_select_country))
            }

            binding.spState.selectedItemPosition <= 0 -> {
                isError = false
                Util.msgDialog(this, getString(R.string.please_select_state))
            }

            binding.spCity.selectedItemPosition <= 0 -> {
                isError = false
                Util.msgDialog(this, getString(R.string.please_select_city))
            }

        }
        return isError
    }
}