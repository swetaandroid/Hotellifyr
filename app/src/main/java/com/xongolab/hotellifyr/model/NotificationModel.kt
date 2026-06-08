package com.xongolab.hotellifyr.model

import com.google.gson.annotations.SerializedName
import com.xongolab.hotellifyr.model.BookingHistoryModel.BookingData

data class NotificationModel(
    @SerializedName("total")
    val total: Long = 0,
    @SerializedName("data")
    var data: ArrayList<NotificationData> = arrayListOf(),
){
    data class NotificationData(
        @SerializedName("_id")
        var _id : String = "",
        @SerializedName("title")
        var title : String = "",
        @SerializedName("description")
        var description : String = "",
        @SerializedName("createdAt")
        var createdAt : String = "",
    ){}
}