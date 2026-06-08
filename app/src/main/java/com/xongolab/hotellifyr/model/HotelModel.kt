package com.xongolab.hotellifyr.model


import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.xongolab.hotellifyr.model.HotelModel.Details
import com.xongolab.hotellifyr.model.HotelModel.Position

data class HotelModel(
    @SerializedName("address")
    val address: String = "",
    @SerializedName("description")
    val description: String = "",
    @SerializedName("details")
    val details: Details = Details(),
    @SerializedName("icon")
    val icon: String = "",
    @SerializedName("_id")
    val id: String = "",
    @SerializedName("isPopular")
    val isPopular: Boolean = false,
    @SerializedName("name")
    var name: String = "",
    @SerializedName("urlName")
    var urlName: String = "",
    @SerializedName("position")
    val position: Position = Position(),
    @SerializedName("state")
    val state: String = "",
    @SerializedName("amenities")
    val amenities: ArrayList<Amenity> = arrayListOf(),
    @SerializedName("Email")
    val email: String = "",
    @SerializedName("images")
    val images: ArrayList<String> = arrayListOf(),
    @SerializedName("Mobile")
    val mobile: String = "",
    @SerializedName("price")
    val price: Double = 0.0,
    @SerializedName("propertyId")
    val propertyId: Int = 0,
    @SerializedName("starRating")
    val starRating: Float = 0F,
    @SerializedName("rate")
    val rate: String = "0",
    @SerializedName("tags")
    val tags: ArrayList<Tag> = arrayListOf(),
    @SerializedName("latitude")
    val latitude: Double = 0.0,
    @SerializedName("longitude")
    val longitude: Double = 0.0,
    @SerializedName("title")
    var title: String = "",
    @SerializedName("code")
    val code: String = "",
    @SerializedName("hotelDetails")
    val hotelDetails: ArrayList<HotelDetail> = arrayListOf(),
    @SerializedName("brandID")
    val brandID: String = "",
    @SerializedName("destinationIDs")
    val destinationIDs: List<String> = listOf(),
    @SerializedName("distanceInKm")
    val distanceInKm: Double? = 0.0,
    @SerializedName("hotelType")
    var hotelType: String = "",
    @SerializedName("isFavorite")
    val isFavorite: Boolean = false,
    var isChecked: Boolean = false,
    var isSelected: Boolean = false,
    @SerializedName("resourceImage")
    var resourceImage: Int? = 0,
    @SerializedName("banquetHalls")
    val banquetHalls: Int = 0,
    @SerializedName("diningData")
    val diningData: ArrayList<DiningData> = arrayListOf(),
    @SerializedName("experienceData")
    val experienceData: ArrayList<ExperienceData> = arrayListOf(),
    @SerializedName("fbOutlet")
    val fbOutlet: Int = 0,
    @SerializedName("galleryImages")
    val galleryImages: ArrayList<String> = arrayListOf(),
    @SerializedName("gym")
    val gym: Int = 0,
    @SerializedName("hotelOffersSections")
    val hotelOffersSections: ArrayList<HotelOffersSection> = arrayListOf(),
    @SerializedName("howToReach")
    val howToReach: ArrayList<HowToReach> = arrayListOf(),
    @SerializedName("nearByHotel")
    val nearByHotel: ArrayList<NearByHotel> = arrayListOf(),
    @SerializedName("overview")
    val overview: Overview = Overview(),
    @SerializedName("paymentOption")
    val paymentOption: String = "",
    @SerializedName("refundAllow")
    val refundAllow: Boolean = false,
    @SerializedName("rooms")
    val rooms: Int = 0,
    @SerializedName("services")
    val services: ArrayList<Service> = arrayListOf(),
    @SerializedName("spa")
    val spa: Int = 0,
    @SerializedName("swimmingPool")
    val swimmingPool: Int = 0,
    @SerializedName("venueData")
    val venueData: ArrayList<VenueData> = arrayListOf(),
    @SerializedName("mapItems")
    val mapItems: ArrayList<MapItem> = arrayListOf(),

    @SerializedName("favHotels")
    val favHotels: ArrayList<FavHotels> = arrayListOf(),
) {
    data class Details(
        @SerializedName("banquetHalls")
        val banquetHalls: Int = 0,
        @SerializedName("fbOutlet")
        val fbOutlet: Int = 0,
        @SerializedName("gym")
        val gym: Int = 0,
        @SerializedName("rooms")
        val rooms: Int = 0,
        @SerializedName("spa")
        val spa: Int = 0,
        @SerializedName("swimmingPool")
        val swimmingPool: Int = 0
    )

    data class Position(
        @SerializedName("lat")
        val lat: Double = 0.0,
        @SerializedName("lng")
        val lng: Double = 0.0
    )

    data class Amenity(
        @SerializedName("icon")
        val icon: String = "",
        @SerializedName("_id")
        val id: String = "",
        @SerializedName("title")
        val title: String = ""
    )

    data class Tag(
        @SerializedName("_id")
        val id: String = "",
        @SerializedName("title")
        val title: String = ""
    )

    data class HotelDetail(
        @SerializedName("address")
        val address: String = "",
        @SerializedName("description")
        val description: String = "",
        @SerializedName("details")
        val details: Details = Details(),
        @SerializedName("icon")
        val icon: String = "",
        @SerializedName("_id")
        val id: String = "",
        @SerializedName("isFavorite")
        val isFavorite: Boolean = false,
        @SerializedName("isPopular")
        val isPopular: Boolean = false,
        @SerializedName("name")
        val name: String = "",
        @SerializedName("position")
        val position: Position = Position(),
        @SerializedName("propertyId")
        val propertyId: Int = 0,
        @SerializedName("starRating")
        val starRating: Float = 0F,
        @SerializedName("urlName")
        val urlName: String = ""
    ) {
        data class Details(
            @SerializedName("banquetHalls")
            val banquetHalls: Int = 0,
            @SerializedName("fbOutlet")
            val fbOutlet: Int = 0,
            @SerializedName("gym")
            val gym: Int = 0,
            @SerializedName("rooms")
            val rooms: Int = 0,
            @SerializedName("spa")
            val spa: Int = 0,
            @SerializedName("swimmingPool")
            val swimmingPool: Int = 0
        )
    }

    data class DiningData(
        @SerializedName("address")
        val address: String = "",
        @SerializedName("close_time")
        val closeTime: String = "",
        @SerializedName("cuisineTitles")
        val cuisineTitles: List<String> = listOf(),
        @SerializedName("description")
        val description: String = "",
        @SerializedName("hotelID")
        val hotelID: String = "",
        @SerializedName("hotelName")
        val hotelName: String = "",
        @SerializedName("icon")
        val icon: String = "",
        @SerializedName("_id")
        val id: String = "",
        @SerializedName("location")
        val location: String = "",
        @SerializedName("mobile")
        val mobile: String = "",
        @SerializedName("name")
        val name: String = "",
        @SerializedName("open_time")
        val openTime: String = "",
        @SerializedName("position")
        val position: Position = Position(),
        @SerializedName("serves")
        val serves: String = ""
    )

    data class HotelOffersSection(
        @SerializedName("description")
        var description: String = "",
        @SerializedName("subtitle")
        var subtitle: String = "",
        @SerializedName("validity")
        var validity: String = "",
        @SerializedName("details")
        var details: List<String> = listOf(),
        @SerializedName("_id")
        var id: String = "",
        @SerializedName("images")
        var images: ArrayList<String> = arrayListOf(),
        @SerializedName("title")
        var title: String = ""
    )

    data class HowToReach(
        @SerializedName("categoryIcon")
        val categoryIcon: String = "",
        @SerializedName("categoryTitle")
        val categoryTitle: String = "",
        @SerializedName("distance")
        val distance: Double = 0.0,
        @SerializedName("_id")
        val id: String = "",
        @SerializedName("title")
        val title: String = ""
    )

    data class NearByHotel(
        @SerializedName("icon")
        val icon: String = "",
        @SerializedName("_id")
        val id: String = "",
        @SerializedName("km")
        val km: String = "",
        @SerializedName("position")
        val position: Position = Position(),
        @SerializedName("title")
        val title: String = ""
    )

    data class Overview(
        @SerializedName("EN")
        val eN: EN = EN(),
        @SerializedName("isVisible")
        val isVisible: Boolean = false
    ) {
        data class EN(
            @SerializedName("description")
            val description: String = "",
            @SerializedName("title")
            val title: String = ""
        )
    }

    data class Service(
        @SerializedName("description")
        val description: String = "",
        @SerializedName("icon")
        val icon: String = "",
        @SerializedName("_id")
        val id: String = "",
        @SerializedName("title")
        val title: String = ""
    )

    data class VenueData(
        @SerializedName("area")
        val area: String = "",
        @SerializedName("description")
        val description: String = "",
        @SerializedName("guestscapacity")
        val guestsCapacity: String = "",
        @SerializedName("hall")
        val hall: String = "",
        @SerializedName("_id")
        val id: String = "",
        @SerializedName("images")
        val images: ArrayList<String> = arrayListOf(),
        @SerializedName("status")
        val status: String = "",
        @SerializedName("title")
        val title: String = ""
    )

    data class ExperienceData(
        @SerializedName("description")
        val description: String = "",
        @SerializedName("icon")
        val icon: String = "",
        @SerializedName("_id")
        val id: String = "",
        @SerializedName("locatedAt")
        val locatedAt: String = "",
        @SerializedName("title")
        val title: String = ""
    )

    data class MapItem(
        @SerializedName("_id")
        val id: String = "",
        @SerializedName("latitude")
        val latitude: Double = 0.0,
        @SerializedName("longitude")
        val longitude: Double = 0.0,
        @SerializedName("title")
        val title: String = ""
    )
}

