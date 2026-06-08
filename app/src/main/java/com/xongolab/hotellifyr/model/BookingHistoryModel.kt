package com.xongolab.hotellifyr.model

import com.google.gson.annotations.SerializedName

data class BookingHistoryModel(
    @SerializedName("total")
    val total: Long = 0,
    @SerializedName("data")
    var data: ArrayList<BookingData> = arrayListOf(),
) {
    data class BookingData(
        @SerializedName("_id")
        val _id : String = "",
        @SerializedName("hotelID")
        val hotelID : String = "",
        @SerializedName("hotel")
        val hotel :String = "",
        @SerializedName("memberType")
        val memberType : String = "",
        @SerializedName("customerID")
        val customerID : String = "",
        @SerializedName("primaryGuestDetail")
        val primaryGuestDetail : PrimaryGuestDetail = PrimaryGuestDetail(),
        @SerializedName("hotelDetails")
        val hotelDetails : HotelDetails = HotelDetails(),
        @SerializedName("checkIn")
        val checkIn: String = "",
        @SerializedName("checkOut")
        val checkOut: String = "",
        @SerializedName("numberOfDays")
        val numberOfDays : Long = 0,
        @SerializedName("bookRoom")
        val bookRoom : ArrayList<BookRoom> = arrayListOf(),
   /*     @SerializedName("addOnExperience")
        val addOnExperience : ArrayList<AddOnExperience> = arrayListOf(),*/
        @SerializedName("totalRoomPrice")
        val totalRoomPrice: Long = 0,
        @SerializedName("totalAddOnPrice")
        val totalAddOnPrice: Long = 0,
        @SerializedName("taxAmount")
        val taxAmount: Double = 0.0,
        @SerializedName("totalPayableAmount")
        val totalPayableAmount: Double = 0.0,
        @SerializedName("paymentOption")
        val paymentOption: String = "",
        @SerializedName("paymentStatus")
        val paymentStatus: String = "",
        @SerializedName("status")
        val status: String = "",
        @SerializedName("createdAt")
        val createdAt: String = "",
        @SerializedName("updatedAt")
        val updatedAt: String = "",
        @SerializedName("bookingStatus")
        val bookingStatus: String = "",
        @SerializedName("isFavorite")
        val isFavorite: Boolean = false,
        )

    data class PrimaryGuestDetail(
        @SerializedName("FirstName")
        val FirstName : String = "",
        @SerializedName("LastName")
        val LastName: String = "",
        @SerializedName("Email")
        val Email : String = "",
        @SerializedName("MobileCountryCode")
        val MobileCountryCode : String = "",
        @SerializedName("Mobile")
        val Mobile : String = "",
    )

    data class BookRoom(
        @SerializedName("roomIndex")
        val roomIndex : Long = 0,
        @SerializedName("adults")
        val adults : Long = 0,
        @SerializedName("children")
        val children: Long = 0,
        @SerializedName("roomID")
        val roomID: String = "",
        @SerializedName("hlRoomID")
        val hlRoomID: String = "",
        @SerializedName("roomCode")
        val roomCode: String = "",
        @SerializedName("rateCode")
        val rateCode: String = "",
        @SerializedName("roomTitle")
        val roomTitle: String = "",
        @SerializedName("price")
        val price: Long = 0,
        @SerializedName("totalPrice")
        val totalPrice : Long = 0,
        @SerializedName("taxPercentage")
        val taxPercentage: Long = 0,
        @SerializedName("taxAmount")
        val taxAmount: Double = 0.0,
        @SerializedName("_id")
        val _id: String = ""
    )

    data class HotelDetails(
        @SerializedName("name")
        val name: String = "",
        @SerializedName("address")
        val address: String = "",
        @SerializedName("starRating")
        val starRating: Long = 0,
        @SerializedName("icon")
        val icon: String = "",
//        val icon: ArrayList<String> = arrayListOf(),
    )

}