package com.xongolab.hotellifyr.view.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.xongolab.hotellifyr.databinding.RawMyPreferencesBinding
import com.xongolab.hotellifyr.model.PreferencesModel

@SuppressLint("NotifyDataSetChanged")
class MyPreferencesListAdapter(var context: Context) :
    RecyclerView.Adapter<MyPreferencesListAdapter.Holder>() {

    var objList: ArrayList<PreferencesModel> = ArrayList()

    var onItemClick: ((position: Int,item : PreferencesModel) -> Unit)? = null

    inner class Holder(val binding: RawMyPreferencesBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding =
            RawMyPreferencesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun getItemCount(): Int {
        return objList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: Holder, position: Int) {
        val item = objList[position]
        holder.binding.apply {
            tvPreferencesTitle.text = item.title
        }

        holder.itemView.setOnClickListener {
            onItemClick?.invoke(position,item)
        }
    }

    fun addData(mObj: ArrayList<PreferencesModel>) {
        objList = ArrayList()
        objList.addAll(mObj)
        this.notifyDataSetChanged()
    }
    fun clearData() {
        objList.clear()
        notifyDataSetChanged()
    }
}