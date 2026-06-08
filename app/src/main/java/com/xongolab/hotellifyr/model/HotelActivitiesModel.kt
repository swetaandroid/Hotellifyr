package com.xongolab.hotellifyr.model


import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose


data class HotelActivitiesModel(
    @SerializedName("description")
    @Expose
    val description: String?,
    @SerializedName("icon")
    @Expose
    val icon: String?,
    @SerializedName("_id")
    @Expose
    val id: String?,
    @SerializedName("locatedAt")
    @Expose
    val locatedAt: String?,
    @SerializedName("title")
    @Expose
    val title: String?,

   // Nearest Destination
    @SerializedName("km")
    @Expose
    val km: String?,


)