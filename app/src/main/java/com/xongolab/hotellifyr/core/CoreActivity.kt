package com.xongolab.hotellifyr.core

import android.Manifest
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Color
import android.graphics.Typeface
import android.location.Location
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.provider.OpenableColumns
import android.text.Selection
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.xongolab.hotellifyr.R
import com.xongolab.hotellifyr.databinding.DialogDirectionBinding
import com.xongolab.hotellifyr.databinding.DialogValidationMessageBinding
import com.xongolab.hotellifyr.idrequest.ApiRepository
import com.xongolab.hotellifyr.idrequest.IDRequestBuilder
import com.xongolab.hotellifyr.locale.LocaleManager
import com.xongolab.hotellifyr.utils.Pref
import com.xongolab.hotellifyr.utils.Util
import com.xongolab.hotellifyr.view.activity.MainActivity
import com.xongolab.hotellifyr.view.activity.NoInternetConnectionActivity
import java.io.File
import java.io.FileOutputStream
import java.text.NumberFormat
import java.util.Locale
import kotlin.math.floor

abstract class CoreActivity : AppCompatActivity(), View.OnClickListener {

    val TAG: String = "CoreActivity"
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private val retrofitService = IDRequestBuilder.getInstance(this)
    val mainRepository = ApiRepository(retrofitService)

    private lateinit var dialog: Dialog

    private var timer: CountDownTimer? = null
    private lateinit var messageDialog: Dialog
    private lateinit var dialogValidationMessageBinding: DialogValidationMessageBinding


    override fun attachBaseContext(newBase: Context) {
        val languageCode = Pref.getStringValue(Pref.PREF_LANGUAGE, "EN").lowercase()
        super.attachBaseContext(LocaleManager.setLocale(newBase, languageCode))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e(TAG, "onCreate: ")

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

    }

