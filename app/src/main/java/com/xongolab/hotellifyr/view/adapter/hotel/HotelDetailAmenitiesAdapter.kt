package com.xongolab.hotellifyr.view.adapter.hotel

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.xongolab.hotellifyr.databinding.RawFilterItemsBinding
import com.xongolab.hotellifyr.model.HotelModel.Amenity
import com.xongolab.hotellifyr.utils.makeGone
import com.xongolab.hotellifyr.utils.makeVisible

@SuppressLint("NotifyDataSetChanged")
class HotelDetailAmenitiesAdapter(var context: Context, var clickListener: View.OnClickListener) :
    RecyclerView.Adapter<HotelDetailAmenitiesAdapter.Holder>() {

    var objList: ArrayList<Amenity> = arrayListOf()
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

            cbBrandLocationActivity.makeGone()
            ratingBar.makeGone()
            imgAmenities.makeVisible()
            tvBrandName.makeVisible()

            tvBrandName.text = item.title

            imgAmenities.setImageURI(item.icon)

            cbBrandLocationActivity.tag = position
            cbBrandLocationActivity.setOnClickListener{
                selectedPosition = position
                notifyDataSetChanged()
            }
        }
    }

    fun addData(mObj: ArrayList<Amenity>) {
        objList = ArrayList()
        objList.addAll(mObj)
        this.notifyDataSetChanged()
    }
}