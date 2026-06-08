package com.xongolab.hotellifyr.view.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.xongolab.hotellifyr.R
import com.xongolab.hotellifyr.core.CoreActivity
import com.xongolab.hotellifyr.databinding.RawRoomRatesBinding
import com.xongolab.hotellifyr.model.OfferRoomPriceModel.OfferPrice
import com.xongolab.hotellifyr.utils.Pref
import com.xongolab.hotellifyr.utils.makeGone


@SuppressLint("NotifyDataSetChanged")
class RoomRatesAdapter(var context: Context) :
    RecyclerView.Adapter<RoomRatesAdapter.Holder>() {

    var objList: ArrayList<OfferPrice> = ArrayList()

    var onItemSelectRoomClick: ((position: Int, item: OfferPrice) -> Unit)? = null
    var onItemRateDetailsClick: ((position: Int, item: OfferPrice) -> Unit)? = null

    inner class Holder(val binding: RawRoomRatesBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding =
            RawRoomRatesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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
            tvPrice.text = (context as CoreActivity).setPriceWithUnit(if (item.isStandardRate) item.standardPricePerDay else item.memberPricePerDay)

            if (item.isStandardRate){
                btnSelectRoom.backgroundTintList = ContextCompat.getColorStateList(context as CoreActivity, R.color.colorPrimary)
            }
            if (position == objList.size - 1) {
                divider.makeGone()
            }

            if (!Pref.getBooleanValue(Pref.PREF_IS_LOGIN,false) && !item.isStandardRate){
                btnSelectRoom.text = context.getString(R.string.login)
            }else{
                btnSelectRoom.text = context.getString(R.string.select_room)
            }
            btnSelectRoom.setOnClickListener {
                onItemSelectRoomClick?.invoke(position, item)
            }

            tvRateDetails.setOnClickListener {
                onItemRateDetailsClick?.invoke(position, item)
            }
        }

    }

    fun addData(mObj: ArrayList<OfferPrice>) {
        objList.clear()
        objList.addAll(mObj)
        this.notifyDataSetChanged()
    }
}