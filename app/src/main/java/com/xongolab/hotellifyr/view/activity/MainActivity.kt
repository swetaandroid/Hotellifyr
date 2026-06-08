package com.xongolab.hotellifyr.view.activity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.xongolab.hotellifyr.R
import com.xongolab.hotellifyr.core.CoreActivity
import com.xongolab.hotellifyr.databinding.ActivityMainBinding
import com.xongolab.hotellifyr.utils.Constants
import com.xongolab.hotellifyr.utils.Pref
import com.xongolab.hotellifyr.utils.Util
import com.xongolab.hotellifyr.view.activity.auth.LoginActivity
import com.xongolab.hotellifyr.view.fragment.BookingsFragment
import com.xongolab.hotellifyr.view.fragment.Home2Fragment
import com.xongolab.hotellifyr.view.fragment.ProfileFragment
import com.xongolab.hotellifyr.view.fragment.WishListFragment
import com.xongolab.hotellifyr.viewModel.UserViewModel
import com.xongolab.hotellifyr.viewModel.ViewModelFactory

class MainActivity : CoreActivity() {
    private lateinit var binding: ActivityMainBinding

    private var isFrom: Int = 0
    private var selectedPosition: Int = 0
    private var fromContinueAsGuest: Boolean = false

    private lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setFullScreenMode(window)

        println("Auth Token: " + Pref.getStringValue(Pref.PREF_AUTH_TOKEN, ""))

        askNotificationPermission(this@MainActivity)

        initView()

        // Handle back press for Android 14 (API 34) and above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    showExitConfirmationDialog()
                }
            })
        } else {
            // For older versions
            onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    showExitConfirmationDialog()
                }
            })
        }
    }

    private fun initViewModel() {
        userViewModel =
            ViewModelProvider(this, ViewModelFactory(mainRepository))[UserViewModel::class.java]
        observeViewModel()
    }

    private fun observeViewModel() {
        userViewModel.getCMSApiResponse.observe(this) { response ->
            response?.let {
                if (it.payload.isNotEmpty()) {
                    for (item in it.payload) {
                        if (item.templateCode == "TERMS_AND_CONITIONS") {
                            Pref.setStringValue(Constants.TERMS_CONDITION, item.link)
                        } else if (item.templateCode == "PRIVACY_POLICY") {
                            Pref.setStringValue(Constants.PRIVACY_POLICY, item.link)
                        }
                    }
                } else {
                    // Handle the case where payload is empty
                }
            }
        }
    }

    private fun getCMSApi() {
        if (isInternetConnected()) {
            userViewModel.getCMSApi(this, "active")
        } else {
            msgDialog(getString(R.string.check_internet))
        }
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.btnHome -> {
                if (selectedPosition != 0) {
                    changeFragment(0)
                }
            }

            R.id.btnWish -> {
                if (Pref.getBooleanValue(Pref.PREF_IS_LOGIN, false)) {
                    if (selectedPosition != 1) {
                        changeFragment(1)
                    }
                } else {
                    startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                }
            }

            R.id.btnBooking -> {
                if (Pref.getBooleanValue(Pref.PREF_IS_LOGIN, false)) {
                    if (selectedPosition != 2) {
                        changeFragment(2)
                    }
                } else {
                    startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                }
            }

            R.id.btnAccount -> {
                if (Pref.getBooleanValue(Pref.PREF_IS_LOGIN, false)) {
                    if (selectedPosition != 3) {
                        changeFragment(3)
                    }
                } else {
                    startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                }
            }
        }
    }

    private fun initView() {
        initViewModel()

        if (intent.hasExtra("fromContinueAsGuest")) {
            fromContinueAsGuest = intent.getStringExtra("fromContinueAsGuest") == "true"

            Log.d("fromContinueAsGuest", "fromContinueAsGuest......$fromContinueAsGuest")
        }

        binding.footer.changeBackground(isFrom)
        changeFragment(isFrom)

        binding.footer.binding.btnHome.setOnClickListener(this)
        binding.footer.binding.btnWish.setOnClickListener(this)
        binding.footer.binding.btnBooking.setOnClickListener(this)
        binding.footer.binding.btnAccount.setOnClickListener(this)

        getCMSApi()
    }

    private fun showExitConfirmationDialog() {

        val dialog = AlertDialog.Builder(this)
            .setMessage(getString(R.string.are_you_sure_want_to_exit_app))
            .setCancelable(false)
            .setPositiveButton(getString(R.string.str_yes)) { _, _ ->
                finish()
            }
            .setNegativeButton(getString(R.string.str_no)) { dialog, _ ->
                dialog.dismiss()
            }
            .create()

        dialog.window?.setBackgroundDrawableResource(R.drawable.dr_white_bg_12)
        dialog.show()

    }


    fun changeFragment(pos: Int) {
        selectedPosition = pos
        binding.footer.changeBackground(selectedPosition)

        val currentFragment =
            when (pos) {
//                0 -> Home1Fragment()
                //   0 -> if (fromContinueAsGuest) Home2Fragment() else Home1Fragment()
                0 -> Home2Fragment()
                1 -> WishListFragment()
                2 -> BookingsFragment()
                3 -> ProfileFragment()
                else -> Home2Fragment()
            }

        val ft = supportFragmentManager.beginTransaction()
        if (pos == 0) {
            val args = Bundle()
            currentFragment.arguments = args
        }
        ft.replace(R.id.frameContainer, currentFragment)
        ft.commit()
    }
}
