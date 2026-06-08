package com.xongolab.hotellifyr.view.adapter.hotel.map

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.xongolab.hotellifyr.R
import com.xongolab.hotellifyr.databinding.RawMapNavigationTitleBinding
import com.xongolab.hotellifyr.model.HotelModel
import com.xongolab.hotellifyr.model.ResortModel


@SuppressLint("NotifyDataSetChanged")
class MapNavigationTitleAdapter(var context: Context) :
    RecyclerView.Adapter<MapNavigationTitleAdapter.Holder>() {

    var objList: ArrayList<HotelModel> = ArrayList()

    var onItemClick: ((position: Int, item: HotelModel) -> Unit)? = null

    inner class Holder(val binding: RawMapNavigationTitleBinding) :
        RecyclerView.ViewHolder(binding.root) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding =
            RawMapNavigationTitleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun getItemCount(): Int {
        return objList.size
    }

    @SuppressLint("SetTextI18n", "UseCompatLoadingForDrawables")
    override fun onBindViewHolder(holder: Holder, @SuppressLint("RecyclerView") position: Int) {
        val item = objList[position]
        holder.binding.apply {
            tvTitle.text = item.name

            tvTitle.setTextColor(ContextCompat.getColor(context, if (item.isSelected) R.color.colorPrimary else R.color.colorGray))
            viewBorder.isVisible = item.isSelected
            viewBorder.setBackgroundColor(
                ContextCompat.getColor(
                    context,
                    R.color.colorPrimary
                )
            )

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

