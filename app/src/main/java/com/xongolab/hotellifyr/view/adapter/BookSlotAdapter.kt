package com.xongolab.hotellifyr.view.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.xongolab.hotellifyr.R
import com.xongolab.hotellifyr.core.CoreActivity
import com.xongolab.hotellifyr.databinding.RawBookSlotBinding
import com.xongolab.hotellifyr.model.AddOnExperience.Package


@SuppressLint("NotifyDataSetChanged")
class BookSlotAdapter(var context: Context) :
    RecyclerView.Adapter<BookSlotAdapter.Holder>() {

    var objList: ArrayList<Package> = ArrayList()

    var onItemClick: ((position: Int, item: Package) -> Unit)? = null

    inner class Holder(val binding: RawBookSlotBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding =
            RawBookSlotBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun getItemCount(): Int {
        return objList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: Holder, position: Int) {
        val item = objList[position]
        holder.binding.apply {
            tvType.text =
                item.packageX + " • " + (context as CoreActivity).setPriceWithUnit(item.price)

            (context as CoreActivity).makeLinks(
                tvType,
                "",
                Pair((context as CoreActivity).setPriceWithUnit(item.price), View.OnClickListener {
                }),
            )

            tvType.setBackgroundResource(if (item.selected) R.drawable.dr_secondary_border_10 else R.drawable.dr_gray_border_10)

            tvType.setOnClickListener {
                onItemClick?.invoke(position, item)
            }
        }
    }

    fun addData(mObj: ArrayList<Package>) {
        objList = ArrayList()
        objList.addAll(mObj)
        this.notifyDataSetChanged()
    }
}