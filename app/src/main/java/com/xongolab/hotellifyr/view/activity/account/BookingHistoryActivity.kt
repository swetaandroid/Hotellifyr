package com.xongolab.hotellifyr.view.activity.account

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.xongolab.hotellifyr.R
import com.xongolab.hotellifyr.core.CoreActivity
import com.xongolab.hotellifyr.databinding.ActivityBookingHistoryBinding
import com.xongolab.hotellifyr.databinding.DialogConfirmationBinding
import com.xongolab.hotellifyr.model.SearchHotel
import com.xongolab.hotellifyr.utils.Constants
import com.xongolab.hotellifyr.utils.Util
import com.xongolab.hotellifyr.utils.Util.createPartFromString
import com.xongolab.hotellifyr.utils.makeGone
import com.xongolab.hotellifyr.utils.makeInvisible
import com.xongolab.hotellifyr.utils.makeVisible
import com.xongolab.hotellifyr.view.activity.MainActivity
import com.xongolab.hotellifyr.view.activity.home.search.hotel.HotelDetailActivity
import com.xongolab.hotellifyr.view.adapter.account.BookingHistoryAdapter
import com.xongolab.hotellifyr.viewModel.HotelViewModel
import com.xongolab.hotellifyr.viewModel.ViewModelFactory
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

class BookingHistoryActivity : CoreActivity() {

    private lateinit var binding: ActivityBookingHistoryBinding
    private lateinit var bookingHistoryAdapter: BookingHistoryAdapter
    private lateinit var hotelViewModel: HotelViewModel
    private var isLastPage = false
    private var isLoading = false
    private var currentPage = 0
    private val pageSize = 10
    var currentTabType: String = ""
    private var lastPos: Int = -1
    private lateinit var dialogBinding: DialogConfirmationBinding
    private lateinit var alertDialog: AlertDialog
    private var clickStatus = ""

    enum class TabType {
        Upcoming, Past, Cancelled
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBookingHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btnBack -> finish()
            R.id.llUpcoming -> selectTab(TabType.Upcoming)
            R.id.llPast -> selectTab(TabType.Past)
            R.id.llCancelled -> selectTab(TabType.Cancelled)
            R.id.btnBookTrip -> {
                val intent = Intent(this@BookingHistoryActivity, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
            }
            R.id.btnWishlist -> {
                lastPos = Integer.parseInt(v.tag.toString())
                showConfirmationDialog()
            }

            /*R.id.btnCheckBooking -> {
                lastPos = Integer.parseInt(v.tag.toString())

                val selectedHotel = bookingHistoryAdapter.objList[lastPos]

                val json = GsonBuilder().setPrettyPrinting().create().toJson(selectedHotel)

                println("SEARCH_HOTEL...setPrettyPrinting JSON wishlist..." + Gson().toJson(selectedHotel))
                println("SEARCH_HOTEL...setPrettyPrinting JSON json..." + json)

                val dateFormat = SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.getDefault())
                dateFormat.timeZone = TimeZone.getDefault()

                // Get current calendar instance
                val calendar = Calendar.getInstance()
                // Format today's date
                val today = dateFormat.format(calendar.time)

                calendar.add(Calendar.DAY_OF_YEAR, 1)
                val tomorrow = dateFormat.format(calendar.time)


                val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
                inputFormat.timeZone = TimeZone.getTimeZone("UTC")

                val outputFormat =
                    SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.getDefault())
                outputFormat.timeZone = TimeZone.getDefault()

                val checkInDate = inputFormat.parse(bookingHistoryAdapter.objList[lastPos].checkIn)
                val checkOutDate = inputFormat.parse(bookingHistoryAdapter.objList[lastPos].checkOut)

                val formattedCheckIn = outputFormat.format(checkInDate!!)
                val formattedCheckOut = outputFormat.format(checkOutDate!!)

                val searchHotel = SearchHotel()
                searchHotel.apply {
                    rooms = bookingHistoryAdapter.objList[lastPos].bookRoom[lastPos].roomIndex.toInt()
                    adults = bookingHistoryAdapter.objList[lastPos].bookRoom[lastPos].adults.toInt()
                    children =  bookingHistoryAdapter.objList[lastPos].bookRoom[lastPos].children.toInt()
                    hotelID = bookingHistoryAdapter.objList[lastPos]._id
                    location = bookingHistoryAdapter.objList[lastPos].hotelDetails.address
                    type = Constants.HOTEL
                    checkIn = formattedCheckIn
                    checkOut = formattedCheckOut
                }
                val intent = Intent(this@BookingHistoryActivity, HotelDetailActivity::class.java)
                intent.putExtra(Constants.SEARCH_HOTEL, Gson().toJson(searchHotel))
                startActivity(intent)
            }*/
        }
    }

