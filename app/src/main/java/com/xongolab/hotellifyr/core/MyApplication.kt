package com.xongolab.hotellifyr.core

import android.app.Application
import android.content.Context
import com.facebook.drawee.backends.pipeline.Fresco
import com.google.firebase.FirebaseApp
import com.stripe.android.PaymentConfiguration
import com.xongolab.hotellifyr.locale.LocaleManager
import com.xongolab.hotellifyr.utils.Pref

class MyApplication : Application() {

    companion object {
        lateinit var instance: MyApplication
    }

    override fun attachBaseContext(base: Context) {
        instance = this
        super.attachBaseContext(base)
        // Perform any locale setup or other configuration here if necessary
    }

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        Fresco.initialize(this)

        // Perform any additional setup after the instance is initialized
        val languageCode = Pref.getStringValue(Pref.PREF_LANGUAGE, "EN").lowercase()
        LocaleManager.setLocale(this, languageCode)

        PaymentConfiguration.init(
            applicationContext,
            "pk_test_51ROYNWSGBCq45og5fsfw24uWcOOYm3cxc66qCeWdFU7OIAVBGiDPTy9hTrZONicKhjJfVT60iOrLuhurjwpZnPPx00u1HtSrpV"
        )
    }
}