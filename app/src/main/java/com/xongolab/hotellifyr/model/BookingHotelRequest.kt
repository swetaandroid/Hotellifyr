package com.xongolab.hotellifyr.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class BookingHotelRequest() : Serializable {

    @Expose
    @SerializedName("customerID")
    var customerID : String = ""
    @Expose
    @SerializedName("memberType")
    var memberType : String = ""
    @Expose
    @SerializedName("hotelID")
    var hotelID : String = ""
    @Expose
    @SerializedName("hotel")
    var hotel: String = ""
    @Expose
    @SerializedName("bookRoom")
    lateinit var bookRoom : ArrayList<BookRoomRequest>
    @Expose
    @SerializedName("checkIn")
    var checkIn : String = ""
    @Expose
    @SerializedName("checkOut")
    var checkOut : String = ""
    @Expose
    @SerializedName("numberOfDays")
    var numberOfDays : Int = 0
    @Expose
    @SerializedName("totalRoomPrice")
    var totalRoomPrice : Double = 0.0
    @Expose
    @SerializedName("totalAddOnPrice")
    var totalAddOnPrice : Double = 0.0
    @Expose
    @SerializedName("taxAmount")
    var taxAmount : Double = 0.0
    @Expose
    @SerializedName("redeemPoints")
    var redeemPoints : Long = 0L
    @Expose
    @SerializedName("redeemTransactionCode")
    var redeemTransactionCode : String = ""
    @Expose
    @SerializedName("totalPayableAmount")
    var totalPayableAmount : Double = 0.0
    @Expose
    @SerializedName("paymentOption")
    var paymentOption : String = ""
    @Expose
    @SerializedName("paymentStatus")
    var paymentStatus : String = ""
    @Expose
    @SerializedName("bookingBy")
    var bookingBy : String = ""
    @Expose
    @SerializedName("primaryGuestDetail")
    var primaryGuestDetail : PrimaryGuestDetailRequest = PrimaryGuestDetailRequest()
    @Expose
    @SerializedName("airportPickupDetail")
    var airportPickupDetail : AirportPickupDetailRequest? = null
    @Expose
    @SerializedName("addOnExperience")
    var addOnExperience : ArrayList<AddOnExperienceRequest> = arrayListOf()
    @Expose
    @SerializedName("couponDetail")
    var couponDetail : CouponDetailRequest? = null
    @Expose
    @SerializedName("paymentDetails")
    var paymentDetails : String = ""
   // var paymentDetails : PayuResponseModel = PayuResponseModel()
}

