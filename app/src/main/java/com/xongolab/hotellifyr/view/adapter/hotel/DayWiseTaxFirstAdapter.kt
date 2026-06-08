package com.xongolab.hotellifyr.view.adapter.hotel

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.xongolab.hotellifyr.R
import com.xongolab.hotellifyr.core.CoreActivity
import com.xongolab.hotellifyr.databinding.RawDayWisePriceBinding
import com.xongolab.hotellifyr.model.DayPriceRequest
import com.xongolab.hotellifyr.utils.makeGone

@SuppressLint("NotifyDataSetChanged")
class DayWiseTaxFirstAdapter(var context: Context) :
    RecyclerView.Adapter<DayWiseTaxFirstAdapter.Holder>() {

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
//            tvDay.text = item.day + " -"
            tvDay.makeGone()
            tvPrice.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary))
            tvPrice.text = "GST:${item.tax}% - ${(context as CoreActivity).setPriceWithUnit(item.taxPrice)}"
        }
    }

    fun addData(mObj: ArrayList<DayPriceRequest>) {
        objList.clear()
        objList = ArrayList()
        objList.addAll(mObj)
        this.notifyDataSetChanged()
    }
}