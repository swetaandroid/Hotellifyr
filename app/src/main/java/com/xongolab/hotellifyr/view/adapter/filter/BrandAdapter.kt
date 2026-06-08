package com.xongolab.hotellifyr.view.adapter.filter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.xongolab.hotellifyr.R
import com.xongolab.hotellifyr.databinding.RawFilterItemsBinding
import com.xongolab.hotellifyr.model.HotelModel
import com.xongolab.hotellifyr.model.ResortModel


@SuppressLint("NotifyDataSetChanged")
class BrandAdapter(var context: Context, var clickListener: View.OnClickListener) :
    RecyclerView.Adapter<BrandAdapter.Holder>() {

    var objList: ArrayList<HotelModel> = ArrayList()

    var onItemClick: ((position: Int, item: HotelModel) -> Unit)? = null

    inner class Holder(val binding: RawFilterItemsBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding =
            RawFilterItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun getItemCount(): Int {
        return objList.size
    }


    @SuppressLint("SetTextI18n", "UseCompatLoadingForDrawables")
    override fun onBindViewHolder(holder: Holder, @SuppressLint("RecyclerView") position: Int) {
        val item = objList[position]
        holder.binding.apply {
            tvBrandName.visibility = View.VISIBLE
            cbBrandLocationActivity.visibility = View.VISIBLE
            tvBrandName.text = item.title

            cbBrandLocationActivity.setImageResource(if (item.isChecked) R.drawable.cb_check else R.drawable.cb_uncheck)

            holder.itemView.setOnClickListener {
                onItemClick?.invoke(position, item)
            }
        }
    }

    fun addData(mObj: ArrayList<HotelModel>) {
        objList = ArrayList()
        objList.addAll(mObj)
        this.notifyDataSetChanged()
    }
}