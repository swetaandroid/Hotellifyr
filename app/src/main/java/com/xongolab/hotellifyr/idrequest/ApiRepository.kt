package com.xongolab.hotellifyr.idrequest

import com.xongolab.hotellifyr.model.AddPromoCodeRequest
import com.xongolab.hotellifyr.model.BookingHotelRequest
import com.xongolab.hotellifyr.model.GenerateHashRequest
import com.xongolab.hotellifyr.model.PreferenceRequest
import com.xongolab.hotellifyr.model.SignUpRequest
import com.xongolab.hotellifyr.model.VerifyBookOTPRequest
import com.xongolab.hotellifyr.model.VerifyOTPRequest
import okhttp3.MultipartBody
import okhttp3.RequestBody

class ApiRepository(private val idRequestService: IDRequestService) {

    suspend fun getLanguageListApi() = idRequestService.getLanguageListApi()

    suspend fun getSendOTPApi(
        mobile: MultipartBody.Part,
        countryCode: MultipartBody.Part,
        type: MultipartBody.Part
    ) = idRequestService.getSendOTPApi(mobile, countryCode, type)

    suspend fun getSendBookOTPApi(
        mobile: MultipartBody.Part,
        countryCode: MultipartBody.Part,
        type: MultipartBody.Part
    ) = idRequestService.getSendBookOTPApi(mobile, countryCode, type)

    suspend fun getVerifyOTPApi(verifyOTPRequest: VerifyOTPRequest) = idRequestService.getVerifyOTPApi(verifyOTPRequest)
    suspend fun getVerifyBookOTPApi(verifyOTPRequest: VerifyBookOTPRequest) = idRequestService.getVerifyBookOTPApi(verifyOTPRequest)
    suspend fun getCMSApi(status: String) = idRequestService.getCMSApi(status)
    suspend fun getSignUpApi(signupRequest: SignUpRequest) = idRequestService.getSignUpApi(signupRequest)

    suspend fun getHotelDetailApi(
        userID: String,hotelID: String, checkIn: String?, checkOut: String?, adults: Int?, children: Int?
    ) = idRequestService.getHotelDetailApi(userID,hotelID, checkIn, checkOut, adults, children)

    suspend fun getAirportPickApi(hotelID: String) = idRequestService.getAirportPickApi(hotelID)
    suspend fun callAddPromoCodeApi(addPromoCodeRequest: AddPromoCodeRequest) = idRequestService.callAddPromoCodeApi(addPromoCodeRequest)

    suspend fun getAddOnExperienceListApi(hotelID: String) = idRequestService.getAddOnExperienceListApi(hotelID)

    suspend fun getBookingListApi(
        page: Int, sizePerPage: Int?, status: String?
    ) = idRequestService.getBookingListApi(page,sizePerPage,status)

    suspend fun getNotificationHistoryListApi(
        page: Int, sizePerPage: Int?
    ) = idRequestService.getNotificationHistoryListApi(page,sizePerPage)

    suspend fun callNotificationDeleteApi(
        actionType: MultipartBody.Part, notificationID : MultipartBody.Part
    ) = idRequestService.callNotificationDeleteApi(actionType,notificationID)

    suspend fun callAllNotificationDeleteApi(
        actionType: MultipartBody.Part
    ) = idRequestService.callAllNotificationDeleteApi(actionType)

    suspend fun getWishListApi(
        actionType: MultipartBody.Part
    ) = idRequestService.getWishListApi(actionType)

    suspend fun deleteWishListApi(
        actionType: MultipartBody.Part,
        hotelId : MultipartBody.Part
    ) = idRequestService.deleteWishListApi(actionType,hotelId)

    suspend fun generateHashApi(generateHashRequest: GenerateHashRequest) = idRequestService.generateHashApi(generateHashRequest)
    suspend fun getTransactionHistoryApi() = idRequestService.getTransactionHistoryApi()
    suspend fun getMyPreferencesListApi() = idRequestService.getMyPreferencesListApi()
    suspend fun setMyPreferencesApi(requestBody: PreferenceRequest) = idRequestService.setMyPreferencesApi(requestBody)


    suspend fun getOfferRoomPriceApi(
        hotelID: String, checkIn: String?, checkOut: String?, adults: Int?, children: Int?
    ) = idRequestService.getOfferRoomPriceApi(hotelID, checkIn, checkOut, adults, children)

