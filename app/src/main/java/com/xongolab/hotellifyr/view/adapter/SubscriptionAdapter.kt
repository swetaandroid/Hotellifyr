package com.xongolab.hotellifyr.view.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.xongolab.hotellifyr.databinding.RawSubscriptionPagerItemBinding
import com.xongolab.hotellifyr.model.ResortModel


class SubscriptionAdapter(var context: Context) :
    RecyclerView.Adapter<SubscriptionAdapter.Holder>() {

    var objList: ArrayList<ResortModel> = ArrayList()

    var onItemClick: ((position: Int) -> Unit)? = null

    inner class Holder(val binding: RawSubscriptionPagerItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = RawSubscriptionPagerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun getItemCount(): Int {
        return objList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: Holder, position: Int) {
        val item = objList[position]
        holder.binding.apply {
            icSubscription.setImageResource(item.profileImg)
            tvTitle.text = item.title
        }

        holder.itemView.setOnClickListener {
            onItemClick?.invoke(position)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addData(mObj: ArrayList<ResortModel>) {
        objList = ArrayList()
        objList.addAll(mObj)
        this.notifyDataSetChanged()
    }
}