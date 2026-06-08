package com.xongolab.hotellifyr.view.adapter.hotel

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.xongolab.hotellifyr.R
import com.xongolab.hotellifyr.core.CoreActivity
import com.xongolab.hotellifyr.databinding.RawDayWisePriceBinding
import com.xongolab.hotellifyr.model.DayPriceRequest

@SuppressLint("NotifyDataSetChanged")
class DayWisePriceThirdAdapter(var context: Context) :
    RecyclerView.Adapter<DayWisePriceThirdAdapter.Holder>() {

    private var objList: ArrayList<DayPriceRequest> = arrayListOf()

    inner class Holder(val binding: RawDayWisePriceBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding =
            RawDayWisePriceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun getItemCount(): Int {
        return objList.size
    }


    @SuppressLint("SetTextI18n", "UseCompatLoadingForDrawables")
    override fun onBindViewHolder(holder: Holder, @SuppressLint("RecyclerView") position: Int) {
        val item = objList[position]
        holder.binding.apply {
            tvDay.text = item.day + " -"
            tvPrice.text = "${(context as CoreActivity).setPriceWithUnit(item.price)} ${context.getString(R.string.night_2)}"
        }
    }

    fun addData(mObj: ArrayList<DayPriceRequest>) {
        objList.clear()
        objList = ArrayList()
        objList.addAll(mObj)
        this.notifyDataSetChanged()
    }
}