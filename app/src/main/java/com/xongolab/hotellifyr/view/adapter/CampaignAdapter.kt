package com.xongolab.hotellifyr.view.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.xongolab.hotellifyr.databinding.RawCampaignBinding
import com.xongolab.hotellifyr.model.CampaignModel.CampaignDetail


@SuppressLint("NotifyDataSetChanged")
class CampaignAdapter(var context: Context) :
    RecyclerView.Adapter<CampaignAdapter.Holder>() {

    private var objList: ArrayList<CampaignDetail> = arrayListOf()

    var onItemClick: ((item: CampaignDetail) -> Unit)? = null


    inner class Holder(val binding: RawCampaignBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding =
            RawCampaignBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun getItemCount(): Int {
        return objList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: Holder, position: Int) {
        val item = objList[position]

        holder.binding.apply {
            ivIcon.setImageURI(item.icon)
            tvTitle.text = item.title
            tvDesc.text = item.description
        }

        holder.itemView.setOnClickListener {
            onItemClick?.invoke(item)
        }
    }

    fun addData(mObj: ArrayList<CampaignDetail>) {
        objList = ArrayList()
        objList.addAll(mObj)
        this.notifyDataSetChanged()
    }
}