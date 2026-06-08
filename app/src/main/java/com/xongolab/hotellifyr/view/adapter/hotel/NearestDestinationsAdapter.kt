package com.xongolab.hotellifyr.view.adapter.hotel

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.xongolab.hotellifyr.databinding.RawHowToReachBinding
import com.xongolab.hotellifyr.model.HotelModel.NearByHotel
import com.xongolab.hotellifyr.utils.makeGone


@SuppressLint("NotifyDataSetChanged")
class NearestDestinationsAdapter(var context: Context, var clickListener: View.OnClickListener) :
    RecyclerView.Adapter<NearestDestinationsAdapter.Holder>() {

    var objList: ArrayList<NearByHotel> = arrayListOf()

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

    @SuppressLint("SetTextI18n", "UseCompatLoadingForDrawables")
    override fun onBindViewHolder(holder: Holder, @SuppressLint("RecyclerView") position: Int) {
        val item = objList[position]
        holder.binding.apply {
            tvTitle.text = item.title
            tvKm.text = item.km

            ivIcon.makeGone()
        }
    }

    fun addData(mObj: ArrayList<NearByHotel>) {
        objList = ArrayList()
        objList.addAll(mObj)
        this.notifyDataSetChanged()
    }
}