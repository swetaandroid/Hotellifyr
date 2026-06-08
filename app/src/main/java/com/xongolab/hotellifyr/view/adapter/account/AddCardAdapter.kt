package com.xongolab.hotellifyr.view.adapter.account

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.xongolab.hotellifyr.databinding.RawAddCardBinding
import com.xongolab.hotellifyr.model.ResortModel


@SuppressLint("NotifyDataSetChanged")
class AddCardAdapter(var context: Context, var clickListener: View.OnClickListener) :
    RecyclerView.Adapter<AddCardAdapter.Holder>() {

    var objList: ArrayList<ResortModel> = ArrayList()

    inner class Holder(val binding: RawAddCardBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding =
            RawAddCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun getItemCount(): Int {
        return objList.size
    }


    @SuppressLint("SetTextI18n", "UseCompatLoadingForDrawables")
    override fun onBindViewHolder(holder: Holder, @SuppressLint("RecyclerView") position: Int) {
        val item = objList[position]
        holder.binding.apply {
            tvCardName.text = item.title
            tvCardNumber.text = item.name
            imgCard.setImageResource(item.profileImg)

            rlAddCard.tag = position
            rlAddCard.setOnClickListener(clickListener)

        }
    }

    fun addData(mObj: ArrayList<ResortModel>) {
        objList = ArrayList()
        objList.addAll(mObj)
        this.notifyDataSetChanged()
    }
}