package com.xongolab.hotellifyr.view.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.xongolab.hotellifyr.databinding.RawMemberDiscountBinding
import com.xongolab.hotellifyr.model.MemberDiscount


@SuppressLint("NotifyDataSetChanged")
class MemberDiscountAdapter(var context: Context) :
    RecyclerView.Adapter<MemberDiscountAdapter.Holder>() {

    var objList: ArrayList<MemberDiscount> = ArrayList()

    var onItemClick: ((position: Int) -> Unit)? = null

    inner class Holder(val binding: RawMemberDiscountBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding =
            RawMemberDiscountBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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
            llItem.background = ContextCompat.getDrawable(context, item.background)

            tvTitle.text = item.title
            tvDesc.text = item.description
        }

        holder.itemView.setOnClickListener {
            onItemClick?.invoke(position)
        }
    }

    fun addData(mObj: ArrayList<MemberDiscount>) {
        objList = ArrayList()
        objList.addAll(mObj)
        this.notifyDataSetChanged()
    }
}