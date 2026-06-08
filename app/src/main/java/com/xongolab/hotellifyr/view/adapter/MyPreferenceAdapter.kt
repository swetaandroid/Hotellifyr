package com.xongolab.hotellifyr.view.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.xongolab.hotellifyr.databinding.RawPreferencesBinding
import com.xongolab.hotellifyr.model.PreferencesItemData

@SuppressLint("NotifyDataSetChanged")
class MyPreferenceAdapter(var context: Context) :
    RecyclerView.Adapter<MyPreferenceAdapter.Holder>() {

    var objList: ArrayList<PreferencesItemData> = ArrayList()
    private lateinit var brandAdapter: MyPreferenceItemAdapter
    
    var onItemClick: ((position: Int, item: PreferencesItemData) -> Unit)? = null

    inner class Holder(val binding: RawPreferencesBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding =
            RawPreferencesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun getItemCount(): Int {
        return objList.size
    }


    @SuppressLint("SetTextI18n", "UseCompatLoadingForDrawables")
    override fun onBindViewHolder(holder: Holder, @SuppressLint("RecyclerView") position: Int) {
        val item = objList[position]
        holder.binding.apply {
            tvTitle.text = item.title

            brandAdapter = MyPreferenceItemAdapter(context)
            rvAccessibility.layoutManager = GridLayoutManager(context, 2)
            brandAdapter.clearData()
            brandAdapter.addData(item.items)
            rvAccessibility.adapter = brandAdapter

        }
    }

    fun addData(mObj: ArrayList<PreferencesItemData>) {
        objList = ArrayList()
        objList.addAll(mObj)
        this.notifyDataSetChanged()
    }
    fun clearData() {
        objList.clear()
        notifyDataSetChanged()
    }
}