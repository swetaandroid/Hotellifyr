package com.xongolab.hotellifyr.model

import com.google.gson.annotations.SerializedName

data class LanguageModel(
    @SerializedName("code")
    val code: String = "",
    @SerializedName("icon")
    val icon: String = "",
    @SerializedName("_id")
    val id: String = "",
    @SerializedName("status")
    val status: String = "",
    @SerializedName("title")
    val title: String = ""
)