package com.xongolab.hotellifyr.view.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.xongolab.hotellifyr.R
import com.xongolab.hotellifyr.core.CoreActivity
import com.xongolab.hotellifyr.databinding.RawPackagesBinding
import com.xongolab.hotellifyr.model.AddOnExperience.Package


@SuppressLint("NotifyDataSetChanged")
class PackagesAdapter(var context: Context, var clickListener: View.OnClickListener) :
    RecyclerView.Adapter<PackagesAdapter.Holder>() {

    var objList: ArrayList<Package> = ArrayList()

    var onItemClick: ((position: Int) -> Unit)? = null

    inner class Holder(val binding: RawPackagesBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding =
            RawPackagesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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
            tvPrice.text = (context as CoreActivity).setPriceWithUnit(item.price)
            tvTitle.text = item.title
            tvDescription.text = item.description

            (context as CoreActivity).makeLinks(
                tvType,
                "",
                Pair((context as CoreActivity).setPriceWithUnit(item.price), View.OnClickListener {
                }),
            )

            val rateDescriptionAdapter =
                RateDescriptionAdapter(context)
            rvDetails.layoutManager = GridLayoutManager(context, 2)
            rvDetails.adapter = rateDescriptionAdapter

            rateDescriptionAdapter.addData(item.details)

            btnAdd.setOnClickListener {
                onItemClick?.invoke(position)
            }
        }
    }

    fun addData(mObj: ArrayList<Package>) {
        objList = ArrayList()
        objList.addAll(mObj)
        this.notifyDataSetChanged()
    }
}