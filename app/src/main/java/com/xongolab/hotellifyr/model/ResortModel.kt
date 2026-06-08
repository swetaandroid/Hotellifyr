package com.xongolab.hotellifyr.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class ResortModel {

    @Expose
    @SerializedName("name")
    var name: String = ""

    @Expose
    @SerializedName("title")
    var title: String = ""

    @Expose
    @SerializedName("location")
    var location: String = ""

    @Expose
    @SerializedName("images")
    var images: List<String> = listOf()

    @Expose
    @SerializedName("icon")
    var icon: String = ""

    @Expose
    @SerializedName("rating")
    var rating: Double = 0.0

    @Expose
    @SerializedName("desc")
    var desc: String = ""

    @Expose
    @SerializedName("guests")
    var guests: Int = 0

    @Expose
    @SerializedName("bedroom")
    var bedroom: Int = 0

    @Expose
    @SerializedName("bath")
    var bath: Int = 0

    @Expose
    @SerializedName("price")
    var price: Double = 0.0

    @Expose
    @SerializedName("offer")
    var offer: Int = 0

    @Expose
    @SerializedName("profileImg")
    var profileImg: Int = 0

}


data class MonthData(
    val monthName: String,
    val days: List<Int>
)

data class Message(
    val date: String,
    val content: String,
    val time: String,
    val isToday: Boolean
)

data class AirportPick(
    val seat: Int,
    val image: Int,
    val title: String,
    val price: Double,
    var isSelect: Boolean = false,
)

data class Packages(
    @Expose
    @SerializedName("type")
    var type: String = "",

    @Expose
    @SerializedName("price")
    var price: String = "",

    @Expose
    @SerializedName("title")
    var title: String = "",

    var isSelect: Boolean = false,

)

data class MemberDiscount(
    val background: Int,
    val icon: Int,
    val title: String,
    val description: String,
)

data class MemberBenefits(
    val icon: Int,
    val title: String,
)

data class PaymentMethod(
    val icon: Int,
    val type: String,
)

data class TimeSlotModel(
    @Expose
    @SerializedName("planName")
    var planName: String = "",

    @Expose
    @SerializedName("diningType")
    var diningType: String = "",

    @Expose
    @SerializedName("title")
    var title: String = "",

    var isSelect: Boolean = false,

    @Expose
    @SerializedName("timeSlotItem")
    var timeSlotItem: ArrayList<TimeSlotItem> = ArrayList()


) {

    data class TimeSlotItem(

        @Expose
        @SerializedName("time")
        var time: String = "",

        var isSelected : Boolean = false
    )
}

class ChildAgeModel {
    var age: Int = 0
}