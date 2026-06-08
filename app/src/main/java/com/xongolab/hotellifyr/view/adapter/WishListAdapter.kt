package com.xongolab.hotellifyr.view.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.xongolab.hotellifyr.R
import com.xongolab.hotellifyr.core.CoreActivity
import com.xongolab.hotellifyr.databinding.RawWishListBinding
import com.xongolab.hotellifyr.model.FavHotels
import com.xongolab.hotellifyr.utils.Pref
import com.xongolab.hotellifyr.utils.Util.calculateDistance
import com.xongolab.hotellifyr.utils.makeInvisible
import com.xongolab.hotellifyr.utils.makeVisible


@SuppressLint("NotifyDataSetChanged")
class WishListAdapter(var context: Context, var clickListener: View.OnClickListener) :
    RecyclerView.Adapter<WishListAdapter.Holder>() {

    var objList: ArrayList<FavHotels> = ArrayList()
    var onItemClick: ((item: FavHotels) -> Unit)? = null

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

            if (item.position.lat != 0.0) {
                val distance = calculateDistance(
                    Pref.getStringValue(Pref.PREF_CURRENT_LAT, "").toString().toDouble(),
                    Pref.getStringValue(Pref.PREF_CURRENT_LONG, "").toString().toDouble(),
                    item.position.lat,
                    item.position.lng
                )
                tvKm.text = String.format("%.2f km", distance)
                llDistance.makeVisible()
            } else {
                llDistance.makeInvisible()
            }


            val params = root.layoutParams as ViewGroup.MarginLayoutParams
            val extraBottomMargin = if (position == objList.size - 1) {
                val scale = context.resources.displayMetrics.density
                (100 * scale + 0.5f).toInt()
            } else {
                10
            }
            params.bottomMargin = extraBottomMargin
            root.layoutParams = params


            ivWish.setImageResource(R.drawable.ic_favourite)
            btnWishlist.tag = position
            btnWishlist.setOnClickListener(clickListener)

            btnViewHotel.tag = position
            btnViewHotel.setOnClickListener(clickListener)
        }
    }

    fun addData(mObj: ArrayList<FavHotels>) {
        objList = ArrayList()
        objList.addAll(mObj)
        this.notifyDataSetChanged()
    }

    fun clearData() {
        objList.clear()
        notifyDataSetChanged()
    }
}