package com.xongolab.hotellifyr.view.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.xongolab.hotellifyr.databinding.RawPopularHotelBinding
import com.xongolab.hotellifyr.model.HotelModel


@SuppressLint("SetTextI18n", "NotifyDataSetChanged")
class PopularHotelAdapter(var context: Context) :
    RecyclerView.Adapter<PopularHotelAdapter.Holder>(),
    Filterable {

    var objList: ArrayList<HotelModel> = ArrayList()
    var popularObjList: ArrayList<HotelModel> = ArrayList()
    var filteredObjList: ArrayList<HotelModel> = ArrayList()

    var onItemClick: ((position: Int, item: HotelModel) -> Unit)? = null

    var onFilterResultsListener: OnFilterResultsListener? = null

    init {
        filteredObjList.addAll(objList)
    }

    inner class Holder(val binding: RawPopularHotelBinding) :
        RecyclerView.ViewHolder(binding.root) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding =
            RawPopularHotelBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun getItemCount(): Int {
        return filteredObjList.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val item = filteredObjList[position]
        holder.binding.apply {
            tvSearchTitle.text = item.name
        }

        holder.itemView.setOnClickListener {
            onItemClick?.invoke(position, item)
        }
    }

    fun addData(mObj: ArrayList<HotelModel>) {
        objList.clear()
        objList.addAll(mObj)

        this.notifyDataSetChanged()
    }

    fun addPopularData(mObj: ArrayList<HotelModel>) {
        popularObjList.clear()
        popularObjList.addAll(mObj)
        filteredObjList.clear()
        filteredObjList.addAll(mObj)
        this.notifyDataSetChanged()
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filterResults = FilterResults()
                if (constraint.isNullOrEmpty()) {
                    filterResults.values = popularObjList
                } else {
                    val query = constraint.toString().lowercase()
                    val filteredList = objList.filter {
                        it.name.lowercase().contains(query) || it.state.lowercase().contains(query)
                    }
                    filterResults.values = filteredList
                }
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredObjList.clear()
                if (results?.values is List<*>) {
                    filteredObjList.addAll(results.values as List<HotelModel>)
                }
                notifyDataSetChanged()

                if (filteredObjList.isEmpty()) {
                    // Notify that no results are found
                    onFilterResultsListener?.onNoResultsFound()
                } else {
                    // Notify that results are found
                    onFilterResultsListener?.onResultsFound()
                }
            }
        }
    }

    interface OnFilterResultsListener {
        fun onNoResultsFound()
        fun onResultsFound()
    }
}