package com.xongolab.hotellifyr.view.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.xongolab.hotellifyr.R
import com.xongolab.hotellifyr.databinding.RawPagerImageBinding


class SmallImagesAdapter(
    var context: Context,
    val onImageSelected: (String) -> Unit
) : RecyclerView.Adapter<SmallImagesAdapter.Holder>() {

    var images: ArrayList<String> = ArrayList()

    inner class Holder(val binding: RawPagerImageBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding =
            RawPagerImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun getItemCount(): Int = images.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val imageUrl = images[position]

        holder.binding.wishListImage.setActualImageResource(R.drawable.ic_placeholder_square)
        if (imageUrl.isNotEmpty())
            holder.binding.wishListImage.setImageURI(imageUrl)

        holder.binding.wishListImage.setOnClickListener {
            onImageSelected.invoke(imageUrl)

        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(imgList: ArrayList<String>) {
        images.clear()
        images.addAll(imgList)
        notifyDataSetChanged()
    }
}