class PayuResponseModel() : Serializable{
    @Expose
    @SerializedName("id")
    var id : Long = 0L
    @Expose
    @SerializedName("mode")
    var mode : String = ""
    @Expose
    @SerializedName("status")
    var status : String = ""
    @Expose
    @SerializedName("unmappedstatus")
    var unmappedstatus : String = ""
    @Expose
    @SerializedName("key")
    var key : String = ""
    @Expose
    @SerializedName("txnid")
    var txnid : String = ""
    @Expose
    @SerializedName("transaction_fee")
    var transaction_fee : String = ""
    @Expose
    @SerializedName("amount")
    var amount : String = ""
    @Expose
    @SerializedName("cardCategory")
    var cardCategory : String = ""
    @Expose
    @SerializedName("discount")
    var discount : String = ""
    @Expose
    @SerializedName("addedon")
    var addedon : String = ""
    @Expose
    @SerializedName("productinfo")
    var productinfo : String = ""
    @Expose
    @SerializedName("firstname")
    var firstname : String = ""
    @Expose
    @SerializedName("email")
    var email : String = ""
    @Expose
    @SerializedName("phone")
    var phone : String = ""
    @Expose
    @SerializedName("hash")
    var hash : String = ""
    @Expose
    @SerializedName("field1")
    var field1 : Long = 0L
    @Expose
    @SerializedName("field2")
    var field2 : Long = 0L
    @Expose
    @SerializedName("field3")
    var field3 : String = ""
    @Expose
    @SerializedName("field5")
    var field5 : String = ""
    @Expose
    @SerializedName("field6")
    var field6 : String = ""
    @Expose
    @SerializedName("field7")
    var field7 : String = ""
    @Expose
    @SerializedName("field8")
    var field8 : String = ""
    @Expose
    @SerializedName("field9")
    var field9 : String = ""
    @Expose
    @SerializedName("payment_source")
    var payment_source : String = ""
    @Expose
    @SerializedName("PG_TYPE")
    var PG_TYPE : String = ""
    @Expose
    @SerializedName("bank_ref_no")
    var bank_ref_no : String = ""
    @Expose
    @SerializedName("ibibo_code")
    var ibibo_code : String = ""
    @Expose
    @SerializedName("error_code")
    var error_code : String = ""
    @Expose
    @SerializedName("Error_Message")
    var Error_Message : String = ""
    @Expose
    @SerializedName("card_no")
    var card_no : String = ""
    @Expose
    @SerializedName("is_seamless")
    var is_seamless : Int = 0
    @Expose
    @SerializedName("surl")
    var surl : String = ""
    @Expose
    @SerializedName("furl")
    var furl : String = ""
}
class PaymentDetailsRequest() : Serializable {
    @Expose
    @SerializedName("status")
    var status : String = ""
    @Expose
    @SerializedName("txnid")
    var txnid : String = ""
    @Expose
    @SerializedName("amount")
    var amount : String = ""
    @Expose
    @SerializedName("payuMoneyId")
    var payuMoneyId : String = ""
    @Expose
    @SerializedName("mode")
    var mode : String = ""
    /*@Expose
    @SerializedName("bankRefNum")
    var bankRefNum : String = ""
    @Expose
    @SerializedName("bankCode")
    var bankCode : String = ""
    @Expose
    @SerializedName("cardMask")
    var cardMask : String = ""
    @Expose
    @SerializedName("timestamp")
    var timestamp : String = ""
    @Expose
    @SerializedName("productInfo")
    var productInfo : String = ""
    @Expose
    @SerializedName("firstName")
    var firstName : String = ""
    @Expose
    @SerializedName("paymentSource")
    var paymentSource : String = ""
    @Expose
    @SerializedName("unmappedStatus")
    var unmappedStatus : String = ""
    @Expose
    @SerializedName("pgType")
    var pgType : String = ""
    @Expose
    @SerializedName("netAmountDebit")
    var netAmountDebit : Double = 0.0*/
}

class CouponDetailRequest() : Serializable {
    @Expose
    @SerializedName("couponID")
    var couponID : String = ""
    @Expose
    @SerializedName("couponCode")
    var couponCode : String = ""
    @Expose
    @SerializedName("discountType")
    var discountType : String = ""
    @Expose
    @SerializedName("value")
    var couponValue : Long = 0L
    @Expose
    @SerializedName("discountValue")
    var discountValue : Double = 0.0
    @Expose
    @SerializedName("title")
    var titleEN : TitleEN = TitleEN()
}

class TitleEN() : Serializable{
    @Expose
    @SerializedName("EN")
    var EN : String = ""
}

class AddOnExperienceRequest() : Serializable {
    @Expose
    @SerializedName("numberOfPerson")
    var numberOfPerson : Long = 0L
    @Expose
    @SerializedName("bookingDate")
    var bookingDate : String = ""
    @Expose
    @SerializedName("addOnID")
    var addOnID : String = ""
    @Expose
    @SerializedName("addOnTitle")
    var addOnTitle : String = ""
    @Expose
    @SerializedName("icon")
    var icon : String = ""
    @Expose
    @SerializedName("title")
    var title : String = ""
    @Expose
    @SerializedName("singlePrice")
    var singlePrice : Double = 0.0
    @Expose
    @SerializedName("price")
    var price : Double = 0.0
    @Expose
    @SerializedName("package")
    var packageTitle : String = ""
    @Expose
    @SerializedName("packageID")
    var packageID : String = ""
    @Expose
    @SerializedName("description")
    var description : String = ""
}

