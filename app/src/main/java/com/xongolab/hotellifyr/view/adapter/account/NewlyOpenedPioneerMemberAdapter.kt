package com.xongolab.hotellifyr.view.adapter.account

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.xongolab.hotellifyr.databinding.RawNewlyOpenedPioneerMemberBinding
import com.xongolab.hotellifyr.model.ResortModel


@SuppressLint("NotifyDataSetChanged")
class NewlyOpenedPioneerMemberAdapter(var context: Context, var clickListener: View.OnClickListener) :
    RecyclerView.Adapter<NewlyOpenedPioneerMemberAdapter.Holder>() {

    var objList: ArrayList<ResortModel> = ArrayList()

    inner class Holder(val binding: RawNewlyOpenedPioneerMemberBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding =
            RawNewlyOpenedPioneerMemberBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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
            tvLocation.text = item.name
            imgNewlyOpen.setImageResource(item.profileImg)

            cvNewlyOpened.tag = position
            cvNewlyOpened.setOnClickListener(clickListener)
        }
    }

    fun addData(mObj: ArrayList<ResortModel>) {
        objList = ArrayList()
        objList.addAll(mObj)
        this.notifyDataSetChanged()
    }
}