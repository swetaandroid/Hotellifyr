package com.xongolab.hotellifyr.idrequest

import com.xongolab.hotellifyr.model.AddOnExperience
import com.xongolab.hotellifyr.model.AddPromo
import com.xongolab.hotellifyr.model.AddPromoCodeRequest
import com.xongolab.hotellifyr.model.AirportPickUp
import com.xongolab.hotellifyr.model.BookingHistoryModel
import com.xongolab.hotellifyr.model.BookingHotel
import com.xongolab.hotellifyr.model.BookingHotelRequest
import com.xongolab.hotellifyr.model.CampaignModel
import com.xongolab.hotellifyr.model.CouponDetailRequest
import com.xongolab.hotellifyr.model.GenerateHash
import com.xongolab.hotellifyr.model.GenerateHashRequest
import com.xongolab.hotellifyr.model.HomeBanner
import com.xongolab.hotellifyr.model.HotelModel
import com.xongolab.hotellifyr.model.HotelRoomPriceModel
import com.xongolab.hotellifyr.model.LanguageModel
import com.xongolab.hotellifyr.model.NotificationModel
import com.xongolab.hotellifyr.model.OfferRoomPriceModel
import com.xongolab.hotellifyr.model.OffersModel
import com.xongolab.hotellifyr.model.PreferenceRequest
import com.xongolab.hotellifyr.model.PreferencesModel
import com.xongolab.hotellifyr.model.SignUpRequest
import com.xongolab.hotellifyr.model.TaxPercentage
import com.xongolab.hotellifyr.model.TransactionModel
import com.xongolab.hotellifyr.model.UserModel
import com.xongolab.hotellifyr.model.VerifyBookOTPRequest
import com.xongolab.hotellifyr.model.VerifyOTPRequest
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Query

interface IDRequestService {

    @GET(IDRequestBuilder.LANGUAGE_LIST_API)
    suspend fun getLanguageListApi(): Response<ResponseArrayModel<LanguageModel>>

    @Multipart
    @PUT(IDRequestBuilder.SEND_OTP_API)
    suspend fun getSendOTPApi(
        @Part mobile: MultipartBody.Part,
        @Part countryCode: MultipartBody.Part,
        @Part type: MultipartBody.Part
    ): Response<ResponseModel<UserModel>>

    @Multipart
    @PUT(IDRequestBuilder.SEND_BOOK_OTP_API)
    suspend fun getSendBookOTPApi(
        @Part mobile: MultipartBody.Part,
        @Part countryCode: MultipartBody.Part,
        @Part type: MultipartBody.Part
    ): Response<ResponseModel<UserModel>>

    @POST(IDRequestBuilder.VERIFY_OTP_API)
    suspend fun getVerifyOTPApi(@Body body: VerifyOTPRequest): Response<ResponseModel<UserModel>>

    @POST(IDRequestBuilder.VERIFY_BOOK_OTP_API)
    suspend fun getVerifyBookOTPApi(@Body body: VerifyBookOTPRequest): Response<ResponseModel<UserModel>>

    @PUT(IDRequestBuilder.SIGN_UP_API)
    suspend fun getSignUpApi(@Body body: SignUpRequest): Response<ResponseModel<UserModel>>

    @GET(IDRequestBuilder.CMS_API)
    suspend fun getCMSApi(@Query("status") status: String): Response<ResponseArrayModel<UserModel>>

    @GET(IDRequestBuilder.HOTEL_DETAIL_API)
    suspend fun getHotelDetailApi(
        @Query("userID") userID: String,
        @Query("hotelID") hotelID: String,
        @Query("checkIn") checkIn: String?,
        @Query("checkOut") checkOut: String?,
        @Query("adults") adults: Int?,
        @Query("children") children: Int?,
    ): Response<ResponseModel<HotelModel>>

    @GET(IDRequestBuilder.AIRPORT_PICKUP_API)
    suspend fun getAirportPickApi(
        @Query("hotelID") hotelID: String
    ): Response<ResponseArrayModel<AirportPickUp>>

    @PUT(IDRequestBuilder.ADD_PROMO_CODE_API)
    suspend fun callAddPromoCodeApi(
        @Body body: AddPromoCodeRequest
    ): Response<ResponseModel<CouponDetailRequest>>

    @GET(IDRequestBuilder.ADD_ON_EXPERIENCE_API)
    suspend fun getAddOnExperienceListApi(
        @Query("hotelID") hotelID: String
    ): Response<ResponseArrayModel<AddOnExperience>>

    @GET(IDRequestBuilder.BOOKING_LIST_API)
    suspend fun getBookingListApi(
        @Query("page") page: Int,
        @Query("sizePerPage") sizePerPage: Int?,
        @Query("status") status: String?
    ): Response<ResponseModel<BookingHistoryModel>>

