package com.xongolab.hotellifyr.idrequest

import android.content.Context
import android.content.Intent
import com.xongolab.hotellifyr.utils.Constants.BASE_URL
import com.xongolab.hotellifyr.utils.Pref
import com.xongolab.hotellifyr.view.activity.MainActivity
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object IDRequestBuilder {

    private const val NETWORK_CALL_TIMEOUT = 60
    private const val API_URL = BASE_URL

    // API_URL
    const val LANGUAGE_LIST_API: String = "app/common/language-list"
    const val CMS_API: String = "api/common/cms"
    const val SEND_OTP_API: String = "app/auth/send-otp"
    const val SEND_BOOK_OTP_API: String = "api/booking/send-otp"
    const val VERIFY_OTP_API: String = "app/auth/verify-otp"
    const val VERIFY_BOOK_OTP_API: String = "api/booking/verify-otp"
    const val SIGN_UP_API: String = "app/auth/signup"
    const val HOTEL_LIST_API: String = "api/common/hotel-list"
    const val CUSTOMER_INFO_API: String = "app/customer/info"
    const val UPDATE_PROFILE_API: String = "app/customer/updateProfile"
    const val UPDATE_PROFILE_PHOTO_API: String = "app/customer/updateProfilePhoto"

    const val HOME_BANNER_LIST_API: String = "api/common/home-banner"
    const val CURRENT_OFFER_LIST_API: String = "app/common/offers"
    const val CAMPAIGN_LIST_API: String = "app/common/campaign"
    const val HOTEL_TAG_LIST_API: String = "app/common/hotel-tags"
    const val HOTEL_DETAIL_API: String = "app/hotel/hotel-details"
    const val AIRPORT_PICKUP_API: String = "app/common/airport-pickup"
    const val ADD_ON_EXPERIENCE_API: String = "app/common/add-on-experience"
    const val OFFER_ROOM_PRICE_API: String = "app/hotel/offer-room-price"
    const val NEAR_BY_PLACE_LIST_API: String = "app/hotel/near-by-place"
    const val BOOKING_LIST_API: String = "app/customer/booking-list"
    const val NOTIFICATION_LIST_API: String = "app/customer/notification-list"
    const val DELETE_NOTIFICATION_API: String = "app/customer/delete-notification"
    const val MY_PREFERENCES_LIST_API: String = "app/customer/preference-category-list"
    const val SET_MY_PREFERENCES_API: String = "app/customer/update-preference"
    const val TRANSACTION_HISTORY_API: String = "app/customer/transaction-details"
    const val GET_WISHLIST_API: String = "app/customer/manage-favourite-hotels"
    const val BOOK_TABLE_API: String = "app/hotel/book-a-table"
    const val BOOK_EVENT_BANQUET_API: String = "app/hotel/book-a-eventAndbanquet"
    const val TAX_PERCENTAGE_API: String = "api/booking/tax-percentage"
    const val ADD_PROMO_CODE_API: String = "api/coupon/check"

    const val COUNTRY_LIST_API: String = "app/common/country"
    const val STATE_LIST_API: String = "app/common/states"
    const val CITY_LIST_API: String = "app/common/cities"
    const val CITY_HOME_LIST_API: String = "api/common/cities"
    const val HOTEL_LISTING_API: String = "app/common/nearest-hotel-list"
    const val DESTINATION_LIST_API: String = "api/common/destination"
    const val BRAND_LIST_API: String = "api/common/brands"
    const val AMENITIES_LIST_API: String = "api/common/amenities"
    const val BOOKING_HOTEL_API: String = "app/booking/book-hotel"
    const val GENERATE_HASH_API: String = "payu/app-generate-hash"
    const val GET_PAYMENT_API: String = "app/customer/create-payment-intent"


    private var ipRequestService: IDRequestService? = null

    fun getInstance(context: Context): IDRequestService {
        if (ipRequestService == null) {
            val httpClientBuilder = OkHttpClient.Builder()
                .readTimeout(NETWORK_CALL_TIMEOUT.toLong(), TimeUnit.SECONDS)
                .writeTimeout(NETWORK_CALL_TIMEOUT.toLong(), TimeUnit.SECONDS)

            if (true) {
                val loggingInterceptor = HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                }
                httpClientBuilder.addInterceptor(loggingInterceptor)
            }

            httpClientBuilder.addInterceptor(UnauthorizedInterceptor(context))

            httpClientBuilder.addInterceptor(Interceptor { chain: Interceptor.Chain ->
                val newRequest = chain.request().newBuilder()
                    .addHeader("code", Pref.getStringValue(Pref.PREF_LANGUAGE, "EN"))
                    .addHeader("authorization", Pref.getStringValue(Pref.PREF_AUTH_TOKEN, ""))
                    .build()
                chain.proceed(newRequest)
            })

            val retrofit = Retrofit.Builder()
                .baseUrl(API_URL)
                .client(httpClientBuilder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            ipRequestService = retrofit.create(IDRequestService::class.java)
        }
        return ipRequestService!!
    }

    class UnauthorizedInterceptor(private val context: Context) : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val request = chain.request()
            val response = chain.proceed(request)

            if (response.code == 401) {
                // Handle unauthorized error
                handleUnauthorizedResponse(context)
            }

            return response
        }

        private fun handleUnauthorizedResponse(context: Context) {
            // Post a logout event or start login activity directly
            Pref.clearAllPref()

            val intent = Intent(context, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            context.startActivity(intent)
        }
    }


}