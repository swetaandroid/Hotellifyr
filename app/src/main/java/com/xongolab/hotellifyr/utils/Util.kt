package com.xongolab.hotellifyr.utils

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.net.ConnectivityManager
import android.os.CountDownTimer
import android.text.TextUtils
import android.util.Patterns
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import com.xongolab.hotellifyr.R
import com.xongolab.hotellifyr.custom.CustomDialog
import com.xongolab.hotellifyr.databinding.DialogValidationMessageBinding
import okhttp3.MultipartBody
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

object Util {

    private var customDialog: CustomDialog? = null
    private var messageDialog: Dialog? = null

    fun showProgress(context: Context) {
        try {
            if (customDialog != null && customDialog!!.isShowing)
                customDialog!!.dismiss()

            customDialog = CustomDialog(context)
            customDialog!!.setCancelable(false)
            customDialog!!.show()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun dismissProgress() {
        if (customDialog != null && customDialog!!.isShowing)
            customDialog!!.dismiss()
        customDialog = null
    }


    fun isOnline(context: Context): Boolean {
        return try {
            val conMgr = context
                .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val info = conMgr.activeNetworkInfo
            info != null && info.isConnected
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    fun isEmptyText(view: View?): Boolean {
        return if (view == null)
            true
        else
            getTextValue(view).isEmpty()
    }

    fun isValidEmail(target: CharSequence?): Boolean {
        return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target!!).matches()
    }

    fun getTextValue(view: View): String {
        return (view as? EditText)?.text?.toString()?.trim { it <= ' ' }
            ?: ((view as? TextView)?.text?.toString()?.trim { it <= ' ' }
                ?: "")
    }

    var timer: CountDownTimer? = null
    fun msgDialog(context: Context, msg: String) {
        try {
            val dialogValidationMessageBinding = DialogValidationMessageBinding.inflate(
                LayoutInflater.from(context)
            )
            messageDialog = Dialog(context, R.style.DialogSheetNoTranBack)

            messageDialog!!.setContentView(dialogValidationMessageBinding.root)

            //val marginInDp = context.resources.getDimension(com.intuit.sdp.R.dimen._15sdp).toInt()
            val marginInDp = 15  // example margin in dp
            val scale = context.resources.displayMetrics.density
            val marginInPx = (marginInDp * scale + 0.5f).toInt()

            val layoutParams = dialogValidationMessageBinding.root.layoutParams as ViewGroup.MarginLayoutParams
            layoutParams.setMargins(marginInPx, marginInPx, marginInPx, 0)
            dialogValidationMessageBinding.root.layoutParams = layoutParams

            messageDialog!!.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            messageDialog!!.window!!.setGravity(Gravity.TOP)
            messageDialog!!.window!!.setWindowAnimations(R.style.DialogMessageAnimation)
            messageDialog!!.setCancelable(true)

            dialogValidationMessageBinding.tvMessage.text = msg

            if (messageDialog != null && messageDialog!!.isShowing) {
                messageDialog!!.dismiss()
            }
            messageDialog!!.show()

            timer = object : CountDownTimer(3000, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    //No operation required
                }

                override fun onFinish() {
                    if (timer != null) {
                        timer!!.cancel()
                    }
                    try {
                        if (messageDialog!!.isShowing) messageDialog!!.dismiss()
                    } catch (e: java.lang.Exception) {
                        e.printStackTrace()
                    }

                }
            }.start()

        } catch (e: NullPointerException) {
            e.printStackTrace()
        }
    }

    fun dismissMsgDialog() {
        if (messageDialog != null && messageDialog!!.isShowing)
            messageDialog!!.dismiss()
        messageDialog = null
    }

    fun createPartFromString(value: String, key: String): MultipartBody.Part {
        return MultipartBody.Part.createFormData(key, value)
    }

    @SuppressLint("SimpleDateFormat")
    fun formatTimestamp(timestamp: String, currentFormat: String = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", parseFormat: String): String {
        val inputFormat = SimpleDateFormat(currentFormat)
        val outputFormat = SimpleDateFormat(parseFormat)
        inputFormat.timeZone = TimeZone.getTimeZone("UTC")

        val date = inputFormat.parse(timestamp)
        return outputFormat.format(date!!)
    }

    fun calculateDistance(
        lat1: Double, lon1: Double,
        lat2: Double, lon2: Double
    ): Double {
        val R = 6371.0 // Radius of the Earth in kilometers
        val latDistance = Math.toRadians(lat2 - lat1)
        val lonDistance = Math.toRadians(lon2 - lon1)

        val a = sin(latDistance / 2) * sin(latDistance / 2) +
                cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
                sin(lonDistance / 2) * sin(lonDistance / 2)

        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        return R * c // Distance in kilometers
    }


    fun calculateDistance2(
        lat1: Double, lon1: Double,
        lat2: Double, lon2: Double
    ): Double {
        val earthRadius = 6371.0 // KM

        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)

        val a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) *
                Math.cos(Math.toRadians(lat2)) *
                Math.sin(dLon / 2) * Math.sin(dLon / 2)

        val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))

        return earthRadius * c
    }

    fun getTimeInMillis(dateString: String, format: String): Long {
        val sdf = SimpleDateFormat(format, Locale.ENGLISH)
        val date = sdf.parse(dateString)!!
        return date.time // Returns time in milliseconds
    }


}