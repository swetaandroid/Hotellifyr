package com.xongolab.hotellifyr.view.adapter.hotel

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.xongolab.hotellifyr.core.CoreActivity
import com.xongolab.hotellifyr.databinding.RawAddOnBinding
import com.xongolab.hotellifyr.model.AddOnExperienceRequest

@SuppressLint("NotifyDataSetChanged")
class AddOnAdapter(var context: Context) :
    RecyclerView.Adapter<AddOnAdapter.Holder>() {

    private var objList: ArrayList<AddOnExperienceRequest> = arrayListOf()

    inner class Holder(val binding: RawAddOnBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding =
            RawAddOnBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun getItemCount(): Int {
        return objList.size
    }


    @SuppressLint("SetTextI18n", "UseCompatLoadingForDrawables")
    override fun onBindViewHolder(holder: Holder, @SuppressLint("RecyclerView") position: Int) {
        val item = objList[position]
        holder.binding.apply {
            tvTitle.text = item.addOnTitle
            tvPrice.text = "${(context as CoreActivity).setPriceWithUnit(item.price)}"
        }
    }

    fun addData(mObj: ArrayList<AddOnExperienceRequest>) {
        objList.clear()
        objList = ArrayList()
        objList.addAll(mObj)
        this.notifyDataSetChanged()
    }
}