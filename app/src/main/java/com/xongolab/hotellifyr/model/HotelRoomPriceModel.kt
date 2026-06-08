package com.xongolab.hotellifyr.model


import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose



data class HotelRoomPriceModel(
    @SerializedName("adults")
    @Expose
    val adults: Int?,
    @SerializedName("amenities")
    @Expose
    val amenities: List<Amenity?>?,
    @SerializedName("amenitiesIDs")
    @Expose
    val amenitiesIDs: List<String?>?,
    @SerializedName("area")
    @Expose
    val area: Int?,
    @SerializedName("bathroom")
    @Expose
    val bathroom: Int?,
    @SerializedName("beds")
    @Expose
    val beds: Int?,
    @SerializedName("children")
    @Expose
    val children: Int?,
    @SerializedName("description")
    @Expose
    val description: Description?,
    @SerializedName("guest")
    @Expose
    val guest: Int?,
    @SerializedName("_id")
    @Expose
    val id: String?,
    @SerializedName("images")
    @Expose
    val images: List<String?>?,
    @SerializedName("memberPrice")
    @Expose
    val memberPrice: Any?,
    @SerializedName("rateDetails")
    @Expose
    val rateDetails: RateDetails?,
    @SerializedName("standardPrice")
    @Expose
    val standardPrice: Any?,
    @SerializedName("suite")
    @Expose
    val suite: String?,
    @SerializedName("title")
    @Expose
    val title: Title?,
    @SerializedName("video")
    @Expose
    val video: Any?
) {
    data class Amenity(
        @SerializedName("icon")
        @Expose
        val icon: String?,
        @SerializedName("_id")
        @Expose
        val id: String?,
        @SerializedName("title")
        @Expose
        val title: String?
    )

    data class Description(
        @SerializedName("EN")
        @Expose
        val eN: String?
    )

    data class RateDetails(
        @SerializedName("cancellationPolicy")
        @Expose
        val cancellationPolicy: CancellationPolicy?,
        @SerializedName("guaranteePolicy")
        @Expose
        val guaranteePolicy: GuaranteePolicy?,
        @SerializedName("rateDescription")
        @Expose
        val rateDescription: List<String?>?,
        @SerializedName("rateTitle")
        @Expose
        val rateTitle: RateTitle?
    ) {
        data class CancellationPolicy(
            @SerializedName("EN")
            @Expose
            val eN: String?
        )

        data class GuaranteePolicy(
            @SerializedName("EN")
            @Expose
            val eN: String?
        )

        data class RateTitle(
            @SerializedName("EN")
            @Expose
            val eN: String?
        )
    }

    data class Title(
        @SerializedName("EN")
        @Expose
        val eN: String?
    )
}