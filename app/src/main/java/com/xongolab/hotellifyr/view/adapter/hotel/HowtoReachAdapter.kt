package com.xongolab.hotellifyr.view.adapter.hotel

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.xongolab.hotellifyr.databinding.RawHowToReachBinding
import com.xongolab.hotellifyr.model.HotelModel.HowToReach


@SuppressLint("NotifyDataSetChanged")
class HowtoReachAdapter(var context: Context, var clickListener: View.OnClickListener) :
    RecyclerView.Adapter<HowtoReachAdapter.Holder>() {

    var objList: ArrayList<HowToReach> = arrayListOf()

    inner class Holder(val binding: RawHowToReachBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding =
            RawHowToReachBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun getItemCount(): Int {
        return objList.size
    }

    @SuppressLint("SetTextI18n", "UseCompatLoadingForDrawables", "DefaultLocale")
    override fun onBindViewHolder(holder: Holder, @SuppressLint("RecyclerView") position: Int) {
        val item = objList[position]
        holder.binding.apply {
            tvTitle.text = item.title
            tvKm.text = String.format("%.2f Km", item.distance)

            ivIcon.setImageURI(item.categoryIcon)
        }
    }

    fun addData(mObj: ArrayList<HowToReach>) {
        objList = ArrayList()
        objList.addAll(mObj)
        this.notifyDataSetChanged()
    }
}