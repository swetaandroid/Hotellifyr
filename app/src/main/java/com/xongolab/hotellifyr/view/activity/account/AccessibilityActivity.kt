package com.xongolab.hotellifyr.view.activity.account

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.xongolab.hotellifyr.R
import com.xongolab.hotellifyr.core.CoreActivity
import com.xongolab.hotellifyr.databinding.ActivityAccessibilityBinding
import com.xongolab.hotellifyr.model.PreferenceRequest
import com.xongolab.hotellifyr.model.PreferencesItemData
import com.xongolab.hotellifyr.utils.Pref
import com.xongolab.hotellifyr.utils.Util
import com.xongolab.hotellifyr.utils.Util.createPartFromString
import com.xongolab.hotellifyr.view.adapter.MyPreferenceAdapter
import com.xongolab.hotellifyr.viewModel.HotelViewModel
import com.xongolab.hotellifyr.viewModel.ViewModelFactory
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody

class AccessibilityActivity : CoreActivity() {

    private lateinit var binding: ActivityAccessibilityBinding
    private lateinit var brandAdapter: MyPreferenceAdapter
    var parentTitle : String = ""
    var itemList: ArrayList<PreferencesItemData>? = ArrayList()
    private lateinit var hotelViewModel: HotelViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAccessibilityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (intent != null) {
            parentTitle = intent.getStringExtra("parentTitle").toString()
            if (intent.hasExtra("itemData")) {
                itemList = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    intent.getParcelableArrayListExtra("itemData", PreferencesItemData::class.java)
                } else {
                    @Suppress("DEPRECATION")
                    intent.getParcelableArrayListExtra("itemData")
                }
            }
        }
        initView()
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btnBack -> finish()
            R.id.btnSave -> {
                setMyPreferencesApi()
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun initView() {
        initViewModel()
        binding.layoutToolBar.toolbarTitle.text = parentTitle
        binding.layoutToolBar.btnBack.setOnClickListener(this)

        // Elevator Proximity
        brandAdapter = MyPreferenceAdapter(this)
        binding.rvPreferences.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        brandAdapter.clearData()
        itemList?.let { brandAdapter.addData(it) }
        binding.rvPreferences.adapter = brandAdapter

        binding.btnSave.setOnClickListener(this)

    }

    private fun initViewModel() {
        hotelViewModel = ViewModelProvider(this, ViewModelFactory(mainRepository))[HotelViewModel::class.java]
        observeViewModel()
    }

    private fun observeViewModel() {
        hotelViewModel.setMyPreferencesApiResponse.observe(this) {response ->
            response?.let {
                Toast.makeText(this@AccessibilityActivity, "Preferences Update Successfully", Toast.LENGTH_SHORT).show()
//                Pref.clearStringList(Pref.PREF_MY_PREFERENCES)
                finish()
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setMyPreferencesApi() {
        if (isInternetConnected()) {
            val specialRequest = createPartFromString("", "specialRequest")
            val jsonArray = Gson().toJson(Pref.getStringList(Pref.PREF_MY_PREFERENCES)) // Convert list to JSON
            val requestBody = jsonArray.toRequestBody("application/json".toMediaTypeOrNull())
            val preferenceRequest = PreferenceRequest()
            preferenceRequest.specialRequest = ""
            preferenceRequest.preferences = Pref.getStringList(Pref.PREF_MY_PREFERENCES)
            hotelViewModel.setMyPreferencesApi(this,preferenceRequest)
        } else {
            msgDialog(getString(R.string.check_internet))
        }
    }

}
