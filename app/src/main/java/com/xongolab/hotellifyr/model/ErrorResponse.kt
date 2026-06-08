package com.xongolab.hotellifyr.model

import com.google.gson.annotations.SerializedName

data class ErrorResponse(
    @SerializedName("error")
    val error: Error = Error()
) {
    data class Error(
        @SerializedName("message")
        val message: String = ""
    )
}