package com.xongolab.hotellifyr.view.adapter.hotel

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.xongolab.hotellifyr.R
import com.xongolab.hotellifyr.databinding.RawHotelTypeBinding
import com.xongolab.hotellifyr.model.HotelModel.ExperienceData
import com.xongolab.hotellifyr.utils.makeGone
import com.xongolab.hotellifyr.utils.makeVisible

@SuppressLint("NotifyDataSetChanged")
class HotelDetailActivitiesAdapter(var context: Context, var clickListener: View.OnClickListener) :
    RecyclerView.Adapter<HotelDetailActivitiesAdapter.Holder>() {

    var objList: ArrayList<ExperienceData> = arrayListOf()
    private var selectedPosition = -1

    inner class Holder(val binding: RawHotelTypeBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding =
            RawHotelTypeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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
            ivHotel.makeGone()

            ivIcon.makeVisible()

            ivIcon.setImageURI(item.icon)

            llHotelType.background =
                ContextCompat.getDrawable(context,if (selectedPosition == position) R.drawable.dr_secondary_border_5 else R.drawable.dr_gray_light_border_5)

            llHotelType.tag = position
            llHotelType.setOnClickListener{
                selectedPosition = position
                notifyDataSetChanged()
            }
        }
    }

    fun addData(mObj: ArrayList<ExperienceData>) {
        objList = ArrayList()
        objList.addAll(mObj)
        this.notifyDataSetChanged()
    }
}