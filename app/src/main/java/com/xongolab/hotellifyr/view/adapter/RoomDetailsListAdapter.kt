package com.xongolab.hotellifyr.view.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.xongolab.hotellifyr.R
import com.xongolab.hotellifyr.databinding.RawRoomDetailsListBinding
import com.xongolab.hotellifyr.model.BookRoomRequest
import com.xongolab.hotellifyr.model.DayPriceRequest
import com.xongolab.hotellifyr.model.OfferRoomPriceModel
import com.xongolab.hotellifyr.model.OfferRoomPriceModel.OfferPrice
import com.xongolab.hotellifyr.utils.DialogManager
import com.xongolab.hotellifyr.utils.Pref


@SuppressLint("NotifyDataSetChanged")
class RoomDetailsListAdapter(var context: Context) :
    RecyclerView.Adapter<RoomDetailsListAdapter.Holder>() {

    var objList: ArrayList<OfferRoomPriceModel> = ArrayList()

    var dayPriceList: ArrayList<DayPriceRequest> = ArrayList()
    private lateinit var roomRatesAdapter: RoomRatesAdapter

    var onItemRoomDetailsClick: ((position: Int, item: OfferRoomPriceModel) -> Unit)? = null
    var onItemSelectRoomClick: ((position: Int, item: BookRoomRequest, roomPrice: Double, isStandardRate: Boolean) -> Unit)? = null
    var onItemRateDetailsClick: ((position: Int, item: OfferPrice) -> Unit)? = null


    inner class Holder(val binding: RawRoomDetailsListBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding =
            RawRoomDetailsListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun getItemCount(): Int {
        return objList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: Holder, position: Int) {
        val mainItem = objList[position]
        holder.binding.apply {
            tvName.text = mainItem.title
            tvArea.text = "${mainItem.area} ${context.getString(R.string.str_m2)}"
            tvGuests.text = "${mainItem.guest} ${context.getString(R.string.guests)}"
            tvBeds.text = "${mainItem.beds} ${context.getString(R.string.beds)}"
            tvBathroom.text = "${mainItem.bathroom} ${context.getString(R.string.bathroom)}"

            roomRatesAdapter = RoomRatesAdapter(context)
            rvRoomRate.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            rvRoomRate.adapter = roomRatesAdapter

            roomRatesAdapter.addData(mainItem.offerPrice)

            roomRatesAdapter.onItemSelectRoomClick = { position, item ->
                if (item.isStandardRate && Pref.getBooleanValue(Pref.PREF_IS_LOGIN, false)) {
                    DialogManager(context).showNonMemberBottomSheetDialog(
                        onYesButtonClick = {
                            val bookRoomRequest = BookRoomRequest()
                            bookRoomRequest.roomID = item.roomID
                            bookRoomRequest.roomCode = mainItem.roomCode
                            bookRoomRequest.rateTitle = item.rateDetails.rateTitle
                            bookRoomRequest.rateCode = item.rateCode
                            bookRoomRequest.roomTitle = mainItem.title
                            bookRoomRequest.price = if (!item.isStandardRate) {
                                item.memberPrice
                            } else item.standardPrice
                            bookRoomRequest.totalPrice = if (!item.isStandardRate) {
                                item.memberPrice
                            } else item.standardPrice
                            bookRoomRequest.roomRatePerDay = if (!item.isStandardRate) {
                                item.memberPricePerDay
                            } else item.standardPricePerDay
                            bookRoomRequest.hotelImageURL = mainItem.images[0]
                            bookRoomRequest.selectedRate = if (item.isStandardRate) "standard" else "member"

                            for (i in item.dayPrice) {
                                val request = DayPriceRequest().apply {
                                    day = i.day
                                    price = if (!item.isStandardRate) {
                                        i.memberPrice
                                    } else i.standardPrice    // or item.standardPrice
                                    tax = if (!item.isStandardRate) {
                                        i.memberTax
                                    } else i.standardTax    // or item.standardTax.toDouble()
                                    taxPrice = if (!item.isStandardRate) {
                                        i.memberTaxPrice
                                    } else i.standardTaxPrice   // or item.standardTaxPrice
                                }
                                dayPriceList.add(request)
                            }
                            bookRoomRequest.dayPrice = dayPriceList
                            bookRoomRequest.taxPercentage = 0
                            bookRoomRequest.taxAmount = dayPriceList.sumOf { it.taxPrice }

                            onItemSelectRoomClick?.invoke(
                                position, bookRoomRequest, if (!item.isStandardRate) {
                                    item.memberPricePerDay
                                } else item.standardPricePerDay, item.isStandardRate
                            )
                        },
                        onNoButtonClick = {
                            val bookRoomRequest = BookRoomRequest()
                            bookRoomRequest.roomID = item.roomID
                            bookRoomRequest.roomCode = mainItem.roomCode
                            bookRoomRequest.rateTitle = item.rateDetails.rateTitle
                            bookRoomRequest.rateCode = item.rateCode
                            bookRoomRequest.roomTitle = mainItem.title
                            bookRoomRequest.price = item.memberPrice
                            bookRoomRequest.totalPrice = item.memberPrice
                            bookRoomRequest.roomRatePerDay = item.memberPricePerDay
                            bookRoomRequest.hotelImageURL = mainItem.images[0]
                            bookRoomRequest.selectedRate = "member"

                            for (i in item.dayPrice) {
                                val request = DayPriceRequest().apply {
                                    day = i.day
                                    price = i.memberPrice  // or item.standardPrice
                                    tax = i.memberTax   // or item.standardTax.toDouble()
                                    taxPrice = i.memberTaxPrice  // or item.standardTaxPrice
                                }
                                dayPriceList.add(request)
                            }
                            bookRoomRequest.dayPrice = dayPriceList
                            bookRoomRequest.taxPercentage = 0
                            bookRoomRequest.taxAmount = dayPriceList.sumOf { it.taxPrice }

                            onItemSelectRoomClick?.invoke(position, bookRoomRequest, item.memberPricePerDay, item.isStandardRate)
                        }
                    )
                } else {
                    val bookRoomRequest = BookRoomRequest()
                    bookRoomRequest.roomID = item.roomID
                    bookRoomRequest.roomCode = mainItem.roomCode
                    bookRoomRequest.rateTitle = item.rateDetails.rateTitle
                    bookRoomRequest.rateCode = item.rateCode
                    bookRoomRequest.roomTitle = mainItem.title
                    bookRoomRequest.price = if (!item.isStandardRate) {
                        item.memberPrice
                    } else item.standardPrice
                    bookRoomRequest.totalPrice = if (!item.isStandardRate) {
                        item.memberPrice
                    } else item.standardPrice
                    bookRoomRequest.roomRatePerDay = if (!item.isStandardRate) {
                        item.memberPricePerDay
                    } else item.standardPricePerDay
                    bookRoomRequest.hotelImageURL = mainItem.images[0]
                    bookRoomRequest.selectedRate = if (item.isStandardRate) "standard" else "member"

                    for (i in item.dayPrice) {
                        val request = DayPriceRequest().apply {
                            day = i.day
                            price = if (!item.isStandardRate) {
                                i.memberPrice
                            } else i.standardPrice    // or item.standardPrice
                            tax = if (!item.isStandardRate) {
                                i.memberTax
                            } else i.standardTax    // or item.standardTax.toDouble()
                            taxPrice = if (!item.isStandardRate) {
                                i.memberTaxPrice
                            } else i.standardTaxPrice   // or item.standardTaxPrice
                        }
                        dayPriceList.add(request)
                    }
                    bookRoomRequest.dayPrice = dayPriceList
                    bookRoomRequest.taxPercentage = 0
                    bookRoomRequest.taxAmount = dayPriceList.sumOf { it.taxPrice }

                    onItemSelectRoomClick?.invoke(
                        position, bookRoomRequest, if (!item.isStandardRate) {
                            item.memberPricePerDay
                        } else item.standardPricePerDay, item.isStandardRate
                    )
                }

            }

            roomRatesAdapter.onItemRateDetailsClick = { position, item ->
                onItemRateDetailsClick?.invoke(position, item)
            }

            tvRoomDetails.setOnClickListener {
                onItemRoomDetailsClick?.invoke(position, mainItem)
            }

            // 👉 Default first image should show in imgMain
            if (mainItem.images.isNotEmpty()) {
                Glide.with(context)
                    .load(mainItem.images[0])
                    .placeholder(R.drawable.ic_placeholder_rectangle)
                    .into(imgMain)
            }

            // Setup small image list with click callback
            val imgAdapter = SmallImagesAdapter(context) { selectedImage ->
                Glide.with(context)
                    .load(selectedImage)
                    .placeholder(R.drawable.ic_placeholder_rectangle)
                    .into(imgMain)
            }

            rvImage.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            rvImage.adapter = imgAdapter
            imgAdapter.setData(mainItem.images)

            rvImage.visibility = if (mainItem.images.size > 1) View.VISIBLE else View.GONE

        }

        holder.itemView.setOnClickListener {
        }
    }

    fun addData(mObj: ArrayList<OfferRoomPriceModel>) {
        objList = ArrayList()
        objList.addAll(mObj)
        this.notifyDataSetChanged()
    }

    fun isStandard() {
        objList.forEach { room ->
            room.offerPrice.forEach { it.isStandardRate = true } // Update data
        }
        this.notifyDataSetChanged()
    }

    fun isUnStandard() {
        objList.forEach { room ->
            room.offerPrice.forEach { it.isStandardRate = false } // Update data
        }
        this.notifyDataSetChanged()
    }
}