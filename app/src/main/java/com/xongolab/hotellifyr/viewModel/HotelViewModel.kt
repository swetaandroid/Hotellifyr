package com.xongolab.hotellifyr.viewModel

import android.app.Dialog
import android.content.Context
import android.os.CountDownTimer
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.xongolab.hotellifyr.R
import com.xongolab.hotellifyr.databinding.DialogValidationMessageBinding
import com.xongolab.hotellifyr.idrequest.ApiRepository
import com.xongolab.hotellifyr.idrequest.ResponseArrayModel
import com.xongolab.hotellifyr.idrequest.ResponseModel
import com.xongolab.hotellifyr.model.AddOnExperience
import com.xongolab.hotellifyr.model.AddPromo
import com.xongolab.hotellifyr.model.AddPromoCodeRequest
import com.xongolab.hotellifyr.model.AirportPickUp
import com.xongolab.hotellifyr.model.BookingHistoryModel
import com.xongolab.hotellifyr.model.BookingHotel
import com.xongolab.hotellifyr.model.BookingHotelRequest
import com.xongolab.hotellifyr.model.CampaignModel
import com.xongolab.hotellifyr.model.CouponDetailRequest
import com.xongolab.hotellifyr.model.ErrorResponse
import com.xongolab.hotellifyr.model.GenerateHash
import com.xongolab.hotellifyr.model.GenerateHashRequest
import com.xongolab.hotellifyr.model.HomeBanner
import com.xongolab.hotellifyr.model.HotelModel
import com.xongolab.hotellifyr.model.HotelRoomPriceModel
import com.xongolab.hotellifyr.model.NotificationModel
import com.xongolab.hotellifyr.model.OfferRoomPriceModel
import com.xongolab.hotellifyr.model.OffersModel
import com.xongolab.hotellifyr.model.PreferenceRequest
import com.xongolab.hotellifyr.model.PreferencesModel
import com.xongolab.hotellifyr.model.TaxPercentage
import com.xongolab.hotellifyr.model.TransactionModel
import com.xongolab.hotellifyr.utils.Util
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import okhttp3.RequestBody


class HotelViewModel(private val apiRepository: ApiRepository) : ViewModel() {

