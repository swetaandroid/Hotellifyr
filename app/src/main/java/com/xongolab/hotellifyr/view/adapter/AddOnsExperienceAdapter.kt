package com.xongolab.hotellifyr.view.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.xongolab.hotellifyr.databinding.RawAddOnsExperienceBinding
import com.xongolab.hotellifyr.model.AddOnExperience


@SuppressLint("NotifyDataSetChanged")
class AddOnsExperienceAdapter(var context: Context) :
    RecyclerView.Adapter<AddOnsExperienceAdapter.Holder>() {

    var objList: ArrayList<AddOnExperience> = ArrayList()

    var onItemClick: ((position: Int, item: AddOnExperience) -> Unit)? = null

    inner class Holder(val binding: RawAddOnsExperienceBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding =
            RawAddOnsExperienceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun getItemCount(): Int {
        return objList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: Holder, position: Int) {
        val item = objList[position]
        holder.binding.apply {
            ivProfile.setImageURI(item.icon)

            tvTitle.text = item.title
            tvDesc.text = item.locatedAt
        }

        holder.itemView.setOnClickListener {
            onItemClick?.invoke(position, item)
        }
    }

    fun addData(mObj: ArrayList<AddOnExperience>) {
        objList = ArrayList()
        objList.addAll(mObj)
        this.notifyDataSetChanged()
    }
}