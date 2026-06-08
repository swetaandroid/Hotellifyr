package com.xongolab.hotellifyr.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import com.xongolab.hotellifyr.R
import com.xongolab.hotellifyr.model.SearchHotel
import com.xongolab.hotellifyr.utils.Constants
import com.xongolab.hotellifyr.utils.Pref
import com.xongolab.hotellifyr.view.activity.MainActivity
import com.xongolab.hotellifyr.view.activity.home.search.hotel.HotelDetailActivity
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        Log.e(TAG, "Refreshed token: $token")
        Pref.setStringValue(Pref.PREF_DEVICE_TOKEN, token)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.e(TAG, "From: " + remoteMessage.from)
        Log.e(TAG, "Notification Payload Data: " + remoteMessage.notification)
        Log.e(TAG, "Notification Payload: " + remoteMessage.data)

        // Extract notification data
      //  val title = remoteMessage.data["title"]
        val title = remoteMessage.data["title"]
        val body = remoteMessage.data["body"]
        val hotelID = remoteMessage.data["hotelID"] ?: ""

        // Handle notification display
        sendPushNotification(applicationContext, title ?: "", body ?: "", hotelID)
    }

    fun sendPushNotification(
        context: Context,
        title: String,
        message: String?,
        getHotelID: String?
    ) {
        val intent: Intent

        if (getHotelID != ""){
            val dateFormat = SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.getDefault())
            dateFormat.timeZone = TimeZone.getDefault()

            // Get current calendar instance
            val calendar = Calendar.getInstance()

            // Format today's date
            val today = dateFormat.format(calendar.time)

            calendar.add(Calendar.DAY_OF_YEAR, 1)
            val tomorrow = dateFormat.format(calendar.time)

            val searchHotel = SearchHotel()
            searchHotel.apply {
                rooms = 1
                adults = 1
                children = 0
                hotelID = getHotelID
                location = ""
                type = Constants.HOTEL
                checkIn = today
                checkOut = tomorrow
            }
            intent = Intent(this, HotelDetailActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            intent.putExtra(Constants.SEARCH_HOTEL, Gson().toJson(searchHotel))
        }else{
            intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
        }
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder: NotificationCompat.Builder = NotificationCompat.Builder(context)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(title)
            .setContentText(message)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val appName = context.getString(R.string.app_name)
            val channel = NotificationChannel(
                appName + "_CHANNEl_ID",
                appName + "_CHANNEL",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationBuilder.setChannelId(appName + "_CHANNEl_ID")
            notificationManager.createNotificationChannel(channel)
        }
        notificationManager.notify(0, notificationBuilder.build())
    }

    companion object {
        private const val TAG = "MyFirebaseMsgService"
    }
}