package com.xongolab.hotellifyr.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class VerifyOTPRequest() {

    @Expose
    @SerializedName("otpID")
    var otpID: String = ""

    @Expose
    @SerializedName("otp")
    var otp: String = ""

    @Expose
    @SerializedName("deviceDetails")
    var deviceDetails: DeviceDetails = DeviceDetails()
}

class VerifyBookOTPRequest() {

    @Expose
    @SerializedName("otpID")
    var otpID: String = ""

    @Expose
    @SerializedName("otp")
    var otp: String = ""
}

class SignUpRequest() {

    @Expose
    @SerializedName("FirstName")
    var firstName: String = ""

    @Expose
    @SerializedName("LastName")
    var lastName: String = ""

    @Expose
    @SerializedName("MobileCountryCode")
    var mobileCountryCode: String = ""

    @Expose
    @SerializedName("Mobile")
    var mobile: String = ""

    @Expose
    @SerializedName("Email")
    var email: String = ""

    @Expose
    @SerializedName("deviceDetails")
    var deviceDetails: DeviceDetails = DeviceDetails()

}

class DeviceDetails() {

    @Expose
    @SerializedName("deviceName")
    var deviceName: String = ""

    @Expose
    @SerializedName("deviceType")
    var deviceType: String = ""

    @Expose
    @SerializedName("deviceToken")
    var deviceToken: String = ""

    @Expose
    @SerializedName("deviceID")
    var deviceID: String = ""

    @Expose
    @SerializedName("appVersion")
    var appVersion: String = ""

}

class BookTableRequest() {
    var restaurantID: String = ""
    var hotelID: String = ""
    var restaurantName: String = ""
    var bookingDate: String = ""
    var bookingTime: String = ""
    var diningType: String = ""
    var numberOfGuests: String = ""
}



