package com.xongolab.hotellifyr.view.activity.home

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.xongolab.hotellifyr.R
import com.xongolab.hotellifyr.core.CoreActivity
import com.xongolab.hotellifyr.databinding.ActivityAddOnsExperienceDetailsBinding
import com.xongolab.hotellifyr.model.AddOnExperience
import com.xongolab.hotellifyr.model.AddOnExperienceRequest
import com.xongolab.hotellifyr.view.adapter.PackagesAdapter


class AddOnsExperienceDetailsActivity : CoreActivity() {
    private lateinit var binding: ActivityAddOnsExperienceDetailsBinding
    private lateinit var packagesAdapter: PackagesAdapter
    private lateinit var addOnExperience: AddOnExperience

    private var checkIn: String = ""
    private var checkOut: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddOnsExperienceDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setFullScreenMode(window)

        initView()
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.btnBack -> finish()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun initView() {

        if (intent != null) {
            checkIn = intent.getStringExtra("checkIn")!!
            checkOut = intent.getStringExtra("checkOut")!!
            addOnExperience =
                Gson().fromJson(intent.getStringExtra("item")!!, AddOnExperience::class.java)
        }

        binding.toolbar.btnBack.setOnClickListener(this)
        binding.toolbar.toolbarTitle.text = addOnExperience.title

        binding.ivIcon.setImageURI(addOnExperience.icon)
        binding.tvDescription.text = addOnExperience.description

        packagesAdapter =
            PackagesAdapter(this@AddOnsExperienceDetailsActivity, this)
        binding.rvPackages.layoutManager =
            LinearLayoutManager(this@AddOnsExperienceDetailsActivity, RecyclerView.VERTICAL, false)
        binding.rvPackages.adapter = packagesAdapter

        packagesAdapter.onItemClick = { position ->
            for (item in addOnExperience.packages) {
                item.selected = false
            }

            addOnExperience.packages[position].selected = true
            val intent = Intent(
                this@AddOnsExperienceDetailsActivity, AddOnsExperienceBookSlotActivity::class.java
            )
            intent.putExtra("checkIn", checkIn)
            intent.putExtra("checkOut", checkOut)
            intent.putExtra("item", Gson().toJson(addOnExperience))
            resultLauncher.launch(intent)
        }

        packagesAdapter.addData(addOnExperience.packages)
    }

    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val resultData = Gson().fromJson(
                    result.data?.getStringExtra("AddOnExperienceRequest"),
                    AddOnExperienceRequest::class.java
                )

                val resultIntent = Intent()
                resultIntent.putExtra("AddOnExperienceRequest", Gson().toJson(resultData))
                setResult(Activity.RESULT_OK, resultIntent)
                finish()
            }
        }
}