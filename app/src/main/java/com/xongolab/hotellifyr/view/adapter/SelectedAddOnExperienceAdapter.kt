package com.xongolab.hotellifyr.view.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.xongolab.hotellifyr.core.CoreActivity
import com.xongolab.hotellifyr.databinding.RawAddOnsExperienceBinding
import com.xongolab.hotellifyr.databinding.RawSelectedAddOnsExperienceBinding
import com.xongolab.hotellifyr.model.AddOnExperience
import com.xongolab.hotellifyr.model.AddOnExperienceRequest


@SuppressLint("NotifyDataSetChanged")
class SelectedAddOnExperienceAdapter(var context: Context) :
    RecyclerView.Adapter<SelectedAddOnExperienceAdapter.Holder>() {

    var objList: ArrayList<AddOnExperienceRequest> = ArrayList()

    var onItemDeleteClick: ((position: Int, item: AddOnExperienceRequest) -> Unit)? = null

    inner class Holder(val binding: RawSelectedAddOnsExperienceBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding =
            RawSelectedAddOnsExperienceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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
            tvPackage.text = item.packageTitle
            tvPrice.text = (context as CoreActivity).setPriceWithUnit(item.price)
            tvTitle.text = item.addOnTitle
            tvPackageTitle.text = item.title

            ivDelete.setOnClickListener {
                onItemDeleteClick?.invoke(position, item)
            }
        }

    }

    fun addData(mObj: ArrayList<AddOnExperienceRequest>) {
        objList = ArrayList()
        objList.addAll(mObj)
        this.notifyDataSetChanged()
    }

    fun removeItem(position: Int) {
        if (position in objList.indices) {
            objList.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, objList.size)
        }
    }
}