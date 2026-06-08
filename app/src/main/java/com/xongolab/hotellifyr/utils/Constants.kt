package com.xongolab.hotellifyr.utils

object Constants {

    const val BASE_URL: String = "https://api-demo.hotellifyr.com/"
  //  const val BASE_URL: String = "http://3.23.121.245:9081/"
//    const val BASE_URL: String = "https://api.regentarewards.com/"
   // const val BASE_URL: String = "https://stg-api.regentarewards.com/"
  //  const val PAYU_KEY: String = "dqfEQ7"
    const val PAYU_KEY: String = "JVAucE"  //live
    const val PAYU_SURL: String = "${BASE_URL}payu/payment-success"
    const val PAYU_FURL: String = "${BASE_URL}payu/payment-failed"
    const val LOCATION_PERMISSION_REQUEST_CODE = 1001

    const val OTP_ID: String = "OTP_ID"
    const val OTP: String = "OTP"
    const val MOBILE: String = "MOBILE"
    const val MOBILE_COUNTRY_CODE: String = "MOBILE_COUNTRY_CODE"
    const val MOBILE_COUNTRY_NAME: String = "MOBILE_COUNTRY_NAME"
    const val TERMS_CONDITION: String = "TERMS_CONDITION"
    const val PRIVACY_POLICY: String = "PRIVACY_POLICY"
    const val TITLE: String = "TITLE"
    const val WEB_URL: String = "WEB_URL"
    const val LOCATION: String = "LOCATION"
    const val CHECK_IN_DATE: String = "CHECK_IN_DATE"
    const val CHECK_OUT_DATE: String = "CHECK_OUT_DATE"
    const val SEARCH_HOTEL: String = "SEARCH_HOTEL"
    const val CITY: String = "CITY"
    const val HOTEL: String = "HOTEL"
    const val HOTELS: String = "HOTELS"
    const val OFFERS: String = "OFFERS"
}