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
import com.xongolab.hotellifyr.model.ErrorResponse
import com.xongolab.hotellifyr.model.LanguageModel
import com.xongolab.hotellifyr.model.SignUpRequest
import com.xongolab.hotellifyr.model.UserModel
import com.xongolab.hotellifyr.model.VerifyBookOTPRequest
import com.xongolab.hotellifyr.model.VerifyOTPRequest
import com.xongolab.hotellifyr.utils.Pref
import com.xongolab.hotellifyr.utils.Util
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody

class UserViewModel(private val apiRepository: ApiRepository) : ViewModel() {

    private var job: Job? = null
    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        onError("Exception handled: ${throwable.localizedMessage}")
    }


    val getCMSApiResponse = MutableLiveData<ResponseArrayModel<UserModel>>()
    private val getLanguageListApiResponse = MutableLiveData<ResponseArrayModel<LanguageModel>>()
    val getSendOTPApiResponse = MutableLiveData<ResponseModel<UserModel>>()
    val getSendBookOTPApiResponse = MutableLiveData<ResponseModel<UserModel>>()
    val getVerifyOTPApiResponse = MutableLiveData<ResponseModel<UserModel>>()
    val getVerifyBookOTPApiResponse = MutableLiveData<ResponseModel<UserModel>>()
    val getSignupApiResponse = MutableLiveData<ResponseModel<UserModel>>()
    val getCustomerInfoApiResponse = MutableLiveData<ResponseModel<UserModel>>()
    val getCountryListApiResponse = MutableLiveData<ResponseArrayModel<UserModel>>()
    val getStateListApiResponse = MutableLiveData<ResponseArrayModel<UserModel>>()
    val getCityListApiResponse = MutableLiveData<ResponseArrayModel<UserModel>>()
    val updateProfileApiResponse = MutableLiveData<ResponseModel<UserModel>>()
    val updateProfilePhotoApiResponse = MutableLiveData<ResponseModel<UserModel>>()
    val getPaymentApiResponse = MutableLiveData<ResponseModel<UserModel>>()


    fun getCMSApi(context: Context, status: String) {
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            try {
                val response = apiRepository.getCMSApi(status)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        getCMSApiResponse.postValue(response.body())
                    } else {
                        val type = object : TypeToken<ErrorResponse>() {}.type
                        val errorResponse: ErrorResponse = Gson().fromJson(response.errorBody()!!.charStream(), type)
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

    fun getLanguageListApi(context: Context) {
        Util.showProgress(context)
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            try {
                val response = apiRepository.getLanguageListApi()
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        getLanguageListApiResponse.postValue(response.body())
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
                }
            }
        }
    }

    fun getSendOTPApi(
        context: Context,
        mobile: MultipartBody.Part,
        countryCode: MultipartBody.Part,
        type: MultipartBody.Part
    ) {
        Util.showProgress(context)
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            try {
                val response = apiRepository.getSendOTPApi(
                    mobile, countryCode, type
                )
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        getSendOTPApiResponse.postValue(response.body())
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

    fun getSendBookOTPApi(
        context: Context,
        mobile: MultipartBody.Part,
        countryCode: MultipartBody.Part,
        type: MultipartBody.Part
    ) {
        Util.showProgress(context)
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            try {
                val response = apiRepository.getSendBookOTPApi(
                    mobile, countryCode, type
                )
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        getSendBookOTPApiResponse.postValue(response.body())
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

    fun getVerifyOTPApi(
        context: Context,
        verifyOTPRequest: VerifyOTPRequest
    ) {
        Util.showProgress(context)
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            try {
                val response = apiRepository.getVerifyOTPApi(
                    verifyOTPRequest
                )
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        getVerifyOTPApiResponse.postValue(response.body())
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

    fun getVerifyBookOTPApi(
        context: Context,
        verifyOTPRequest: VerifyBookOTPRequest
    ) {
        Util.showProgress(context)
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            try {
                val response = apiRepository.getVerifyBookOTPApi(
                    verifyOTPRequest
                )
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        getVerifyBookOTPApiResponse.postValue(response.body())
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

    fun getSignUpApi(
        context: Context,
        signUpRequest: SignUpRequest
    ) {
        Util.showProgress(context)
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            try {
                val response = apiRepository.getSignUpApi(
                    signUpRequest
                )
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        getSignupApiResponse.postValue(response.body())
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

    fun getCustomerInfoApi(
        context: Context,
    ) {
        Util.showProgress(context)
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            try {
                val response = apiRepository.getCustomerInfoApi()
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        getCustomerInfoApiResponse.postValue(response.body())
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

    fun getCountryListApi(
        context: Context,
    ) {
        Util.showProgress(context)
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            try {
                val response = apiRepository.getCountryListApi()
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        getCountryListApiResponse.postValue(response.body())
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

    fun getStateListApi(
        context: Context,
        countryID: String
    ) {
        Util.showProgress(context)
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            try {
                val response = apiRepository.getStateListApi(countryID)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        getStateListApiResponse.postValue(response.body())
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
        stateID: String
    ) {
            Util.showProgress(context)
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            try {
                val response = apiRepository.getCityListApi(stateID)
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

    fun updateProfileApi(
        context: Context,
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
    ) {
            Util.showProgress(context)
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            try {
                val response = apiRepository.updateProfileApi(firstName, lastName, email, mobile, mobileCountryCode, address, countryID, stateID, cityID, anniversaryDate, dateOfBirth)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        updateProfileApiResponse.postValue(response.body())
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

    fun updateProfilePhotoApi(
        context: Context,
        avatar: MultipartBody.Part,
    ) {
        Util.showProgress(context)
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            try {
                val response = apiRepository.updateProfilePhotoApi(avatar)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        updateProfilePhotoApiResponse.postValue(response.body())
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

    fun getPaymentApi(context: Context, amount: String) {
        Util.showProgress(context)
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            try {
                val params = hashMapOf(
                    "consumer_id" to Pref.getStringValue(Pref.PREF_USER_ID, ""),
                    "amount" to amount,
                )

                val response = apiRepository.getPaymentApi(params)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        getPaymentApiResponse.postValue(response.body())
                        Util.dismissProgress()
                    } else {
                        val type = object : TypeToken<ErrorResponse>() {}.type
                        val errorResponse: ErrorResponse = Gson().fromJson(response.errorBody()!!.charStream(), type)
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