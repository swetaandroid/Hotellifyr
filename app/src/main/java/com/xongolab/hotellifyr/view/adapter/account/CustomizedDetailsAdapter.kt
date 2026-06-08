package com.xongolab.hotellifyr.view.adapter.account

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.xongolab.hotellifyr.R
import com.xongolab.hotellifyr.databinding.RawFilterItemsBinding
import com.xongolab.hotellifyr.model.ResortModel


@SuppressLint("NotifyDataSetChanged")
class CustomizedDetailsAdapter(var context: Context, var clickListener: View.OnClickListener) :
    RecyclerView.Adapter<CustomizedDetailsAdapter.Holder>() {

    var objList: ArrayList<ResortModel> = ArrayList()
    private var selectedPosition = -1


    inner class Holder(val binding: RawFilterItemsBinding) :
        RecyclerView.ViewHolder(binding.root)

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
            tvDescription.visibility = View.VISIBLE
            tvBrandName.text = item.title
            tvDescription.text = item.name

            cbBrandLocationActivity.setImageResource(if (selectedPosition == position) R.drawable.cb_check else R.drawable.cb_uncheck)

            cbBrandLocationActivity.tag = position
            cbBrandLocationActivity.setOnClickListener {
                selectedPosition = position
                notifyDataSetChanged()
            }
        }
    }

    fun addData(mObj: ArrayList<ResortModel>) {
        objList = ArrayList()
        objList.addAll(mObj)
        this.notifyDataSetChanged()
    }
}