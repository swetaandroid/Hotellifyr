package com.xongolab.hotellifyr.view.adapter.time_slot

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.xongolab.hotellifyr.databinding.RawOuterTimeSlotBinding
import com.xongolab.hotellifyr.model.TimeSlotModel
import com.xongolab.hotellifyr.view.activity.home.search.hotel.BookTableActivity


@SuppressLint("NotifyDataSetChanged")
class OuterTimeSlotAdapter(var context: Context) :
    RecyclerView.Adapter<OuterTimeSlotAdapter.Holder>() {

    var objList: ArrayList<TimeSlotModel> = ArrayList()

    var onItemClick: ((type: String, item: TimeSlotModel.TimeSlotItem) -> Unit)? = null

    inner class Holder(val binding: RawOuterTimeSlotBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding =
            RawOuterTimeSlotBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun getItemCount(): Int {
        return objList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: Holder, position: Int) {
        val item = objList[position]
        holder.binding.apply {
            tvTitle.text = item.title

            val innerAdapter = InnerTimeSlotAdapter(context, position)
            rvInnerTimeSlot.layoutManager = GridLayoutManager(context, 3)
            rvInnerTimeSlot.adapter = innerAdapter
            innerAdapter.addData(item.timeSlotItem)

            innerAdapter.onItemClick = { categoryPosition, _, item ->
                for(i in objList.indices){
                    for(slot in objList[i].timeSlotItem){
                        slot.isSelected = false
                    }
                }

                item.isSelected = true
                notifyDataSetChanged()

                onItemClick?.invoke(objList[categoryPosition].diningType, item)
            }
        }
    }

    fun addData(mObj: ArrayList<TimeSlotModel>) {
        objList = ArrayList()
        objList.addAll(mObj)
        this.notifyDataSetChanged()
    }
}