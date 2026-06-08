package com.xongolab.hotellifyr.view.adapter.hotel.map

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.model.LatLng
import com.xongolab.hotellifyr.R
import com.xongolab.hotellifyr.databinding.RawMapNavigationBinding
import com.xongolab.hotellifyr.model.HotelModel
import com.xongolab.hotellifyr.model.HotelModel.MapItem
import com.xongolab.hotellifyr.model.ResortModel
import com.xongolab.hotellifyr.utils.Pref
import com.xongolab.hotellifyr.utils.Util.calculateDistance


@SuppressLint("NotifyDataSetChanged")
class MapNavigationAdapter(var context: Context) :
    RecyclerView.Adapter<MapNavigationAdapter.Holder>() {

    var objList: ArrayList<MapItem> = ArrayList()
    var iconUrl: String = ""
    private var mCenterLatLong: LatLng = LatLng(0.0, 0.0)

    var onItemClick: ((position: Int, item: MapItem) -> Unit)? = null

    inner class Holder(val binding: RawMapNavigationBinding) :
        RecyclerView.ViewHolder(binding.root) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding =
            RawMapNavigationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun getItemCount(): Int {
        return objList.size
    }


    @SuppressLint("SetTextI18n", "UseCompatLoadingForDrawables", "DefaultLocale")
    override fun onBindViewHolder(holder: Holder, @SuppressLint("RecyclerView") position: Int) {
        val item = objList[position]
        holder.binding.apply {
            tvTitle.text = item.title
            //tvDescription.text = item.title
            ivIcon.setImageURI(iconUrl)

            val distance = calculateDistance(mCenterLatLong.latitude, mCenterLatLong.longitude, item.latitude, item.longitude)
            tvDescription.text = String.format("%.2f km", distance) + " " + context.getString(R.string.from_hotel)

            btnNavigation.setOnClickListener {
                onItemClick?.invoke(position, item)
            }
        }
    }

    fun addData(mObj: ArrayList<MapItem>, icon: String,centerLatLang : LatLng) {
        mCenterLatLong = centerLatLang
        iconUrl = icon
        objList = ArrayList()
        objList.addAll(mObj)
        this.notifyDataSetChanged()
    }
}