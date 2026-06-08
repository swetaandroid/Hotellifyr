package com.xongolab.hotellifyr.view.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.xongolab.hotellifyr.R
import com.xongolab.hotellifyr.databinding.RawFilterItemsBinding
import com.xongolab.hotellifyr.model.PreferencesModel
import com.xongolab.hotellifyr.model.SubPreferencesItemData
import com.xongolab.hotellifyr.utils.Pref

@SuppressLint("NotifyDataSetChanged")
class MyPreferenceItemAdapter(var context: Context) :
    RecyclerView.Adapter<MyPreferenceItemAdapter.Holder>() {

    var objList: ArrayList<SubPreferencesItemData> = ArrayList()

    var onItemClick: ((position: Int, item: SubPreferencesItemData) -> Unit)? = null

    inner class Holder(val binding: RawFilterItemsBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding =
            RawFilterItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun getItemCount(): Int {
        return objList.size
    }


    @SuppressLint("SetTextI18n", "UseCompatLoadingForDrawables")
    override fun onBindViewHolder(holder: Holder, @SuppressLint("RecyclerView") position: Int) {
        val item = objList[position]
        var savedList = Pref.getStringList(Pref.PREF_MY_PREFERENCES)
        item.isChecked = savedList.contains(item._id)

        holder.binding.apply {
            tvBrandName.visibility = View.VISIBLE
            cbBrandLocationActivity.visibility = View.VISIBLE
            tvBrandName.text = item.name

            cbBrandLocationActivity.setImageResource(if (item.isChecked) R.drawable.cb_check else R.drawable.cb_uncheck)

            holder.itemView.setOnClickListener {
                val currentList = Pref.getStringList(Pref.PREF_MY_PREFERENCES)

                if (currentList.contains(item._id)) {
                    // Optional: toggle off
                    currentList.remove(item._id)
                    item.isChecked = false
                } else {
                    currentList.add(item._id)
                    item.isChecked = true
                }

                Pref.setStringList(Pref.PREF_MY_PREFERENCES, currentList)
                notifyItemChanged(position)
            }
        }
    }

    fun addData(mObj: ArrayList<SubPreferencesItemData>) {
        objList = ArrayList()
        objList.addAll(mObj)
        this.notifyDataSetChanged()
    }
    fun clearData() {
        objList.clear()
        notifyDataSetChanged()
    }
}