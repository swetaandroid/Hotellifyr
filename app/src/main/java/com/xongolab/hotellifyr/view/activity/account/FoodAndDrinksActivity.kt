package com.xongolab.hotellifyr.view.activity.account

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.xongolab.hotellifyr.R
import com.xongolab.hotellifyr.core.CoreActivity
import com.xongolab.hotellifyr.databinding.ActivityFoodAndDrinksBinding
import com.xongolab.hotellifyr.model.HotelModel
import com.xongolab.hotellifyr.view.adapter.filter.BrandAdapter

class FoodAndDrinksActivity : CoreActivity() {

    private lateinit var binding: ActivityFoodAndDrinksBinding
    private lateinit var brandAdapter: BrandAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFoodAndDrinksBinding.inflate(layoutInflater)
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
        binding.layoutToolBar.toolbarTitle.text = getString(R.string.food_drinks)
        binding.layoutToolBar.btnBack.setOnClickListener(this)

        // Elevator Proximity
        brandAdapter = BrandAdapter(this, this)
        binding.rvNeedHelp.layoutManager = GridLayoutManager(this, 2)
        binding.rvNeedHelp.adapter = brandAdapter
        needHelpList()

        brandAdapter = BrandAdapter(this, this)
        binding.rvCuisines.layoutManager = GridLayoutManager(this, 2)
        binding.rvCuisines.adapter = brandAdapter
        rvCuisinesList()

        brandAdapter = BrandAdapter(this, this)
        binding.rvDrink.layoutManager = GridLayoutManager(this, 2)
        binding.rvDrink.adapter = brandAdapter
        drinkList()
    }

    private fun needHelpList() {
        brandAdapter.objList = ArrayList()

        var model = HotelModel()
        model.title = "Alcohol free"
        brandAdapter.objList.add(model)

        model = HotelModel()
        model.title = "Alcohol free"
        brandAdapter.objList.add(model)

        model = HotelModel()
        model.title = "Eggs free"
        brandAdapter.objList.add(model)

        model = HotelModel()
        model.title = "Lactose free"
        brandAdapter.objList.add(model)

        model = HotelModel()
        model.title = "Nuts free"
        brandAdapter.objList.add(model)

        model = HotelModel()
        model.title = "Peanuts free"
        brandAdapter.objList.add(model)

        model = HotelModel()
        model.title = "Salt free"
        brandAdapter.objList.add(model)

        brandAdapter.addData(brandAdapter.objList)
    }

    private fun rvCuisinesList() {
        brandAdapter.objList = ArrayList()

        var model = HotelModel()
        model.title = "By top chefs"
        brandAdapter.objList.add(model)

        model = HotelModel()
        model.title = "Kosher"
        brandAdapter.objList.add(model)

        model = HotelModel()
        model.title = "Local"
        brandAdapter.objList.add(model)

        model = HotelModel()
        model.title = "Light"
        brandAdapter.objList.add(model)

        model = HotelModel()
        model.title = "Hindu"
        brandAdapter.objList.add(model)

        model = HotelModel()
        model.title = "Hindu"
        brandAdapter.objList.add(model)

        brandAdapter.addData(brandAdapter.objList)
    }

    private fun drinkList() {
        brandAdapter.objList = ArrayList()

        var model = HotelModel()
        model.title = "Beers"
        brandAdapter.objList.add(model)

        model = HotelModel()
        model.title = "Tea"
        brandAdapter.objList.add(model)

        model = HotelModel()
        model.title = "Champagne"
        brandAdapter.objList.add(model)

        model = HotelModel()
        model.title = "Wine"
        brandAdapter.objList.add(model)

        model = HotelModel()
        model.title = "Coffee"
        brandAdapter.objList.add(model)

        model = HotelModel()
        model.title = "Cocktails"
        brandAdapter.objList.add(model)

        brandAdapter.addData(brandAdapter.objList)
    }
}

