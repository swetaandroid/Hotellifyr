package com.xongolab.hotellifyr.view.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.xongolab.hotellifyr.databinding.RawMemberBenefitsCarouselSliderBinding
import com.xongolab.hotellifyr.model.ResortModel

@SuppressLint("NotifyDataSetChanged")
class MemberBenefitsCarouselSliderAdapter(var context: Context) :
    RecyclerView.Adapter<MemberBenefitsCarouselSliderAdapter.Holder>() {

    var objList: ArrayList<ResortModel> = ArrayList()

    var onItemClick: ((position: Int) -> Unit)? = null

    inner class Holder(val binding: RawMemberBenefitsCarouselSliderBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = RawMemberBenefitsCarouselSliderBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return Holder(binding)
    }

    override fun getItemCount(): Int {
        return objList.size
    }


    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: Holder, position: Int) {
        val item = objList[position]
        holder.binding.apply {
            ivProfile.setImageResource(item.profileImg)
        }

        holder.itemView.setOnClickListener {
            onItemClick?.invoke(position)
        }
    }

    fun addData(mObj: ArrayList<ResortModel>) {
        objList = ArrayList()
        objList.addAll(mObj)
        this.notifyDataSetChanged()
    }
}