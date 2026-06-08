package com.xongolab.hotellifyr.view.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.xongolab.hotellifyr.R
import com.xongolab.hotellifyr.core.CoreActivity
import com.xongolab.hotellifyr.databinding.RawCurrentOfferBinding
import com.xongolab.hotellifyr.model.HotelModel
import com.xongolab.hotellifyr.model.OffersModel
import com.xongolab.hotellifyr.model.ResortModel


@SuppressLint("NotifyDataSetChanged")
class CurrentOfferAdapter(var context: Context) :
    RecyclerView.Adapter<CurrentOfferAdapter.Holder>() {

     var objList: ArrayList<OffersModel> = ArrayList()

    var onItemClick: ((item: OffersModel) -> Unit)? = null

    inner class Holder(val binding: RawCurrentOfferBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding =
            RawCurrentOfferBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun getItemCount(): Int {
        return objList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: Holder, position: Int) {
        val item = objList[position]
        holder.binding.apply {
            ivIcon.setImageURI(item.images)
            tvTitle.text = item.title
            tvCouponDiscount.text = item.validity
            tvCouponDiscount.setTextColor(ContextCompat.getColor(context, R.color.colorSecondary))
        }

        holder.itemView.setOnClickListener {
            onItemClick?.invoke(item)
        }
    }

    fun addData(mObj: ArrayList<OffersModel>) {
        objList = ArrayList()
        objList.addAll(mObj)
        this.notifyDataSetChanged()
    }
}