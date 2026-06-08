package com.xongolab.hotellifyr.view.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.xongolab.hotellifyr.databinding.RawImageBinding

@SuppressLint("NotifyDataSetChanged")
class ImageAdapter(var context: Context) :
    RecyclerView.Adapter<ImageAdapter.Holder>() {

    var objList: ArrayList<String> = ArrayList()
    var drawableList: ArrayList<Int> = ArrayList()
    var onItemClick: ((position: Int) -> Unit)? = null

    inner class Holder(val binding: RawImageBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = RawImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun getItemCount(): Int {
        if (objList.isNotEmpty()) {
            return objList.size
        } else if (drawableList.isNotEmpty()) {
            return drawableList.size
        }
        return 0
    }

    @SuppressLint("SetTextI18n")

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.binding.apply {
            if (objList.isNotEmpty()) {
                val item = objList[position]
                ivReward.setImageURI(item)
            }else{
                val drawable = drawableList[position]
                ivReward.setImageResource(drawable)
            }
        }

        holder.itemView.setOnClickListener {
            onItemClick?.invoke(position)
        }
    }

    fun addData(mObj: ArrayList<String>) {
        objList = ArrayList()
        objList.addAll(mObj)
        this.notifyDataSetChanged()
    }

    fun addDrawableData(mObj: ArrayList<Int>) {
        drawableList = ArrayList()
        drawableList.addAll(mObj)
        this.notifyDataSetChanged()
    }
}