    fun makeLinks(
        textView: TextView,
        from: String,
        vararg links: Pair<String, View.OnClickListener>
    ) {
        if (textView.text.isNotEmpty()) {

            val spannableString = SpannableString(textView.text)
            for (link in links) {
                val clickableSpan = object : ClickableSpan() {
                    override fun onClick(view: View) {
                        Selection.setSelection((view as TextView).text as Spannable, 0)
                        view.invalidate()
                        link.second.onClick(view)
                    }

                    override fun updateDrawState(ds: TextPaint) {
                        ds.isUnderlineText = true

                        val typeface = when {
                            from == "otp" -> {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                    resources.getFont(R.font.product_sans_bold)
                                } else {
                                    Typeface.createFromAsset(assets, "font/product_sans_bold.ttf")
                                }
                            }
                            else -> {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                    resources.getFont(R.font.product_sans_regular)
                                } else {
                                    Typeface.createFromAsset(assets, "font/product_sans_regular.ttf")
                                }
                            }
                        }
                        ds.typeface = typeface

                        when (from) {
                            "onboarding" -> ds.color = ContextCompat.getColor(this@CoreActivity, R.color.colorPrimary)
                            "otp" -> ds.color = ContextCompat.getColor(this@CoreActivity, R.color.colorBlack)
                            else -> ds.color = ContextCompat.getColor(this@CoreActivity, R.color.colorPrimary)

                        }
                    }
                }
                val startIndexOfLink = textView.text.toString().indexOf(link.first)
                if (startIndexOfLink != -1) {
                    spannableString.setSpan(
                        clickableSpan, startIndexOfLink, startIndexOfLink + link.first.length,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                }

            }
            textView.movementMethod =
                LinkMovementMethod.getInstance() // without LinkMovementMethod, link can not click
            textView.setText(spannableString, TextView.BufferType.SPANNABLE)
        }
    }


    fun makeStyledText(textView: TextView, from: String, vararg links: String) {
        if (textView.text.isNotEmpty()) {
            val spannableString = SpannableString(textView.text)

            for (link in links) {
                val color = ContextCompat.getColor(textView.context, R.color.colorPrimary)

                val startIndexOfLink = textView.text.toString().indexOf(link)
                if (startIndexOfLink != -1) {
                    spannableString.setSpan(
                        ForegroundColorSpan(color),
                        startIndexOfLink,
                        startIndexOfLink + link.length,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                }
            }

            textView.setText(spannableString, TextView.BufferType.SPANNABLE)
        }
    }


    // Function to set full screen mode with transparent status bar
    fun setFullScreenMode(window: Window) {
        window.decorView.systemUiVisibility =
            (View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
        window.statusBarColor = Color.TRANSPARENT
    }

    fun getAppVersionName(): String {
        val pm = packageManager
        val pkgName = packageName
        var pkgInfo: PackageInfo? = null
        try {
            pkgInfo = pm.getPackageInfo(pkgName, 0)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return pkgInfo!!.versionName.toString()
    }

    // Convert the Uri to a File
    fun getFileFromUri(uri: Uri): String {
        var fileName = ""
        val cursor: Cursor? = contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (nameIndex >= 0) {
                    fileName = it.getString(nameIndex)
                }
            }
        }

        //return fileName

        val tempFile = File(this.filesDir, fileName)
        tempFile.deleteOnExit()

        val inputStream = contentResolver.openInputStream(uri)
        val outputStream = FileOutputStream(tempFile)

        inputStream?.use { input ->
            outputStream.use { output ->
                input.copyTo(output)
            }
        }
        Log.e(TAG, "getFileFromUri: $tempFile")

        return tempFile.absolutePath
    }

    fun setPriceWithUnit(price: Double): String {
        // Create a NumberFormat instance for the default locale (India)
        val numberFormat = NumberFormat.getNumberInstance(Locale.US)
        numberFormat.maximumFractionDigits = if (checkIfInt(price)) 0 else 2

        // Format the price with commas for thousands
        val priceStr = numberFormat.format(price)

        // Get the currency symbol from preferences
        val currencySymbol = Pref.getStringValue(Pref.PREF_CURRENCY, "₹")

        // Return the formatted price with the currency symbol
        return "$currencySymbol $priceStr"
    }

    private fun checkIfInt(value: Double): Boolean {
        return value - floor(value) == 0.0
    }

    fun locationDialog() {
        val dialogBinding = DialogDirectionBinding.inflate(LayoutInflater.from(this))

        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setView(dialogBinding.root)

        var alertDialog: AlertDialog?
        alertDialog = alertDialogBuilder.create()
        alertDialog.show()

        alertDialog.setCancelable(false)

        dialogBinding.apply {
            llDirection.background = ContextCompat.getDrawable(
                this@CoreActivity,
                R.drawable.dr_white_bg_15
            )

            tvOpenGoogleMap.setOnClickListener {
                getCurrentLocationAndOpenMap()
                alertDialog?.dismiss()
            }

            btnCancel.setOnClickListener {
                alertDialog?.dismiss()
                alertDialog = null
            }
        }

        alertDialog?.window!!.setLayout(
            (resources.displayMetrics.widthPixels * 0.9).toInt(),
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        alertDialog?.window!!.setBackgroundDrawableResource(android.R.color.transparent)

        val window = alertDialog?.window!!
        val layoutParams = window.attributes
        layoutParams.gravity = Gravity.BOTTOM
        layoutParams.y = 0
        window.attributes = layoutParams
    }


    private fun getCurrentLocationAndOpenMap() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Request permission
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
            return
        }

        // Get the last known location
        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            location?.let {
                // Open Google Maps with dynamic coordinates
                openGoogleMaps(it.latitude, it.longitude)
            } ?: run {
                // Handle the case when location is null
                Toast.makeText(this, "Unable to retrieve location.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun openGoogleMaps(latitude: Double, longitude: Double) {
        val uri = Uri.parse("geo:$latitude,$longitude?q=$latitude,$longitude")
        val mapIntent = Intent(Intent.ACTION_VIEW, uri)
        mapIntent.setPackage("com.google.android.apps.maps")
        startActivity(mapIntent)
    }

    fun askNotificationPermission(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED
            ) {
            } else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
            } else {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        } else {

        }
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { isGranted: Boolean ->
        if (isGranted) {
            // FCM SDK (and your app) can post notifications.
        } else {
            // TODO: Inform user that that your app will not show notifications.
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                getCurrentLocationAndOpenMap()
            } else {
                Toast.makeText(this, "Location permission denied.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun msgDialog(msg: String) {
        try {
            dialogValidationMessageBinding = DialogValidationMessageBinding.inflate(layoutInflater)

            messageDialog = Dialog(this)

            messageDialog.setContentView(dialogValidationMessageBinding.root)

            messageDialog.window!!.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            messageDialog.window!!.setGravity(Gravity.TOP)
            messageDialog.window!!.setWindowAnimations(R.style.DialogMessageAnimation)
            messageDialog.setCancelable(true)

            dialogValidationMessageBinding.tvMessage.text = msg

            messageDialog.show()

            timer = object : CountDownTimer(1000, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    //No operation required
                }

                override fun onFinish() {
                    if (timer != null) {
                        timer!!.cancel()
                    }
                    try {
                        if (messageDialog.isShowing)
                            messageDialog.dismiss()
                    } catch (e: java.lang.Exception) {
                        e.printStackTrace()
                    }

                }
            }.start()

        } catch (e: NullPointerException) {
            e.printStackTrace()
        }
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }

    var isConnected: Boolean = false

    fun isInternetConnected(): Boolean {
        isConnected = Util.isOnline(baseContext)
        if (!isConnected) {
            startActivity(Intent(this@CoreActivity, NoInternetConnectionActivity::class.java))
            finishAffinity()
        }
        return isConnected
    }

}