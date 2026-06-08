package com.xongolab.hotellifyr.view.activity.home.search.hotel

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.GridLayoutManager
import com.google.gson.Gson
import com.xongolab.hotellifyr.R
import com.xongolab.hotellifyr.core.CoreActivity
import com.xongolab.hotellifyr.databinding.ActivityRestaurantDetailBinding
import com.xongolab.hotellifyr.databinding.DialogDirectionBinding
import com.xongolab.hotellifyr.model.HotelModel.DiningData
import com.xongolab.hotellifyr.view.adapter.hotel.map.RestaurantMenuAdapter

class RestaurantDetailActivity : CoreActivity() {

    private lateinit var binding: ActivityRestaurantDetailBinding
    private lateinit var restaurantMenuAdapter: RestaurantMenuAdapter

    private var diningData: DiningData = DiningData()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRestaurantDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setFullScreenMode(window)
        initView()
    }

    @SuppressLint("SetTextI18n")
    private fun initView() {
        if (intent.hasExtra("response")) {
            diningData = Gson().fromJson(intent.getStringExtra("response"), DiningData::class.java)
        }

        binding.btnBack.setOnClickListener(this)
        binding.icLocation.setOnClickListener(this)
        binding.icLocationDirection.setOnClickListener(this)
        binding.btnMakeReservation.setOnClickListener(this)
        binding.btnCall.setOnClickListener(this)

        restaurantMenuAdapter = RestaurantMenuAdapter(this, this)
        binding.rvMenu.layoutManager =
            GridLayoutManager(this, 2)
        binding.rvMenu.adapter = restaurantMenuAdapter

        binding.apply {
            ivIcon.setImageURI(diningData.icon)
            tvName.text = diningData.name
            tvTag.text = diningData.cuisineTitles.joinToString(" • ")
            tvDescription.text = diningData.description
            tvTime.text = "${diningData.openTime} - ${diningData.closeTime}"
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btnBack -> finish()

            R.id.icLocation -> chooseDirectionDialog()

            R.id.icLocationDirection -> {
                val intent = Intent(this@RestaurantDetailActivity, HotelMapActivity::class.java)
                    .putExtra("hotelID", diningData.id)
                    .putExtra("name", diningData.name)
                    .putExtra("latitude", diningData.position.lat)
                    .putExtra("longitude", diningData.position.lng)
                startActivity(intent)
            }

            R.id.btnMakeReservation -> {
                val intent = Intent(this, BookTableActivity::class.java)
                intent.putExtra("response", Gson().toJson(diningData))
                startActivity(intent)
            }

            R.id.btnCall -> {
                val callIntent = Intent(Intent.ACTION_DIAL)
                callIntent.data = Uri.parse("tel:${diningData.mobile}")
                startActivity(callIntent)
            }
        }
    }

    private fun chooseDirectionDialog() {
        val dialogBinding = DialogDirectionBinding.inflate(LayoutInflater.from(this))

        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setView(dialogBinding.root)

        val alertDialog: AlertDialog = alertDialogBuilder.create()
        alertDialog.show()

        alertDialog.setCancelable(false)

        dialogBinding.apply {
            tvOpenGoogleMap.setOnClickListener {
                openGoogleMap(diningData.position.lat, diningData.position.lng)
                alertDialog.dismiss()
            }

            tvCopyAddress.setOnClickListener {
                val addressToCopy = diningData.address
                println("diningData: addressToCopy: $addressToCopy")

                if (addressToCopy.isNotEmpty()) {
                    val clipboard =
                        getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    val clip = ClipData.newPlainText("Hotel Address", addressToCopy)
                    clipboard.setPrimaryClip(clip)
                    Toast.makeText(
                        this@RestaurantDetailActivity,
                        getString(R.string.address_copied_to_clipboard),
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        this@RestaurantDetailActivity,
                        getString(R.string.address_not_available),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            btnCancel.setOnClickListener {
                alertDialog.dismiss()
            }
        }

        alertDialog.window!!.setLayout(
            (resources.displayMetrics.widthPixels * 0.9).toInt(),
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        alertDialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)

        val window = alertDialog.window!!
        val layoutParams = window.attributes
        layoutParams.gravity = Gravity.BOTTOM
        layoutParams.y = 0
        window.attributes = layoutParams
    }

    private fun openGoogleMap(latitude: Double, longitude: Double) {
        val uri = Uri.parse("geo:$latitude,$longitude?q=$latitude,$longitude")
        val mapIntent = Intent(Intent.ACTION_VIEW, uri)
        mapIntent.setPackage("com.google.android.apps.maps")
        startActivity(mapIntent)
    }

}