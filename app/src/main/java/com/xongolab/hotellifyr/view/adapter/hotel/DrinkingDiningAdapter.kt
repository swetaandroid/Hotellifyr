package com.xongolab.hotellifyr.view.adapter.hotel

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.xongolab.hotellifyr.databinding.RawDrinkingAndDiningBinding
import com.xongolab.hotellifyr.model.HotelModel.DiningData


@SuppressLint("NotifyDataSetChanged")
class DrinkingDiningAdapter(var context: Context) :
    RecyclerView.Adapter<DrinkingDiningAdapter.Holder>() {

    var objList: ArrayList<DiningData> = arrayListOf()
    var onItemClick: ((item: DiningData?) -> Unit)? = null

    inner class Holder(val binding: RawDrinkingAndDiningBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding =
            RawDrinkingAndDiningBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun getItemCount(): Int {
        return objList.size
    }

    @SuppressLint("SetTextI18n", "UseCompatLoadingForDrawables")
    override fun onBindViewHolder(holder: Holder, @SuppressLint("RecyclerView") position: Int) {
        val item = objList[position]
        holder.binding.apply {
            tvTitle.text = item.name
            tvDesc.text = item.cuisineTitles.joinToString(" • ")

            ivIcon.setImageURI(item.icon)

            holder.itemView.setOnClickListener { onItemClick!!.invoke(item) }
        }
    }

    fun addData(mObj: ArrayList<DiningData>) {
        objList = ArrayList()
        objList.addAll(mObj)
        this.notifyDataSetChanged()
    }
}