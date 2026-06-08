package com.xongolab.hotellifyr.view.adapter.hotel

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.xongolab.hotellifyr.R
import com.xongolab.hotellifyr.databinding.RawDrinkingAndDiningBinding
import com.xongolab.hotellifyr.model.HotelDetailModel
import com.xongolab.hotellifyr.model.HotelModel.VenueData


@SuppressLint("NotifyDataSetChanged")
class WeddingEventAdapter(var context: Context) :
    RecyclerView.Adapter<WeddingEventAdapter.Holder>() {

    var objList: ArrayList<VenueData> = arrayListOf()

    var onItemClick: ((item: VenueData) -> Unit)? = null

    inner class Holder(val binding: RawDrinkingAndDiningBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding =
            RawDrinkingAndDiningBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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
            tvDesc.text = "${item.area} sqm • ${item.guestsCapacity}"

            if (item.images.isNotEmpty())
                ivIcon.setImageURI(item.images[0])

            holder.itemView.setOnClickListener {
                onItemClick!!.invoke(item)
            }
        }
    }

    fun addData(mObj: List<VenueData>) {
        objList = ArrayList()
        objList.addAll(mObj)
        this.notifyDataSetChanged()
    }
}