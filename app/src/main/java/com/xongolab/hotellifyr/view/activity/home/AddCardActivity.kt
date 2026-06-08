package com.xongolab.hotellifyr.view.activity.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import com.xongolab.hotellifyr.R
import com.xongolab.hotellifyr.core.CoreActivity
import com.xongolab.hotellifyr.databinding.ActivityAddCardBinding
import com.xongolab.hotellifyr.utils.ExpiryDateFormatTextWatcher
import com.xongolab.hotellifyr.utils.FourDigitCardFormatTextWatcher
import com.xongolab.hotellifyr.utils.Util

class AddCardActivity : CoreActivity() {
    private lateinit var binding: ActivityAddCardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddCardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setFullScreenMode(window)

        initView()
    }

    @SuppressLint("SetTextI18n")
    private fun initView() {
        binding.toolbar.toolbarTitle.text = getString(R.string.add_card)
        binding.toolbar.btnBack.setOnClickListener(this)
        binding.btnDone.setOnClickListener(this)

        binding.edtCardNumber.addTextChangedListener(FourDigitCardFormatTextWatcher())
        binding.edtExpiryDate.addTextChangedListener(ExpiryDateFormatTextWatcher())
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.btnBack -> finish()

            R.id.btnDone -> {
                if (isValidate())
                    finish()
            }
        }
    }

    private fun isValidate(): Boolean {
        binding.apply {
            return when {
                edtName.text.toString().isEmpty() -> {
                    Util.msgDialog(this@AddCardActivity, getString(R.string.please_enter_name_on_card))
                    false
                }

                edtCardNumber.text.toString().isEmpty() -> {
                    Util.msgDialog(this@AddCardActivity, getString(R.string.please_enter_card_number))
                    false
                }

                edtCardNumber.text.toString().length < 19 -> {
                    Util.msgDialog(this@AddCardActivity, getString(R.string.please_enter_valid_card_number))
                    false
                }

                edtExpiryDate.text.toString().isEmpty() -> {
                    Util.msgDialog(this@AddCardActivity, getString(R.string.please_enter_expire_date))
                    false
                }

                edtExpiryDate.text.toString().length < 7 -> {
                    Util.msgDialog(this@AddCardActivity, getString(R.string.please_enter_valid_expire_date))
                    false
                }

                edtPostalCode.text.toString().isEmpty() -> {
                    Util.msgDialog(this@AddCardActivity, getString(R.string.please_enter_postal_code))
                    false
                }

                edtPostalCode.text.toString().length < 3 -> {
                    Util.msgDialog(this@AddCardActivity, getString(R.string.please_enter_valid_postal_code))
                    false
                }

                else -> true
            }
        }
    }
}