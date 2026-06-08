package com.xongolab.hotellifyr.view.adapter.account

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.xongolab.hotellifyr.R
import com.xongolab.hotellifyr.databinding.RawPioneerMemberDiscountBinding
import com.xongolab.hotellifyr.model.ResortModel
import com.xongolab.hotellifyr.view.activity.account.subscription.MemberDiscountActivity

@SuppressLint("NotifyDataSetChanged")
class PioneerMemberDiscountAdapter(
    private val context: Context,
    private val clickListener: View.OnClickListener
) : RecyclerView.Adapter<PioneerMemberDiscountAdapter.Holder>() {

    var objList: ArrayList<ResortModel> = ArrayList()
    var selectedPosition = -1

    inner class Holder(val binding: RawPioneerMemberDiscountBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = RawPioneerMemberDiscountBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return Holder(binding)
    }

    override fun getItemCount(): Int {
        return objList.size
    }

    @SuppressLint("SetTextI18n", "UseCompatLoadingForDrawables")
    override fun onBindViewHolder(holder: Holder, @SuppressLint("RecyclerView") position: Int) {
        val item = objList[position]

        holder.binding.apply {
            tvDiscount.text = item.title
            tvDiscountTitle.text = item.name
            imgDiscount.setImageResource(item.profileImg)

            llDiscount.background = when (position) {
                0 -> context.getDrawable(R.drawable.dr_gradient_purpal_corner_10)
                1 -> context.getDrawable(R.drawable.dr_gradient_blue_corner_10)
                2 -> context.getDrawable(R.drawable.dr_gradient_red_corner_10)
                3 -> context.getDrawable(R.drawable.dr_gradient_orange_corner_10)
                else -> context.getDrawable(R.drawable.dr_gradient_purpal_corner_10)
            }

            llDiscount.setOnClickListener {
                selectedPosition = position
                notifyDataSetChanged()

                val intent = Intent(context, MemberDiscountActivity::class.java)
                intent.putExtra("selected_position", selectedPosition)
                context.startActivity(intent)
            }
        }
    }

    fun addData(mObj: ArrayList<ResortModel>) {
        objList = ArrayList()
        objList.addAll(mObj)
        notifyDataSetChanged()
    }
}