    suspend fun getTaxPercentageApi(roomPrice: Double) = idRequestService.getTaxPercentageApi(roomPrice)
    suspend fun getHomeBannerListApi() = idRequestService.getHomeBannerListApi()
    suspend fun getCampaignListApi() = idRequestService.getCampaignListApi()
    suspend fun getHotelTagListApi(userID: String?) = idRequestService.getHotelTagListApi(userID)
    suspend fun getCurrentOfferListApi() = idRequestService.getCurrentOfferListApi()
    suspend fun getHomeCityListApi(stateID: String) = idRequestService.getHomeCityListApi(stateID)

    suspend fun getHotelListApi(segmentId: String, exploreId: String) =
        idRequestService.getHotelListApi(segmentId, exploreId)

    suspend fun getHotelListingApi(
        userID: String,
        adults: Int?,
        children: Int?,
        checkIn: String?,
        checkOut: String?,
        hotelType: String?,
        starRating: Float?,
        cityID: String?,
        stateID: String?,
        brandID: String?,
        longitude: Double?,
        latitude: Double?,
        hotelIds: String?,
        amenitiesIDs: String?,
        brandIDs: String?,
        destinationIDs: String?,
        sortBy: String?
    ) =
        idRequestService.getHotelListingApi(
            userID,
            adults,
            children,
            checkIn,
            checkOut,
            hotelType,
            starRating,
            cityID,
            stateID,
            brandID,
            longitude,
            latitude,
            hotelIds,
            amenitiesIDs,
            brandIDs,
            destinationIDs,
            sortBy
        )

    suspend fun getCustomerInfoApi() = idRequestService.getCustomerInfoApi()
    suspend fun getCountryListApi() = idRequestService.getCountryListApi()
    suspend fun getStateListApi(countryID: String) = idRequestService.getStateListApi(countryID)
    suspend fun getCityListApi(stateID: String) =
        idRequestService.getCityListApi(stateID)

    suspend fun updateProfileApi(
       firstName: MultipartBody.Part,
       lastName: MultipartBody.Part,
       email: MultipartBody.Part,
       mobile: MultipartBody.Part,
       mobileCountryCode: MultipartBody.Part,
       address: MultipartBody.Part,
       countryID: MultipartBody.Part,
       stateID: MultipartBody.Part,
       cityID: MultipartBody.Part,
       anniversaryDate: MultipartBody.Part,
       dateOfBirth: MultipartBody.Part,
    ) = idRequestService.updateProfileApi(firstName, lastName, email, mobile, mobileCountryCode, address, countryID, stateID, cityID, anniversaryDate, dateOfBirth)

    suspend fun updateProfilePhotoApi(
        avatar: MultipartBody.Part,
    ) = idRequestService.updateProfilePhotoApi(avatar)

    suspend fun getDestinationListApi() = idRequestService.getDestinationListApi()
    suspend fun getBrandListApi() = idRequestService.getBrandListApi()
    suspend fun getAmenitiesListApi() = idRequestService.getAmenitiesListApi()
    suspend fun callBookingHotelApi(bookingHotelRequest: BookingHotelRequest) = idRequestService.callBookingHotelApi(bookingHotelRequest)
    suspend fun getNearByPlaceListApi(hotelID: String) =
        idRequestService.getNearByPlaceListApi(hotelID)
    suspend fun getBookTableApi(
        restaurantID: MultipartBody.Part,
        hotelID: MultipartBody.Part,
        bookingDate: MultipartBody.Part,
        firstName: MultipartBody.Part,
        lastName: MultipartBody.Part,
        email: MultipartBody.Part,
        mobileCountryCode: MultipartBody.Part,
        mobile: MultipartBody.Part,
        diningType: MultipartBody.Part,
        numberOfGuests: MultipartBody.Part
    ) = idRequestService.getBookTableApi(
        restaurantID,
        hotelID,
        bookingDate,
        firstName,
        lastName,
        email,
        mobileCountryCode,
        mobile,
        diningType,
        numberOfGuests
    )

    suspend fun getBookEventBanquetApi(
        type: MultipartBody.Part,
        id: MultipartBody.Part,
        hotelID: MultipartBody.Part,
        bookingDate: MultipartBody.Part,
        firstName: MultipartBody.Part,
        lastName: MultipartBody.Part,
        email: MultipartBody.Part,
        mobileCountryCode: MultipartBody.Part,
        mobile: MultipartBody.Part,
        diningType: MultipartBody.Part,
        numberOfGuests: MultipartBody.Part
    ) = idRequestService.getBookEventBanquetApi(
        type,
        id,
        hotelID,
        bookingDate,
        firstName,
        lastName,
        email,
        mobileCountryCode,
        mobile,
        diningType,
        numberOfGuests
    )


    suspend fun getPaymentApi(params: HashMap<String, String>) = idRequestService.getPaymentApi(params)
}