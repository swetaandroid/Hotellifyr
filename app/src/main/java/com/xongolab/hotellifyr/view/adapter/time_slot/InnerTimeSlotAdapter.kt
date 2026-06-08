package com.xongolab.hotellifyr.view.adapter.time_slot

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.xongolab.hotellifyr.R
import com.xongolab.hotellifyr.databinding.RawInnerTimeSlotBinding
import com.xongolab.hotellifyr.model.AirportPick
import com.xongolab.hotellifyr.model.TimeSlotModel


@SuppressLint("NotifyDataSetChanged")
class InnerTimeSlotAdapter(
    var context: Context, private var categoryPosition: Int
) :
    RecyclerView.Adapter<InnerTimeSlotAdapter.Holder>() {

    var objList: ArrayList<TimeSlotModel.TimeSlotItem> = ArrayList()

    var onItemClick: ((categoryPosition: Int, position: Int, item: TimeSlotModel.TimeSlotItem) -> Unit)? = null

    inner class Holder(val binding: RawInnerTimeSlotBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding =
            RawInnerTimeSlotBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun getItemCount(): Int {
        return objList.size
    }


    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: Holder, @SuppressLint("RecyclerView") position: Int) {
        val item = objList[position]
        holder.binding.apply {
            tvTime.text = item.time

            val isSelected = item.isSelected

            rlInnerTimeSlot.background = ContextCompat.getDrawable(context, if (isSelected) R.drawable.dr_primary_bg_10 else R.drawable.dr_white_fill_gray_border_10)
            tvTime.setTextColor(ContextCompat.getColor(context, if (isSelected) R.color.colorWhite else R.color.colorBlack))

            rlInnerTimeSlot.setOnClickListener {
                onItemClick?.invoke(categoryPosition, position, item)
            }
        }
    }

    fun addData(mObj: ArrayList<TimeSlotModel.TimeSlotItem>) {
        objList = ArrayList()
        objList.addAll(mObj)
        this.notifyDataSetChanged()
    }
}