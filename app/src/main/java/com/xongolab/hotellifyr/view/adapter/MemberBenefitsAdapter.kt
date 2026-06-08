package com.xongolab.hotellifyr.view.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.xongolab.hotellifyr.databinding.RawMemberBenefitsBinding
import com.xongolab.hotellifyr.model.MemberBenefits


@SuppressLint("NotifyDataSetChanged")
class MemberBenefitsAdapter(var context: Context) :
    RecyclerView.Adapter<MemberBenefitsAdapter.Holder>() {

    var objList: ArrayList<MemberBenefits> = ArrayList()

    var onItemClick: ((position: Int) -> Unit)? = null

    inner class Holder(val binding: RawMemberBenefitsBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding =
            RawMemberBenefitsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun getItemCount(): Int {
        return objList.size
    }


    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: Holder, position: Int) {
        val item = objList[position]
        holder.binding.apply {
            ivIcon.setImageResource(item.icon)

            tvBenefits.text = item.title
        }

        holder.itemView.setOnClickListener {
            onItemClick?.invoke(position)
        }
    }

    fun addData(mObj: ArrayList<MemberBenefits>) {
        objList = ArrayList()
        objList.addAll(mObj)
        this.notifyDataSetChanged()
    }
}