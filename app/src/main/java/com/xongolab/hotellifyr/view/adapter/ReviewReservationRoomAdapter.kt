package com.xongolab.hotellifyr.view.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.xongolab.hotellifyr.R
import com.xongolab.hotellifyr.core.CoreActivity
import com.xongolab.hotellifyr.databinding.RawReservationRoomBinding
import com.xongolab.hotellifyr.model.BookRoomRequest

@SuppressLint("NotifyDataSetChanged")
class ReviewReservationRoomAdapter(var context: Context) :
    RecyclerView.Adapter<ReviewReservationRoomAdapter.Holder>() {

    var objList: ArrayList<BookRoomRequest> = ArrayList()

    inner class Holder(val binding: RawReservationRoomBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = RawReservationRoomBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun getItemCount(): Int {
        return objList.size
    }

    @SuppressLint("SetTextI18n", "DefaultLocale")
    override fun onBindViewHolder(holder: Holder, position: Int) {

        val item = objList[position]

        holder.binding.apply {
            tvRoomName.text = item.roomTitle
            tvTotalGuests.text = "${item.adults + item.children} ${context.getString(R.string.guest)}"
            tvDateRange.text = (context as CoreActivity).setPriceWithUnit(item.price)
            ivRoom.setImageURI(item.hotelImageURL)
        }
    }

    fun addData(mObj: ArrayList<BookRoomRequest>) {
        objList = ArrayList()
        objList.addAll(mObj)
        this.notifyDataSetChanged()
    }

}