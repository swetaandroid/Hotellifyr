package com.xongolab.hotellifyr.view.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.xongolab.hotellifyr.R
import com.xongolab.hotellifyr.core.CoreFragment
import com.xongolab.hotellifyr.databinding.FragmentProfileBinding
import com.xongolab.hotellifyr.model.HotelModel
import com.xongolab.hotellifyr.model.ResortModel
import com.xongolab.hotellifyr.model.UserModel
import com.xongolab.hotellifyr.utils.Pref
import com.xongolab.hotellifyr.utils.Util
import com.xongolab.hotellifyr.utils.Util.msgDialog
import com.xongolab.hotellifyr.view.activity.MainActivity
import com.xongolab.hotellifyr.view.activity.account.AccountActivity
import com.xongolab.hotellifyr.view.activity.account.MemberDiscountDetailsActivity
import com.xongolab.hotellifyr.view.activity.account.SettingActivity
import com.xongolab.hotellifyr.view.activity.home.search.hotel.HotelOfferDetailActivity
import com.xongolab.hotellifyr.view.adapter.CurrentOfferAdapter
import com.xongolab.hotellifyr.viewModel.HotelViewModel
import com.xongolab.hotellifyr.viewModel.UserViewModel
import com.xongolab.hotellifyr.viewModel.ViewModelFactory


class ProfileFragment : CoreFragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var currentOfferAdapter: CurrentOfferAdapter
    private lateinit var userViewModel: UserViewModel
    private lateinit var hotelViewModel: HotelViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
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

            R.id.icSetting -> {
                startActivity(Intent(requireContext(), SettingActivity::class.java))
            }

            R.id.rlMemberLevel -> {
                startActivity(Intent(requireContext(), MemberDiscountDetailsActivity::class.java))
            }

            R.id.rlEarnPoints -> {
                startActivity(Intent(requireContext(), AccountActivity::class.java))
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun initView() {
        initViewModel()
        binding.btnBack.setOnClickListener(this)
        binding.icSetting.setOnClickListener(this)
        binding.rlMemberLevel.setOnClickListener(this)
        binding.rlEarnPoints.setOnClickListener(this)

        currentOfferAdapter = CurrentOfferAdapter(requireContext())
        binding.rvCurrentOffer.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        binding.rvCurrentOffer.adapter = currentOfferAdapter

        currentOfferAdapter.onItemClick = { item ->
            val model = HotelModel.HotelOffersSection().apply {
                title = item.title
                description = item.description
                details = item.details
                id = item.id
                images.add(item.images)
                validity = item.validity
                subtitle = item.subtitle
            }

            val intent = Intent(coreActivity!!, HotelOfferDetailActivity::class.java)
            intent.putExtra("response", Gson().toJson(model))
            startActivity(intent)
        }


        val snapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(binding.rvCurrentOffer)

        getCurrentOfferListApi()

    }


    private fun initViewModel() {
        userViewModel = ViewModelProvider(this, ViewModelFactory(coreActivity!!.mainRepository))[UserViewModel::class.java]
        hotelViewModel = ViewModelProvider(this, ViewModelFactory(coreActivity!!.mainRepository))[HotelViewModel::class.java]
        observeViewModel()
    }

    private fun observeViewModel() {
        userViewModel.getCustomerInfoApiResponse.observe(coreActivity!!) { response ->
            response?.let {
                val payload = it.payload!!
                binding.apply {
                    tvAvailablePoints.text = "${payload.availablePoints}"
                    tvCurrentTier.text = payload.currentTier

                    Pref.setStringValue(Pref.PREF_FIRST_NAME, payload.firstName)
                    Pref.setStringValue(Pref.PREF_LAST_NAME, payload.lastName)
                    Pref.setStringValue(Pref.PREF_EMAIL, payload.email)
                    Pref.setStringValue(Pref.PREF_COUNTRY_CODE, payload.mobileCountryCode)
                    Pref.setStringValue(Pref.PREF_PROFILE_PIC, payload.avatar ?: "")
                }
            }
        }

        hotelViewModel.getCurrentOfferListApiResponse.observe(coreActivity!!) { response ->
            response?.let {
                currentOfferAdapter.addData(it.payload)
            }
        }
    }


    @SuppressLint("SetTextI18n")
    private fun getCurrentOfferListApi() {
        if (isInternetConnected()) {
            hotelViewModel.getCurrentOfferListApi(coreActivity!!)
        }
    }


    @SuppressLint("HardwareIds")
    private fun getCustomerInfoApi() {
        if (isInternetConnected()) {
            userViewModel.getCustomerInfoApi(coreActivity!!)
        } else {
            msgDialog(coreActivity!!, getString(R.string.check_internet))
        }
    }


    override fun onResume() {
        super.onResume()
        getCustomerInfoApi()
    }
}