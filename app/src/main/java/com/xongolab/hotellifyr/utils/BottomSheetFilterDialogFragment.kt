package com.xongolab.hotellifyr.utils

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.xongolab.hotellifyr.R
import com.xongolab.hotellifyr.core.CoreActivity
import com.xongolab.hotellifyr.databinding.BottomsheetFilterBinding
import com.xongolab.hotellifyr.model.HotelModel
import com.xongolab.hotellifyr.model.SearchHotel
import com.xongolab.hotellifyr.view.adapter.filter.AmenitiesAdapter
import com.xongolab.hotellifyr.view.adapter.filter.BrandAdapter
import com.xongolab.hotellifyr.view.adapter.filter.HotelTypeAdapter
import com.xongolab.hotellifyr.view.adapter.filter.RatingAdapter
import com.xongolab.hotellifyr.viewModel.HotelViewModel
import com.xongolab.hotellifyr.viewModel.ViewModelFactory

@SuppressLint("NotifyDataSetChanged", "SetTextI18n")
class BottomSheetFilterDialogFragment(
    private val searchHotel: SearchHotel,
    val onApplyClick: ((searchHotel: SearchHotel) -> Unit)? = null
) : BottomSheetDialogFragment() {

    private lateinit var dialogBinding: BottomsheetFilterBinding
    private lateinit var hotelTypeAdapter: HotelTypeAdapter
    private lateinit var brandAdapter: BrandAdapter
    private lateinit var destinationAdapter: BrandAdapter
    private lateinit var amenitiesAdapter: AmenitiesAdapter
    private lateinit var ratingAdapter: RatingAdapter

    private lateinit var hotelViewModel: HotelViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        dialogBinding = BottomsheetFilterBinding.inflate(inflater, container, false)
        return dialogBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewModel()
        setupAdapters()
        setupClickListeners()

        getFilterApi()
    }

    private fun initViewModel() {
        hotelViewModel = ViewModelProvider(
            requireActivity(),
            ViewModelFactory((requireActivity() as CoreActivity).mainRepository)
        )[HotelViewModel::class.java]
        observeViewModel()
    }

    @SuppressLint("SetTextI18n")
    private fun observeViewModel() {
        hotelViewModel.getDestinationListApiResponse.observe(viewLifecycleOwner) { response ->
            response?.let { data ->
                data.payload.forEach { destination ->
                    val isChecked = searchHotel.destinationIDs!!.any { it.id == destination.id }
                    destination.isChecked = isChecked
                }

                destinationAdapter.addData(data.payload)
            }
        }

        hotelViewModel.getBrandListApiResponse.observe(viewLifecycleOwner) { response ->
            response?.let { data ->
                data.payload.forEach { brand ->
                    val isChecked = searchHotel.brandIDs!!.any { it.id == brand.id }
                    brand.isChecked = isChecked
                }

                brandAdapter.addData(data.payload)
            }
        }

        hotelViewModel.getAmenitiesListApiResponse.observe(viewLifecycleOwner) { response ->
            response?.let { data ->
                data.payload.forEach { amenities ->
                    val isChecked = searchHotel.amenitiesIDs!!.any { it.id == amenities.id }
                    amenities.isChecked = isChecked
                }

                amenitiesAdapter.addData(data.payload)
            }
        }
    }

    private fun getFilterApi() {
        if (Util.isOnline(requireContext())) {
            hotelViewModel.getDestinationListApi(requireContext())
            hotelViewModel.getBrandListApi(requireContext())
            hotelViewModel.getAmenitiesListApi(requireContext())
        }
    }


    private fun setupAdapters() {
        dialogBinding.apply {
            priceRangeSlider.valueFrom = 0F
            priceRangeSlider.valueTo = 100000F
//            priceRangeSlider.stepSize = 100F
            priceRangeSlider.trackActiveTintList = ColorStateList.valueOf(
                ContextCompat.getColor(
                    context as CoreActivity,
                    R.color.colorPrimary
                )
            ) // Pink color
//            priceRangeSlider.trackInactiveTintList = ColorStateList.valueOf(Color.parseColor("#D9D9D9")) // Light Gray
            priceRangeSlider.thumbTintList = ColorStateList.valueOf(
                ContextCompat.getColor(
                    context as CoreActivity,
                    R.color.colorPrimary
                )
            )// Pink thumb color
            priceRangeSlider.setValues(searchHotel.minPrice, searchHotel.maxPrice)
            priceRangeSlider.setMinSeparationValue(100F)

            priceRangeSlider.addOnChangeListener { slider, _, _ ->
                val values = slider.values
                val minPrice = values[0].toInt()
                val maxPrice = values[1].toInt()

                tvPriceRange.text = "₹ $minPrice  -  ₹ $maxPrice"
            }


        }

        // Hotel Type
        hotelTypeAdapter = HotelTypeAdapter(requireContext())
        dialogBinding.rvHotelType.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        dialogBinding.rvHotelType.adapter = hotelTypeAdapter
        hotelTypeList()

        hotelTypeAdapter.onItemClick = { _, item ->
            hotelTypeAdapter.objList.forEach { it.isChecked = false }
            item.isChecked = true
            hotelTypeAdapter.notifyDataSetChanged()
        }

        // Brand
        brandAdapter = BrandAdapter(requireContext(), requireActivity() as CoreActivity)
        dialogBinding.rvBrand.layoutManager = GridLayoutManager(requireContext(), 2)
        dialogBinding.rvBrand.adapter = brandAdapter

        brandAdapter.onItemClick = { position, item ->
            item.isChecked = !item.isChecked
            brandAdapter.notifyItemChanged(position)
        }

        // Rating
        ratingAdapter = RatingAdapter(requireContext())
        dialogBinding.rvRating.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        dialogBinding.rvRating.adapter = ratingAdapter
        ratingList()

        ratingAdapter.onItemClick = { _, item ->
            ratingAdapter.objList.forEach { it.isChecked = false }
            item.isChecked = true
            ratingAdapter.notifyDataSetChanged()
        }

        // Location
        destinationAdapter = BrandAdapter(requireContext(), requireActivity() as CoreActivity)
        dialogBinding.rvLocation.layoutManager = GridLayoutManager(requireContext(), 2)
        dialogBinding.rvLocation.adapter = destinationAdapter

        destinationAdapter.onItemClick = { position, item ->
            item.isChecked = !item.isChecked
            destinationAdapter.notifyItemChanged(position)
        }

        // Amenities
        amenitiesAdapter = AmenitiesAdapter(requireContext())
        dialogBinding.rvAmenities.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        dialogBinding.rvAmenities.adapter = amenitiesAdapter

        amenitiesAdapter.onItemClick = { position, item ->
            item.isChecked = !item.isChecked
            amenitiesAdapter.notifyItemChanged(position)
        }
    }

    private fun setupClickListeners() {
        dialogBinding.apply {
            rlBrand.setOnClickListener { toggleVisibility(rvBrand, icBrandArrow) }
            rlLocation.setOnClickListener { toggleVisibility(rvLocation, icLocationArrow) }
            rlAmenities.setOnClickListener { toggleVisibility(rvAmenities, icAmenitiesArrow) }

            icBack.setOnClickListener {
                dismiss()
            }

            tvClearAll.setOnClickListener {
                resetFilters()
            }

            btnApply.setOnClickListener {
                // Apply Filters
                searchHotel.hotelType = hotelTypeAdapter.objList.find { it.isChecked }?.hotelType
                searchHotel.starRating = ratingAdapter.objList.find { it.isChecked }?.starRating
                searchHotel.minPrice = dialogBinding.priceRangeSlider.values[0]
                searchHotel.maxPrice = dialogBinding.priceRangeSlider.values[1]

                searchHotel.brandIDs =
                    brandAdapter.objList.filter { it.isChecked } as ArrayList<HotelModel>
                searchHotel.destinationIDs =
                    destinationAdapter.objList.filter { it.isChecked } as ArrayList<HotelModel>
                searchHotel.amenitiesIDs =
                    amenitiesAdapter.objList.filter { it.isChecked } as ArrayList<HotelModel>

                onApplyClick?.invoke(searchHotel)
                dismiss()
            }
        }
    }

    private fun toggleVisibility(recyclerView: RecyclerView, arrowIcon: ImageView) {
        if (recyclerView.visibility == View.GONE) {
            recyclerView.visibility = View.VISIBLE
            arrowIcon.setImageResource(R.drawable.ic_up_arrow_gray)
        } else {
            recyclerView.visibility = View.GONE
            arrowIcon.setImageResource(R.drawable.ic_down_arrow_gray)
        }
    }

    private fun hotelTypeList() {
        hotelTypeAdapter.objList = arrayListOf(
            HotelModel(
                title = "Resorts",
                hotelType = "Resort",
                resourceImage = R.drawable.ic_resorts
            ),
            HotelModel(title = "Hotels", hotelType = "Hotel", resourceImage = R.drawable.ic_hotels)
        )

        hotelTypeAdapter.objList.find { it.hotelType == searchHotel.hotelType }?.isChecked = true

        hotelTypeAdapter.addData(hotelTypeAdapter.objList)
    }

    private fun ratingList() {
        ratingAdapter.objList = arrayListOf(
            HotelModel(starRating = 5.0F, rate = "5"),
            HotelModel(starRating = 4.0F, rate = "4"),
            HotelModel(starRating = 3.0F, rate = "3"),
            HotelModel(starRating = 2.0F, rate = "2"),
            HotelModel(starRating = 1.0F, rate = "1")
        )

        ratingAdapter.objList.find { it.starRating == searchHotel.starRating }?.isChecked = true

        ratingAdapter.addData(ratingAdapter.objList)
    }

    override fun onStart() {
        super.onStart()
        dialog?.let { bottomSheetDialog ->
            val bottomSheet =
                bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.let {
                val behavior = BottomSheetBehavior.from(it)
                behavior.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }
        dialog?.setCancelable(false)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun resetFilters() {
        // Reset Price Range
        dialogBinding.priceRangeSlider.setValues(0F, 100000F)

        // Reset Hotel Type
        hotelTypeAdapter.objList.forEach { it.isChecked = false }
        hotelTypeAdapter.notifyDataSetChanged()

        // Reset Star Rating
        ratingAdapter.objList.forEach { it.isChecked = false }
        ratingAdapter.notifyDataSetChanged()

        // Reset Brands
        brandAdapter.objList.forEach { it.isChecked = false }
        brandAdapter.notifyDataSetChanged()

        // Reset Destinations
        destinationAdapter.objList.forEach { it.isChecked = false }
        destinationAdapter.notifyDataSetChanged()

        // Reset Amenities
        amenitiesAdapter.objList.forEach { it.isChecked = false }
        amenitiesAdapter.notifyDataSetChanged()
    }
}
