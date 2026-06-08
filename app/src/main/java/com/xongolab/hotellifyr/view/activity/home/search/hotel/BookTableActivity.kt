package com.xongolab.hotellifyr.view.activity.home.search.hotel

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.xongolab.hotellifyr.R
import com.xongolab.hotellifyr.core.CoreActivity
import com.xongolab.hotellifyr.databinding.ActivityBookTableBinding
import com.xongolab.hotellifyr.databinding.DialogSelectDateBinding
import com.xongolab.hotellifyr.model.HotelModel
import com.xongolab.hotellifyr.model.TimeSlotModel
import com.xongolab.hotellifyr.view.adapter.time_slot.OuterTimeSlotAdapter
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class BookTableActivity : CoreActivity() {

    private lateinit var binding: ActivityBookTableBinding
    private lateinit var outerTimeSlotAdapter: OuterTimeSlotAdapter

    private var diningData: HotelModel.DiningData = HotelModel.DiningData()

    private var guestList = ArrayList<String>()

    private var bookingDate = ""
    private var bookingTime = ""
    private var diningType = ""
    private var numberOfGuests = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBookTableBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setFullScreenMode(window)

        initView()
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btnBack -> finish()
            R.id.llGuest -> binding.spGuest.performClick()
            R.id.btnConfirmReservation -> {
                if (isValid()) {
                    val intent = Intent(this, ReservationFormActivity::class.java)
                    intent.putExtra("response", Gson().toJson(diningData))
                    intent.putExtra("bookingDate", bookingDate)
                    intent.putExtra("bookingTime", bookingTime)
                    intent.putExtra("diningType", diningType)
                    intent.putExtra("numberOfGuests", numberOfGuests)
                    startActivity(intent)
                }
            }
        }
    }

    private fun initView() {
        if (intent.hasExtra("response")) {
            diningData =
                Gson().fromJson(
                    intent.getStringExtra("response"),
                    HotelModel.DiningData::class.java
                )
        }

        binding.tvHotelName.text = diningData.name

        binding.btnBack.setOnClickListener(this)

        binding.btnConfirmReservation.isClickable = false
        binding.btnConfirmReservation.backgroundTintList =
            ContextCompat.getColorStateList(this, R.color.colorOutLine)

        val calendar = Calendar.getInstance()
        binding.tvDate.text = formatDate(calendar)

        val calendar1 = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        bookingDate = dateFormat.format(calendar1.time)

        setGuestSpinnerData()

        binding.llDate.setOnClickListener(this)
        binding.llGuest.setOnClickListener(this)
        binding.btnConfirmReservation.setOnClickListener(this)

        outerTimeSlotAdapter = OuterTimeSlotAdapter(this)
        binding.rvOuterTimeSlot.layoutManager =
            LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        binding.rvOuterTimeSlot.adapter = outerTimeSlotAdapter
        timeSlotList()

        outerTimeSlotAdapter.onItemClick = { type, item ->
            binding.btnConfirmReservation.isClickable = true
            binding.btnConfirmReservation.backgroundTintList =
                ContextCompat.getColorStateList(this@BookTableActivity, R.color.colorPrimary)

            diningType = type
            bookingTime = item.time
        }

        binding.llDate.setOnClickListener {
            datePickerDialog()
        }
    }

    private fun setGuestSpinnerData() {
        guestList.addAll(listOf("2 Guests", "3 Guests", "4 Guests", "5 Guests", "6 Guests", "7 Guests", "8 Guests"))

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
                        binding.tvGuest.text = guestList[position]

                        numberOfGuests = guestList[position]
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }

        binding.spGuest.setPopupBackgroundResource(R.drawable.dr_spinner_bg)

        binding.spGuest.adapter = adapter
    }

    private fun timeSlotList() {
        outerTimeSlotAdapter.objList = ArrayList()

        var model = TimeSlotModel().apply {
            title = "Breakfast Time"
            diningType = "Breakfast"
            timeSlotItem = arrayListOf(
                TimeSlotModel.TimeSlotItem("07:00 AM"),
                TimeSlotModel.TimeSlotItem("07:30 AM"),
                TimeSlotModel.TimeSlotItem("08:00 AM"),
                TimeSlotModel.TimeSlotItem("08:30 AM"),
                TimeSlotModel.TimeSlotItem("09:00 AM"),
                )
        }
        outerTimeSlotAdapter.objList.add(model)

        model = TimeSlotModel().apply {
            title = "Lunch Time"
            diningType = "Lunch"
            timeSlotItem = arrayListOf(
                TimeSlotModel.TimeSlotItem("11:00 AM"),
                TimeSlotModel.TimeSlotItem("11:30 AM"),
                TimeSlotModel.TimeSlotItem("12:00 PM"),
                TimeSlotModel.TimeSlotItem("12:30 PM"),
                TimeSlotModel.TimeSlotItem("01:00 PM"),
                TimeSlotModel.TimeSlotItem("01:30 PM"),
                TimeSlotModel.TimeSlotItem("02:00 PM"),
                TimeSlotModel.TimeSlotItem("02:30 PM"),
            )
        }
        outerTimeSlotAdapter.objList.add(model)

        model = TimeSlotModel().apply {
            title = "Dinner Time"
            diningType = "Dinner"
            timeSlotItem = arrayListOf(
                TimeSlotModel.TimeSlotItem("08:00 PM"),
                TimeSlotModel.TimeSlotItem("08:30 PM"),
                TimeSlotModel.TimeSlotItem("09:00 PM"),
                TimeSlotModel.TimeSlotItem("09:30 PM"),
                TimeSlotModel.TimeSlotItem("10:00 PM"),
                TimeSlotModel.TimeSlotItem("10:30 PM"),
                TimeSlotModel.TimeSlotItem("11:00 PM"),
                TimeSlotModel.TimeSlotItem("11:30 PM"),
            )
        }
        outerTimeSlotAdapter.objList.add(model)

        outerTimeSlotAdapter.addData(outerTimeSlotAdapter.objList)
    }

    @SuppressLint("DefaultLocale")
    private fun datePickerDialog() {
        val dateDialog = Dialog(this)
        val dialogBinding = DialogSelectDateBinding.inflate(layoutInflater)
        dateDialog.setContentView(dialogBinding.root)
        dateDialog.setCanceledOnTouchOutside(true)
        dateDialog.setCancelable(false)

        dialogBinding.llSpinner.visibility = View.GONE
        dialogBinding.calendarView.visibility = View.VISIBLE
        dialogBinding.calendarView.minDate = System.currentTimeMillis() - 1000
        dialogBinding.calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            bookingDate = String.format("%d-%02d-%02d", year, month + 1, dayOfMonth)
            val calendar = Calendar.getInstance()
            calendar.set(year, month, dayOfMonth) // Update calendar instance
            binding.tvDate.text = formatDate(calendar)
            dateDialog.dismiss()
        }

        val window = dateDialog.window
        val params = window?.attributes
        params?.width = ((resources.displayMetrics.widthPixels * 0.9).toInt())
        window?.attributes = params
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dateDialog.show()
    }

    private fun formatDate(calendar: Calendar): String {
        val dateFormat = SimpleDateFormat("EEE, dd MMM", Locale.getDefault())
        return dateFormat.format(calendar.time)
    }

    private fun isValid(): Boolean {
        return when {
            bookingDate.isEmpty() -> {
                msgDialog(getString(R.string.please_select_date))
                false
            }

            numberOfGuests.isEmpty() -> {
                msgDialog(getString(R.string.please_select_guests))
                false
            }

            diningType.isEmpty() -> {
                msgDialog(getString(R.string.please_select_time_slot))
                false
            }

            else -> true
        }
    }

}