    private var job: Job? = null
    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        onError("Exception handled: ${throwable.localizedMessage}")
    }

    val getHomeBannerListApiResponse = MutableLiveData<ResponseModel<HomeBanner>>()
    val getCurrentOfferListApiResponse = MutableLiveData<ResponseArrayModel<OffersModel>>()
    val getCampaignListApiResponse = MutableLiveData<ResponseArrayModel<CampaignModel>>()
    val getHotelTagListApiResponse = MutableLiveData<ResponseArrayModel<HotelModel>>()
    val getCityListApiResponse = MutableLiveData<ResponseArrayModel<HotelModel>>()
    val getHotelListApiResponse = MutableLiveData<ResponseArrayModel<HotelModel>>()
    val getHotelListingApiResponse = MutableLiveData<ResponseArrayModel<HotelModel>>()
    val getDestinationListApiResponse = MutableLiveData<ResponseArrayModel<HotelModel>>()
    val getBrandListApiResponse = MutableLiveData<ResponseArrayModel<HotelModel>>()
    val getAmenitiesListApiResponse = MutableLiveData<ResponseArrayModel<HotelModel>>()
    val getNearByPlaceListApiResponse = MutableLiveData<ResponseArrayModel<HotelModel>>()
    val getBookTableApiResponse = MutableLiveData<ResponseModel<HotelModel>>()
    val getBookEventBanquetApiResponse = MutableLiveData<ResponseModel<HotelModel>>()
    val getHotelDetailApiResponse = MutableLiveData<ResponseModel<HotelModel>>()
    val getBookingListApiResponse = MutableLiveData<ResponseModel<BookingHistoryModel>>()
    val getNotificationHistoryListApiResponse = MutableLiveData<ResponseModel<NotificationModel>>()
    val callNotificationDeleteApiResponse = MutableLiveData<ResponseModel<NotificationModel>>()
    val callAllNotificationDeleteApiResponse = MutableLiveData<ResponseModel<NotificationModel>>()
    val setMyPreferencesApiResponse = MutableLiveData<ResponseModel<PreferencesModel>>()
    val getTransactionHistoryApiResponse = MutableLiveData<ResponseModel<TransactionModel>>()
    val getMyPreferencesListApiResponse = MutableLiveData<ResponseArrayModel<PreferencesModel>>()
    val getWishListApiResponse = MutableLiveData<ResponseModel<HotelModel>>()
    val deleteWishListApiResponse = MutableLiveData<ResponseModel<HotelModel>>()
    val getTaxPercentageApiResponse = MutableLiveData<ResponseModel<TaxPercentage>>()
    val callBookingHotelApiResponse = MutableLiveData<ResponseModel<BookingHotel>>()
    val generateHashApiResponse = MutableLiveData<ResponseModel<GenerateHash>>()
    val callAddPromoCodeApiResponse = MutableLiveData<ResponseModel<CouponDetailRequest>>()
    val getOfferRoomPriceApiResponse = MutableLiveData<ResponseArrayModel<OfferRoomPriceModel>>()
    val getAirportPickApiResponse = MutableLiveData<ResponseArrayModel<AirportPickUp>>()
    val getAddOnExperienceListApiResponse = MutableLiveData<ResponseArrayModel<AddOnExperience>>()

    fun getHomeBannerListApi(
        context: Context,
    ) {
        Util.showProgress(context)
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            try {
                val response = apiRepository.getHomeBannerListApi()
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        getHomeBannerListApiResponse.postValue(response.body())
                        Util.dismissProgress()
                    } else {
                        val type = object : TypeToken<ErrorResponse>() {}.type
                        val errorResponse: ErrorResponse =
                            Gson().fromJson(response.errorBody()!!.charStream(), type)
                        onError("Error : ${errorResponse.error.message} ")
                        msgDialog(context, errorResponse.error.message)
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Util.dismissProgress()
                    onError("Error: ${e.message}")
                    msgDialog(context, "Error: ${e.message}")
                }
            }
        }
    }

    fun getCampaignListApi(
        context: Context,
    ) {
        Util.showProgress(context)
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            try {
                val response = apiRepository.getCampaignListApi()
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        getCampaignListApiResponse.postValue(response.body())
                        Util.dismissProgress()
                    } else {
                        val type = object : TypeToken<ErrorResponse>() {}.type
                        val errorResponse: ErrorResponse =
                            Gson().fromJson(response.errorBody()!!.charStream(), type)
                        onError("Error : ${errorResponse.error.message} ")
                        msgDialog(context, errorResponse.error.message)
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Util.dismissProgress()
                    onError("Error: ${e.message}")
                    msgDialog(context, "Error: ${e.message}")
                }
            }
        }
    }

    fun getHotelTagListApi(
        context: Context,
        userID: String? = null,
    ) {
        Util.showProgress(context)
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            try {
                val response = apiRepository.getHotelTagListApi(userID)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        getHotelTagListApiResponse.postValue(response.body())
                        Util.dismissProgress()
                    } else {
                        val type = object : TypeToken<ErrorResponse>() {}.type
                        val errorResponse: ErrorResponse =
                            Gson().fromJson(response.errorBody()!!.charStream(), type)
                        onError("Error : ${errorResponse.error.message} ")
                        msgDialog(context, errorResponse.error.message)
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Util.dismissProgress()
                    onError("Error: ${e.message}")
                    msgDialog(context, "Error: ${e.message}")
                }
            }
        }
    }

    fun getCurrentOfferListApi(
        context: Context,
    ) {
        Util.showProgress(context)
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            try {
                val response = apiRepository.getCurrentOfferListApi()
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        getCurrentOfferListApiResponse.postValue(response.body())
                        Util.dismissProgress()
                    } else {
                        val type = object : TypeToken<ErrorResponse>() {}.type
                        val errorResponse: ErrorResponse =
                            Gson().fromJson(response.errorBody()!!.charStream(), type)
                        onError("Error : ${errorResponse.error.message} ")
                        msgDialog(context, errorResponse.error.message)
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Util.dismissProgress()
                    onError("Error: ${e.message}")
                    msgDialog(context, "Error: ${e.message}")
                }
            }
        }
    }

    fun getCityListApi(
        context: Context,
        isShowProgress: Boolean,
        stateID: String
    ) {
        if (isShowProgress)
            Util.showProgress(context)
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            try {
                val response = apiRepository.getHomeCityListApi(stateID)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        getCityListApiResponse.postValue(response.body())
                        Util.dismissProgress()
                    } else {
                        val type = object : TypeToken<ErrorResponse>() {}.type
                        val errorResponse: ErrorResponse =
                            Gson().fromJson(response.errorBody()!!.charStream(), type)
                        onError("Error : ${errorResponse.error.message} ")
                        msgDialog(context, errorResponse.error.message)
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Util.dismissProgress()
                    onError("Error: ${e.message}")
                    msgDialog(context, "Error: ${e.message}")
                }
            }
        }
    }

    fun getHotelListApi(
        context: Context,
        isShowProgress: Boolean,
        segmentId: String, exploreId: String,
    ) {
        if (isShowProgress)
            Util.showProgress(context)
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            try {
                val response = apiRepository.getHotelListApi(
                    segmentId, exploreId
                )
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        getHotelListApiResponse.postValue(response.body())
                        Util.dismissProgress()
                    } else {
                        val type = object : TypeToken<ErrorResponse>() {}.type
                        val errorResponse: ErrorResponse =
                            Gson().fromJson(response.errorBody()!!.charStream(), type)
                        onError("Error : ${errorResponse.error.message} ")
                        msgDialog(context, errorResponse.error.message)
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Util.dismissProgress()
                    onError("Error: ${e.message}")
                    msgDialog(context, "Error: ${e.message}")
                }
            }
        }
    }


    fun getHotelListingApi(
        context: Context,
        userID: String,
        adults: Int? = null,
        children: Int? = null,
        checkIn: String? = null,
        checkOut: String? = null,
        hotelType: String? = null,
        starRating: Float? = null,
        cityID: String? = null,
        stateID: String? = null,
        brandID: String? = null,
        longitude: Double? = null,
        latitude: Double? = null,
        hotelIds: String? = null,
        amenitiesIDs: String? = null,
        brandIDs: String? = null,
        destinationIDs: String? = null,
        sortBy: String? = null
    ) {
        Util.showProgress(context)
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            try {
                val response = apiRepository.getHotelListingApi(
                    userID, adults, children, checkIn, checkOut, hotelType, starRating, cityID, stateID, brandID, longitude, latitude, hotelIds, amenitiesIDs, brandIDs, destinationIDs, sortBy
                )
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        getHotelListingApiResponse.postValue(response.body())
                        Util.dismissProgress()
                    } else {
                        val type = object : TypeToken<ErrorResponse>() {}.type
                        val errorResponse: ErrorResponse =
                            Gson().fromJson(response.errorBody()!!.charStream(), type)
                        onError("Error : ${errorResponse.error.message} ")
                        msgDialog(context, errorResponse.error.message)
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Util.dismissProgress()
                    onError("Error: ${e.message}")
                    msgDialog(context, "Error: ${e.message}")
                }
            }
        }
    }

    fun getHotelDetailApi(
        context: Context,
        userID: String,
        hotelID: String,
        checkIn: String? = null,
        checkOut: String? = null,
        adults: Int? = null,
        children: Int? = null
    ) {
        Util.showProgress(context)
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            try {
                val response = apiRepository.getHotelDetailApi(
                    userID,hotelID, checkIn, checkOut, adults, children
                )
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        getHotelDetailApiResponse.postValue(response.body())
                        Util.dismissProgress()
                    } else {
                        val type = object : TypeToken<ErrorResponse>() {}.type
                        val errorResponse: ErrorResponse =
                            Gson().fromJson(response.errorBody()!!.charStream(), type)
                        onError("Error : ${errorResponse.error.message} ")
                        msgDialog(context, errorResponse.error.message)

                        Log.d("getProductListApi", "try" + errorResponse.error.message)
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    onError("Error: ${e.message}")
                    msgDialog(context, "Error: ${e.message}")

                    Log.d("getProductListApi", "catch...." + e.message)
                }
            }
        }
    }

    fun getAirportPickApi(
        context: Context,
        hotelID: String,
    ) {
        Util.showProgress(context)
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            try {
                val response = apiRepository.getAirportPickApi(hotelID)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        getAirportPickApiResponse.postValue(response.body())
                        Util.dismissProgress()
                    } else {
                        val type = object : TypeToken<ErrorResponse>() {}.type
                        val errorResponse: ErrorResponse =
                            Gson().fromJson(response.errorBody()!!.charStream(), type)
                        onError("Error : ${errorResponse.error.message} ")
                        msgDialog(context, errorResponse.error.message)

                        Log.d("getProductListApi", "try" + errorResponse.error.message)
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    onError("Error: ${e.message}")
                    msgDialog(context, "Error: ${e.message}")

                    Log.d("getProductListApi", "catch...." + e.message)
                }
            }
        }
    }

    fun callAddPromoCodeApi(
        context: Context,
        addPromoCodeRequest: AddPromoCodeRequest,
    ) {
        Util.showProgress(context)
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            try {
                val response = apiRepository.callAddPromoCodeApi(addPromoCodeRequest)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        callAddPromoCodeApiResponse.postValue(response.body())
                        Util.dismissProgress()
                    } else {
                        val type = object : TypeToken<ErrorResponse>() {}.type
                        val errorResponse: ErrorResponse =
                            Gson().fromJson(response.errorBody()!!.charStream(), type)
                        onError("Error : ${errorResponse.error.message} ")
                        msgDialog(context, errorResponse.error.message)

                        Log.d("getProductListApi", "try" + errorResponse.error.message)
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    onError("Error: ${e.message}")
                    msgDialog(context, "Error: ${e.message}")

                    Log.d("getProductListApi", "catch...." + e.message)
                }
            }
        }
    }

    fun getAddOnExperienceListApi(
        context: Context,
        hotelID: String,
    ) {
        Util.showProgress(context)
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            try {
                val response = apiRepository.getAddOnExperienceListApi(hotelID)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        getAddOnExperienceListApiResponse.postValue(response.body())
                        Util.dismissProgress()
                    } else {
                        val type = object : TypeToken<ErrorResponse>() {}.type
                        val errorResponse: ErrorResponse =
                            Gson().fromJson(response.errorBody()!!.charStream(), type)
                        onError("Error : ${errorResponse.error.message} ")
                        msgDialog(context, errorResponse.error.message)

                        Log.d("getProductListApi", "try" + errorResponse.error.message)
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    onError("Error: ${e.message}")
                    msgDialog(context, "Error: ${e.message}")

                    Log.d("getProductListApi", "catch...." + e.message)
                }
            }
        }
    }

    fun getBookingListApi(
        context: Context,
        page: Int,
        sizePerPage: Int? = null,
        status: String? = null,
    ) {
        Util.showProgress(context)
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            try {
                val response = apiRepository.getBookingListApi(
                    page,sizePerPage,status
                )
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        getBookingListApiResponse.postValue(response.body())
                        Util.dismissProgress()
                    } else {
                        val type = object : TypeToken<ErrorResponse>() {}.type
                        val errorResponse: ErrorResponse =
                            Gson().fromJson(response.errorBody()!!.charStream(), type)
                        onError("Error : ${errorResponse.error.message} ")
                        msgDialog(context, errorResponse.error.message)

                        Log.d("getBookListApi", "try" + errorResponse.error.message)
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    onError("Error: ${e.message}")
                    msgDialog(context, "Error: ${e.message}")

                    Log.d("getProductListApi", "catch...." + e.message)
                }
            }
        }
    }

    fun getNotificationHistoryListApi(
        context: Context,
        page: Int,
        sizePerPage: Int? = null
    ) {
        Util.showProgress(context)
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            try {
                val response = apiRepository.getNotificationHistoryListApi(
                    page,sizePerPage
                )
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        getNotificationHistoryListApiResponse.postValue(response.body())
                        Util.dismissProgress()
                    } else {
                        val type = object : TypeToken<ErrorResponse>() {}.type
                        val errorResponse: ErrorResponse =
                            Gson().fromJson(response.errorBody()!!.charStream(), type)
                        onError("Error : ${errorResponse.error.message} ")
                        msgDialog(context, errorResponse.error.message)

                        Log.d("getBookListApi", "try" + errorResponse.error.message)
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    onError("Error: ${e.message}")
                    msgDialog(context, "Error: ${e.message}")

                    Log.d("getProductListApi", "catch...." + e.message)
                }
            }
        }
    }

    fun callNotificationDeleteApi(
        context: Context,
        actionType: MultipartBody.Part,
        notificationID: MultipartBody.Part
    ) {
        Util.showProgress(context)
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            try {
                val response = apiRepository.callNotificationDeleteApi(
                   actionType,notificationID
                )
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        callNotificationDeleteApiResponse.postValue(response.body())
                        Util.dismissProgress()
                    } else {
                        val type = object : TypeToken<ErrorResponse>() {}.type
                        val errorResponse: ErrorResponse =
                            Gson().fromJson(response.errorBody()!!.charStream(), type)
                        onError("Error : ${errorResponse.error.message} ")
                        msgDialog(context, errorResponse.error.message)

                        Log.d("getBookListApi", "try" + errorResponse.error.message)
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    onError("Error: ${e.message}")
                    msgDialog(context, "Error: ${e.message}")

                    Log.d("getProductListApi", "catch...." + e.message)
                }
            }
        }
    }

    fun callAllNotificationDeleteApi(
        context: Context,
        actionType: MultipartBody.Part,
    ) {
        Util.showProgress(context)
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            try {
                val response = apiRepository.callAllNotificationDeleteApi(
                   actionType
                )
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        callAllNotificationDeleteApiResponse.postValue(response.body())
                        Util.dismissProgress()
                    } else {
                        val type = object : TypeToken<ErrorResponse>() {}.type
                        val errorResponse: ErrorResponse =
                            Gson().fromJson(response.errorBody()!!.charStream(), type)
                        onError("Error : ${errorResponse.error.message} ")
                        msgDialog(context, errorResponse.error.message)

                        Log.d("getBookListApi", "try" + errorResponse.error.message)
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    onError("Error: ${e.message}")
                    msgDialog(context, "Error: ${e.message}")

                    Log.d("getProductListApi", "catch...." + e.message)
                }
            }
        }
    }

    fun getWishListApi(
        context: Context,
        actionType : MultipartBody.Part
    ) {
        Util.showProgress(context)
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            try {
                val response = apiRepository.getWishListApi(actionType)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        getWishListApiResponse.postValue(response.body())
                        Util.dismissProgress()
                    } else {
                        val type = object : TypeToken<ErrorResponse>() {}.type
                        val errorResponse: ErrorResponse =
                            Gson().fromJson(response.errorBody()!!.charStream(), type)
                        onError("Error : ${errorResponse.error.message} ")
                        msgDialog(context, errorResponse.error.message)

                        Log.d("getBookListApi", "try" + errorResponse.error.message)
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    onError("Error: ${e.message}")
                    msgDialog(context, "Error: ${e.message}")

                    Log.d("getProductListApi", "catch...." + e.message)
                }
            }
        }
    }

    fun deleteWishListApi(
        context: Context,
        actionType : MultipartBody.Part,
        hotelId : MultipartBody.Part,
    ) {
        Util.showProgress(context)
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            try {
                val response = apiRepository.deleteWishListApi(actionType,hotelId)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        deleteWishListApiResponse.postValue(response.body())
                        Util.dismissProgress()
                    } else {
                        val type = object : TypeToken<ErrorResponse>() {}.type
                        val errorResponse: ErrorResponse =
                            Gson().fromJson(response.errorBody()!!.charStream(), type)
                        onError("Error : ${errorResponse.error.message} ")
                        msgDialog(context, errorResponse.error.message)

                        Log.d("getBookListApi", "try" + errorResponse.error.message)
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    onError("Error: ${e.message}")
                    msgDialog(context, "Error: ${e.message}")

                    Log.d("getProductListApi", "catch...." + e.message)
                }
            }
        }
    }

    fun getMyPreferencesListApi(
        context: Context
    ) {
        Util.showProgress(context)
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            try {
                val response = apiRepository.getMyPreferencesListApi()
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        getMyPreferencesListApiResponse.postValue(response.body())
                        Util.dismissProgress()
                    } else {
                        val type = object : TypeToken<ErrorResponse>() {}.type
                        val errorResponse: ErrorResponse =
                            Gson().fromJson(response.errorBody()!!.charStream(), type)
                        onError("Error : ${errorResponse.error.message} ")
                        msgDialog(context, errorResponse.error.message)

                        Log.d("getBookListApi", "try" + errorResponse.error.message)
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    onError("Error: ${e.message}")
                    msgDialog(context, "Error: ${e.message}")

                    Log.d("getProductListApi", "catch...." + e.message)
                }
            }
        }
    }

    fun getTransactionHistoryApi(
        context: Context
    ) {
        Util.showProgress(context)
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            try {
                val response = apiRepository.getTransactionHistoryApi()
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        getTransactionHistoryApiResponse.postValue(response.body())
                        Util.dismissProgress()
                    } else {
                        val type = object : TypeToken<ErrorResponse>() {}.type
                        val errorResponse: ErrorResponse =
                            Gson().fromJson(response.errorBody()!!.charStream(), type)
                        onError("Error : ${errorResponse.error.message} ")
                        msgDialog(context, errorResponse.error.message)

                        Log.d("getBookListApi", "try" + errorResponse.error.message)
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    onError("Error: ${e.message}")
                    msgDialog(context, "Error: ${e.message}")

                    Log.d("getProductListApi", "catch...." + e.message)
                }
            }
        }
    }

    fun setMyPreferencesApi(
        context: Context,
        requestBody: PreferenceRequest
    ) {
        Util.showProgress(context)
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            try {
                val response = apiRepository.setMyPreferencesApi(requestBody)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        setMyPreferencesApiResponse.postValue(response.body())
                        Util.dismissProgress()
                    } else {
                        val type = object : TypeToken<ErrorResponse>() {}.type
                        val errorResponse: ErrorResponse =
                            Gson().fromJson(response.errorBody()!!.charStream(), type)
                        onError("Error : ${errorResponse.error.message} ")
                        msgDialog(context, errorResponse.error.message)

                        Log.d("getBookListApi", "try" + errorResponse.error.message)
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    onError("Error: ${e.message}")
                    msgDialog(context, "Error: ${e.message}")

                    Log.d("getProductListApi", "catch...." + e.message)
                }
            }
        }
    }

    fun generateHashApi(
        context: Context,
        generateHashRequest: GenerateHashRequest
    ) {
        Util.showProgress(context)
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            try {
                val response = apiRepository.generateHashApi(generateHashRequest)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        generateHashApiResponse.postValue(response.body())
                        Util.dismissProgress()
                    } else {
                        val type = object : TypeToken<ErrorResponse>() {}.type
                        val errorResponse: ErrorResponse =
                            Gson().fromJson(response.errorBody()!!.charStream(), type)
                        onError("Error : ${errorResponse.error.message} ")
                        msgDialog(context, errorResponse.error.message)

                        Log.d("getBookListApi", "try" + errorResponse.error.message)
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    onError("Error: ${e.message}")
                    msgDialog(context, "Error: ${e.message}")

                    Log.d("getProductListApi", "catch...." + e.message)
                }
            }
        }
    }

    fun getOfferRoomPriceApi(
        context: Context,
        hotelID: String,
        checkIn: String? = null,
        checkOut: String? = null,
        adults: Int? = null,
        children: Int? = null
    ) {
        Util.showProgress(context)
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            try {
                val response = apiRepository.getOfferRoomPriceApi(hotelID, checkIn, checkOut, adults, children)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        getOfferRoomPriceApiResponse.postValue(response.body())
                        Util.dismissProgress()
                    } else {
                        val type = object : TypeToken<ErrorResponse>() {}.type
                        val errorResponse: ErrorResponse =
                            Gson().fromJson(response.errorBody()!!.charStream(), type)
                        onError("Error : ${errorResponse.error.message} ")
                        msgDialog(context, errorResponse.error.message)

                        Log.d("getProductListApi", "try" + errorResponse.error.message)
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    onError("Error: ${e.message}")
                    msgDialog(context, "Error: ${e.message}")

                    Log.d("getProductListApi", "catch...." + e.message)
                }
            }
        }
    }

    fun getTaxPercentageApi(
        context: Context,
        roomPrice: Double,
    ) {
        Util.showProgress(context)
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            try {
                val response = apiRepository.getTaxPercentageApi(roomPrice)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        getTaxPercentageApiResponse.postValue(response.body())
                        Util.dismissProgress()
                    } else {
                        val type = object : TypeToken<ErrorResponse>() {}.type
                        val errorResponse: ErrorResponse =
                            Gson().fromJson(response.errorBody()!!.charStream(), type)
                        onError("Error : ${errorResponse.error.message} ")
                        msgDialog(context, errorResponse.error.message)

                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    onError("Error: ${e.message}")
                    msgDialog(context, "Error: ${e.message}")

                    Log.d("getProductListApi", "catch...." + e.message)
                }
            }
        }
    }


    fun getDestinationListApi(
        context: Context,
    ) {
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            try {
                val response = apiRepository.getDestinationListApi(
                )
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        getDestinationListApiResponse.postValue(response.body())
                    } else {
                        val type = object : TypeToken<ErrorResponse>() {}.type
                        val errorResponse: ErrorResponse =
                            Gson().fromJson(response.errorBody()!!.charStream(), type)
                        //onError("Error : ${errorResponse.error.message} ")
                        //msgDialog(context, errorResponse.error.message)
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    //onError("Error: ${e.message}")
                    //msgDialog(context, "Error: ${e.message}")
                }
            }
        }
    }

    fun getBrandListApi(
        context: Context,
    ) {
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            try {
                val response = apiRepository.getBrandListApi(
                )
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        getBrandListApiResponse.postValue(response.body())
                    } else {
                        val type = object : TypeToken<ErrorResponse>() {}.type
                        val errorResponse: ErrorResponse =
                            Gson().fromJson(response.errorBody()!!.charStream(), type)
                        //onError("Error : ${errorResponse.error.message} ")
                        //msgDialog(context, errorResponse.error.message)
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    //onError("Error: ${e.message}")
                    //msgDialog(context, "Error: ${e.message}")
                }
            }
        }
    }

    fun getAmenitiesListApi(
        context: Context,
    ) {
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            try {
                val response = apiRepository.getAmenitiesListApi(
                )
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        getAmenitiesListApiResponse.postValue(response.body())
                    } else {
                        val type = object : TypeToken<ErrorResponse>() {}.type
                        val errorResponse: ErrorResponse =
                            Gson().fromJson(response.errorBody()!!.charStream(), type)
                        //onError("Error : ${errorResponse.error.message} ")
                        //msgDialog(context, errorResponse.error.message)
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    //onError("Error: ${e.message}")
                    //msgDialog(context, "Error: ${e.message}")
                }
            }
        }
    }

    fun getNearByPlaceListApi(
        context: Context,
        hotelID: String,
    ) {
        Util.showProgress(context)
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            try {
                val response = apiRepository.getNearByPlaceListApi(hotelID)
                withContext(Dispatchers.Main) {
                    Util.dismissProgress()

                    if (response.isSuccessful) {
                        getNearByPlaceListApiResponse.postValue(response.body())
                    } else {
                        val type = object : TypeToken<ErrorResponse>() {}.type
                        val errorResponse: ErrorResponse =
                            Gson().fromJson(response.errorBody()!!.charStream(), type)
                        onError("Error : ${errorResponse.error.message} ")
                        msgDialog(context, errorResponse.error.message)
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    onError("Error: ${e.message}")
                    msgDialog(context, "Error: ${e.message}")
                }
            }
        }
    }

    fun callBookingHotelApi(
        context: Context,
        bookingHotelRequest: BookingHotelRequest,
    ) {
        Util.showProgress(context)
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            try {
                val response = apiRepository.callBookingHotelApi(bookingHotelRequest)
                withContext(Dispatchers.Main) {
                    Util.dismissProgress()

                    if (response.isSuccessful) {
                        callBookingHotelApiResponse.postValue(response.body())
                    } else {
                        val type = object : TypeToken<ErrorResponse>() {}.type
                        val errorResponse: ErrorResponse =
                            Gson().fromJson(response.errorBody()!!.charStream(), type)
                        onError("Error : ${errorResponse.error.message} ")
                        msgDialog(context, errorResponse.error.message)
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    onError("Error: ${e.message}")
                    msgDialog(context, "Error: ${e.message}")
                }
            }
        }
    }

    fun getBookTableApi(
        context: Context,
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
    ) {
        Util.showProgress(context)
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            try {
                val response = apiRepository.getBookTableApi(
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
                withContext(Dispatchers.Main) {
                    Util.dismissProgress()

                    if (response.isSuccessful) {
                        getBookTableApiResponse.postValue(response.body())
                    } else {
                        val type = object : TypeToken<ErrorResponse>() {}.type
                        val errorResponse: ErrorResponse =
                            Gson().fromJson(response.errorBody()!!.charStream(), type)
                        onError("Error : ${errorResponse.error.message} ")
                        msgDialog(context, errorResponse.error.message)
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    onError("Error: ${e.message}")
                    msgDialog(context, "Error: ${e.message}")
                }
            }
        }
    }

    fun getBookEventBanquetApi(
        context: Context,
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
    ) {
        Util.showProgress(context)
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            try {
                val response = apiRepository.getBookEventBanquetApi(
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
                withContext(Dispatchers.Main) {
                    Util.dismissProgress()

                    if (response.isSuccessful) {
                        getBookEventBanquetApiResponse.postValue(response.body())
                    } else {
                        val type = object : TypeToken<ErrorResponse>() {}.type
                        val errorResponse: ErrorResponse =
                            Gson().fromJson(response.errorBody()!!.charStream(), type)
                        onError("Error : ${errorResponse.error.message} ")
                        msgDialog(context, errorResponse.error.message)
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    onError("Error: ${e.message}")
                    msgDialog(context, "Error: ${e.message}")
                }
            }
        }
    }

    private fun onError(message: String) {
        Log.d("TAG", "onError: MESSAGE :: >> $message")
        Util.dismissProgress()
    }

    var timer: CountDownTimer? = null

    fun msgDialog(context: Context, msg: String, msgType: String = "error") {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val dialogValidationMessageBinding = DialogValidationMessageBinding.inflate(
                    LayoutInflater.from(context)
                )
                val messageDialog = Dialog(context)

                messageDialog.setContentView(dialogValidationMessageBinding.root)

                messageDialog.window!!.setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                messageDialog.window!!.setGravity(Gravity.TOP)
                messageDialog.window!!.setWindowAnimations(R.style.DialogMessageAnimation)
                messageDialog.setCancelable(true)

                dialogValidationMessageBinding.tvMessage.text = msg

                if (msgType == "success") {
                    dialogValidationMessageBinding.mainLayout.setBackgroundColor(
                        ContextCompat.getColor(
                            context,
                            R.color.colorWhite
                        )
                    )
                    dialogValidationMessageBinding.tvMessage.setTextColor(
                        ContextCompat.getColor(
                            context,
                            R.color.colorBlack
                        )
                    )
                } else {
                    dialogValidationMessageBinding.mainLayout.setBackgroundColor(
                        ContextCompat.getColor(
                            context,
                            R.color.colorRed
                        )
                    )
                    dialogValidationMessageBinding.tvMessage.setTextColor(
                        ContextCompat.getColor(
                            context,
                            R.color.colorWhite
                        )
                    )
                }

                messageDialog.show()

                timer = object : CountDownTimer(3000, 1000) {
                    override fun onTick(millisUntilFinished: Long) {
                        //No operation required
                    }

                    override fun onFinish() {
                        timer?.cancel()

                        try {
                            if (messageDialog.isShowing) messageDialog.dismiss()
                        } catch (e: java.lang.Exception) {
                            e.printStackTrace()
                        }

                    }
                }.start()

            } catch (e: NullPointerException) {
                e.printStackTrace()
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
        timer?.cancel()
    }

}