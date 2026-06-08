package com.xongolab.hotellifyr.view.adapter.hotel

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.xongolab.hotellifyr.databinding.RawHotelTagBinding
import com.xongolab.hotellifyr.model.HotelModel
import com.xongolab.hotellifyr.model.HotelModel.HotelDetail


@SuppressLint("NotifyDataSetChanged")
class HotelTagAdapter(var context: Context) :
    RecyclerView.Adapter<HotelTagAdapter.Holder>() {

    var objList: ArrayList<HotelModel> = ArrayList()

    var onItemClick: ((code: String, item: HotelDetail) -> Unit)? = null
    var onViewAllItemClick: ((code: String, item: HotelModel) -> Unit)? = null

    inner class Holder(val binding: RawHotelTagBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding =
            RawHotelTagBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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

            val adapter = HotelListAdapter(context, item.code)
            rvHotel.layoutManager = LinearLayoutManager(context, if (item.code == "NEWLY_OPENED") RecyclerView.VERTICAL else RecyclerView.HORIZONTAL, false)
            rvHotel.adapter = adapter

            adapter.addData(item.hotelDetails)

            tvViewAll.setOnClickListener {
                onViewAllItemClick?.invoke(item.code, item)
            }

            adapter.onItemClick = {
                onItemClick?.invoke(item.code, it)
            }
        }
    }

    fun addData(mObj: ArrayList<HotelModel>) {
        objList = ArrayList()
        objList.addAll(mObj)
        this.notifyDataSetChanged()
    }
}