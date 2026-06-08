package com.xongolab.hotellifyr.view.activity.account

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.xongolab.hotellifyr.R
import com.xongolab.hotellifyr.core.CoreActivity
import com.xongolab.hotellifyr.databinding.ActivityRoomPreferencesBinding
import com.xongolab.hotellifyr.model.HotelModel
import com.xongolab.hotellifyr.model.ResortModel
import com.xongolab.hotellifyr.view.adapter.account.CustomizedDetailsAdapter
import com.xongolab.hotellifyr.view.adapter.filter.BrandAdapter

class RoomPreferencesActivity : CoreActivity() {
    private lateinit var binding: ActivityRoomPreferencesBinding
    private lateinit var brandAdapter: BrandAdapter
    private lateinit var customizedDetailsAdapter: CustomizedDetailsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRoomPreferencesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btnBack-> finish()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun initView() {
        binding.layoutToolBar.toolbarTitle.text = getString(R.string.room_preferences)
        binding.layoutToolBar.btnBack.setOnClickListener(this)

        binding.apply {

            // Elevator Proximity
            brandAdapter = BrandAdapter(this@RoomPreferencesActivity, this@RoomPreferencesActivity)
            rvElevatorProximity.layoutManager = GridLayoutManager(this@RoomPreferencesActivity, 2)
            rvElevatorProximity.adapter = brandAdapter
            elevatorProximityList()

            // Floor Location
            brandAdapter = BrandAdapter(this@RoomPreferencesActivity, this@RoomPreferencesActivity)
            rvFloorLocation.layoutManager = GridLayoutManager(this@RoomPreferencesActivity, 2)
            rvFloorLocation.adapter = brandAdapter
            floorLocationList()

            // Room Type
            brandAdapter = BrandAdapter(this@RoomPreferencesActivity, this@RoomPreferencesActivity)
            rvRoomType.layoutManager = GridLayoutManager(this@RoomPreferencesActivity, 2)
            rvRoomType.adapter = brandAdapter
            roomTypeList()

            // Air-conditioning
            brandAdapter = BrandAdapter(this@RoomPreferencesActivity, this@RoomPreferencesActivity)
            rvAirConditioning.layoutManager = GridLayoutManager(this@RoomPreferencesActivity, 2)
            rvAirConditioning.adapter = brandAdapter
            airConditioningList()

            // Bathroom
            brandAdapter = BrandAdapter(this@RoomPreferencesActivity, this@RoomPreferencesActivity)
            rvBathroom.layoutManager = GridLayoutManager(this@RoomPreferencesActivity, 2)
            rvBathroom.adapter = brandAdapter
            bathroomList()

            // Newspaper
            brandAdapter = BrandAdapter(this@RoomPreferencesActivity, this@RoomPreferencesActivity)
            rvNewspaper.layoutManager = GridLayoutManager(this@RoomPreferencesActivity, 2)
            rvNewspaper.adapter = brandAdapter
            newspaperList()

          //  Bed
            brandAdapter = BrandAdapter(this@RoomPreferencesActivity, this@RoomPreferencesActivity)
            rvBed.layoutManager = GridLayoutManager(this@RoomPreferencesActivity, 2)
            rvBed.adapter = brandAdapter
            bedList()

            // Pillow
            brandAdapter = BrandAdapter(this@RoomPreferencesActivity, this@RoomPreferencesActivity)
            rvPillow.layoutManager = GridLayoutManager(this@RoomPreferencesActivity, 2)
            rvPillow.adapter = brandAdapter
            pillowList()

            // Customized Detail
            customizedDetailsAdapter =
                CustomizedDetailsAdapter(this@RoomPreferencesActivity, this@RoomPreferencesActivity)
            rvCustomizedDetail.layoutManager =
                LinearLayoutManager(this@RoomPreferencesActivity, RecyclerView.VERTICAL, false)
            rvCustomizedDetail.adapter = customizedDetailsAdapter
            customizedDetailList()
        }
    }

