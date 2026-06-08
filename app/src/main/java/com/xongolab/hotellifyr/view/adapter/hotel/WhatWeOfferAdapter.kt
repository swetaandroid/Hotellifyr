package com.xongolab.hotellifyr.view.adapter.hotel

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.xongolab.hotellifyr.R
import com.xongolab.hotellifyr.databinding.RawWhatWeOfferBinding
import com.xongolab.hotellifyr.model.HotelModel.Service


@SuppressLint("NotifyDataSetChanged")
class WhatWeOfferAdapter(var context: Context, var clickListener: View.OnClickListener) :
    RecyclerView.Adapter<WhatWeOfferAdapter.Holder>() {

    var objList: ArrayList<Service> = arrayListOf()
    private var selectedPosition = -1

    inner class Holder(val binding: RawWhatWeOfferBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding =
            RawWhatWeOfferBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun getItemCount(): Int {
        return objList.size
    }


    @SuppressLint("SetTextI18n", "UseCompatLoadingForDrawables")
    override fun onBindViewHolder(holder: Holder, @SuppressLint("RecyclerView") position: Int) {
        val item = objList[position]
        holder.binding.apply {
            tvTitle.text = item.title

            ivIcon.setImageURI(item.icon)

            llOffer.background =
                ContextCompat.getDrawable(context, if (selectedPosition == position)  R.drawable.dr_secondary_border_5 else R.drawable.dr_gray_light_border_5)

            llOffer.tag = position
            llOffer.setOnClickListener {
                selectedPosition = position
                notifyDataSetChanged()
            }
        }
    }

    fun addData(mObj: ArrayList<Service>) {
        objList = ArrayList()
        objList.addAll(mObj)
        this.notifyDataSetChanged()
    }
}