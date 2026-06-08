package com.xongolab.hotellifyr.view.adapter.filter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.xongolab.hotellifyr.R
import com.xongolab.hotellifyr.databinding.RawHotelTypeBinding
import com.xongolab.hotellifyr.model.HotelModel
import com.xongolab.hotellifyr.model.ResortModel


@SuppressLint("NotifyDataSetChanged")
class HotelTypeAdapter(var context: Context) :
    RecyclerView.Adapter<HotelTypeAdapter.Holder>() {

    var objList: ArrayList<HotelModel> = ArrayList()

    var onItemClick: ((position: Int, item: HotelModel) -> Unit)? = null

    inner class Holder(val binding: RawHotelTypeBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding =
            RawHotelTypeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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
            ivHotel.setImageResource(item.resourceImage!!)

            llHotelType.background = ContextCompat.getDrawable(context, if (item.isChecked) R.drawable.dr_secondary_border_5 else R.drawable.dr_gray_light_border_5)

            holder.itemView.setOnClickListener {
                onItemClick?.invoke(position, item)
            }
        }
    }

    fun addData(mObj: ArrayList<HotelModel>) {
        objList = ArrayList()
        objList.addAll(mObj)
        this.notifyDataSetChanged()
    }
}