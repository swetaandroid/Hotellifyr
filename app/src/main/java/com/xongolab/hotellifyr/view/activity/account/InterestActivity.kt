package com.xongolab.hotellifyr.view.activity.account

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.xongolab.hotellifyr.R
import com.xongolab.hotellifyr.core.CoreActivity
import com.xongolab.hotellifyr.databinding.ActivityInterestBinding
import com.xongolab.hotellifyr.model.HotelModel
import com.xongolab.hotellifyr.view.adapter.filter.BrandAdapter

class InterestActivity : CoreActivity() {

    private lateinit var binding: ActivityInterestBinding
    private lateinit var brandAdapter: BrandAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityInterestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btnBack -> finish()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun initView() {

        binding.layoutToolBar.toolbarTitle.text = getString(R.string.interest)
        binding.layoutToolBar.btnBack.setOnClickListener(this)

        // Elevator Proximity
        brandAdapter = BrandAdapter(this, this)
        binding.rvSportsWellness.layoutManager = GridLayoutManager(this, 2)
        binding.rvSportsWellness.adapter = brandAdapter
        needHelpList()

        brandAdapter = BrandAdapter(this, this)
        binding.rvArtsCulture.layoutManager = GridLayoutManager(this, 2)
        binding.rvArtsCulture.adapter = brandAdapter
        artsCultureList()

        brandAdapter = BrandAdapter(this, this)
        binding.rvLifestyle.layoutManager = GridLayoutManager(this, 2)
        binding.rvLifestyle.adapter = brandAdapter
        artsCultureList()
    }

    private fun needHelpList() {
        brandAdapter.objList = ArrayList()

        var model = HotelModel()
        model.title = getString(R.string.str_beauty)
        brandAdapter.objList.add(model)

        model = HotelModel()
        model.title = "Gym"
        brandAdapter.objList.add(model)

        model = HotelModel()
        model.title = "Spa/Massage"
        brandAdapter.objList.add(model)

        model = HotelModel()
        model.title = "Meditation"
        brandAdapter.objList.add(model)

        model = HotelModel()
        model.title = "Yoga"
        brandAdapter.objList.add(model)

        brandAdapter.addData(brandAdapter.objList)
    }

    private fun artsCultureList() {
        brandAdapter.objList = ArrayList()

        var model = HotelModel()
        model.title = "Architecture"
        brandAdapter.objList.add(model)

        model = HotelModel()
        model.title = "Painting"
        brandAdapter.objList.add(model)

        model = HotelModel()
        model.title = "History"
        brandAdapter.objList.add(model)

        model = HotelModel()
        model.title = "Theatre"
        brandAdapter.objList.add(model)

        model = HotelModel()
        model.title = "Cinema"
        brandAdapter.objList.add(model)

        model = HotelModel()
        model.title = "Photographs"
        brandAdapter.objList.add(model)

        brandAdapter.addData(brandAdapter.objList)
    }
}
