package com.xongolab.hotellifyr.view.activity.account

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.xongolab.hotellifyr.R
import com.xongolab.hotellifyr.core.CoreActivity
import com.xongolab.hotellifyr.databinding.ActivityTransactionHistoryBinding
import com.xongolab.hotellifyr.model.ResortModel
import com.xongolab.hotellifyr.utils.Util
import com.xongolab.hotellifyr.view.adapter.account.TransactionHistoryAdapter
import com.xongolab.hotellifyr.viewModel.HotelViewModel
import com.xongolab.hotellifyr.viewModel.ViewModelFactory

class TransactionHistoryActivity : CoreActivity() {

    private lateinit var binding: ActivityTransactionHistoryBinding
    private lateinit var transactionHistoryAdapter: TransactionHistoryAdapter
    private lateinit var hotelViewModel: HotelViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTransactionHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btnBack -> finish()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun initView() {
        initViewModel()
        binding.layoutToolBar.toolbarTitle.text = getString(R.string.transaction_history)
        binding.layoutToolBar.btnBack.setOnClickListener(this)

        transactionHistoryAdapter = TransactionHistoryAdapter(this, this)
        binding.rvTransactionHistory.layoutManager =
            LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        binding.rvTransactionHistory.adapter = transactionHistoryAdapter

        binding.edtTransactionSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                val searchText = s.toString()
                if (searchText.length >= 2) {
                    // Perform search based on searchText
                    transactionHistoryAdapter.filter.filter(searchText)
                    binding.rvTransactionHistory.visibility = View.VISIBLE
                }else{
                    transactionHistoryAdapter.filter.filter("")
                }
            }
        })

       // transactionHistoryList()
    }

    private fun initViewModel() {
        hotelViewModel = ViewModelProvider(this, ViewModelFactory(mainRepository))[HotelViewModel::class.java]
        observeViewModel()
    }

    private fun observeViewModel() {
        hotelViewModel.getTransactionHistoryApiResponse.observe(this){response ->
            response?.let {
                val payload = it.payload
                if (payload!!.historyData.size > 0){
                    transactionHistoryAdapter.clearData()
                    transactionHistoryAdapter.addData(payload.historyData)
                }
                if (transactionHistoryAdapter.objList.size > 0){
                    binding.rvTransactionHistory.visibility = View.VISIBLE
                    binding.tvNoData.visibility = View.GONE
                }else{
                    binding.rvTransactionHistory.visibility = View.GONE
                    binding.tvNoData.visibility = View.VISIBLE
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun getTransactionHistoryApi() {
        if (isInternetConnected()) {
            hotelViewModel.getTransactionHistoryApi(this)
        } else {
            msgDialog(getString(R.string.check_internet))
        }
    }

    override fun onResume() {
        super.onResume()
        getTransactionHistoryApi()
    }
}