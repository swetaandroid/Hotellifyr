package com.xongolab.hotellifyr.model


import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose


data class HotelDiningModel(
    @SerializedName("address")
    @Expose
    val address: String?,
    @SerializedName("close_time")
    @Expose
    val closeTime: String?,
    @SerializedName("cuisineTitles")
    @Expose
    val cuisineTitles: List<String?>?,
    @SerializedName("description")
    @Expose
    val description: String?,
    @SerializedName("hotelID")
    @Expose
    val hotelID: String?,
    @SerializedName("hotelName")
    @Expose
    val hotelName: String?,
    @SerializedName("icon")
    @Expose
    val icon: String?,
    @SerializedName("_id")
    @Expose
    val id: String?,
    @SerializedName("location")
    @Expose
    val location: String?,
    @SerializedName("name")
    @Expose
    val name: String?,
    @SerializedName("open_time")
    @Expose
    val openTime: String?,
    @SerializedName("position")
    @Expose
    val position: Position?,
    @SerializedName("serves")
    @Expose
    val serves: String?
) {
    data class Position(
        @SerializedName("lat")
        @Expose
        val lat: Double?,
        @SerializedName("lng")
        @Expose
        val lng: Double?
    )
}