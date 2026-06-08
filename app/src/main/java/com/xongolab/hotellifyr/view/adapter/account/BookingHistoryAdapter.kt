package com.xongolab.hotellifyr.view.adapter.account

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.xongolab.hotellifyr.R
import com.xongolab.hotellifyr.core.CoreActivity
import com.xongolab.hotellifyr.databinding.LoaderItemBinding
import com.xongolab.hotellifyr.databinding.RawWishListBinding
import com.xongolab.hotellifyr.model.BookingHistoryModel
import com.xongolab.hotellifyr.utils.Util

@SuppressLint("NotifyDataSetChanged", "SetTextI18n", "DefaultLocale")
class BookingHistoryAdapter(
    private val context: Context,
    private val clickListener: View.OnClickListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var isLoaderVisible = false
    private val ITEM_NORMAL = 1
    private val ITEM_LOADER = 2

    var objList: ArrayList<BookingHistoryModel.BookingData> = ArrayList()

    inner class Holder(val binding: RawWishListBinding) :
        RecyclerView.ViewHolder(binding.root)

    inner class LoaderHolder(val binding: LoaderItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    // ---------------------------
    // VIEW TYPE
    // ---------------------------
    override fun getItemViewType(position: Int): Int {
        return if (isLoaderVisible && position == itemCount - 1) {
            ITEM_LOADER
        } else {
            ITEM_NORMAL
        }
    }

    // ---------------------------
    // ITEM COUNT
    // ---------------------------
    override fun getItemCount(): Int {
        return objList.size + if (isLoaderVisible) 1 else 0
    }

    // ---------------------------
    // CREATE HOLDER
    // ---------------------------
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == ITEM_LOADER) {
            val binding = LoaderItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            LoaderHolder(binding)
        } else {
            val binding = RawWishListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            Holder(binding)
        }
    }

    // ---------------------------
    // BIND HOLDER
    // ---------------------------
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if (holder is Holder) {
            val item = objList[position]

            holder.binding.apply {

                rvImage.visibility = View.GONE
                llWishList.visibility = View.GONE
                llBookingHistory.visibility = View.VISIBLE

                btnViewHotel.text = context.getString(R.string.book_now)
                btnViewHotel.visibility =
                    if (item.bookingStatus == "cancelled") View.VISIBLE else View.GONE

                tvStar.text = item.hotelDetails.starRating.toString()
                tvKmBookingHistory.text = item.hotelDetails.address
                tvTitleBookingHistory.text = item.hotelDetails.name

                tvPriceBookingHistory.text =
                    (context as CoreActivity).setPriceWithUnit(item.totalPayableAmount.toDouble())

                val checkIn = Util.formatTimestamp(
                    item.checkIn,
                    "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
                    "YYYY-MM-dd"
                )
                val checkOut = Util.formatTimestamp(
                    item.checkOut,
                    "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
                    "YYYY-MM-dd"
                )

                val totalPersons = item.bookRoom.sumOf { it.adults + it.children }
                tvTotalGuests.text = "$totalPersons ${context.getString(R.string.guest)}"
                tvNoOfNights.text = "${item.numberOfDays} ${context.getString(R.string.nights)}"

                tvCheckInDate.text = checkIn
                tvCheckOut.text = checkOut

                Glide.with(context)
                    .load(item.hotelDetails.icon)
                    .placeholder(R.drawable.ic_placeholder_rectangle)
                    .into(imgMain)

                ivWish.setImageResource(
                    if (item.isFavorite) R.drawable.ic_favourite
                    else R.drawable.ic_wish
                )

                tvPrice.text =
                    (context as CoreActivity).setPriceWithUnit(item.totalPayableAmount)

                btnViewHotel.tag = position
                btnViewHotel.setOnClickListener(clickListener)

                btnWishlist.tag = position
                btnWishlist.setOnClickListener(clickListener)

                btnCheckBooking.tag = position
                btnCheckBooking.setOnClickListener(clickListener)
            }
        }
    }

    // ---------------------------
    // DATA FUNCTIONS
    // ---------------------------
    fun setData(list: List<BookingHistoryModel.BookingData>) {
        objList.clear()
        objList.addAll(list)
        notifyDataSetChanged()
    }

    fun addData(list: List<BookingHistoryModel.BookingData>) {
        val start = objList.size
        objList.addAll(list)
        notifyItemRangeInserted(start, list.size)
    }

    fun clearData() {
        objList.clear()
        notifyDataSetChanged()
    }

    // ---------------------------
    // LOADER FUNCTIONS
    // ---------------------------

    fun addLoader() {
        if (!isLoaderVisible) {
            isLoaderVisible = true
            notifyItemInserted(itemCount) // loader at end
        }
    }

    fun removeLoader() {
        if (isLoaderVisible) {
            isLoaderVisible = false
            notifyItemRemoved(itemCount) // remove last item (loader)
        }
    }
}
