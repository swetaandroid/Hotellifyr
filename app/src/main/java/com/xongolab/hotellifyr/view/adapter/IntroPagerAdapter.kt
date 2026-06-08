package com.xongolab.hotellifyr.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.xongolab.hotellifyr.databinding.RawIntroListBinding

class IntroPagerAdapter(
    private var images: IntArray
) : RecyclerView.Adapter<IntroPagerAdapter.ImageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = RawIntroListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ImageViewHolder(view)
    }

    override fun getItemCount(): Int {
        return images.size
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.binding.ivIntro.setImageResource(images[position])
    }

    class ImageViewHolder(val binding: RawIntroListBinding) : RecyclerView.ViewHolder(binding.root)

}