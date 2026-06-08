package com.xongolab.hotellifyr.idrequest

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResponseArrayModel<T> {

    @Expose
    @SerializedName("payload")
    var payload: ArrayList<T> = ArrayList()
}