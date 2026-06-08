package com.xongolab.hotellifyr.view.adapter.account

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.xongolab.hotellifyr.databinding.RawTransactionHistoryBinding
import com.xongolab.hotellifyr.model.HotelModel
import com.xongolab.hotellifyr.model.ResortModel
import com.xongolab.hotellifyr.model.TransactionModel
import com.xongolab.hotellifyr.utils.Util
import com.xongolab.hotellifyr.view.adapter.PopularHotelAdapter.OnFilterResultsListener


@SuppressLint("NotifyDataSetChanged")
class TransactionHistoryAdapter(var context: Context, var clickListener: View.OnClickListener) :
    RecyclerView.Adapter<TransactionHistoryAdapter.Holder>(),Filterable {

    var objList: ArrayList<TransactionModel.TransactionData> = ArrayList()
    var filteredObjList: ArrayList<TransactionModel.TransactionData> = ArrayList()

    var onFilterResultsListener: OnFilterResultsListener? = null

    init {
        filteredObjList.addAll(objList)
    }

    inner class Holder(val binding: RawTransactionHistoryBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding =
            RawTransactionHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun getItemCount(): Int {
        return filteredObjList.size
    }


    @SuppressLint("SetTextI18n", "UseCompatLoadingForDrawables")
    override fun onBindViewHolder(holder: Holder, @SuppressLint("RecyclerView") position: Int) {
        val item = filteredObjList[position]
        holder.binding.apply {
            tvInvoiceId.text = item.BillNo
            tvDate.text = Util.formatTimestamp(
                item.BillDate,
                "yyyy-MM-dd'T'HH:mm:ss",
                "dd MMM yyyy"
            )
            tvPointsAccrued.text = item.TotalAccruedPoints
            tvPointsRedeemed.text = item.TotalRedeemPoints
            tvDescription.text = item.Narration.lowercase()

            llTransactionHistory.tag = position
            llTransactionHistory.setOnClickListener(clickListener)

        }
    }

    fun addData(mObj: ArrayList<TransactionModel.TransactionData>) {
        objList = ArrayList()
        filteredObjList = ArrayList()
        objList.addAll(mObj)
        filteredObjList.addAll(mObj)
        this.notifyDataSetChanged()
    }

    fun clearData() {
        objList.clear()
        filteredObjList.clear()
        notifyDataSetChanged()
    }

    override fun getFilter(): Filter {
        return object : Filter(){
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filterResults = FilterResults()
                if (constraint.isNullOrEmpty()) {
                    filterResults.values = objList
                } else {
                    val query = constraint.toString().lowercase()
                    val filteredList = objList.filter {
                        it.BillNo.lowercase().contains(query)
                    }
                    filterResults.values = filteredList
                }
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredObjList.clear()
                if (results?.values is List<*>) {
                    filteredObjList.addAll(results.values as List<TransactionModel.TransactionData>)
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