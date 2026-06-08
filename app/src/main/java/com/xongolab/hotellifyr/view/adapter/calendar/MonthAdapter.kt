package com.xongolab.hotellifyr.view.adapter.calendar

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.xongolab.hotellifyr.databinding.RawCalendarMonthBinding
import com.xongolab.hotellifyr.model.MonthData


class MonthAdapter(
    private val monthDataList: List<MonthData>,
    private val currentYear: Int,
    private val listener: MonthClickListener
) : RecyclerView.Adapter<MonthAdapter.MonthViewHolder>() {

    interface MonthClickListener {
        fun onDaySelected(month: String, year: Int, day: Int)
    }

    class MonthViewHolder(val binding: RawCalendarMonthBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MonthViewHolder {
        val binding =
            RawCalendarMonthBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MonthViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MonthViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val monthData = monthDataList[position]

        holder.binding.apply {
            tvMonthName.text = "${monthData.monthName} $currentYear"

            recyclerViewDays.layoutManager = GridLayoutManager(holder.itemView.context, 7)

            recyclerViewDays.adapter =
                DayAdapter(monthData.days, object : DayAdapter.DayClickListener {
                    override fun onDayClick(day: Int) {
                        listener.onDaySelected(monthData.monthName, currentYear, day)
                    }
                }, position)
        }

    }

    override fun getItemCount(): Int {
        return monthDataList.size
    }
}

