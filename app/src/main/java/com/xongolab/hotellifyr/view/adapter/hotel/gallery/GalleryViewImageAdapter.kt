package com.xongolab.hotellifyr.view.adapter.hotel.gallery

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.xongolab.hotellifyr.R
import com.xongolab.hotellifyr.databinding.RawViewGalleryImageBinding
import com.xongolab.hotellifyr.model.AirportPick
import com.xongolab.hotellifyr.utils.makeGone
import com.xongolab.hotellifyr.utils.makeVisible


@SuppressLint("NotifyDataSetChanged")
class GalleryViewImageAdapter(var context: Context, private var currentPosition: Int) :
    RecyclerView.Adapter<GalleryViewImageAdapter.Holder>() {

    var objList: List<String?> = listOf()
    private var selectedPosition: Int = -1

    var onItemClick: ((position: Int, imageUrl: String?) -> Unit)? = null

    inner class Holder(val binding: RawViewGalleryImageBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        selectedPosition = currentPosition
        val binding =
            RawViewGalleryImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun getItemCount(): Int {
        return objList.size
    }


    @SuppressLint("SetTextI18n", "UseCompatLoadingForDrawables", "SuspiciousIndentation")
    override fun onBindViewHolder(holder: Holder, @SuppressLint("RecyclerView") position: Int) {
        val item = objList[position]
        holder.binding.apply {
            Glide.with(context).load(item).into(imageGallery)

            if (selectedPosition == position) {
                view.makeGone()
            } else {
                view.makeVisible()
            }

            holder.itemView.setOnClickListener {
                selectedPosition = position
                onItemClick?.invoke(position, item)
                notifyDataSetChanged()
            }
        }
    }

    fun setData(images: List<String?>) {
        objList = images
        selectedPosition = 0
        notifyDataSetChanged()
    }
}