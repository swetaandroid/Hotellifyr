package com.xongolab.hotellifyr.view.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.xongolab.hotellifyr.databinding.RawNewlyOpenedVerticalBinding
import com.xongolab.hotellifyr.model.HotelModel


@SuppressLint("NotifyDataSetChanged")
class NewlyOpenedAdapter(var context: Context) :
    RecyclerView.Adapter<NewlyOpenedAdapter.Holder>() {

    var objList: ArrayList<HotelModel> = ArrayList()

    var onItemClick: ((position: Int, item: HotelModel) -> Unit)? = null

    inner class Holder(val binding: RawNewlyOpenedVerticalBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding =
            RawNewlyOpenedVerticalBinding.inflate(
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
            tvLocation.text = item.address
            tvRating.text = "${item.starRating}"
        }

        holder.itemView.setOnClickListener {
            onItemClick?.invoke(position, item)
        }
    }

    fun addData(mObj: ArrayList<HotelModel>) {
        objList = ArrayList()
        objList.addAll(mObj)
        this.notifyDataSetChanged()
    }
}