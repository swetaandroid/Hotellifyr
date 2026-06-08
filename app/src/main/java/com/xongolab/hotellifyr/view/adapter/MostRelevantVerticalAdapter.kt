package com.xongolab.hotellifyr.view.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.xongolab.hotellifyr.databinding.RawMostRelevantVerticalBinding
import com.xongolab.hotellifyr.model.HotelModel
import com.xongolab.hotellifyr.model.HotelModel.HotelDetail


@SuppressLint("NotifyDataSetChanged")
class MostRelevantVerticalAdapter(var context: Context) :
    RecyclerView.Adapter<MostRelevantVerticalAdapter.Holder>() {

    var onItemClick: ((item: HotelDetail) -> Unit)? = null

    var objList: ArrayList<HotelModel.HotelDetail> = ArrayList()

    inner class Holder(val binding: RawMostRelevantVerticalBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding =
            RawMostRelevantVerticalBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return Holder(binding)
    }

    override fun getItemCount(): Int {
        return objList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: Holder, position: Int) {
        val item = objList[position]
        holder.binding.apply {
            ivIcon.setImageURI(item.icon)

            tvTitle.text = item.name
        }

        holder.itemView.setOnClickListener {
            onItemClick?.invoke(item)
        }
    }

    fun addData(mObj: ArrayList<HotelModel.HotelDetail>) {
        objList = ArrayList()
        objList.addAll(mObj)
        this.notifyDataSetChanged()
    }
}