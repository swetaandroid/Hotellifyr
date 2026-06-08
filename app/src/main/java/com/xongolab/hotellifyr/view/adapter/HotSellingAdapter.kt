package com.xongolab.hotellifyr.view.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.xongolab.hotellifyr.R
import com.xongolab.hotellifyr.databinding.RawHotSellingBinding
import com.xongolab.hotellifyr.model.ResortModel


@SuppressLint("NotifyDataSetChanged")
class HotSellingAdapter(var context: Context, var clickListener: View.OnClickListener) :
    RecyclerView.Adapter<HotSellingAdapter.Holder>() {

    var objList: ArrayList<ResortModel> = ArrayList()

    inner class Holder(val binding: RawHotSellingBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding =
            RawHotSellingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun getItemCount(): Int {
        return objList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: Holder, position: Int) {
        val item = objList[position]
        holder.binding.apply {
            ivIcon.setImageResource(item.profileImg)
            tvTitle.text = item.title
            tvOffer.text = "${item.offer}% ${context.getString(R.string.str_off)}"

            tvBookNow.tag = position
            tvBookNow.setOnClickListener(clickListener)
        }
    }

    fun addData(mObj: ArrayList<ResortModel>) {
        objList = ArrayList()
        objList.addAll(mObj)
        this.notifyDataSetChanged()
    }
}