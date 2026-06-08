package com.xongolab.hotellifyr.view.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.xongolab.hotellifyr.databinding.RawHotelAmenitiesBinding
import com.xongolab.hotellifyr.model.OfferRoomPriceModel.Amenity


@SuppressLint("NotifyDataSetChanged")
class HotelAmenitiesAdapter(var context: Context) :
    RecyclerView.Adapter<HotelAmenitiesAdapter.Holder>() {

    var objList: ArrayList<Amenity> = ArrayList()

    inner class Holder(val binding: RawHotelAmenitiesBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding =
            RawHotelAmenitiesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun getItemCount(): Int {
        return objList.size
    }

    @SuppressLint("SetTextI18n", "UseCompatLoadingForDrawables")
    override fun onBindViewHolder(holder: Holder, @SuppressLint("RecyclerView") position: Int) {
        val item = objList[position]
        holder.binding.apply {
            ivIcon.setImageURI(item.icon)
            tvTitle.text = item.title
        }
    }

    fun addData(mObj: ArrayList<Amenity>) {
        objList = ArrayList()
        objList.addAll(mObj)
        this.notifyDataSetChanged()
    }
}