    @SuppressLint("SetTextI18n")
    private fun initView() {
        initViewModel()
        binding.layoutToolBar.toolbarTitle.text = getString(R.string.booking_history)
        binding.layoutToolBar.btnBack.setOnClickListener(this)

        binding.llUpcoming.setOnClickListener(this)
        binding.llPast.setOnClickListener(this)
        binding.llCancelled.setOnClickListener(this)
        binding.btnBookTrip.setOnClickListener(this)
        selectTab(TabType.Upcoming)
        currentTabType = "upcoming"

        bookingHistoryAdapter = BookingHistoryAdapter(this, this)
        binding.rvBookingHistory.layoutManager =
            LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        binding.rvBookingHistory.adapter = bookingHistoryAdapter


        binding.rvBookingHistory.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                if (!isLoading && !isLastPage) {
                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount && firstVisibleItemPosition >= 0) {
                        recyclerView.post {
                            loadMoreItems()
                        }
                    }
                }
            }
        })
        /* Handler(Looper.getMainLooper()).postDelayed({
             bookingHistoryList()
         }, 1000)*/
    }

    private fun initViewModel() {
        hotelViewModel = ViewModelProvider(this, ViewModelFactory(mainRepository))[HotelViewModel::class.java]
        observeViewModel()
    }

    private fun observeViewModel() {
        hotelViewModel.deleteWishListApiResponse.observe(this) { response ->
            response?.let {
                if (clickStatus == "Add") {
                    Util.msgDialog(this, getString(R.string.you_ve_successfully_added_this_hotel_to_your_wishlist))
                } else if (clickStatus == "Remove") {
                    Util.msgDialog(this, getString(R.string.you_ve_successfully_removed_this_hotel_from_your_wishlist))
                }
                selectTab(TabType.Upcoming)
                currentTabType = "upcoming"
            }
        }
        hotelViewModel.getBookingListApiResponse.observe(this) { response ->
            response?.let {
                val payload = it.payload!!
                if (currentPage == 0) {
                    bookingHistoryAdapter.removeLoader()
                    if (currentPage == 0) {
                        bookingHistoryAdapter.clearData()
                        bookingHistoryAdapter.addData(payload.data)
                    } else {
                        if (it.payload!!.data.size > 0) {
                            bookingHistoryAdapter.addData(it.payload!!.data)
                        }
                    }

                    if (currentPage == 0) {
                        if (bookingHistoryAdapter.itemCount == 0) {
                            binding.llNoData.makeVisible()
                            binding.rvBookingHistory.makeGone()
                            when (currentTabType) {
                                "upcoming" -> {
                                    binding.tvNoDataDesc.text = getString(R.string.no_upcoming_hotel_stays_found_start_planning_your_next_trip)
                                }

                                "past" -> {
                                    binding.tvNoDataDesc.text = getString(R.string.no_past_hotel_stays_found_start_planning_your_next_trip)
                                }

                                "cancelled" -> {
                                    binding.tvNoDataDesc.text = getString(R.string.no_cancelled_hotel_stays_found_start_planning_your_next_trip)
                                }
                            }
                        } else {
                            binding.rvBookingHistory.makeVisible()
                            binding.llNoData.makeGone()
                        }
                    }

                    isLoading = false
                    isLastPage = it.payload!!.data.size < pageSize
                } else {
                    Handler(Looper.getMainLooper()).postDelayed({
                        bookingHistoryAdapter.removeLoader()
                        if (currentPage == 0) {
                            bookingHistoryAdapter.clearData()
                            bookingHistoryAdapter.addData(it.payload!!.data)
                        } else {
                            if (it.payload!!.data.size > 0) {
                                bookingHistoryAdapter.addData(it.payload!!.data)
                            }
                        }

                        if (currentPage == 0) {
                            if (bookingHistoryAdapter.itemCount == 0) {
                                binding.llNoData.makeVisible()
                                binding.rvBookingHistory.makeGone()
                            } else {
                                binding.rvBookingHistory.makeVisible()
                                binding.llNoData.makeGone()
                            }
                        }

                        isLoading = false
                        isLastPage = it.payload!!.data.size < pageSize
                    }, 3000L)
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun bookingHistoryListApi() {
        if (isInternetConnected()) {
            hotelViewModel.getBookingListApi(
                this,
                currentPage,
                pageSize,
                currentTabType
            )
        } else {
            msgDialog(getString(R.string.check_internet))
        }
    }

    private fun resetAndLoadData() {
        bookingHistoryAdapter.clearData()
        currentPage = 0
        isLastPage = false
        currentTabType = "upcoming"
        bookingHistoryListApi()
    }

    private fun loadMoreItems() {
        isLoading = true
        bookingHistoryAdapter.addLoader()
        currentPage += 1
        bookingHistoryListApi()
    }


    private fun selectTab(tab: TabType) {
        resetTabs()

        when (tab) {
            TabType.Upcoming -> {
                binding.viewUpcoming.makeVisible()
                binding.tvUpcoming.setTextColor(ContextCompat.getColor(this, R.color.colorSecondary))
                currentTabType = "upcoming"
                currentPage = 0
                bookingHistoryListApi()
            }

            TabType.Past -> {
                binding.viewPast.makeVisible()
                binding.tvPast.setTextColor(ContextCompat.getColor(this, R.color.colorSecondary))
                currentTabType = "past"
                currentPage = 0
                bookingHistoryListApi()
            }

            TabType.Cancelled -> {
                binding.viewCancelled.makeVisible()
                binding.tvCancelled.setTextColor(ContextCompat.getColor(this, R.color.colorSecondary))
                currentTabType = "cancelled"
                currentPage = 0
                bookingHistoryListApi()
            }
        }
    }


    @SuppressLint("SetTextI18n")
    private fun addWishListApi() {
        if (isInternetConnected()) {
            val actionType = createPartFromString("add", "actionType")
            val hotelID = createPartFromString(bookingHistoryAdapter.objList[lastPos].hotelID, "hotelID")

            hotelViewModel.deleteWishListApi(
                this,
                actionType,
                hotelID
            )
        } else {
            Util.msgDialog(this, getString(R.string.check_internet))
        }
    }

    @SuppressLint("SetTextI18n")
    private fun deleteWishListApi() {
        if (isInternetConnected()) {
            val actionType = createPartFromString("delete", "actionType")
            val hotelID = createPartFromString(bookingHistoryAdapter.objList[lastPos].hotelID, "hotelID")

            hotelViewModel.deleteWishListApi(
                this,
                actionType,
                hotelID
            )
        } else {
            Util.msgDialog(this, getString(R.string.check_internet))
        }
    }

    private fun resetTabs() {
        // Reset all views
        binding.viewUpcoming.makeInvisible()
        binding.viewPast.makeInvisible()
        binding.viewCancelled.makeInvisible()

        // Reset text colors
        binding.tvUpcoming.setTextColor(ContextCompat.getColor(this, R.color.colorOutLine))
        binding.tvPast.setTextColor(ContextCompat.getColor(this, R.color.colorOutLine))
        binding.tvCancelled.setTextColor(ContextCompat.getColor(this, R.color.colorOutLine))
    }

    fun showConfirmationDialog() {
        dialogBinding = DialogConfirmationBinding.inflate(LayoutInflater.from(this))
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setView(dialogBinding.root)

        alertDialog = alertDialogBuilder.create()
        alertDialog.show()

        alertDialog.window!!.attributes.width =
            ((resources.displayMetrics.widthPixels * 0.9).toInt())
        alertDialog.window?.apply {
            setDimAmount(0.4f) // Adjust dim intensity (0.0f = no dim, 1.0f = full dim)
            addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        }
        alertDialog.window!!.setBackgroundDrawableResource(R.drawable.dr_white_bg_15)

        dialogBinding.tvMessage.text = resources.getString(R.string.are_you_sure_you_want_to_remove_this_hotel_from_your_wishlist)
        dialogBinding.tvConfirm.text = resources.getString(R.string.str_yes)
        dialogBinding.tvCancel.text = resources.getString(R.string.str_no)
        dialogBinding.apply {
            tvConfirm.setOnClickListener {
                alertDialog.dismiss()
                if (bookingHistoryAdapter.objList[lastPos].isFavorite) {
                    clickStatus = "Remove"
                    deleteWishListApi()
                } else {
                    clickStatus = "Add"
                    addWishListApi()
                }
                // deleteWishListApi()
            }
            tvCancel.setOnClickListener { alertDialog.dismiss() }
        }

        alertDialog.setCancelable(false)
    }
}