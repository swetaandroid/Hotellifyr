package com.xongolab.hotellifyr.view.activity.account

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.xongolab.hotellifyr.R
import com.xongolab.hotellifyr.core.CoreActivity
import com.xongolab.hotellifyr.databinding.ActivitySettingBinding
import com.xongolab.hotellifyr.utils.Pref
import com.xongolab.hotellifyr.utils.Util
import com.xongolab.hotellifyr.utils.makeGone
import com.xongolab.hotellifyr.utils.makeVisible
import com.xongolab.hotellifyr.view.activity.auth.LoginActivity
import com.xongolab.hotellifyr.view.adapter.MyPreferencesListAdapter
import com.xongolab.hotellifyr.viewModel.HotelViewModel
import com.xongolab.hotellifyr.viewModel.UserViewModel
import com.xongolab.hotellifyr.viewModel.ViewModelFactory

class SettingActivity : CoreActivity() {
    private lateinit var binding: ActivitySettingBinding
    private lateinit var myPreferencesListAdapter: MyPreferencesListAdapter
    private lateinit var hotelViewModel: HotelViewModel
    private lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btnBack -> finish()
            R.id.rlPersonalInformation -> startActivity(Intent(this, PersonalInformationActivity::class.java))
            R.id.rlPaymentMethod -> startActivity(Intent(this, PaymentMethodActivity::class.java))
            R.id.rlBookingHistory -> startActivity(Intent(this, BookingHistoryActivity::class.java))
            R.id.rlTransactionHistory -> startActivity(Intent(this, TransactionHistoryActivity::class.java))
            /*R.id.rlRoom -> startActivity(Intent(this, RoomPreferencesActivity::class.java))
            R.id.rlAccessibility -> startActivity(Intent(this, AccessibilityActivity::class.java))
            R.id.rlFoodDrinks -> startActivity(Intent(this, FoodAndDrinksActivity::class.java))
            R.id.rlInterest -> startActivity(Intent(this, InterestActivity::class.java))
            R.id.rlSpecialRequest -> startActivity(Intent(this, SpecialRequestActivity::class.java))
            R.id.rlConfirmIdentity -> startActivity(Intent(this, ConfirmIdentityActivity::class.java))*/
            R.id.tvLogout -> {

                AlertDialog.Builder(this@SettingActivity)
                    .setTitle(getString(R.string.logout))
                    .setMessage(getString(R.string.are_you_sure_you_want_to_logout_the_app))
                    .setCancelable(true)
                    .setPositiveButton(getString(R.string.str_yes)) { dialog, _ ->
                        dialog.dismiss()

                        Pref.clearAllPref()
                        val intent = Intent(this, LoginActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent)
                        finish()
                    }
                    .setNegativeButton(getString(R.string.str_no)) { dialog, _ ->
                        dialog.dismiss()
                    }
                    .show()

            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun initView() {
        initViewModel()
        binding.layoutToolBar.toolbarTitle.text = getString(R.string.settings)
        binding.layoutToolBar.btnBack.setOnClickListener(this)

        myPreferencesListAdapter = MyPreferencesListAdapter(this)
        binding.rvMyPreferences.layoutManager =
            LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        binding.rvMyPreferences.adapter = myPreferencesListAdapter

        myPreferencesListAdapter.onItemClick = { _, item ->
            startActivity(Intent(this, AccessibilityActivity::class.java)
                .putExtra("parentTitle", item.title)
                .putExtra("itemData", item.itemList)
            )
        }


        binding.rlPersonalInformation.setOnClickListener(this)
        binding.rlPaymentMethod.setOnClickListener(this)
        binding.rlBookingHistory.setOnClickListener(this)
        binding.rlTransactionHistory.setOnClickListener(this)

        binding.tvLogout.setOnClickListener(this)
    }

    private fun initViewModel() {
        hotelViewModel = ViewModelProvider(this, ViewModelFactory(mainRepository))[HotelViewModel::class.java]
        userViewModel = ViewModelProvider(this, ViewModelFactory(mainRepository))[UserViewModel::class.java]
        observeViewModel()
    }

    private fun observeViewModel() {
        hotelViewModel.getMyPreferencesListApiResponse.observe(this) { response ->
            response?.let {
                val payload = it.payload
                if (payload.size > 0) {
                    myPreferencesListAdapter.clearData()
                    myPreferencesListAdapter.addData(payload)
                    binding.tvMyPreferencesTitle.makeVisible()
                    binding.rvMyPreferences.makeVisible()
                } else {
                    binding.tvMyPreferencesTitle.makeGone()
                    binding.rvMyPreferences.makeGone()
                }
            }
        }
        userViewModel.getCustomerInfoApiResponse.observe(this) { response ->
            response?.let {
                val payload = it.payload
                Pref.setStringList(Pref.PREF_MY_PREFERENCES, payload!!.preferences)

                Pref.setStringValue(Pref.PREF_FIRST_NAME, payload.firstName)
                Pref.setStringValue(Pref.PREF_LAST_NAME, payload.lastName)
                Pref.setStringValue(Pref.PREF_EMAIL, payload.email)
                Pref.setStringValue(Pref.PREF_COUNTRY_CODE, payload.mobileCountryCode)
                Pref.setStringValue(Pref.PREF_PROFILE_PIC, payload.avatar ?: "")
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun myPreferencesListApi() {
        if (isInternetConnected()) {
            hotelViewModel.getMyPreferencesListApi(this)
        } else {
            msgDialog(getString(R.string.check_internet))
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

    override fun onResume() {
        super.onResume()
        myPreferencesListApi()
        getCustomerInfoApi()
    }
}