    @GET(IDRequestBuilder.NOTIFICATION_LIST_API)
    suspend fun getNotificationHistoryListApi(
        @Query("page") page: Int,
        @Query("sizePerPage") sizePerPage: Int?
    ): Response<ResponseModel<NotificationModel>>

    @Multipart
    @PATCH(IDRequestBuilder.DELETE_NOTIFICATION_API)
    suspend fun callNotificationDeleteApi(
       @Part actionType: MultipartBody.Part,
       @Part notificationID: MultipartBody.Part
    ): Response<ResponseModel<NotificationModel>>

    @Multipart
    @PATCH(IDRequestBuilder.DELETE_NOTIFICATION_API)
    suspend fun callAllNotificationDeleteApi(
       @Part actionType: MultipartBody.Part
    ): Response<ResponseModel<NotificationModel>>

    @PATCH(IDRequestBuilder.TRANSACTION_HISTORY_API)
    suspend fun getTransactionHistoryApi(): Response<ResponseModel<TransactionModel>>

    @POST(IDRequestBuilder.GENERATE_HASH_API)
    suspend fun generateHashApi(@Body generateHashRequest: GenerateHashRequest): Response<ResponseModel<GenerateHash>>

    @GET(IDRequestBuilder.MY_PREFERENCES_LIST_API)
    suspend fun getMyPreferencesListApi(): Response<ResponseArrayModel<PreferencesModel>>

    @PATCH(IDRequestBuilder.SET_MY_PREFERENCES_API)
    suspend fun setMyPreferencesApi(@Body preferenceRequest : PreferenceRequest): Response<ResponseModel<PreferencesModel>>

    @Multipart
    @PATCH(IDRequestBuilder.GET_WISHLIST_API)
    suspend fun getWishListApi(@Part actionType : MultipartBody.Part): Response<ResponseModel<HotelModel>>

    @Multipart
    @PATCH(IDRequestBuilder.GET_WISHLIST_API)
    suspend fun deleteWishListApi(@Part actionType : MultipartBody.Part,@Part hotelID: MultipartBody.Part): Response<ResponseModel<HotelModel>>

    @GET(IDRequestBuilder.OFFER_ROOM_PRICE_API)
    suspend fun getOfferRoomPriceApi(
        @Query("hotelID") hotelID: String,
        @Query("checkIn") checkIn: String?,
        @Query("checkOut") checkOut: String?,
        @Query("adults") adults: Int?,
        @Query("children") children: Int?,
    ): Response<ResponseArrayModel<OfferRoomPriceModel>>

    @GET(IDRequestBuilder.TAX_PERCENTAGE_API)
    suspend fun getTaxPercentageApi(@Query("price") price : Double
    ): Response<ResponseModel<TaxPercentage>>

    @GET(IDRequestBuilder.HOME_BANNER_LIST_API)
    suspend fun getHomeBannerListApi(
    ): Response<ResponseModel<HomeBanner>>

    @GET(IDRequestBuilder.CAMPAIGN_LIST_API)
    suspend fun getCampaignListApi(
    ): Response<ResponseArrayModel<CampaignModel>>

    @GET(IDRequestBuilder.HOTEL_TAG_LIST_API)
    suspend fun getHotelTagListApi(
        @Query("userID") userID: String?,
    ): Response<ResponseArrayModel<HotelModel>>

    @GET(IDRequestBuilder.CURRENT_OFFER_LIST_API)
    suspend fun getCurrentOfferListApi(
    ): Response<ResponseArrayModel<OffersModel>>

    @GET(IDRequestBuilder.CITY_HOME_LIST_API)
    suspend fun getHomeCityListApi(
        @Query("stateID") stateID: String,
    ): Response<ResponseArrayModel<HotelModel>>

    @GET(IDRequestBuilder.HOTEL_LIST_API)
    suspend fun getHotelListApi(
        @Query("segmentId") segmentId: String,
        @Query("exploreId") exploreId: String,
    ): Response<ResponseArrayModel<HotelModel>>

    @GET(IDRequestBuilder.HOTEL_LISTING_API)
    suspend fun getHotelListingApi(
        @Query("userID") userID: String,
        @Query("adults") adults: Int?,
        @Query("children") children: Int?,
        @Query("checkIn") checkIn: String?,
        @Query("checkOut") checkOut: String?,
        @Query("hotelType") hotelType: String?,
        @Query("starRating") starRating: Float?,
        @Query("cityID") cityID: String?,
        @Query("stateID") stateID: String?,
        @Query("brandID") brandID: String?,
        @Query("longitude") longitude: Double?,
        @Query("latitude") latitude: Double?,
        @Query("hotelIds") hotelIds: String?,
        @Query("amenitiesIDs") amenitiesIDs: String?,
        @Query("brandIDs") brandIDs: String?,
        @Query("destinationIDs") destinationIDs: String?,
        @Query("sortBy") sortBy: String?,
    ): Response<ResponseArrayModel<HotelModel>>

