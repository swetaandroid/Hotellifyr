package com.xongolab.hotellifyr.view.activity.home.search.hotel

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.core.content.FileProvider
import com.google.gson.Gson
import com.xongolab.hotellifyr.R
import com.xongolab.hotellifyr.core.CoreActivity
import com.xongolab.hotellifyr.databinding.ActivityReservationSuccessBinding
import com.xongolab.hotellifyr.model.HotelModel
import com.xongolab.hotellifyr.utils.Util
import com.xongolab.hotellifyr.view.activity.MainActivity
import com.xongolab.hotellifyr.view.activity.home.BookingConfirmedActivity
import java.io.File
import java.io.FileOutputStream

class ReservationSuccessActivity : CoreActivity() {
    private lateinit var binding: ActivityReservationSuccessBinding

    private var diningData: HotelModel.DiningData = HotelModel.DiningData()
    private var bookingDate = ""
    private var bookingTime = ""
    private var diningType = ""
    private var numberOfGuests = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityReservationSuccessBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setFullScreenMode(window)
        initView()

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                startActivity(Intent(this@ReservationSuccessActivity, MainActivity::class.java))
                finish()
            }
        })
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btnBack -> {
                onBackPressedDispatcher.onBackPressed()
            }
            R.id.btnShareNow -> {
                captureAndShareScreen()
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun initView() {
        if (intent.hasExtra("response")) {
            diningData = Gson().fromJson(
                intent.getStringExtra("response"),
                HotelModel.DiningData::class.java
            )
            bookingDate = intent.getStringExtra("bookingDate")!!
            bookingTime = intent.getStringExtra("bookingTime")!!
            diningType = intent.getStringExtra("diningType")!!
            numberOfGuests = intent.getStringExtra("numberOfGuests")!!
        }

        binding.btnBack.setOnClickListener(this)
        binding.btnShareNow.setOnClickListener(this)

        binding.apply {
            ivIcon.setImageURI(diningData.icon)
            tvName.text = diningData.name
            tvTag.text = diningData.cuisineTitles.joinToString(" • ")
            tvLocation.text = diningData.address
            tvDate.text =" : " + Util.formatTimestamp(bookingDate, "yyyy-MM-dd", "EEE, dd MMM")
            tvTime.text = " : $bookingTime"
            tvAdults.text = "$numberOfGuests Guest(s)"
        }
    }

    private fun captureAndShareScreen() {
        // Capture the screenshot
        val rootView = window.decorView.rootView
        rootView.isDrawingCacheEnabled = true
        val bitmap = Bitmap.createBitmap(rootView.drawingCache)
        rootView.isDrawingCacheEnabled = false

        // Save bitmap to cache directory
        val cachePath = File(cacheDir, "images")
        cachePath.mkdirs()
        val file = File(cachePath, "screenshot.png")
        val fileOutputStream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream)
        fileOutputStream.flush()
        fileOutputStream.close()

        // Get URI using FileProvider
        val uri = FileProvider.getUriForFile(this, "$packageName.fileprovider", file)

        // Share Intent
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "image/png"
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        startActivity(Intent.createChooser(shareIntent, "Share Now"))
    }
}