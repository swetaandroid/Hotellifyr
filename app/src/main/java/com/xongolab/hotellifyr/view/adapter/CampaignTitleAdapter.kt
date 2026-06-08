package com.xongolab.hotellifyr.view.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.xongolab.hotellifyr.databinding.RawCampaignTitleBinding
import com.xongolab.hotellifyr.model.CampaignModel
import com.xongolab.hotellifyr.model.CampaignModel.CampaignDetail
import com.xongolab.hotellifyr.utils.makeGone
import com.xongolab.hotellifyr.utils.makeVisible


@SuppressLint("NotifyDataSetChanged")
class CampaignTitleAdapter(var context: Context) :
    RecyclerView.Adapter<CampaignTitleAdapter.Holder>() {

    var objList: ArrayList<CampaignModel> = ArrayList()

    var onItemClick: ((id: String, item: CampaignDetail) -> Unit)? = null

    inner class Holder(val binding: RawCampaignTitleBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding =
            RawCampaignTitleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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

            val adapter = CampaignAdapter(context)
            rvCampaign.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            rvCampaign.adapter = adapter

            adapter.addData(item.campaignDetail)

            if (item.campaignDetail.size > 0){
                tvTitle.makeVisible()
                rvCampaign.makeVisible()
            }else{
                tvTitle.makeGone()
                rvCampaign.makeGone()
            }

            adapter.onItemClick = {
                onItemClick?.invoke(item.id, it)
            }
        }
    }

    fun addData(mObj: ArrayList<CampaignModel>) {
        objList = ArrayList()
        objList.addAll(mObj)
        this.notifyDataSetChanged()
    }
}