    private fun elevatorProximityList() {
        brandAdapter.objList = ArrayList()

        var model = HotelModel()
        model.title = getString(R.string.near_to_elevator)
        brandAdapter.objList.add(model)

        model = HotelModel()
        model.title = getString(R.string.far_from_elevator)
        brandAdapter.objList.add(model)

        brandAdapter.addData(brandAdapter.objList)
    }

    private fun floorLocationList() {
        brandAdapter.objList = ArrayList()

        var model = HotelModel()
        model.title = getString(R.string.lower_floor)
        brandAdapter.objList.add(model)

        model = HotelModel()
        model.title = getString(R.string.far_from_elevatorupper_floor)
        brandAdapter.objList.add(model)

        brandAdapter.addData(brandAdapter.objList)
    }

    private fun roomTypeList() {
        brandAdapter.objList = ArrayList()

        var model = HotelModel()
        model.title = getString(R.string.smoking_room)
        brandAdapter.objList.add(model)

        model = HotelModel()
        model.title = getString(R.string.non_smoking_room)
        brandAdapter.objList.add(model)

        brandAdapter.addData(brandAdapter.objList)
    }

    private fun airConditioningList() {
        brandAdapter.objList = ArrayList()

        var model = HotelModel()
        model.title = getString(R.string.turned_on)
        brandAdapter.objList.add(model)

        model = HotelModel()
        model.title = getString(R.string.turned_off)
        brandAdapter.objList.add(model)

        brandAdapter.addData(brandAdapter.objList)
    }

    private fun bathroomList() {
        brandAdapter.objList = ArrayList()

        var model = HotelModel()
        model.title = getString(R.string.bathtub)
        brandAdapter.objList.add(model)

        model = HotelModel()
        model.title = getString(R.string.shower)
        brandAdapter.objList.add(model)

        brandAdapter.addData(brandAdapter.objList)
    }

    private fun newspaperList() {
        brandAdapter.objList = ArrayList()

        var model = HotelModel()
        model.title = getString(R.string.international)
        brandAdapter.objList.add(model)

        model = HotelModel()
        model.title = getString(R.string.local)
        brandAdapter.objList.add(model)

        brandAdapter.addData(brandAdapter.objList)
    }

    private fun bedList() {
        brandAdapter.objList = ArrayList()

        var model = HotelModel()
        model.title = getString(R.string.one_bed)
        brandAdapter.objList.add(model)

        model = HotelModel()
        model.title = getString(R.string.two_bed)
        brandAdapter.objList.add(model)

        brandAdapter.addData(brandAdapter.objList)
    }

    private fun pillowList() {
        brandAdapter.objList = ArrayList()

        var model = HotelModel()
        model.title = getString(R.string.feathers)
        brandAdapter.objList.add(model)

        model = HotelModel()
        model.title = getString(R.string.foam)
        brandAdapter.objList.add(model)

        brandAdapter.addData(brandAdapter.objList)
    }

    private fun customizedDetailList() {
        customizedDetailsAdapter.objList = ArrayList()

        var model = ResortModel()
        model.title = getString(R.string.fruit_in_season)
        customizedDetailsAdapter.objList.add(model)

        model = ResortModel()
        model.title = getString(R.string.cheese_grapes)
        customizedDetailsAdapter.objList.add(model)

        model = ResortModel()
        model.title = getString(R.string.chocolate_delight)
        customizedDetailsAdapter.objList.add(model)

        model = ResortModel()
        model.title = getString(R.string.sweet_surrender_pastries_cookies)
        customizedDetailsAdapter.objList.add(model)

        model = ResortModel()
        model.title = getString(R.string.healthy_obsession)
        model.name = getString(R.string.dried_fruits_assorted_nuts_granola_barel)
        customizedDetailsAdapter.objList.add(model)

        model = ResortModel()
        model.title = getString(R.string.surprise_me)
        model.name = getString(R.string.authentically_local_amenity)
        customizedDetailsAdapter.objList.add(model)

        customizedDetailsAdapter.addData(customizedDetailsAdapter.objList)
    }
}