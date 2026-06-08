package com.xongolab.hotellifyr.view.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.RecyclerView
import com.xongolab.hotellifyr.R
import com.xongolab.hotellifyr.databinding.RawSelectChildAgeBinding
import com.xongolab.hotellifyr.model.ChildAgeModel

@SuppressLint("NotifyDataSetChanged")
class SelectChildAgeAdapter(var context: Context, var clickListener: View.OnClickListener) :
    RecyclerView.Adapter<SelectChildAgeAdapter.Holder>() {

    var objList: ArrayList<ChildAgeModel> = ArrayList()

    private var ageList =
        ArrayList<Int>(listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18))


    inner class Holder(val binding: RawSelectChildAgeBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding =
            RawSelectChildAgeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun getItemCount(): Int {
        return objList.size
    }

    @SuppressLint("SetTextI18n", "UseCompatLoadingForDrawables")
    override fun onBindViewHolder(holder: Holder, @SuppressLint("RecyclerView") position: Int) {
        val item = objList[position]
        holder.binding.apply {
            tvTitle.text = "Child ${position + 1}"

            val adapter = ArrayAdapter(
                context, R.layout.spinner_dropdown_item, ageList
            )

            spChild.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    @SuppressLint("SetTextI18n")
                    override fun onItemSelected(
                        parent: AdapterView<*>, view: View?, position: Int, id: Long
                    ) {
                        if (view != null) {
                            tvChild.text = "" + ageList[position]
                            item.age = ageList[position]
                        }
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {}
                }

            spChild.setPopupBackgroundResource(R.drawable.dr_spinner_bg)

            spChild.adapter = adapter

            llChild.setOnClickListener {
                spChild.performClick()
            }
        }
    }

    fun addData(mObj: ArrayList<ChildAgeModel>) {
        objList = ArrayList()
        objList.addAll(mObj)
        this.notifyDataSetChanged()
    }

    fun addChild() {
        objList.add(ChildAgeModel()) // or whatever your model is
        notifyItemInserted(objList.size - 1)
    }

    fun removeChild() {
        if (objList.isNotEmpty()) {
            objList.removeAt(objList.size - 1)
            notifyItemRemoved(objList.size)
        }
    }
}