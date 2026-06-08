package com.xongolab.hotellifyr.view.activity.home.search

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.xongolab.hotellifyr.R
import com.xongolab.hotellifyr.core.CoreActivity
import com.xongolab.hotellifyr.databinding.ActivitySelectRoomAndGuestsBinding
import com.xongolab.hotellifyr.model.ResortModel
import com.xongolab.hotellifyr.view.adapter.GuestsPerRoomAdapter


@SuppressLint("SetTextI18n")
class SelectRoomAndGuestsActivity : CoreActivity() {

    private lateinit var binding: ActivitySelectRoomAndGuestsBinding
    private lateinit var guestsPerRoomAdapter: GuestsPerRoomAdapter

    private var totalRooms = 1
    private var totalAdults = 1
    private var totalChildren = 0
    private var maxAdults = 4
    private var maxChildren = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySelectRoomAndGuestsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setFullScreenMode(window)
        initView()
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btnBack -> finish()
            R.id.tvClear -> {
                totalRooms = 1
                totalAdults = 1
                totalChildren = 0

                maxAdults = 4
                maxChildren = 2

                binding.tvRooms.text = "$totalRooms"
                binding.tvAdults.text = "$totalAdults"
                binding.tvChildren.text = "$totalChildren"
                binding.tvTotal.text = "$totalRooms ${getString(R.string.room)}, ${totalAdults + totalChildren} ${getString(R.string.guest)}"
            }

            R.id.ivPlusRooms -> {
                if (totalRooms >= 3)
                    return

                totalRooms++

                maxAdults = totalRooms * 4
                maxChildren = totalRooms * 2

                binding.tvRooms.text = "$totalRooms"
                binding.tvTotal.text = "$totalRooms ${getString(R.string.room)}, ${totalAdults + totalChildren} ${getString(R.string.guest)}"
            }

            R.id.ivMinusRooms -> {
                if (totalRooms == 1)
                    return

                totalRooms--

                maxAdults = totalRooms * 4
                maxChildren = totalRooms * 2

                if (totalAdults > maxAdults) {
                    totalAdults = maxAdults
                    binding.tvAdults.text = "$totalAdults"
                }

                if (totalChildren > maxChildren) {
                    totalChildren = maxChildren
                    binding.tvChildren.text = "$totalChildren"
                }

                binding.tvRooms.text = "$totalRooms"
                binding.tvTotal.text = "$totalRooms ${getString(R.string.room)}, ${totalAdults + totalChildren} ${getString(R.string.guest)}"
            }

            R.id.ivPlusAdults -> {
                if (totalAdults >= maxAdults)
                    return

                totalAdults++

                binding.tvAdults.text = "$totalAdults"
                binding.tvTotal.text = "$totalRooms ${getString(R.string.room)}, ${totalAdults + totalChildren} ${getString(R.string.guest)}"
            }

            R.id.ivMinusAdults -> {
                if (totalAdults == 1)
                    return

                totalAdults--
                binding.tvAdults.text = "$totalAdults"
                binding.tvTotal.text = "${totalRooms} ${getString(R.string.room)}, ${totalAdults + totalChildren} ${getString(R.string.guest)}"
            }

            R.id.ivPlusChildren -> {
                if (totalChildren >= maxChildren)
                    return

                totalChildren++
                binding.tvChildren.text = "$totalChildren"
                binding.tvTotal.text = "$totalRooms ${getString(R.string.room)}, ${totalAdults + totalChildren} ${getString(R.string.guest)}"
            }

            R.id.ivMinusChildren -> {
                if (totalChildren == 0)
                    return

                totalChildren--
                binding.tvChildren.text = "$totalChildren"
                binding.tvTotal.text = "$totalRooms ${getString(R.string.room)}, ${totalAdults + totalChildren} ${getString(R.string.guest)}"
            }

            R.id.btnApply -> {
                val resultIntent = Intent()
                resultIntent.putExtra("rooms", totalRooms)
                resultIntent.putExtra("adults", totalAdults)
                resultIntent.putExtra("children", totalChildren)
                setResult(Activity.RESULT_OK, resultIntent)
                finish()
            }
        }
    }


    private fun initView() {
        if (intent != null) {
            totalRooms = intent.getIntExtra("rooms", 1)
            totalAdults = intent.getIntExtra("adults", 1)
            totalChildren = intent.getIntExtra("children", 0)
        }

        binding.toolbar.btnBack.setOnClickListener(this)
        binding.toolbar.toolbarTitle.text = getString(R.string.room_guests)

        binding.tvRooms.text = "$totalRooms"
        binding.tvAdults.text = "$totalAdults"
        binding.tvChildren.text = "$totalChildren"

        binding.tvClear.setOnClickListener(this)
        binding.ivMinusRooms.setOnClickListener(this)
        binding.ivPlusRooms.setOnClickListener(this)
        binding.ivMinusAdults.setOnClickListener(this)
        binding.ivPlusAdults.setOnClickListener(this)
        binding.ivMinusChildren.setOnClickListener(this)
        binding.ivPlusChildren.setOnClickListener(this)
        binding.btnApply.setOnClickListener(this)

        guestsPerRoomAdapter = GuestsPerRoomAdapter(this, this)
        binding.rvGuestPerRoom.layoutManager =
            LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        binding.rvGuestPerRoom.adapter = guestsPerRoomAdapter

        guestPerRoomList()

        binding.tvTotal.text = "$totalRooms ${getString(R.string.room)}, ${totalAdults + totalChildren} ${getString(R.string.guest)}"
    }

    private fun guestPerRoomList() {
        guestsPerRoomAdapter.objList = ArrayList()

        var model = ResortModel()
        model.title = getString(R.string.rooms)
        guestsPerRoomAdapter.objList.add(model)

        model = ResortModel()
        model.title = getString(R.string.adults_per_room)
        guestsPerRoomAdapter.objList.add(model)

        model = ResortModel()
        model.title = getString(R.string.children_per_room)
        guestsPerRoomAdapter.objList.add(model)

        guestsPerRoomAdapter.addData(guestsPerRoomAdapter.objList)
    }
}