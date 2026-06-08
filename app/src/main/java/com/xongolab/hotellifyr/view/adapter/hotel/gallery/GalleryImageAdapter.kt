package com.xongolab.hotellifyr.view.adapter.hotel.gallery

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.xongolab.hotellifyr.databinding.RawGalleryImageBinding

class GalleryImageAdapter(
    private val images: List<String>,
) : RecyclerView.Adapter<GalleryImageAdapter.GalleryViewHolder>() {

    var onItemClick: ((position: Int, imageUrl: String) -> Unit)? = null

    inner class GalleryViewHolder(val binding: RawGalleryImageBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryViewHolder {
        val binding =
            RawGalleryImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GalleryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GalleryViewHolder, position: Int) {
        val imageRes = images[position]

        holder.binding.ivGallery.setImageURI(imageRes)

        holder.binding.root.setOnClickListener {
            onItemClick?.invoke(position,imageRes)
        }
    }

    override fun getItemCount(): Int {
        return images.size
    }
}