class AirportPickupDetailRequest() : Serializable {
    @Expose
    @SerializedName("vehicleID")
    var vehicleID : String = ""
    @Expose
    @SerializedName("from")
    var from : String = ""
    @Expose
    @SerializedName("flightNumber")
    var flightNumber : String = ""
    @Expose
    @SerializedName("vehicle")
    var vehicle : String = ""
    @Expose
    @SerializedName("flightDate")
    var flightDate : String = ""
    @Expose
    @SerializedName("seater")
    var seater : Int = 0
    @Expose
    @SerializedName("price")
    var price : Long = 0L
}

class PrimaryGuestDetailRequest() : Serializable {
    @Expose
    @SerializedName("FirstName")
    var FirstName : String = ""
    @Expose
    @SerializedName("LastName")
    var LastName : String = ""
    @Expose
    @SerializedName("Email")
    var Email : String = ""
    @Expose
    @SerializedName("MobileCountryCode")
    var MobileCountryCode : String = ""
    @Expose
    @SerializedName("Mobile")
    var Mobile : String = ""
    @Expose
    @SerializedName("GSTNo")
    var GSTNo : String = ""
    @Expose
    @SerializedName("specialRequest")
    var specialRequest : String = ""
}

class BookRoomRequest() : Serializable {

    @Expose
    @SerializedName("roomIndex")
    var roomIndex : Long = 0L
    @Expose
    @SerializedName("adults")
    var adults : Int = 0
    @Expose
    @SerializedName("children")
    var children : Int = 0
    @Expose
    @SerializedName("roomID")
    var roomID : String = ""
    @Expose
    @SerializedName("roomCode")
    var roomCode : String = ""
    @Expose
    @SerializedName("rateTitle")
    var rateTitle : String = ""
    @Expose
    @SerializedName("rateCode")
    var rateCode : String = ""
    @Expose
    @SerializedName("roomTitle")
    var roomTitle : String = ""
    @Expose
    @SerializedName("selectedRate")
    var selectedRate : String = ""
    @Expose
    @SerializedName("price")
    var price : Double = 0.0
    @Expose
    @SerializedName("totalPrice")
    var totalPrice : Double = 0.0
    @Expose
    @SerializedName("taxPercentage")
    var taxPercentage : Int = 0
    @Expose
    @SerializedName("dayPrice")
    var dayPrice : ArrayList<DayPriceRequest> = arrayListOf()
    @Expose
    @SerializedName("taxAmount")
    var taxAmount : Double = 0.0
    var roomRatePerDay : Double = 0.0
    var hotelImageURL : String = ""

}

class DayPriceRequest() : Serializable{
    @Expose
    @SerializedName("day")
    var day : String = ""
    @Expose
    @SerializedName("Price")
    var price : Double = 0.0
    @Expose
    @SerializedName("Tax")
    var tax : Double = 0.0
    @Expose
    @SerializedName("TaxPrice")
    var taxPrice : Double = 0.0
}

class GenerateHashRequest() : Serializable {
    /*@Expose
    @SerializedName("key")
    var key: String = ""
    @Expose
    @SerializedName("txnid")
    var txnid: String = ""
    @Expose
    @SerializedName("amount")
    var amount: String = ""
    @Expose
    @SerializedName("firstname")
    var firstname: String = ""
    @Expose
    @SerializedName("email")
    var email: String = ""
    @Expose
    @SerializedName("phone")
    var phone: String = ""
    @Expose
    @SerializedName("productinfo")
    var productinfo: String = ""
    @Expose
    @SerializedName("surl")
    var surl: String = ""
    @Expose
    @SerializedName("furl")
    var furl: String = ""*/
    @Expose
    @SerializedName("hashString")
    var hashString : String = ""

}

class PreferenceRequest() : Serializable{

    @Expose
    @SerializedName("specialRequest")
    var specialRequest : String = ""
    @Expose
    @SerializedName("preferences")
    var preferences : ArrayList<String> = arrayListOf()
}

class AddPromoCodeRequest() : Serializable{

    @Expose
    @SerializedName("Mobile")
    var mobile : String = ""
    @Expose
    @SerializedName("couponCode")
    var couponCode : String = ""
    @Expose
    @SerializedName("customerID")
    var customerID : String = ""
    @Expose
    @SerializedName("hotelID")
    var hotelID : String = ""
    @Expose
    @SerializedName("amount")
    var amount : Double = 0.0

}