package com.xongolab.hotellifyr.idrequest

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResponseModel<T> {

    @Expose
    @SerializedName("payload")
    var payload: T? = null

}