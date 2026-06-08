package com.xongolab.hotellifyr.view.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.xongolab.hotellifyr.databinding.RawImageBinding

class HotelDetailViewPager(var context: Context) :
    RecyclerView.Adapter<HotelDetailViewPager.Holder>() {

    private var objList: ArrayList<String> = arrayListOf()
    var onItemClick: ((position: Int) -> Unit)? = null

    inner class Holder(val binding: RawImageBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = RawImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun getItemCount(): Int {
        return objList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: Holder, position: Int) {
        val imageUrl = objList[position]
        holder.binding.apply {
            ivReward.setImageURI(imageUrl)
        }

        holder.itemView.setOnClickListener {
            onItemClick?.invoke(position)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addData(mObj: ArrayList<String>) {
        objList = ArrayList()
        objList.addAll(mObj)
        this.notifyDataSetChanged()
    }
}