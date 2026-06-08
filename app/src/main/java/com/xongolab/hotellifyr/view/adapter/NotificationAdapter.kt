package com.xongolab.hotellifyr.view.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.xongolab.hotellifyr.databinding.LoaderItemBinding
import com.xongolab.hotellifyr.databinding.RawNotificationBinding
import com.xongolab.hotellifyr.model.NotificationModel
import com.xongolab.hotellifyr.utils.Util
import com.xongolab.hotellifyr.utils.makeGone
import com.xongolab.hotellifyr.utils.makeVisible
import com.xongolab.hotellifyr.view.adapter.account.BookingHistoryAdapter


@SuppressLint("NotifyDataSetChanged")
class NotificationAdapter(var context: Context) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var isLoaderVisible = false
    private val ITEM_TYPE_NORMAL = 1
    private val ITEM_TYPE_LOADER = 2
    var objList: ArrayList<NotificationModel.NotificationData> = ArrayList()

    var onItemClick: ((position: Int) -> Unit)? = null

    inner class Holder(val binding: RawNotificationBinding) :
        RecyclerView.ViewHolder(binding.root)

    inner class LoaderHolder(val binding: LoaderItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == ITEM_TYPE_LOADER) {
            val binding = LoaderItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            LoaderHolder(binding)
        } else {
            val binding = RawNotificationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            Holder(binding)
        }
    }


    override fun getItemViewType(position: Int): Int {
        return if (position == objList.size && isLoaderVisible) ITEM_TYPE_LOADER else ITEM_TYPE_NORMAL
    }

    override fun getItemCount(): Int {
        return objList.size + if (isLoaderVisible) 1 else 0
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is NotificationAdapter.Holder) {
            holder.binding.apply {
                val item = objList[position]
                holder.binding.apply {
                    tvContent.text = item.description
                    tvTime.text = Util.formatTimestamp(
                        item.createdAt,
                        "yyyy-MM-dd'T'HH:mm:ss",
                        "hh:mm a"
                    )

                    val currentDate = Util.formatTimestamp(item.createdAt, "yyyy-MM-dd'T'HH:mm:ss", "yyyy-MM-dd")
                    val previousDate = if (position > 0) {
                        Util.formatTimestamp(objList[position - 1].createdAt, "yyyy-MM-dd'T'HH:mm:ss", "yyyy-MM-dd")
                    } else {
                        null
                    }

                    if (position == 0 || currentDate != previousDate) {
                        tvDate.text = Util.formatTimestamp(
                            item.createdAt,
                            "yyyy-MM-dd'T'HH:mm:ss",
                            "EEEE, MMM dd"
                        )
                        tvDate.makeVisible()
                    } else {
                        tvDate.makeGone()
                    }

                    if (position == objList.size - 1) {
                        divider.makeGone()
                    }
                }

                holder.itemView.setOnClickListener {
                    onItemClick?.invoke(position)
                }
            }
        } else if (holder is BookingHistoryAdapter.LoaderHolder) {
            // No binding necessary for the loader; it just shows the progress bar
        }
    }

    fun addData(mObj: ArrayList<NotificationModel.NotificationData>) {
        val startPosition = objList.size
        objList.addAll(mObj)
        (context as Activity).runOnUiThread {
            notifyItemRangeInserted(startPosition, mObj.size)
        }
    }

    fun clearData() {
        objList.clear()
        notifyDataSetChanged()
    }

    fun addLoader() {
        if (!isLoaderVisible) {
            isLoaderVisible = true
            Handler(Looper.getMainLooper()).post {
                notifyItemInserted(objList.size)
            }
        }
    }

    fun removeLoader() {
        if (isLoaderVisible) {
            isLoaderVisible = false
            Handler(Looper.getMainLooper()).post {
                notifyItemRemoved(objList.size)
            }
        }
    }
}