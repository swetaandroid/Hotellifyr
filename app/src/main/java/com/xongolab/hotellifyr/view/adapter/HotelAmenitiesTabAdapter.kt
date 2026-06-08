package com.xongolab.hotellifyr.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.xongolab.hotellifyr.R
import com.xongolab.hotellifyr.databinding.RawHotelAmenitiesTabBinding
import com.xongolab.hotellifyr.model.HotelModel


class HotelAmenitiesTabAdapter(var context: Context, var clickListener: View.OnClickListener) :
    RecyclerView.Adapter<HotelAmenitiesTabAdapter.Holder>() {

    var objList: ArrayList<HotelModel> = ArrayList()

    class Holder(val binding: RawHotelAmenitiesTabBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding =
            RawHotelAmenitiesTabBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun getItemCount(): Int {
        return objList.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val item = objList[position]
        holder.binding.apply {
            tvTitle.text = item.title

            tvTitle.setTextColor(ContextCompat.getColor(context, if (item.isSelected)  R.color.colorPrimary else R.color.colorGray) )
            viewBorder.visibility = if (item.isSelected) View.VISIBLE else View.GONE

            tvTitle.post {
                val width = tvTitle.width
                val params = viewBorder.layoutParams
                params.width = width
                viewBorder.layoutParams = params
            }


            llHotelAmenitiesView.tag = position
            llHotelAmenitiesView.setOnClickListener(clickListener)
        }
    }

    fun addData(mObj: ArrayList<HotelModel>) {
        objList = ArrayList()
        objList.addAll(mObj)
        this.notifyDataSetChanged()
    }
}