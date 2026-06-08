package com.xongolab.hotellifyr.view.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.xongolab.hotellifyr.databinding.RawSortBinding
import com.xongolab.hotellifyr.model.ResortModel
import com.xongolab.hotellifyr.model.SortHotel


@SuppressLint("NotifyDataSetChanged")
class SortAdapter(var context: Context) :
    RecyclerView.Adapter<SortAdapter.Holder>() {

    var objList: ArrayList<SortHotel> = ArrayList()
    var onItemClick: ((item: SortHotel) -> Unit)? = null

    inner class Holder(val binding: RawSortBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding =
            RawSortBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun getItemCount(): Int {
        return objList.size
    }

    @SuppressLint("SetTextI18n", "UseCompatLoadingForDrawables")
    override fun onBindViewHolder(holder: Holder, @SuppressLint("RecyclerView") position: Int) {
        val item = objList[position]
        holder.binding.apply {
            tvSort.text = item.title

            radioSort.isChecked = item.isChecked

            holder.itemView.setOnClickListener {
                onItemClick?.invoke(item)
            }
        }
    }

    fun addData(mObj: ArrayList<SortHotel>) {
        objList = ArrayList()
        objList.addAll(mObj)
        this.notifyDataSetChanged()
    }
}