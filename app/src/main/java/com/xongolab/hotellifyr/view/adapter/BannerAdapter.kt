package com.xongolab.hotellifyr.view.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.xongolab.hotellifyr.R
import com.xongolab.hotellifyr.databinding.RawBannerBinding
import com.xongolab.hotellifyr.model.HomeBanner.BannerData


@SuppressLint("NotifyDataSetChanged")
class BannerAdapter(var context: Context) :
    RecyclerView.Adapter<BannerAdapter.Holder>() {

    var objList: ArrayList<BannerData> = ArrayList()

    class Holder(val binding: RawBannerBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding =
            RawBannerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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

            ivReward.setActualImageResource(R.drawable.ic_placeholder_rectangle)
            if (item.icon.isNotEmpty())
                ivReward.setImageURI(item.icon)

        }
    }

    fun addData(mObj: ArrayList<BannerData>) {
        objList = ArrayList()
        objList.addAll(mObj)
        this.notifyDataSetChanged()
    }
}