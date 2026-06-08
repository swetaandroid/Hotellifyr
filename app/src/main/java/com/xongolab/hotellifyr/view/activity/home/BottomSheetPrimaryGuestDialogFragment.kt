package com.xongolab.hotellifyr.view.activity.home

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.xongolab.hotellifyr.R
import com.xongolab.hotellifyr.core.CoreActivity
import com.xongolab.hotellifyr.databinding.PrimaryGuestDetailsDialogBinding
import com.xongolab.hotellifyr.model.BookingHotelRequest
import com.xongolab.hotellifyr.model.PrimaryGuestDetailRequest
import com.xongolab.hotellifyr.utils.Constants
import com.xongolab.hotellifyr.utils.Pref
import com.xongolab.hotellifyr.utils.Util
import com.xongolab.hotellifyr.view.activity.auth.WebActivity

class BottomSheetPrimaryGuestDialogFragment(
    private val bookingHotelRequest: BookingHotelRequest,
    val onApplyClick: ((bookingHotelRequest: BookingHotelRequest) -> Unit)? = null
) : BottomSheetDialogFragment() {

    private lateinit var dialogBinding: PrimaryGuestDetailsDialogBinding
    private var isAgree = false


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialogBinding = PrimaryGuestDetailsDialogBinding.inflate(inflater, container, false)
        return dialogBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
    }

    private fun initView() {
        dialogBinding.apply {
            ivClose.setOnClickListener {
                dismiss()
            }

            (requireActivity() as CoreActivity).makeLinks(
                tvAgreeTerms,
                "other",
                Pair("Privacy Policy", View.OnClickListener {
                    startActivity(Intent(requireActivity(), WebActivity::class.java).apply {
                        putExtra(Constants.TITLE, "Privacy Policy")
                        putExtra(Constants.WEB_URL, Pref.getStringValue(Constants.PRIVACY_POLICY, ""))
                    })
                }),
                Pair("Terms & Conditions", View.OnClickListener {
                    startActivity(Intent(requireActivity(), WebActivity::class.java).apply {
                        putExtra(Constants.TITLE, "Terms & Conditions")
                        putExtra(Constants.WEB_URL, Pref.getStringValue(Constants.TERMS_CONDITION, ""))
                    })
                }),
            )


            if (Pref.getBooleanValue(Pref.PREF_IS_LOGIN, false)) {
                edtFirstName.setText(Pref.getStringValue(Pref.PREF_FIRST_NAME, ""))
                edtLastName.setText(Pref.getStringValue(Pref.PREF_LAST_NAME, ""))
                edtEmailId.setText(Pref.getStringValue(Pref.PREF_EMAIL, ""))

                // Fix: Safely parse country code to avoid NumberFormatException
                val countryCode = Pref.getStringValue(Pref.PREF_COUNTRY_CODE, "91")
                if (countryCode.isNotEmpty()) {
                    ccp.setCountryForPhoneCode(countryCode.toIntOrNull() ?: 91)
                }

                edtPhoneNumber.setText(Pref.getStringValue(Pref.PREF_MOBILE_NO, ""))

                listOf(ccp, edtFirstName, edtLastName, edtEmailId, edtPhoneNumber).forEach {
                    it.isClickable = false
                    it.isEnabled = false
                }
            } else {
                listOf(ccp, edtFirstName, edtLastName, edtEmailId, edtPhoneNumber).forEach {
                    it.isClickable = true
                    it.isEnabled = true
                }
            }

            ivAgree.setOnClickListener {
                isAgree = !isAgree
                ivAgree.setImageResource(if (isAgree) R.drawable.cb_check else R.drawable.cb_uncheck)
                checkFieldsAndEnableButton()
            }

            val textWatcher = object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    checkFieldsAndEnableButton()
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            }

            btnSubmit.isEnabled = false
            btnSubmit.alpha = 0.4F

            edtFirstName.addTextChangedListener(textWatcher)
            edtLastName.addTextChangedListener(textWatcher)
            edtEmailId.addTextChangedListener(textWatcher)
            edtPhoneNumber.addTextChangedListener(textWatcher)

            btnSubmit.setOnClickListener {
                if (Util.isEmptyText(edtFirstName)) {
                    edtFirstName.requestFocus()
                    Util.msgDialog(requireContext(), getString(R.string.first_name_validation))
                } else if (Util.isEmptyText(edtLastName)) {
                    edtLastName.requestFocus()
                    Util.msgDialog(requireContext(), getString(R.string.last_name_validation))
                } else if (Util.isEmptyText(edtEmailId)) {
                    edtEmailId.requestFocus()
                    Util.msgDialog(requireContext(), getString(R.string.email_validation))
                } else if (!Util.isValidEmail(edtEmailId.text.toString().trim())) {
                    edtEmailId.requestFocus()
                    Util.msgDialog(requireContext(), getString(R.string.email_invalid))
                } else if (Util.isEmptyText(edtPhoneNumber)) {
                    edtPhoneNumber.requestFocus()
                    Util.msgDialog(requireContext(), getString(R.string.mobile_validation))
                } else if (edtPhoneNumber.text.toString().trim().length != 10) {
                    edtPhoneNumber.requestFocus()
                    Util.msgDialog(requireContext(), getString(R.string.mobile_invalid))
                } else if (!isAgree) {
                    Util.msgDialog(requireContext(), getString(R.string.terms_and_condition_validation))
                } else {
                    val primaryGuestDetail = PrimaryGuestDetailRequest()
                    primaryGuestDetail.FirstName = Util.getTextValue(edtFirstName)
                    primaryGuestDetail.LastName = Util.getTextValue(edtLastName)
                    primaryGuestDetail.Email = Util.getTextValue(edtEmailId)
                    primaryGuestDetail.MobileCountryCode = ccp.selectedCountryCode
                    primaryGuestDetail.Mobile = Util.getTextValue(edtPhoneNumber)
                    primaryGuestDetail.GSTNo = Util.getTextValue(edtGSTNumber)
                    primaryGuestDetail.specialRequest = Util.getTextValue(edtSpecialRequest)

                    bookingHotelRequest.primaryGuestDetail = primaryGuestDetail
                    onApplyClick?.invoke(bookingHotelRequest)
                    dismiss()
                }
            }
        }
    }

    private fun PrimaryGuestDetailsDialogBinding.validateFields(): Boolean {
        return edtFirstName.text.toString().trim().isNotEmpty() &&
                edtLastName.text.toString().trim().isNotEmpty() &&
                edtEmailId.text.toString().trim().isNotEmpty() &&
                edtPhoneNumber.text.toString().trim().isNotEmpty() &&
                Util.isValidEmail(edtEmailId.text.toString().trim()) &&
                edtPhoneNumber.text.toString().trim().length == 10 &&
                isAgree
    }

    private fun PrimaryGuestDetailsDialogBinding.checkFieldsAndEnableButton() {
        btnSubmit.isEnabled = validateFields()
        btnSubmit.alpha = if (validateFields()) 1F else 0.4F
    }

    override fun onStart() {
        super.onStart()
        dialog?.let { bottomSheetDialog ->
            val bottomSheet =
                bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.let {
                val behavior = BottomSheetBehavior.from(it)
                behavior.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }
        dialog?.setCancelable(false)
    }
}
