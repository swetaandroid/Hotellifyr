package com.xongolab.hotellifyr.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class PreferencesModel(
    @SerializedName("_id")
    val _id: String = "",
    @SerializedName("title")
    val title: String = "",
    @SerializedName("itemList")
    var itemList: ArrayList<PreferencesItemData> = arrayListOf(),
)

data class PreferencesItemData(
    @SerializedName("parentID")
    val parentID: String = "",
    @SerializedName("title")
    val title: String = "",
    @SerializedName("items")
    val items: ArrayList<SubPreferencesItemData> = arrayListOf(),
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.createTypedArrayList(SubPreferencesItemData.CREATOR) ?: arrayListOf()
    )

    override fun describeContents(): Int = 0

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(parentID)
        parcel.writeString(title)
        parcel.writeTypedList(items)
    }

    companion object CREATOR : Parcelable.Creator<PreferencesItemData> {
        override fun createFromParcel(parcel: Parcel): PreferencesItemData {
            return PreferencesItemData(parcel)
        }

        override fun newArray(size: Int): Array<PreferencesItemData?> {
            return arrayOfNulls(size)
        }
    }
}

data class SubPreferencesItemData(
    @SerializedName("_id")
    val _id: String = "",
    @SerializedName("categoryId")
    val categoryId: String = "",
    @SerializedName("subcategoryId")
    val subcategoryId: String = "",
    @SerializedName("name")
    val name: String = "",
    var isChecked: Boolean = false,
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readByte() != 0.toByte()
    )

    override fun describeContents(): Int = 0

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(_id)
        parcel.writeString(categoryId)
        parcel.writeString(subcategoryId)
        parcel.writeString(name)
        parcel.writeByte(if (isChecked) 1 else 0)
    }

    companion object CREATOR : Parcelable.Creator<SubPreferencesItemData> {
        override fun createFromParcel(parcel: Parcel): SubPreferencesItemData {
            return SubPreferencesItemData(parcel)
        }

        override fun newArray(size: Int): Array<SubPreferencesItemData?> {
            return arrayOfNulls(size)
        }
    }
}
