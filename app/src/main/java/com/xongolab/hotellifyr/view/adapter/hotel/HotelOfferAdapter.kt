package com.xongolab.hotellifyr.view.adapter.hotel

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.xongolab.hotellifyr.databinding.RawHotelOfferBinding
import com.xongolab.hotellifyr.databinding.RawViewAllHotelBinding
import com.xongolab.hotellifyr.model.HotelModel.HotelOffersSection

@SuppressLint("NotifyDataSetChanged")
class HotelOfferAdapter(
    private val context: Context,
    private val isDetailActivity: Boolean
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var objList: ArrayList<HotelOffersSection> = arrayListOf()

    var onItemClick: ((item: HotelOffersSection) -> Unit)? = null

    private val VIEW_TYPE_OFFER = 1
    private val VIEW_TYPE_VIEW_ALL = 2

    inner class OfferViewHolder(val binding: RawHotelOfferBinding) :
        RecyclerView.ViewHolder(binding.root) {}

    inner class ViewAllViewHolder(val binding: RawViewAllHotelBinding) :
        RecyclerView.ViewHolder(binding.root) {}

    override fun getItemViewType(position: Int): Int {
        return if (isDetailActivity) {
            VIEW_TYPE_OFFER
        } else {
            VIEW_TYPE_VIEW_ALL
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_OFFER -> {
                val binding = RawHotelOfferBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                OfferViewHolder(binding)
            }
            VIEW_TYPE_VIEW_ALL -> {
                val binding = RawViewAllHotelBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                ViewAllViewHolder(binding)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun getItemCount(): Int {
        return objList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = objList[position]

        when (holder) {
            is OfferViewHolder -> {
                // Binding for RawHotelOfferBinding
                holder.binding.apply {
                    tvTitle.text = item.title

                    if (item.images.isNotEmpty())
                        ivIcon.setImageURI(item.images.first())

                    cvOffer.setOnClickListener {
                        onItemClick!!.invoke(item)
                    }
                }
            }
            is ViewAllViewHolder -> {
                // Binding for RawViewAllHotelBinding
                holder.binding.apply {
                    tvTitle.text = item.title

                    if (item.images.isNotEmpty())
                        ivIcon.setImageURI(item.images.first())

                    cvViewAllOffer.setOnClickListener {
                        onItemClick!!.invoke(item)
                    }
                }
            }
        }
    }

    fun addData(mObj: ArrayList<HotelOffersSection>) {
        objList = ArrayList()
        objList.addAll(mObj)
        this.notifyDataSetChanged()
    }
}