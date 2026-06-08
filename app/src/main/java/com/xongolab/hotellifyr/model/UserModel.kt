package com.xongolab.hotellifyr.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class UserModel(

    @SerializedName("ID")
    val otpID: String = "",

    @SerializedName("otp")
    val otp: Int = 0,

    @SerializedName("isRegistered")
    val isRegistered: Boolean = false,

    @SerializedName("isPopular")
    val isPopular: Boolean = false,

    @SerializedName("urlTitle")
    val urlTitle: String = "",

    @SerializedName("state")
    val state: String = "",

    @SerializedName("icon")
    val icon: String = "",

    @SerializedName("address")
    val address: String = "",
    @SerializedName("cityID")
    val cityID: String = "",
    @SerializedName("countryID")
    val countryID: String = "",
    @SerializedName("stateID")
    val stateID: String = "",

    @SerializedName("anniversaryDate")
    val anniversaryDate: String? = "",

    @SerializedName("AvailablePoints")
    val availablePoints: Double = 0.0,

    @SerializedName("createdAt")
    val createdAt: String = "",

    @SerializedName("CurrentTier")
    val currentTier: String = "",

    @SerializedName("DateOfBirth")
    val dateOfBirth: String? = "",

    @SerializedName("deviceDetails")
    val deviceDetails: DeviceDetails = DeviceDetails(),

    @SerializedName("Email")
    val email: String = "",

    @SerializedName("FirstName")
    val firstName: String = "",

    @SerializedName("_id")
    val id: String = "",

    @SerializedName("LastName")
    val lastName: String = "",

    @SerializedName("MembershipCardNumber")
    val membershipCardNumber: String = "",

    @SerializedName("Mobile")
    val mobile: String = "",

    @SerializedName("MobileCountryCode")
    val mobileCountryCode: String = "",

    @SerializedName("preferences")
    val preferences: ArrayList<String> = arrayListOf(),

    @SerializedName("status")
    val status: String = "",

    @SerializedName("token")
    val token: String = "",

    @SerializedName("updatedAt")
    val updatedAt: String = "",

    @SerializedName("templateCode")
    val templateCode: String = "",

    @SerializedName("title")
    var title: String = "",

    @SerializedName("callingCode")
    val callingCode: String = "",

    @SerializedName("capital")
    val capital: String = "",

    @SerializedName("latitude")
    val latitude: Double = 0.0,

    @SerializedName("longitude")
    val longitude: Double = 0.0,

    @SerializedName("country")
    val country: String = "",

    @SerializedName("link")
    val link: String = "",

    @SerializedName("userInfo")
    val userInfo: UserInfo = UserInfo(),

    @SerializedName("AccrualPoints")
    val accrualPoints: String = "",
    @SerializedName("Address1")
    val address1: String = "",
    @SerializedName("Address2")
    val address2: String = "",
    @SerializedName("avatar")
    val avatar: String? = "",
    @SerializedName("ChannelCode")
    val channelCode: String = "",
    @SerializedName("ChildDOB")
    val childDOB: Any = Any(),
    @SerializedName("ChildName")
    val childName: Any = Any(),
    @SerializedName("ClientID")
    val clientID: Any = Any(),
    @SerializedName("CompanyName")
    val companyName: Any = Any(),
    @SerializedName("ConsentStatus")
    val consentStatus: Any = Any(),

    @SerializedName("CustomerType")
    val customerType: String = "",
    @SerializedName("EmailSubscribe")
    val emailSubscribe: String = "",
    @SerializedName("EmirateResidence")
    val emirateResidence: Any = Any(),
    @SerializedName("EndDate")
    val endDate: String = "",
    @SerializedName("EnrollDate")
    val enrollDate: String = "",
    @SerializedName("EnrolledStoreCode")
    val enrolledStoreCode: String = "",
    @SerializedName("Gender")
    val gender: String = "",
    @SerializedName("IsAnnivUpdated")
    val isAnnivUpdated: String = "",
    @SerializedName("IsBdayUpdated")
    val isBdayUpdated: String = "",
    @SerializedName("IsProfileUpdated")
    val isProfileUpdated: String = "",
    @SerializedName("IsRCSSubscribe")
    val isRCSSubscribe: String = "",
    @SerializedName("LastMigrationDate")
    val lastMigrationDate: Any = Any(),
    @SerializedName("LifeTimeATV")
    val lifeTimeATV: String = "",
    @SerializedName("MigratedSpends")
    val migratedSpends: String = "",
    @SerializedName("MigratedVisits")
    val migratedVisits: String = "",
    @SerializedName("Nationality")
    val nationality: Any = Any(),
    @SerializedName("PANNo")
    val pANNo: String = "",
    @SerializedName("PinCode")
    val pinCode: String = "",
    @SerializedName("ProfileUpdateStoreCode")
    val profileUpdateStoreCode: String = "",
    @SerializedName("ProfileUpdateStoreCodeDatetime")
    val profileUpdateStoreCodeDatetime: String = "",
    @SerializedName("PushNotificationSubscribe")
    val pushNotificationSubscribe: String = "",
    @SerializedName("RecordCount")
    val recordCount: String = "",
    @SerializedName("ReferralCode")
    val referralCode: String = "",
    @SerializedName("ReferralPoints")
    val referralPoints: String = "",
    @SerializedName("ReferredCount")
    val referredCount: String = "",
    @SerializedName("RemainingReferrals")
    val remainingReferrals: String = "",
    @SerializedName("ReturnCode")
    val returnCode: String = "",
    @SerializedName("ReturnMessage")
    val returnMessage: String = "",
    @SerializedName("SMSSubscribe")
    val sMSSubscribe: String = "",
    @SerializedName("ServicePersonNo")
    val servicePersonNo: Any = Any(),
    @SerializedName("StoreCustomerId")
    val storeCustomerId: String = "",
    @SerializedName("TierEndDate")
    val tierEndDate: String = "",
    @SerializedName("TierStartDate")
    val tierStartDate: String = "",
    @SerializedName("TotalPointsAccrued")
    val totalPointsAccrued: String = "",
    @SerializedName("TotalPointsLapsed")
    val totalPointsLapsed: String = "",
    @SerializedName("TotalPointsRedeemed")
    val totalPointsRedeemed: String = "",
    @SerializedName("TotalSpends")
    val totalSpends: String = "",
    @SerializedName("TotalVisits")
    val totalVisits: String = "",
    @SerializedName("UAEResident")
    val uAEResident: String = "",
    @SerializedName("WhatsAppSubscribe")
    val whatsAppSubscribe: String = "",
    @SerializedName("ephemeralKey")
    var ephemeralKey: String = "",
    @SerializedName("paymentIntent")
    var paymentIntent: String = "",
    @SerializedName("customer")
    var paymentCustomerId: String = ""
) {


    data class DeviceDetails(
        @SerializedName("appVersion")
        val appVersion: String = "",
        @SerializedName("deviceID")
        val deviceID: String = "",
        @SerializedName("deviceName")
        val deviceName: String = "",
        @SerializedName("deviceToken")
        val deviceToken: String = "",
        @SerializedName("deviceType")
        val deviceType: String = ""
    )

    data class UserInfo(
        @SerializedName("avatar")
        val avatar: String? = "",
        @SerializedName("Email")
        val email: String = "",
        @SerializedName("FirstName")
        val firstName: String = "",
        @SerializedName("_id")
        val id: String = "",
        @SerializedName("LastName")
        val lastName: String = "",
        @SerializedName("Mobile")
        val mobile: String = "",
        @SerializedName("MobileCountryCode")
        val mobileCountryCode: String = ""
    )

}

