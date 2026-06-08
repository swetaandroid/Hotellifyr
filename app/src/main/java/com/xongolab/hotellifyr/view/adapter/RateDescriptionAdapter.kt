package com.xongolab.hotellifyr.view.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.xongolab.hotellifyr.databinding.RawRateDescriptionBinding
import com.xongolab.hotellifyr.model.OfferRoomPriceModel.Amenity


@SuppressLint("NotifyDataSetChanged")
class RateDescriptionAdapter(var context: Context) :
    RecyclerView.Adapter<RateDescriptionAdapter.Holder>() {

    var objList: ArrayList<String> = ArrayList()


    inner class Holder(val binding: RawRateDescriptionBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding =
            RawRateDescriptionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun getItemCount(): Int {
        return objList.size
    }

    @SuppressLint("SetTextI18n", "UseCompatLoadingForDrawables")
    override fun onBindViewHolder(holder: Holder, @SuppressLint("RecyclerView") position: Int) {
        val item = objList[position]
        holder.binding.apply {
            tvDescription.text = item
        }
    }

    fun addData(mObj: ArrayList<String>) {
        objList = ArrayList()
        objList.addAll(mObj)
        this.notifyDataSetChanged()
    }
}