    @GET(IDRequestBuilder.CUSTOMER_INFO_API)
    suspend fun getCustomerInfoApi(
    ): Response<ResponseModel<UserModel>>

    @GET(IDRequestBuilder.COUNTRY_LIST_API)
    suspend fun getCountryListApi(
    ): Response<ResponseArrayModel<UserModel>>

    @GET(IDRequestBuilder.STATE_LIST_API)
    suspend fun getStateListApi(
        @Query("countryID") countryID: String,
    ): Response<ResponseArrayModel<UserModel>>

    @GET(IDRequestBuilder.CITY_LIST_API)
    suspend fun getCityListApi(
        @Query("stateID") stateID: String,
    ): Response<ResponseArrayModel<UserModel>>

    @Multipart
    @PATCH(IDRequestBuilder.UPDATE_PROFILE_API)
    suspend fun updateProfileApi(
        @Part firstName: MultipartBody.Part,
        @Part lastName: MultipartBody.Part,
        @Part email: MultipartBody.Part,
        @Part mobile: MultipartBody.Part,
        @Part mobileCountryCode: MultipartBody.Part,
        @Part address: MultipartBody.Part,
        @Part countryID: MultipartBody.Part,
        @Part stateID: MultipartBody.Part,
        @Part cityID: MultipartBody.Part,
        @Part anniversaryDate: MultipartBody.Part,
        @Part dateOfBirth: MultipartBody.Part,
    ): Response<ResponseModel<UserModel>>

    @Multipart
    @POST(IDRequestBuilder.UPDATE_PROFILE_PHOTO_API)
    suspend fun updateProfilePhotoApi(
        @Part avatar: MultipartBody.Part,
    ): Response<ResponseModel<UserModel>>

    @GET(IDRequestBuilder.DESTINATION_LIST_API)
    suspend fun getDestinationListApi(
    ): Response<ResponseArrayModel<HotelModel>>

    @GET(IDRequestBuilder.BRAND_LIST_API)
    suspend fun getBrandListApi(
    ): Response<ResponseArrayModel<HotelModel>>

    @GET(IDRequestBuilder.AMENITIES_LIST_API)
    suspend fun getAmenitiesListApi(
    ): Response<ResponseArrayModel<HotelModel>>

    @POST(IDRequestBuilder.BOOKING_HOTEL_API)
    suspend fun callBookingHotelApi(
        @Body body: BookingHotelRequest
    ): Response<ResponseModel<BookingHotel>>

    @GET(IDRequestBuilder.NEAR_BY_PLACE_LIST_API)
    suspend fun getNearByPlaceListApi(
        @Query("hotelID") hotelID: String?,
    ): Response<ResponseArrayModel<HotelModel>>

    @Multipart
    @PUT(IDRequestBuilder.BOOK_TABLE_API)
    suspend fun getBookTableApi(
        @Part restaurantID: MultipartBody.Part,
        @Part hotelID: MultipartBody.Part,
        @Part bookingDate: MultipartBody.Part,
        @Part firstName: MultipartBody.Part,
        @Part lastName: MultipartBody.Part,
        @Part email: MultipartBody.Part,
        @Part mobileCountryCode: MultipartBody.Part,
        @Part mobile: MultipartBody.Part,
        @Part diningType: MultipartBody.Part,
        @Part numberOfGuests: MultipartBody.Part,
        ): Response<ResponseModel<HotelModel>>

    @Multipart
    @PUT(IDRequestBuilder.BOOK_EVENT_BANQUET_API)
    suspend fun getBookEventBanquetApi(
        @Part type: MultipartBody.Part,
        @Part id: MultipartBody.Part,
        @Part hotelID: MultipartBody.Part,
        @Part bookingDate: MultipartBody.Part,
        @Part firstName: MultipartBody.Part,
        @Part lastName: MultipartBody.Part,
        @Part email: MultipartBody.Part,
        @Part mobileCountryCode: MultipartBody.Part,
        @Part mobile: MultipartBody.Part,
        @Part diningType: MultipartBody.Part,
        @Part numberOfGuests: MultipartBody.Part,
        ): Response<ResponseModel<HotelModel>>


    @FormUrlEncoded
    @POST(IDRequestBuilder.GET_PAYMENT_API)
    suspend fun getPaymentApi(
        @FieldMap params: HashMap<String, String>
    ): Response<ResponseModel<UserModel>>
}