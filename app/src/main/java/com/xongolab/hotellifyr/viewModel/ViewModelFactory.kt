package com.xongolab.hotellifyr.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.xongolab.hotellifyr.idrequest.ApiRepository

@Suppress("UNCHECKED_CAST")
class ViewModelFactory constructor(private val apiRepository: ApiRepository) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            UserViewModel(this.apiRepository) as T
        }else if (modelClass.isAssignableFrom(HotelViewModel::class.java)) {
            HotelViewModel(this.apiRepository) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }

}