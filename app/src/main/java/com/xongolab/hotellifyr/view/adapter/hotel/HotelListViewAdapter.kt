package com.xongolab.hotellifyr.view.adapter.hotel

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.xongolab.hotellifyr.R
import com.xongolab.hotellifyr.core.CoreActivity
import com.xongolab.hotellifyr.databinding.RawWishListBinding
import com.xongolab.hotellifyr.model.HotelModel
import com.xongolab.hotellifyr.utils.Pref
import com.xongolab.hotellifyr.utils.Util.calculateDistance2
import com.xongolab.hotellifyr.utils.makeInvisible
import com.xongolab.hotellifyr.utils.makeVisible
import com.xongolab.hotellifyr.view.adapter.SmallImagesAdapter

@SuppressLint("NotifyDataSetChanged")
class HotelListViewAdapter(var context: Context, var clickListener: View.OnClickListener) :
    RecyclerView.Adapter<HotelListViewAdapter.Holder>() {

    var objList: ArrayList<HotelModel> = ArrayList()
    var onItemClick: ((item: HotelModel) -> Unit)? = null

    inner class Holder(val binding: RawWishListBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = RawWishListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun getItemCount(): Int {
        return objList.size
    }

    @SuppressLint("SetTextI18n", "DefaultLocale")
    override fun onBindViewHolder(holder: Holder, position: Int) {

        val item = objList[position]

        holder.binding.apply {
            llWishList.visibility = View.VISIBLE
            llBookingHistory.visibility = View.GONE

            // 👉 Default first image should show in imgMain
            if (item.images.isNotEmpty()) {
                Glide.with(context)
                    .load(item.images[0])
                    .placeholder(R.drawable.ic_placeholder_rectangle)
                    .into(imgMain)
            }

            // Setup small image list with click callback
            val imgAdapter = SmallImagesAdapter(context) { selectedImage ->
                Glide.with(context)
                    .load(selectedImage)
                    .placeholder(R.drawable.ic_placeholder_rectangle)
                    .into(imgMain)
            }

            rvImage.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            rvImage.adapter = imgAdapter
            imgAdapter.setData(item.images)

            rvImage.visibility = if (item.images.size > 1) View.VISIBLE else View.GONE

            tvTitle.text = item.name
            tvStar.text = "" + item.starRating
            tvPrice.text = (context as CoreActivity).setPriceWithUnit(item.price)

           /* if (item.position.lat != 0.0) {
                val currentLat = Pref.getStringValue(Pref.PREF_CURRENT_LAT, "0.0").toDouble()
                val currentLng = Pref.getStringValue(Pref.PREF_CURRENT_LONG, "0.0").toDouble()

                val distance = calculateDistance2(
                    currentLat,
                    currentLng,
                    item.position.lat,
                    item.position.lng
                )

                tvKm.text = String.format("%.1f km", distance)   // Example: 18.2 km

                Log.d("WISH_LIST_DISTANCE", "WISH_LIST_DISTANCE:.. " + distance)
                llDistance.makeVisible()
            } else {
                llDistance.makeInvisible()
            }*/

            ivWish.setImageResource(if (item.isFavorite) R.drawable.ic_favourite else R.drawable.ic_wish)

            btnWishlist.tag = position
            btnWishlist.setOnClickListener(clickListener)
            btnViewHotel.setOnClickListener { onItemClick?.invoke(item) }
        }
    }

    fun addData(mObj: ArrayList<HotelModel>) {
        objList = ArrayList()
        objList.addAll(mObj)
        this.notifyDataSetChanged()
    }
}