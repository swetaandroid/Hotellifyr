package com.xongolab.hotellifyr.view.activity.home

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import androidx.core.content.FileProvider
import com.xongolab.hotellifyr.R
import com.xongolab.hotellifyr.core.CoreActivity
import com.xongolab.hotellifyr.databinding.ActivityBookingConfirmedBinding
import com.xongolab.hotellifyr.utils.Util
import com.xongolab.hotellifyr.view.activity.MainActivity
import java.io.File
import java.io.FileOutputStream

class BookingConfirmedActivity : CoreActivity() {
    private lateinit var binding: ActivityBookingConfirmedBinding

    var bookingId: String = ""
    var hotelName: String = ""
    var checkIn: String = ""
    var checkOut: String = ""
    var numberOfDays: Int = 0
    var adults: Int = 0
    var children: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookingConfirmedBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setFullScreenMode(window)

        initView()
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.btnBack -> {
                startActivity(Intent(this@BookingConfirmedActivity, MainActivity::class.java))
                finish()
            }

            R.id.btnShareNow -> {
                captureAndShareScreen()
            }
        }
    }


    @SuppressLint("SetTextI18n")
    private fun initView() {

        if (intent != null) {
            hotelName = intent.getStringExtra("hotelName").toString()
            bookingId = intent.getStringExtra("bookingId").toString()
            checkIn = intent.getStringExtra("checkIn").toString()
            checkOut = intent.getStringExtra("checkOut").toString()
            numberOfDays = intent.getIntExtra("numberOfDays", 0)
            adults = intent.getIntExtra("adults", 0)
            children = intent.getIntExtra("children", 0)
        }

        binding.tvBookingId.text = "${getString(R.string.booking_reference_no)} $bookingId"
        binding.tvHotelName.text = hotelName
        binding.tvCheckInDate.text = Util.formatTimestamp(checkIn, "yyyy-MM-dd", "dd")
        binding.tvCheckInDay.text = Util.formatTimestamp(checkIn, "yyyy-MM-dd", "EEEE, MMM")
        binding.tvCheckOutDate.text = Util.formatTimestamp(checkOut, "yyyy-MM-dd", "dd")
        binding.tvCheckOutDay.text = Util.formatTimestamp(checkOut, "yyyy-MM-dd", "EEEE, MMM")


        // Total Guests
        val totalGuest = adults + children
        val nights = if (numberOfDays > 0) {
            numberOfDays - 1
        } else {
            0
        }

        val dayText = if (numberOfDays == 1) "Day" else "Days"
        val nightText = if (nights == 1) "Night" else "Nights"
        val guestText = if (totalGuest == 1) "Guest" else "Guests"

        // Final Package Text
        binding.tvPackage.text = "$numberOfDays $dayText & " + "$nights $nightText " + "(1 Room, $totalGuest $guestText)"

        binding.btnBack.setOnClickListener(this)
        binding.btnShareNow.setOnClickListener(this)
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

    override fun onBackPressed() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
    }

}