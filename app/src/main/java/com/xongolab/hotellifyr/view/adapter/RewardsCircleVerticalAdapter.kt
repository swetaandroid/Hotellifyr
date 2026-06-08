package com.xongolab.hotellifyr.view.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.xongolab.hotellifyr.databinding.RawRewardsCircleVerticalBinding


@SuppressLint("NotifyDataSetChanged")
class RewardsCircleVerticalAdapter(var context: Context, private var images: Array<Int>) :
    RecyclerView.Adapter<RewardsCircleVerticalAdapter.Holder>() {

    var onItemClick: ((position: Int) -> Unit)? = null

    inner class Holder(val binding: RawRewardsCircleVerticalBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding =
            RawRewardsCircleVerticalBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return Holder(binding)
    }

    override fun getItemCount(): Int {
        return images.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.binding.apply {
            ivReward.setImageResource(images[position])
        }

        holder.itemView.setOnClickListener {
            onItemClick?.invoke(position)
        }
    }
}