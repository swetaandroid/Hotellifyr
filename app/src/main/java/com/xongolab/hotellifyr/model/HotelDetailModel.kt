package com.xongolab.hotellifyr.model


import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class HotelDetailModel(
    @SerializedName("address")
    @Expose
    val address: String?,
    @SerializedName("amenitiesIDs")
    @Expose
    val amenitiesIDs: List<String?>?,
    @SerializedName("attraction")
    @Expose
    val attraction: Attraction?,
    @SerializedName("banquetHalls")
    @Expose
    val banquetHalls: Int?,
    @SerializedName("brandID")
    @Expose
    val brandID: String?,
    @SerializedName("cityID")
    @Expose
    val cityID: String?,
    @SerializedName("countryID")
    @Expose
    val countryID: String?,
    @SerializedName("createdAt")
    @Expose
    val createdAt: String?,
    @SerializedName("description")
    @Expose
    val description: String?,
    @SerializedName("destinationIDs")
    @Expose
    val destinationIDs: List<String?>?,
    @SerializedName("detailURL")
    @Expose
    val detailURL: String?,
    @SerializedName("dining")
    @Expose
    val dining: Dining?,
    @SerializedName("diningImages")
    @Expose
    val diningImages: List<String?>?,
    @SerializedName("Email")
    @Expose
    val email: String?,
    @SerializedName("eventIDs")
    @Expose
    val eventIDs: List<String?>?,
    @SerializedName("experiences")
    @Expose
    val experiences: Experiences?,
    @SerializedName("exploreIDs")
    @Expose
    val exploreIDs: List<Any?>?,
    @SerializedName("fbOutlet")
    @Expose
    val fbOutlet: Int?,
    @SerializedName("gallery")
    @Expose
    val gallery: Gallery?,
    @SerializedName("galleryImages")
    @Expose
    val galleryImages: List<String?>?,
    @SerializedName("gym")
    @Expose
    val gym: Int?,
    @SerializedName("hotelEvents")
    @Expose
    val hotelEvents: List<HotelEvent?>?,
    @SerializedName("hotelOffersSections")
    @Expose
    val hotelOffersSections: List<HotelOffersSection?>?,
    @SerializedName("hotelType")
    @Expose
    val hotelType: String?,
    @SerializedName("howToReach")
    @Expose
    val howToReach: List<HowToReach?>?,
    @SerializedName("icon")
    @Expose
    val icon: String?,
    @SerializedName("_id")
    @Expose
    val id: String?,
    @SerializedName("images")
    @Expose
    val images: List<String?>?,
    @SerializedName("isPopular")
    @Expose
    val isPopular: Boolean?,
    @SerializedName("latitude")
    @Expose
    val latitude: Double?,
    @SerializedName("location")
    @Expose
    val location: Location?,
    @SerializedName("longitude")
    @Expose
    val longitude: Double?,
    @SerializedName("Mobile")
    @Expose
    val mobile: String?,
    @SerializedName("name")
    @Expose
    val name: String?,
    @SerializedName("offer")
    @Expose
    val offer: Offer?,
    @SerializedName("overDetails")
    @Expose
    val overDetails: OverDetails?,
    @SerializedName("overview")
    @Expose
    val overview: Overview?,
    @SerializedName("overviewGalleryImages")
    @Expose
    val overviewGalleryImages: List<String?>?,
    @SerializedName("overviewSections")
    @Expose
    val overviewSections: List<OverviewSection?>?,
    @SerializedName("price")
    @Expose
    val price: Any?,
    @SerializedName("propertyId")
    @Expose
    val propertyId: Int?,
    @SerializedName("room")
    @Expose
    val room: Room?,
    @SerializedName("rooms")
    @Expose
    val rooms: Int?,
    @SerializedName("roomsSection")
    @Expose
    val roomsSection: List<RoomsSection?>?,
    @SerializedName("serviceIDs")
    @Expose
    val serviceIDs: List<String?>?,
    @SerializedName("services")
    @Expose
    val services: List<Service?>?,
    @SerializedName("spa")
    @Expose
    val spa: Int?,
    @SerializedName("starRating")
    @Expose
    val starRating: Int?,
    @SerializedName("stateID")
    @Expose
    val stateID: String?,
    @SerializedName("status")
    @Expose
    val status: String?,
    @SerializedName("swimmingPool")
    @Expose
    val swimmingPool: Int?,
    @SerializedName("tagsIDs")
    @Expose
    val tagsIDs: List<String?>?,
    @SerializedName("tax")
    @Expose
    val tax: Int?,
    @SerializedName("updatedAt")
    @Expose
    val updatedAt: String?,
    @SerializedName("venue")
    @Expose
    val venue: Venue?,
    @SerializedName("venueData")
    @Expose
    val venueData: List<VenueData?>?,
    @SerializedName("wellness")
    @Expose
    val wellness: Wellness?,
    @SerializedName("wellnessIDs")
    @Expose
    val wellnessIDs: List<String?>?
) {
    data class Attraction(
        @SerializedName("EN")
        @Expose
        val eN: EN?,
        @SerializedName("isVisible")
        @Expose
        val isVisible: Boolean?
    ) {
        data class EN(
            @SerializedName("description")
            @Expose
            val description: String?,
            @SerializedName("title")
            @Expose
            val title: String?
        )
    }

    data class Dining(
        @SerializedName("EN")
        @Expose
        val eN: EN?,
        @SerializedName("isVisible")
        @Expose
        val isVisible: Boolean?
    ) {
        data class EN(
            @SerializedName("description")
            @Expose
            val description: String?,
            @SerializedName("title")
            @Expose
            val title: String?
        )
    }

    data class Experiences(
        @SerializedName("EN")
        @Expose
        val eN: EN?,
        @SerializedName("isVisible")
        @Expose
        val isVisible: Boolean?
    ) {
        data class EN(
            @SerializedName("description")
            @Expose
            val description: String?,
            @SerializedName("title")
            @Expose
            val title: String?
        )
    }

    data class Gallery(
        @SerializedName("EN")
        @Expose
        val eN: EN?,
        @SerializedName("isVisible")
        @Expose
        val isVisible: Boolean?
    ) {
        data class EN(
            @SerializedName("description")
            @Expose
            val description: String?,
            @SerializedName("title")
            @Expose
            val title: String?
        )
    }

    data class HotelEvent(
        @SerializedName("createdAt")
        @Expose
        val createdAt: String?,
        @SerializedName("description")
        @Expose
        val description: String?,
        @SerializedName("icon")
        @Expose
        val icon: String?,
        @SerializedName("_id")
        @Expose
        val id: String?,
        @SerializedName("status")
        @Expose
        val status: String?,
        @SerializedName("title")
        @Expose
        val title: String?,
        @SerializedName("updatedAt")
        @Expose
        val updatedAt: String?
    ) {
        data class Description(
            @SerializedName("EN")
            @Expose
            val eN: String?
        )

        data class Title(
            @SerializedName("EN")
            @Expose
            val eN: String?
        )
    }

    data class HotelOffersSection(
        @SerializedName("description")
        @Expose
        val description: String?,
        @SerializedName("details")
        @Expose
        val details: List<String?>?,
        @SerializedName("_id")
        @Expose
        val id: String?,
        @SerializedName("images")
        @Expose
        val images: List<String?>?,
        @SerializedName("title")
        @Expose
        val title: String?
    ) {
        data class Description(
            @SerializedName("EN")
            @Expose
            val eN: String?
        )

        data class Title(
            @SerializedName("EN")
            @Expose
            val eN: String?
        )
    }

    data class HowToReach(
        @SerializedName("categoryIcon")
        @Expose
        val categoryIcon: String?,
        @SerializedName("categoryTitle")
        @Expose
        val categoryTitle: String?,
        @SerializedName("distance")
        @Expose
        val distance: Double?,
        @SerializedName("_id")
        @Expose
        val id: String?,
        @SerializedName("title")
        @Expose
        val title: String?
    ) {
        data class CategoryTitle(
            @SerializedName("EN")
            @Expose
            val eN: String?
        )

        data class Title(
            @SerializedName("EN")
            @Expose
            val eN: String?
        )
    }

    data class Location(
        @SerializedName("coordinates")
        @Expose
        val coordinates: List<Double?>?,
        @SerializedName("type")
        @Expose
        val type: String?
    )

    data class Offer(
        @SerializedName("EN")
        @Expose
        val eN: EN?,
        @SerializedName("isVisible")
        @Expose
        val isVisible: Boolean?
    ) {
        data class EN(
            @SerializedName("description")
            @Expose
            val description: String?,
            @SerializedName("title")
            @Expose
            val title: String?
        )
    }

    data class OverDetails(
        @SerializedName("airport_distance")
        @Expose
        val airportDistance: String?,
        @SerializedName("airport_title")
        @Expose
        val airportTitle: String?,
        @SerializedName("banner1")
        @Expose
        val banner1: Banner1?,
        @SerializedName("banquets_title")
        @Expose
        val banquetsTitle: String?,
        @SerializedName("checkin_time")
        @Expose
        val checkinTime: String?,
        @SerializedName("checkout_time")
        @Expose
        val checkoutTime: String?,
        @SerializedName("createdAt")
        @Expose
        val createdAt: String?,
        @SerializedName("dining_drinking")
        @Expose
        val diningDrinking: DiningDrinking?,
        @SerializedName("dining_title")
        @Expose
        val diningTitle: String?,
        @SerializedName("diningicon")
        @Expose
        val diningicon: String?,
        @SerializedName("fullAddress")
        @Expose
        val fullAddress: String?,
        @SerializedName("GSTIN")
        @Expose
        val gSTIN: String?,
        @SerializedName("hotelID")
        @Expose
        val hotelID: String?,
        @SerializedName("_id")
        @Expose
        val id: String?,
        @SerializedName("other_distance")
        @Expose
        val otherDistance: String?,
        @SerializedName("other_title")
        @Expose
        val otherTitle: String?,
        @SerializedName("room_amenities")
        @Expose
        val roomAmenities: RoomAmenities?,
        @SerializedName("service")
        @Expose
        val service: Service?,
        @SerializedName("suites_title")
        @Expose
        val suitesTitle: String?,
        @SerializedName("train_distance")
        @Expose
        val trainDistance: String?,
        @SerializedName("train_title")
        @Expose
        val trainTitle: String?,
        @SerializedName("updatedAt")
        @Expose
        val updatedAt: String?
    ) {
        data class Banner1(
            @SerializedName("description")
            @Expose
            val description: String?,
            @SerializedName("icon")
            @Expose
            val icon: String?,
            @SerializedName("title")
            @Expose
            val title: String?
        ) {
            data class Description(
                @SerializedName("EN")
                @Expose
                val eN: String?
            )

            data class Title(
                @SerializedName("EN")
                @Expose
                val eN: String?
            )
        }

        data class DiningDrinking(
            @SerializedName("description")
            @Expose
            val description: String?,
            @SerializedName("title")
            @Expose
            val title: String?
        ) {
            data class Description(
                @SerializedName("EN")
                @Expose
                val eN: String?
            )

            data class Title(
                @SerializedName("EN")
                @Expose
                val eN: String?
            )
        }

        data class RoomAmenities(
            @SerializedName("banner")
            @Expose
            val banner: String?,
            @SerializedName("description")
            @Expose
            val description: String?,
            @SerializedName("title")
            @Expose
            val title: String?
        ) {
            data class Description(
                @SerializedName("EN")
                @Expose
                val eN: String?
            )

            data class Title(
                @SerializedName("EN")
                @Expose
                val eN: String?
            )
        }

        data class Service(
            @SerializedName("description")
            @Expose
            val description: String?,
            @SerializedName("image1")
            @Expose
            val image1: String?,
            @SerializedName("image2")
            @Expose
            val image2: String?,
            @SerializedName("title")
            @Expose
            val title: String?
        ) {
            data class Description(
                @SerializedName("EN")
                @Expose
                val eN: String?
            )

            data class Title(
                @SerializedName("EN")
                @Expose
                val eN: String?
            )
        }
    }

    data class Overview(
        @SerializedName("EN")
        @Expose
        val eN: EN?,
        @SerializedName("isVisible")
        @Expose
        val isVisible: Boolean?
    ) {
        data class EN(
            @SerializedName("description")
            @Expose
            val description: String?,
            @SerializedName("title")
            @Expose
            val title: String?
        )
    }

    data class OverviewSection(
        @SerializedName("description")
        @Expose
        val description: String?,
        @SerializedName("icon")
        @Expose
        val icon: String?,
        @SerializedName("_id")
        @Expose
        val id: String?,
        @SerializedName("title")
        @Expose
        val title: String?
    ) {
        data class Description(
            @SerializedName("EN")
            @Expose
            val eN: String?
        )

        data class Title(
            @SerializedName("EN")
            @Expose
            val eN: String?
        )
    }

    data class Room(
        @SerializedName("EN")
        @Expose
        val eN: EN?,
        @SerializedName("isVisible")
        @Expose
        val isVisible: Boolean?
    ) {
        data class EN(
            @SerializedName("description")
            @Expose
            val description: String?,
            @SerializedName("title")
            @Expose
            val title: String?
        )
    }

    data class RoomsSection(
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
        val description: String?,
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
        val memberPrice: Int?,
        @SerializedName("rateDetails")
        @Expose
        val rateDetails: RateDetails?,
        @SerializedName("standardPrice")
        @Expose
        val standardPrice: Int?,
        @SerializedName("suite")
        @Expose
        val suite: String?,
        @SerializedName("title")
        @Expose
        val title: String?,
        @SerializedName("video")
        @Expose
        val video: String?
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

    data class Service(
        @SerializedName("description")
        @Expose
        val description: String?,
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

    data class Venue(
        @SerializedName("EN")
        @Expose
        val eN: EN?,
        @SerializedName("isVisible")
        @Expose
        val isVisible: Boolean?
    ) {
        data class EN(
            @SerializedName("description")
            @Expose
            val description: String?,
            @SerializedName("title")
            @Expose
            val title: String?
        )
    }

    data class VenueData(
        @SerializedName("area")
        @Expose
        val area: String?,
        @SerializedName("description")
        @Expose
        val description: String?,
        @SerializedName("guestscapacity")
        @Expose
        val guestscapacity: String?,
        @SerializedName("hall")
        @Expose
        val hall: String?,
        @SerializedName("_id")
        @Expose
        val id: String?,
        @SerializedName("images")
        @Expose
        val images: List<String?>?,
        @SerializedName("status")
        @Expose
        val status: String?,
        @SerializedName("title")
        @Expose
        val title: String?
    ) {
        data class Description(
            @SerializedName("EN")
            @Expose
            val eN: String?
        )

        data class Title(
            @SerializedName("EN")
            @Expose
            val eN: String?
        )
    }

    data class Wellness(
        @SerializedName("EN")
        @Expose
        val eN: EN?,
        @SerializedName("isVisible")
        @Expose
        val isVisible: Boolean?
    ) {
        data class EN(
            @SerializedName("description")
            @Expose
            val description: String?,
            @SerializedName("title")
            @Expose
            val title: String?
        )
    }
}