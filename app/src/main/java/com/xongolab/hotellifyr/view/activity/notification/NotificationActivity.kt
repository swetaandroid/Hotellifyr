package com.xongolab.hotellifyr.view.activity.notification

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.xongolab.hotellifyr.R
import com.xongolab.hotellifyr.core.CoreActivity
import com.xongolab.hotellifyr.databinding.ActivityNotificationBinding
import com.xongolab.hotellifyr.model.Message
import com.xongolab.hotellifyr.utils.DialogManager
import com.xongolab.hotellifyr.utils.Util
import com.xongolab.hotellifyr.utils.Util.createPartFromString
import com.xongolab.hotellifyr.utils.makeGone
import com.xongolab.hotellifyr.utils.makeVisible
import com.xongolab.hotellifyr.view.adapter.NotificationAdapter
import com.xongolab.hotellifyr.viewModel.HotelViewModel
import com.xongolab.hotellifyr.viewModel.ViewModelFactory

class NotificationActivity : CoreActivity() {

    private lateinit var binding: ActivityNotificationBinding
    private lateinit var notificationAdapter: NotificationAdapter
    private lateinit var dialogManager: DialogManager
    private var isLastPage = false
    private var isLoading = false
    private var currentPage = 0
    private val pageSize = 10
    private lateinit var hotelViewModel: HotelViewModel
    var deleteIconBounds: Rect? = null
    var swipedPosition: Int = RecyclerView.NO_POSITION

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotificationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setFullScreenMode(window)
        initView()
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.btnBack -> finish()
            R.id.btnDelete -> {
                AlertDialog.Builder(this@NotificationActivity)
                    .setMessage(getString(R.string.are_you_sure_you_want_to_delete_all_notification))
                    .setCancelable(true)
                    .setPositiveButton(getString(R.string.yes)) { dialog, _ ->
                        dialog.dismiss()
                        // Your API call to delete here
                        callAllNotificationDeleteApi()
                    }
                    .setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
                        dialog.dismiss()
                    }
                    .show()
            }
           // R.id.ivBell -> showBottomSheet()
        }
    }

    @SuppressLint("SetTextI18n", "ClickableViewAccessibility")
    private fun initView() {
        initViewModel()
        dialogManager = DialogManager(this)

        binding.toolbar.toolbarTitle.text = getString(R.string.notification)
        binding.toolbar.btnBack.setOnClickListener(this)
        binding.toolbar.btnDelete.setOnClickListener(this)
        binding.toolbar.viewBlank.visibility = View.GONE
        binding.toolbar.btnDelete.visibility = View.VISIBLE
        binding.ivBell.setOnClickListener(this)

        notificationAdapter = NotificationAdapter(this)
        binding.rvNotification.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvNotification.adapter = notificationAdapter

        binding.rvNotification.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                if (!isLoading && !isLastPage) {
                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount && firstVisibleItemPosition >= 0) {
                        recyclerView.post {
                            loadMoreItems()
                        }
                    }
                }
            }
        })

        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                // We don't delete directly on swipe
                // Instead reset item back
                swipedPosition = viewHolder.adapterPosition
                notificationAdapter.notifyItemChanged(viewHolder.adapterPosition)
            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                    val itemView = viewHolder.itemView
                    val paint = Paint()
                    paint.color = Color.WHITE

                    val background = RectF(
                        itemView.right + dX, itemView.top.toFloat(),
                        itemView.right.toFloat(), itemView.bottom.toFloat()
                    )
                    c.drawRect(background, paint)

                    val deleteIcon = ContextCompat.getDrawable(this@NotificationActivity, R.drawable.ic_delete)
                    deleteIcon?.let {
                        val iconMargin = (itemView.height - it.intrinsicHeight) / 2
                        val iconTop = itemView.top + iconMargin
                        val iconLeft = itemView.right - iconMargin - it.intrinsicWidth
                        val iconRight = itemView.right - iconMargin
                        val iconBottom = iconTop + it.intrinsicHeight

                        it.setBounds(iconLeft, iconTop, iconRight, iconBottom)
                        it.draw(c)

                        // Save icon bounds for click detection
                        deleteIconBounds = Rect(iconLeft, iconTop, iconRight, iconBottom)
                    }
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            }
        }

        // Attach to RecyclerView
        ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(binding.rvNotification)

        // Set touch listener to detect delete icon click
        binding.rvNotification.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP && deleteIconBounds != null) {
                if (swipedPosition != RecyclerView.NO_POSITION) {
                    // Delete icon clicked
                    AlertDialog.Builder(this@NotificationActivity)
                        .setMessage(getString(R.string.are_you_sure_you_want_to_delete_this_notification))
                        .setCancelable(true)
                        .setPositiveButton(getString(R.string.delete)) { dialog, _ ->
                            dialog.dismiss()
                            // Your API call to delete here
                            callNotificationDeleteApi(swipedPosition)
                        }
                        .setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
                            dialog.dismiss()
                        }
                        .show()
                }
            }
            false
        }
    }

    private fun initViewModel() {
        hotelViewModel = ViewModelProvider(this, ViewModelFactory(mainRepository))[HotelViewModel::class.java]
        observeViewModel()
    }

    private fun observeViewModel() {
        hotelViewModel.callNotificationDeleteApiResponse.observe(this){response ->
            response?.let {
                msgDialog(getString(R.string.notification_deleted_successfully))
                notificationHistoryListApi()
            }
        }
        hotelViewModel.callAllNotificationDeleteApiResponse.observe(this){response ->
            response?.let {
                msgDialog(getString(R.string.all_notifications_deleted_successfully))
                notificationHistoryListApi()
            }
        }
        hotelViewModel.getNotificationHistoryListApiResponse.observe(this) { response ->
            response?.let {
                val payload = it.payload!!
                if (currentPage == 0){
                    notificationAdapter.removeLoader()
                    if (currentPage == 0){
                        notificationAdapter.clearData()
                        notificationAdapter.addData(payload.data)
                    }else{
                        if (it.payload!!.data.size > 0) {
                            notificationAdapter.addData(it.payload!!.data)
                        }
                    }

                    if (currentPage == 0) {
                        if (notificationAdapter.objList.isEmpty()) {
                            binding.llNoData.makeVisible()
                            binding.rvNotification.makeGone()
                        } else {
                            binding.rvNotification.makeVisible()
                            binding.llNoData.makeGone()
                        }
                    }

                    isLoading = false
                    isLastPage = it.payload!!.data.size < pageSize
                }else {
                    Handler(Looper.getMainLooper()).postDelayed({
                        notificationAdapter.removeLoader()
                        if (currentPage == 0) {
                            notificationAdapter.clearData()
                            notificationAdapter.addData(it.payload!!.data)
                        } else {
                            if (it.payload!!.data.size > 0) {
                                notificationAdapter.addData(it.payload!!.data)
                            }
                        }

                        if (currentPage == 0) {
                            if (notificationAdapter.objList.isEmpty()) {
                                binding.llNoData.makeVisible()
                                binding.rvNotification.makeGone()
                            } else {
                                binding.rvNotification.makeVisible()
                                binding.llNoData.makeGone()
                            }
                        }

                        isLoading = false
                        isLastPage = it.payload!!.data.size < pageSize
                    }, 3000L)
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun notificationHistoryListApi() {
        if (isInternetConnected()) {
            hotelViewModel.getNotificationHistoryListApi(
                this,
                currentPage,
                pageSize
            )
        } else {
            msgDialog(getString(R.string.check_internet))
        }
    }

    @SuppressLint("SetTextI18n")
    private fun callNotificationDeleteApi(position: Int) {
        if (isInternetConnected()) {
            val actionType = createPartFromString("single", "deleteType")
            val notificationID = createPartFromString(notificationAdapter.objList[position]._id, "notificationID")

            hotelViewModel.callNotificationDeleteApi(
                this,
                actionType,
                notificationID
            )
        } else {
            msgDialog(getString(R.string.check_internet))
        }
    }

    @SuppressLint("SetTextI18n")
    private fun callAllNotificationDeleteApi() {
        if (isInternetConnected()) {
            val actionType = createPartFromString("all", "deleteType")

            hotelViewModel.callAllNotificationDeleteApi(
                this,
                actionType
            )
        } else {
            msgDialog(getString(R.string.check_internet))
        }
    }

    private fun loadMoreItems() {
        isLoading = true
        notificationAdapter.addLoader()
        currentPage += 1
        notificationHistoryListApi()
    }

    override fun onResume() {
        super.onResume()
        notificationHistoryListApi()
    }
}
