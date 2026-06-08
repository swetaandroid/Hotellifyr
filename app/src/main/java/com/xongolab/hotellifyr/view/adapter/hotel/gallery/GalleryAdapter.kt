package com.xongolab.hotellifyr.view.adapter.hotel.gallery

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.xongolab.hotellifyr.R
import com.xongolab.hotellifyr.databinding.RawGalleryBinding

@SuppressLint("NotifyDataSetChanged")

class GalleryAdapter(
    private val context: Context,
) : RecyclerView.Adapter<GalleryAdapter.Holder>() {

    var objList: ArrayList<String> = arrayListOf()

    var onItemClick: ((position: Int, imageUrl: String) -> Unit)? = null
    var onMoreItemClick: (() -> Unit)? = null

    inner class Holder(val binding: RawGalleryBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = RawGalleryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun getItemCount(): Int {
        return minOf(objList.size, 8)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: Holder, position: Int) {
        val imageUrl = objList[position]
        holder.binding.apply {
            ivGallery.setImageURI(imageUrl)
            ivGallery.visibility = View.VISIBLE

            if (position == 7 && objList.size > 8) {
                viewShadow.visibility = View.VISIBLE
                tvMorePhotos.visibility = View.VISIBLE
                tvMorePhotos.text = "+${objList.size - 8} ${context.getString(R.string.photos)}"

                ivGallery.setOnClickListener {
                    onMoreItemClick?.invoke()
                }
            } else {
                viewShadow.visibility = View.GONE
                tvMorePhotos.visibility = View.GONE
                ivGallery.setOnClickListener {
                    onItemClick?.invoke(position, imageUrl)
                }
            }
        }
    }

    fun addData(mObj: ArrayList<String>) {
        objList = ArrayList()
        objList.addAll(mObj)
        this.notifyDataSetChanged()
    }
}


