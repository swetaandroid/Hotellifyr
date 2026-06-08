package com.xongolab.hotellifyr.view.activity.account

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.xongolab.hotellifyr.R
import com.xongolab.hotellifyr.core.CoreActivity
import com.xongolab.hotellifyr.databinding.ActivityPaymentMethodBinding
import com.xongolab.hotellifyr.model.ResortModel
import com.xongolab.hotellifyr.view.adapter.account.AddCardAdapter

class PaymentMethodActivity : CoreActivity() {

    private lateinit var binding: ActivityPaymentMethodBinding
    private lateinit var addCardAdapter: AddCardAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPaymentMethodBinding.inflate(layoutInflater)
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
        binding.layoutToolBar.toolbarTitle.text = getString(R.string.payment_method)
        binding.layoutToolBar.btnBack.setOnClickListener(this)

        addCardAdapter = AddCardAdapter(this, this)
        binding.rvAddCard.layoutManager =
            LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        binding.rvAddCard.adapter = addCardAdapter

        addCard()
    }

    private fun addCard() {
        addCardAdapter.objList = ArrayList()

        var model = ResortModel()
        model.title = getString(R.string.master_card)
        model.name = "**** **** **** 1234"
        model.profileImg = R.drawable.ic_master_card
        addCardAdapter.objList.add(model)

        model = ResortModel()
        model.title = getString(R.string.upi_payment)
        model.name = "**** **** **** 1234"
        model.profileImg = R.drawable.ic_master_card
        addCardAdapter.objList.add(model)

        model = ResortModel()
        model.title = getString(R.string.net_banking)
        model.name = "**** **** **** 1234"
        model.profileImg = R.drawable.ic_master_card
        addCardAdapter.objList.add(model)

        addCardAdapter.addData(addCardAdapter.objList)
    }
}