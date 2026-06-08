package com.xongolab.hotellifyr.model

import com.google.gson.annotations.SerializedName

data class TransactionModel(
    @SerializedName("MemberTransactionResponseListDTO")
    var historyData: ArrayList<TransactionData> = arrayListOf()
) {
    data class TransactionData(
        @SerializedName("ReturnCode")
        val ReturnCode : String = "",
        @SerializedName("BillNo")
        val BillNo : String = "",
        @SerializedName("BillDate")
        val BillDate : String = "",
        @SerializedName("TotalBilledAmount")
        val TotalBilledAmount : String = "",
        @SerializedName("Mobile")
        val Mobile : String = "",
        @SerializedName("TotalAccruedPoints")
        val TotalAccruedPoints : String = "",
        @SerializedName("TotalRedeemPoints")
        val TotalRedeemPoints : String = "",
        @SerializedName("EnrolledDate")
        val EnrolledDate : String = "",
        @SerializedName("IsEnableReferralAccruals")
        val IsEnableReferralAccruals : String = "",
        @SerializedName("UserName")
        val UserName : String = "",
        @SerializedName("CardNumber")
        val CardNumber : String = "",
        @SerializedName("IsVoucher")
        val IsVoucher : String = "",
        @SerializedName("IsRefunded")
        val IsRefunded : String = "",
        @SerializedName("Code")
        val Code : String = "",
        @SerializedName("Brand")
        val Brand : String = "",
        @SerializedName("Narration")
        val Narration : String = "",
        @SerializedName("AccruedPoints")
        val AccruedPoints : String = "",
        @SerializedName("MemberShipCardNumber")
        val MemberShipCardNumber : String = "",
        @SerializedName("IsPointType")
        val IsPointType : String = "",
        @SerializedName("Tier")
        val Tier : String = "",
        @SerializedName("RecordCount")
        val RecordCount : String = "",
        @SerializedName("StoreCode")
        val StoreCode : String = "",
        @SerializedName("EasyPointTypeId")
        val EasyPointTypeId : String = "",
        @SerializedName("IsRedeemed")
        val IsRedeemed : String = "",
        @SerializedName("WalletId")
        val WalletId : String = "",
        @SerializedName("PrimaryOrderNumber")
        val PrimaryOrderNumber : String = "",
        @SerializedName("NumberTransactions")
        val NumberTransactions : String = "",
        @SerializedName("ExpiryDate")
        val ExpiryDate : String = "",
        @SerializedName("AvailablePoints")
        val AvailablePoints : String = "",
        @SerializedName("Channel")
        val Channel : String = "",
        @SerializedName("OfferName")
        val OfferName : String = "",
        @SerializedName("Offercode")
        val Offercode : String = "",
    )
}