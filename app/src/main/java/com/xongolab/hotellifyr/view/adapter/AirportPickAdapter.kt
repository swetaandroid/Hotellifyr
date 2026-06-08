package com.xongolab.hotellifyr.view.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.xongolab.hotellifyr.R
import com.xongolab.hotellifyr.core.CoreActivity
import com.xongolab.hotellifyr.databinding.RawAirportPickBinding
import com.xongolab.hotellifyr.model.AirportPick
import com.xongolab.hotellifyr.model.AirportPickUp


@SuppressLint("NotifyDataSetChanged")
class AirportPickAdapter(var context: Context) :
    RecyclerView.Adapter<AirportPickAdapter.Holder>() {

    var objList: ArrayList<AirportPickUp> = ArrayList()

    var onItemClick: ((position: Int, item: AirportPickUp) -> Unit)? = null

    inner class Holder(val binding: RawAirportPickBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding =
            RawAirportPickBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun getItemCount(): Int {
        return objList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: Holder, position: Int) {
        val item = objList[position]
        holder.binding.apply {
            tvSeat.text = "${item.seater} ${context.getString(R.string.seater)}"
            tvTitle.text = item.vehicle
            tvPrice.text = (context as CoreActivity).setPriceWithUnit(item.price.toDouble())
            ivProfile.setImageURI(item.icon)

            ivRadio.setImageResource(if (item.isSelect) R.drawable.ic_redio_select else R.drawable.ic_radio_unselect)

            ivRadio.setOnClickListener {
                onItemClick?.invoke(position, item)
            }
        }
    }

    fun addData(mObj: ArrayList<AirportPickUp>) {
        objList.clear()
        objList = ArrayList()
        objList.addAll(mObj)
        this.notifyDataSetChanged()
    }
}