package com.xongolab.hotellifyr.view.adapter.hotel

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.xongolab.hotellifyr.databinding.RawHomeMostRelevantBinding
import com.xongolab.hotellifyr.databinding.RawHotSellingBinding
import com.xongolab.hotellifyr.databinding.RawNewlyLaunchedBinding
import com.xongolab.hotellifyr.databinding.RawNewlyOpenedVerticalBinding
import com.xongolab.hotellifyr.model.HotelModel.HotelDetail


@SuppressLint("NotifyDataSetChanged")
class HotelListAdapter(var context: Context,var type: String) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val TYPE_MOST_RELEVANT = 1
    private val TYPE_NEWLY_LAUNCED = 2
    private val TYPE_NEWLY_OPENED = 3
    private val TYPE_HOT_SELLING = 4

    var objList: ArrayList<HotelDetail> = ArrayList()

    var onItemClick: ((item: HotelDetail) -> Unit)? = null

    inner class MostRelevantHolder(val binding: RawHomeMostRelevantBinding) :
        RecyclerView.ViewHolder(binding.root)

    inner class NewlyLaunchedHolder(val binding: RawNewlyLaunchedBinding) :
        RecyclerView.ViewHolder(binding.root)

    inner class NewlyOpenedHolder(val binding: RawNewlyOpenedVerticalBinding) :
        RecyclerView.ViewHolder(binding.root)

    inner class HotSellingHolder(val binding: RawHotSellingBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder   {

        return when (viewType) {
            TYPE_NEWLY_OPENED -> {
                val binding = RawNewlyOpenedVerticalBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
                NewlyOpenedHolder(binding)
            }
            TYPE_HOT_SELLING -> {
                val binding = RawHotSellingBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
                HotSellingHolder(binding)
            }

            TYPE_NEWLY_LAUNCED -> {
                val binding = RawNewlyLaunchedBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
                NewlyLaunchedHolder(binding)
            }
            else -> {
                val binding = RawHomeMostRelevantBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
                MostRelevantHolder(binding)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when(type){
            "NEWLY_OPENED" -> TYPE_NEWLY_OPENED
            "POPULAR_CHOICE" -> TYPE_NEWLY_LAUNCED
            "HOT_SELLING" -> TYPE_HOT_SELLING
            else -> TYPE_MOST_RELEVANT
        }
    }

    override fun getItemCount(): Int {
        return objList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = objList[position]
        when (holder) {
            is MostRelevantHolder -> {
                holder.binding.apply {
                    ivIcon.setImageURI(item.icon)
                    tvTitle.text = item.name
                }
                holder.itemView.setOnClickListener {
                    onItemClick?.invoke(item)
                }
            }
            is NewlyLaunchedHolder -> {
                holder.binding.apply {
                    ivIcon.setImageURI(item.icon)
                    tvTitle.text = item.name
                }
                holder.itemView.setOnClickListener {
                    onItemClick?.invoke(item)
                }
            }
            is NewlyOpenedHolder -> {
                holder.binding.apply {
                    ivIcon.setImageURI(item.icon)
                    tvTitle.text = item.name
                    tvLocation.text = item.address
                    tvRating.text = "${item.starRating}"
                }
                holder.itemView.setOnClickListener {
                    onItemClick?.invoke(item)
                }
            }
            is HotSellingHolder -> {
                holder.binding.apply {
                    ivIcon.setImageURI(item.icon)
                    tvTitle.text = item.name
                }
                holder.itemView.setOnClickListener {
                    onItemClick?.invoke(item)
                }
            }
        }
    }

    fun addData(mObj: ArrayList<HotelDetail>) {
        objList = ArrayList()
        objList.addAll(mObj)
        this.notifyDataSetChanged()
    }
}