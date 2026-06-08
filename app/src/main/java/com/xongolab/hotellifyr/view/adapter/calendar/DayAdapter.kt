package com.xongolab.hotellifyr.view.adapter.calendar

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.xongolab.hotellifyr.R
import com.xongolab.hotellifyr.databinding.RawCalendarDayBinding
import java.util.Calendar


class DayAdapter(
    private val days: List<Int>,
    private val listener: DayClickListener,
    private val currentMonth: Int
) : RecyclerView.Adapter<DayAdapter.DayViewHolder>() {

    private var selectedPosition = -1
    private val currentDay: Int =
        Calendar.getInstance().get(Calendar.DAY_OF_MONTH) // Get today's day
    private val currentYearMonth: Int =
        Calendar.getInstance().get(Calendar.MONTH) // Get the current month

    interface DayClickListener {
        fun onDayClick(day: Int)
    }

    class DayViewHolder(val binding: RawCalendarDayBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayViewHolder {
        val binding =
            RawCalendarDayBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DayViewHolder(binding)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(
        holder: DayViewHolder,
        @SuppressLint("RecyclerView") position: Int
    ) {
        val day = days[position]
        holder.binding.apply {
            tvDay.text = day.toString()
            // Condition to check if it's the current day AND it's in the current month
            if (day == currentDay && currentMonth == currentYearMonth && position != selectedPosition) {
                // Apply gray background for the current day of the current month
                llDay.background = ContextCompat.getDrawable(
                    holder.itemView.context,
                    R.drawable.dr_dark_gray_circle // Background for current day
                )
                tvDay.setTextColor(
                    ContextCompat.getColor(
                        holder.itemView.context,
                        R.color.colorF1F1F1 // Text color for current day
                    )
                )
            } else if (position == selectedPosition) {
                // Apply selection background for the selected day
                llDay.background = ContextCompat.getDrawable(
                    holder.itemView.context,
                    R.drawable.dr_black_circle // Background for selected day
                )
                tvDay.setTextColor(
                    ContextCompat.getColor(
                        holder.itemView.context,
                        R.color.colorF1F1F1 // Text color for selected day
                    )
                )
            } else {
                // For non-selected and non-current days, reset the background and text color
                llDay.background = null
                tvDay.setTextColor(
                    ContextCompat.getColor(
                        holder.itemView.context,
                        android.R.color.black // Default text color
                    )
                )
            }

            // Set click listener for day selection
            llDay.setOnClickListener {
                selectedPosition = position
                listener.onDayClick(day)
                notifyDataSetChanged() // Update the view on click
            }
        }
    }

    override fun getItemCount(): Int {
        return days.size
    }

}

