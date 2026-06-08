package com.xongolab.hotellifyr.view.adapter.hotel.map

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.xongolab.hotellifyr.databinding.RawRestaurantMenuBinding
import com.xongolab.hotellifyr.model.ResortModel


class RestaurantMenuAdapter(var context: Context, var clickListener: View.OnClickListener) :
    RecyclerView.Adapter<RestaurantMenuAdapter.Holder>() {

    var objList: ArrayList<ResortModel> = ArrayList()

    inner class Holder(val binding: RawRestaurantMenuBinding) :
        RecyclerView.ViewHolder(binding.root) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding =
            RawRestaurantMenuBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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
            icMenu.setImageResource(item.profileImg)

            cvMenu.tag = position
            cvMenu.setOnClickListener(clickListener)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addData(mObj: ArrayList<ResortModel>) {
        objList = ArrayList()
        objList.addAll(mObj)
        this.notifyDataSetChanged()
    }
}