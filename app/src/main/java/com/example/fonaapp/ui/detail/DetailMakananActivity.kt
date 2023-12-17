package com.example.fonaapp.ui.detail

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import com.example.fonaapp.R
import com.example.fonaapp.data.response.BreakfastItem
import com.example.fonaapp.data.response.DinnerItem
import com.example.fonaapp.data.response.LunchItem
import com.example.fonaapp.databinding.ActivityDetailMakananBinding
import com.example.fonaapp.databinding.ActivityResultUserPreferenceBinding
import com.example.fonaapp.utils.ViewModelFactory

class DetailMakananActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailMakananBinding
    private var token = ""
    private var breakfastItem: BreakfastItem? = null
    private var lunchItem: LunchItem? = null
    private var dinnerItem: DinnerItem? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupView()
        setupUser()

        breakfastItem = intent.getParcelableExtra("BREAKFAST_ITEM_KEY")
        lunchItem = intent.getParcelableExtra("LUNCH_ITEM_KEY")
        dinnerItem = intent.getParcelableExtra("DINNER_ITEM_KEY")
        displayData()
        setupAdapter()
        setupAction()
    }

    private fun setupView(){
        binding = ActivityDetailMakananBinding.inflate(layoutInflater)
        setContentView(binding.root)
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
        }
    }

    private fun setupUser(){

    }

    private fun setupAdapter(){

    }

    private fun setupAction(){

        binding.backAction.setOnClickListener{
            onBackPressed()
        }

    }

    private fun displayData() {
        val namaMakanan = when {
            breakfastItem != null -> breakfastItem?.name
            lunchItem != null -> lunchItem?.name
            dinnerItem != null -> dinnerItem?.name
            else -> ""
        }

        val caloryTakes = when {
            breakfastItem != null -> breakfastItem?.total_cals.toString()
            lunchItem != null -> lunchItem?.total_cals.toString()
            dinnerItem != null -> dinnerItem?.total_cals.toString()
            else -> ""
        }

        val carboTakes = when {
            breakfastItem != null -> breakfastItem?.total_carbos.toString()
            lunchItem != null -> lunchItem?.total_carbos.toString()
            dinnerItem != null -> dinnerItem?.total_carbos.toString()
            else -> ""
        }

        val fatTakes = when {
            breakfastItem != null -> breakfastItem?.total_fats.toString()
            lunchItem != null -> lunchItem?.total_fats.toString()
            dinnerItem != null -> dinnerItem?.total_fats.toString()
            else -> ""
        }

        val proteinTakes = when {
            breakfastItem != null -> breakfastItem?.total_proteins.toString()
            lunchItem != null -> lunchItem?.total_proteins.toString()
            dinnerItem != null -> dinnerItem?.total_proteins.toString()
            else -> ""
        }

        val servingSize = when {
            breakfastItem != null -> breakfastItem?.serving_size.toString()
            lunchItem != null -> lunchItem?.serving_size.toString()
            dinnerItem != null -> dinnerItem?.serving_size.toString()
            else -> ""
        }

        val quantity = when {
            breakfastItem != null -> breakfastItem?.quantity.toString()
            lunchItem != null -> lunchItem?.quantity?.toString()
            dinnerItem != null -> dinnerItem?.quantity?.toString()
            else -> ""
        }

        val fiberTakes = when {
            breakfastItem != null -> breakfastItem?.total_fibers.toString()
            lunchItem != null -> lunchItem?.total_fibers.toString()
            dinnerItem != null -> dinnerItem?.total_fibers.toString()
            else -> ""
        }

        val caliumsTakes = when {
            breakfastItem != null -> breakfastItem?.total_caliums.toString()
            lunchItem != null -> lunchItem?.total_caliums.toString()
            dinnerItem != null -> dinnerItem?.total_caliums.toString()
            else -> ""
        }

        val sodiumsTakes = when {
            breakfastItem != null -> breakfastItem?.total_sodiums.toString()
            lunchItem != null -> lunchItem?.total_sodiums.toString()
            dinnerItem != null -> dinnerItem?.total_sodiums.toString()
            else -> ""
        }

        binding.namaMakanan.text = namaMakanan
        binding.tvCaloryTakes.text = caloryTakes
        binding.tvCarboTakes.text = carboTakes
        binding.tvFatTakes.text = fatTakes
        binding.tvProteinTakes.text = proteinTakes
        binding.tvServingSize.text = servingSize
        binding.tvQuantity.text = quantity
        binding.tvCalory.text = caloryTakes
        binding.tvCarbo.text =carboTakes
        binding.tvFat.text = fatTakes
        binding.tvProtein.text = proteinTakes
        binding.tvSerat.text = fiberTakes
        binding.tvCalium.text = caliumsTakes
        binding.tvSodium.text = sodiumsTakes

        // Lanjutkan dengan menetapkan nilai ke TextView lainnya sesuai kebutuhan.
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}