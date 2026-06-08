package com.xongolab.hotellifyr.view.adapter.filter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.xongolab.hotellifyr.R
import com.xongolab.hotellifyr.databinding.RawAmenitiesBinding
import com.xongolab.hotellifyr.databinding.RawFilterItemsBinding
import com.xongolab.hotellifyr.model.HotelModel
import com.xongolab.hotellifyr.model.ResortModel


@SuppressLint("NotifyDataSetChanged")
class AmenitiesAdapter(var context: Context) :
    RecyclerView.Adapter<AmenitiesAdapter.Holder>() {

    var objList: ArrayList<HotelModel> = ArrayList()

    var onItemClick: ((position: Int, item: HotelModel) -> Unit)? = null

    inner class Holder(val binding: RawAmenitiesBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding =
            RawAmenitiesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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
            ivAmenities.setImageURI(item.icon)

            ivCheck.setImageResource(if (item.isChecked) R.drawable.cb_check else R.drawable.cb_uncheck)

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