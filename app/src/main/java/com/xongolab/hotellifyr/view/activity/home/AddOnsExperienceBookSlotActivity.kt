package com.xongolab.hotellifyr.view.activity.home

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.xongolab.hotellifyr.R
import com.xongolab.hotellifyr.core.CoreActivity
import com.xongolab.hotellifyr.databinding.ActivityAddOnsExperienceBookSlotBinding
import com.xongolab.hotellifyr.model.AddOnExperience
import com.xongolab.hotellifyr.model.AddOnExperienceRequest
import com.xongolab.hotellifyr.utils.Util
import com.xongolab.hotellifyr.view.adapter.BookSlotAdapter
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AddOnsExperienceBookSlotActivity : CoreActivity() {
    private lateinit var binding: ActivityAddOnsExperienceBookSlotBinding
    private lateinit var bookSlotAdapter: BookSlotAdapter

    private lateinit var addOnExperience: AddOnExperience

    private var checkIn: String = ""
    private var checkOut: String = ""

    private var guestList = ArrayList<Int>()
    private var numberOfGuests = 1

    private var addOnExperienceRequest: AddOnExperienceRequest = AddOnExperienceRequest()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddOnsExperienceBookSlotBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setFullScreenMode(window)

        initView()
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.btnBack -> finish()
            R.id.tvDate -> datePickerDialog()
            R.id.llGuest -> binding.spGuest.performClick()
            R.id.llBookSlot -> {
                if (isValidate()) {
                    addOnExperienceRequest.numberOfPerson =
                        numberOfGuests.toLong()

                    val resultIntent = Intent()
                    resultIntent.putExtra(
                        "AddOnExperienceRequest",
                        Gson().toJson(addOnExperienceRequest)
                    )
                    setResult(Activity.RESULT_OK, resultIntent)
                    finish()
                }
            }
        }
    }

    @SuppressLint("SetTextI18n", "NotifyDataSetChanged")
    private fun initView() {
        if (intent != null) {
            checkIn = intent.getStringExtra("checkIn")!!
            checkOut = intent.getStringExtra("checkOut")!!
            addOnExperience =
                Gson().fromJson(intent.getStringExtra("item")!!, AddOnExperience::class.java)
        }

        binding.toolbar.toolbarTitle.text = addOnExperience.title
        binding.toolbar.btnBack.setOnClickListener(this)
        binding.tvDate.setOnClickListener(this)
        binding.llGuest.setOnClickListener(this)
        binding.llBookSlot.setOnClickListener(this)

        binding.ivIcon.setImageURI(addOnExperience.icon)
        binding.tvDescription.text = addOnExperience.description
        binding.tvBookSlotLBL.text =
            addOnExperience.title + getString(R.string.is_subject_to_availability_hence_select_package_date_time_to_block_your_slot)

        bookSlotAdapter =
            BookSlotAdapter(this)
        binding.rvBookSlot.layoutManager =
            LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        binding.rvBookSlot.adapter = bookSlotAdapter

        addOnExperienceRequest.addOnID = addOnExperience.id
        addOnExperienceRequest.addOnTitle = addOnExperience.title
        addOnExperienceRequest.icon = addOnExperience.icon

        for (item in addOnExperience.packages) {
            if (item.selected) {
                binding.tvPrice.text = setPriceWithUnit(item.price)
                binding.tvType.text = "INR /"

                addOnExperienceRequest.packageID = item.id
                addOnExperienceRequest.title = item.title
                addOnExperienceRequest.packageTitle = item.packageX
                addOnExperienceRequest.price = numberOfGuests * item.price
                addOnExperienceRequest.singlePrice = item.price
                addOnExperienceRequest.description = item.description

                break
            }
        }

        bookSlotAdapter.onItemClick = { _, item ->
            for (obj in bookSlotAdapter.objList) {
                obj.selected = false
            }
            item.selected = true

            addOnExperienceRequest.packageID = item.id
            addOnExperienceRequest.title = item.title
            addOnExperienceRequest.packageTitle = item.packageX
            addOnExperienceRequest.price = numberOfGuests * item.price
            addOnExperienceRequest.singlePrice = item.price
            addOnExperienceRequest.description = item.description

            bookSlotAdapter.notifyDataSetChanged()

            binding.tvPrice.text = setPriceWithUnit(item.price)
            binding.tvType.text = getString(R.string.inr_capital)
        }

        bookSlotAdapter.addData(addOnExperience.packages)

        setGuestSpinnerData()
    }

    private fun setGuestSpinnerData() {
        guestList.addAll(listOf(1,2,3,4))

        val adapter = ArrayAdapter(
            this, R.layout.spinner_dropdown_item, guestList
        )

        binding.spGuest.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                @SuppressLint("SetTextI18n")
                override fun onItemSelected(
                    parent: AdapterView<*>, view: View?, position: Int, id: Long
                ) {
                    if (view != null) {
                        binding.tvGuest.text = ""+ guestList[position]

                        numberOfGuests = guestList[position]

                        addOnExperienceRequest.price = numberOfGuests * addOnExperienceRequest.singlePrice

                        binding.tvPrice.text = setPriceWithUnit(addOnExperienceRequest.price)
                        binding.tvType.text = getString(R.string.str_inr)
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }

        binding.spGuest.setPopupBackgroundResource(R.drawable.dr_spinner_bg)

        binding.spGuest.adapter = adapter
    }


    private fun datePickerDialog() {
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
            addOnExperienceRequest.bookingDate = outputFormat.format(pickedDateTime.time)

            val dateFormat = SimpleDateFormat("EEE, dd MMM", Locale.getDefault())
            val formattedDateTime = dateFormat.format(pickedDateTime.time)
            binding.tvDate.text = formattedDateTime
        }, startYear, startMonth, startDay)

        // Set the minimum date to today

        if (checkIn.isNotEmpty()) {
            val minDateCalendar = Calendar.getInstance().apply {
                time = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(checkIn)!!
            }
            datePickerDialog.datePicker.minDate = minDateCalendar.timeInMillis
        }

        if (checkOut.isNotEmpty()) {
            val maxDateCalendar = Calendar.getInstance().apply {
                time = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(checkOut)!!
            }
            datePickerDialog.datePicker.maxDate = maxDateCalendar.timeInMillis
        }
        datePickerDialog.show()
    }

    private fun isValidate(): Boolean {
        var isError = true
        when {
            addOnExperienceRequest.packageID.isEmpty() -> {
                isError = false
                Util.msgDialog(this, getString(R.string.please_select_package))
            }

            binding.spGuest.selectedItemPosition == -1 -> {
                isError = false
                Util.msgDialog(this, getString(R.string.please_select_number_of_guest))
            }

            addOnExperienceRequest.bookingDate.isEmpty() -> {
                isError = false
                Util.msgDialog(this, getString(R.string.please_select_date))
            }
        }
        return isError
    }
}