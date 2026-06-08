package com.xongolab.hotellifyr.view.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.xongolab.hotellifyr.R
import com.xongolab.hotellifyr.databinding.RawPaymentMethodBinding
import com.xongolab.hotellifyr.model.PaymentMethod


@SuppressLint("NotifyDataSetChanged")
class PaymentMethodAdapter(var context: Context) :
    RecyclerView.Adapter<PaymentMethodAdapter.Holder>() {

    var objList: ArrayList<PaymentMethod> = ArrayList()

    var onItemClick: ((position: Int) -> Unit)? = null
    var selectedPosition = -1


    inner class Holder(val binding: RawPaymentMethodBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding =
            RawPaymentMethodBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun getItemCount(): Int {
        return objList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: Holder, position: Int) {
        val item = objList[position]
        holder.binding.apply {
            ivIcon.setImageResource(item.icon)
            tvPaymentType.text = item.type

            // Change radio icon based on selection
            if (position == selectedPosition) {
                icSelectRadio.setImageResource(R.drawable.ic_radio_select_primary)
            } else {
                icSelectRadio.setImageResource(R.drawable.ic_radio_unselect)
            }
        }


        holder.itemView.setOnClickListener {
            selectedPosition = position
            notifyDataSetChanged()
            onItemClick?.invoke(position)
        }
    }

    fun addData(mObj: ArrayList<PaymentMethod>) {
        objList = ArrayList()
        objList.addAll(mObj)
        this.notifyDataSetChanged()
    }
}