data class SearchHotel(
    @SerializedName("adults") var adults: Int? = null,
    @SerializedName("children") var children: Int? = null,
    @SerializedName("checkIn") var checkIn: String? = null,
    @SerializedName("checkOut") var checkOut: String? = null,
    @SerializedName("hotelType") var hotelType: String? = null,
    @SerializedName("starRating") var starRating: Float? = null,
    @SerializedName("cityID") var cityID: String? = null,
    @SerializedName("stateID") var stateID: String? = null,
    @SerializedName("brandID") var brandID: String? = null,
    @SerializedName("longitude") var longitude: Double? = null,
    @SerializedName("latitude") var latitude: Double? = null,
    @SerializedName("hotelIds") var hotelIds: ArrayList<String>? = arrayListOf(),
    @SerializedName("amenitiesIDs") var amenitiesIDs: ArrayList<HotelModel>? = arrayListOf(),
    @SerializedName("brandIDs") var brandIDs: ArrayList<HotelModel>? = arrayListOf(),
    @SerializedName("destinationIDs") var destinationIDs: ArrayList<HotelModel>? = arrayListOf(),
    @SerializedName("sortBy") var sortBy: String? = null,
    @SerializedName("hotelID") var hotelID: String? = "",
    @SerializedName("location") var location: String = "",
    @SerializedName("type") var type: String? = "",
    @SerializedName("rooms") var rooms: Int? = null,
    @SerializedName("minPrice") var minPrice: Float = 0F,
    @SerializedName("maxPrice") var maxPrice: Float = 100000F,
    @SerializedName("roomWiseGuest") var roomWiseGuest: ArrayList<RoomWiseGuest>? = arrayListOf()
)

