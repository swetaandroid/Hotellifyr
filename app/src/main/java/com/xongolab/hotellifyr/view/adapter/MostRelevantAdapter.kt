package com.xongolab.hotellifyr.view.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.xongolab.hotellifyr.R
import com.xongolab.hotellifyr.core.CoreActivity
import com.xongolab.hotellifyr.databinding.RawMostRelevantBinding
import com.xongolab.hotellifyr.model.ResortModel


@SuppressLint("NotifyDataSetChanged")
class MostRelevantAdapter(var context: Context) :
    RecyclerView.Adapter<MostRelevantAdapter.Holder>() {

     var objList: ArrayList<ResortModel> = ArrayList()

    var onItemClick: ((position: Int) -> Unit)? = null

    inner class Holder(val binding: RawMostRelevantBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding =
            RawMostRelevantBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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

            tvTitle.text = item.title

            tvGuest.text = "${item.guests} ${context.getString(R.string.guests)}"
            tvBedroom.text = "${item.bedroom} ${context.getString(R.string.bedrooms)}"
            tvBath.text = "${item.bedroom} ${context.getString(R.string.bath)}"

            tvPrice.text = (context as CoreActivity).setPriceWithUnit(item.price)
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