data class RoomWiseGuest(
    @SerializedName("adults") var adults: Int? = 0,
    @SerializedName("children") var children: Int? = 0,
)

data class SortHotel(
    @SerializedName("title") var title: String = "",
    @SerializedName("sortBy") var sortBy: String = "",
    @SerializedName("isChecked") var isChecked: Boolean = false,
)

data class FavHotels(
    @SerializedName("_id") var _id: String = "",
    @SerializedName("name") var name: String = "",
    @SerializedName("address") var address: String = "",
    @SerializedName("propertyId")
    val propertyId: Int = 0,
    @SerializedName("starRating")
    val starRating: Float = 0F,
    @SerializedName("isPopular") var isPopular: Boolean = false,
    @SerializedName("description") var description: String = "",
    @SerializedName("images")
    val images: ArrayList<String> = arrayListOf(),
    @SerializedName("urlName") var urlName: String = "",
    @SerializedName("icon") var icon: String = "",
    @SerializedName("position") val position: Position = Position(),
    @SerializedName("details") val details: Details = Details(),
    @SerializedName("price")
    val price: Double = 0.0,
)

data class OffersModel(
    @SerializedName("description")
    val description: String = "",
    @SerializedName("details")
    val details: List<String> = listOf(),
    @SerializedName("_id")
    val id: String = "",
    @SerializedName("images")
    val images: String = "",
    @SerializedName("subtitle")
    val subtitle: String = "",
    @SerializedName("title")
    val title: String = "",
    @SerializedName("validity")
    val validity: String = ""
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.createStringArrayList() ?: arrayListOf(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(description)
        parcel.writeStringList(details)
        parcel.writeString(id)
        parcel.writeString(images)
        parcel.writeString(subtitle)
        parcel.writeString(title)
        parcel.writeString(validity)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<OffersModel> {
        override fun createFromParcel(parcel: Parcel): OffersModel {
            return OffersModel(parcel)
        }

        override fun newArray(size: Int): Array<OffersModel?> {
            return arrayOfNulls(size)
        }
    }

}

data class OfferRoomPriceModel(
    @SerializedName("adults")
    val adults: Int = 0,
    @SerializedName("amenities")
    val amenities: ArrayList<Amenity> = arrayListOf(),
    @SerializedName("area")
    val area: Int = 0,
    @SerializedName("availabilityMessage")
    val availabilityMessage: String = "",
    @SerializedName("bathroom")
    val bathroom: Int = 0,
    @SerializedName("beds")
    val beds: Int = 0,
    @SerializedName("children")
    val children: Int = 0,
    @SerializedName("description")
    val description: String = "",
    @SerializedName("guest")
    val guest: Int = 0,
    @SerializedName("hlPricePerDay")
    val hlPricePerDay: Any = Any(),
    @SerializedName("hlRoomID")
    val hlRoomID: String = "",
    @SerializedName("_id")
    val id: String = "",
    @SerializedName("images")
    val images: ArrayList<String> = arrayListOf(),
    @SerializedName("isAvailable")
    val isAvailable: Boolean = false,
    @SerializedName("memberPrice")
    val memberPrice: Long = 0L,
    @SerializedName("memberPricePerDay")
    val memberPricePerDay: Long = 0L,
    @SerializedName("offerPrice")
    val offerPrice: ArrayList<OfferPrice> = arrayListOf(),
    @SerializedName("roomCode")
    val roomCode: String = "",
    @SerializedName("standardPrice")
    val standardPrice: Long = 0L,
    @SerializedName("standardPricePerDay")
    val standardPricePerDay: Long = 0L,
    @SerializedName("title")
    val title: String = "",
    @SerializedName("video")
    val video: String = ""
) {
    data class Amenity(
        @SerializedName("icon")
        val icon: String = "",
        @SerializedName("_id")
        val id: String = "",
        @SerializedName("title")
        val title: String = ""
    )

    data class OfferPrice(
        @SerializedName("dayPrice")
        val dayPrice: List<DayPrice> = listOf(),
        @SerializedName("hotelofferDetails")
        val hotelOfferDetails: List<String> = listOf(),
        @SerializedName("memberPrice")
        val memberPrice: Double = 0.0,
        @SerializedName("memberPricePerDay")
        val memberPricePerDay: Double = 0.0,
        @SerializedName("roomID")
        val roomID: String = "",
        @SerializedName("offerID")
        val offerID: String = "",
        @SerializedName("offerUnit")
        val offerUnit: String = "",
        @SerializedName("offerValue")
        val offerValue: Double = 0.0,
        @SerializedName("orignalMemberPrice")
        val originalMemberPrice: Double = 0.0,
        @SerializedName("orignalPrice")
        val originalPrice: Double = 0.0,
        @SerializedName("rateCode")
        val rateCode: String = "",
        @SerializedName("rateDetails")
        val rateDetails: RateDetails = RateDetails(),
        @SerializedName("shortDescription")
        val shortDescription: String = "",
        @SerializedName("standardPrice")
        val standardPrice: Double = 0.0,
        @SerializedName("standardPricePerDay")
        val standardPricePerDay: Double = 0.0,
        @SerializedName("title")
        val title: String = "",
        var isStandardRate: Boolean = false
    ) {
        data class DayPrice(
            @SerializedName("day")
            val day: String = "",
            @SerializedName("standardPrice")
            val standardPrice: Double = 0.0,
            @SerializedName("standardTax")
            val standardTax: Double = 0.0,
            @SerializedName("standardTaxPrice")
            val standardTaxPrice: Double = 0.0,
            @SerializedName("memberPrice")
            val memberPrice: Double = 0.0,
            @SerializedName("memberTax")
            val memberTax: Double = 0.0,
            @SerializedName("memberTaxPrice")
            val memberTaxPrice: Double = 0.0,
        )

        data class RateDetails(
            @SerializedName("cancellationPolicy")
            val cancellationPolicy: String = "",
            @SerializedName("guaranteePolicy")
            val guaranteePolicy: String = "",
            @SerializedName("memberCancellationPolicy")
            val memberCancellationPolicy: String = "",
            @SerializedName("memberGuaranteePolicy")
            val memberGuaranteePolicy: String = "",
            @SerializedName("memberRateDescription")
            val memberRateDescription: ArrayList<String> = arrayListOf(),
            @SerializedName("rateDescription")
            val rateDescription: ArrayList<String> = arrayListOf(),
            @SerializedName("rateTitle")
            val rateTitle: String = ""
        )
    }
}

data class TaxPercentage(
    @SerializedName("taxPercentage")
    var taxPercentage: Int = 0
)

data class BookingHotel(
    @SerializedName("ID")
    var bookingMainId: String = "",
    @SerializedName("bookingID")
    var bookingId: String = ""
)

data class AirportPickUp(
    @SerializedName("_id")
    var _id: String = "",
    @SerializedName("vehicleID")
    var vehicleID: String = "",
    @SerializedName("price")
    var price: Long = 0L,
    @SerializedName("vehicle")
    var vehicle: String = "",
    @SerializedName("seater")
    var seater: Int = 0,
    @SerializedName("icon")
    var icon: String = "",
    var isSelect: Boolean = false
)

data class AddPromo(
    @SerializedName("couponID")
    var couponID: String = "",
    @SerializedName("couponCode")
    var couponCode: String = "",
    @SerializedName("discountType")
    var discountType: String = "",
    @SerializedName("value")
    var value: Long = 0L,
    @SerializedName("discountValue")
    var discountValue: Double = 0.0,
)

data class AddOnExperience(
    @SerializedName("description")
    val description: String = "",
    @SerializedName("icon")
    val icon: String = "",
    @SerializedName("_id")
    val id: String = "",
    @SerializedName("locatedAt")
    val locatedAt: String = "",
    @SerializedName("packages")
    val packages: ArrayList<Package> = arrayListOf(),
    @SerializedName("title")
    val title: String = ""
) {
    data class Package(
        @SerializedName("description")
        val description: String = "",
        @SerializedName("details")
        val details: ArrayList<String> = arrayListOf(),
        @SerializedName("_id")
        val id: String = "",
        @SerializedName("package")
        val packageX: String = "",
        @SerializedName("price")
        val price: Double = 0.0,
        @SerializedName("title")
        val title: String = "",
        var selected: Boolean = false
    )
}

data class CampaignModel(
    @SerializedName("campaignDetail")
    val campaignDetail: ArrayList<CampaignDetail> = arrayListOf(),
    @SerializedName("description")
    val description: String = "",
    @SerializedName("_id")
    val id: String = "",
    @SerializedName("orderNo")
    val orderNo: Int = 0,
    @SerializedName("status")
    val status: String = "",
    @SerializedName("title")
    val title: String = ""
) {
    data class CampaignDetail(
        @SerializedName("banquetIDs")
        val banquetIDs: ArrayList<String> = arrayListOf(),
        @SerializedName("description")
        val description: String = "",
        @SerializedName("hotelIDs")
        val hotelIDs: ArrayList<String> = arrayListOf(),
        @SerializedName("icon")
        val icon: String = "",
        @SerializedName("_id")
        val id: String = "",
        @SerializedName("restaurantIDs")
        val restaurantIDs: List<String> = arrayListOf(),
        @SerializedName("title")
        val title: String = "",
        @SerializedName("type")
        val type: String = ""
    )
}

data class GenerateHash(
    @SerializedName("hash")
    var hash: String = ""
)

data class HomeBanner(
    @SerializedName("bannerData")
    var bannerData: ArrayList<BannerData> = arrayListOf(),
    @SerializedName("videoURL")
    var videoURL: String = "",
    @SerializedName("showHomeVideo")
    var showHomeVideo: Boolean = false
) {
    data class BannerData(
        @SerializedName("_id")
        var _id: String = "",
        @SerializedName("redirectURL")
        var redirectURL: String = "",
        @SerializedName("icon")
        var icon: String = "",
        @SerializedName("displayAlign")
        var displayAlign: String = "",
        @SerializedName("orderNo")
        var orderNo: Int = 0,
        @SerializedName("title")
        var title: String = "",
        @SerializedName("description")
        var description: String = "",
        @SerializedName("buttonText")
        var buttonText: String = "",
    )
}