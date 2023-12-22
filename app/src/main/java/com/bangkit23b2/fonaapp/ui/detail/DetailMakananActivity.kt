package com.bangkit23b2.fonaapp.ui.detail

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bangkit23b2.fonaapp.data.response.BreakfastItem
import com.bangkit23b2.fonaapp.data.response.DinnerItem
import com.bangkit23b2.fonaapp.data.response.LunchItem
import com.bangkit23b2.fonaapp.databinding.ActivityDetailMakananBinding
import com.bangkit23b2.fonaapp.main.MainActivity
import com.bangkit23b2.fonaapp.ui.home.HomeViewModel
import com.bangkit23b2.fonaapp.ui.intro.WelcomeActivity
import com.bangkit23b2.fonaapp.utils.ViewModelFactory

class DetailMakananActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailMakananBinding
    private var token = ""
    private var breakfastItem: BreakfastItem? = null
    private var lunchItem: LunchItem? = null
    private var dinnerItem: DinnerItem? = null
    private lateinit var factory: ViewModelFactory
    private var formattedDate: String = ""
    private val homeViewModel: HomeViewModel by viewModels { factory }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupView()
        setupViewModel()
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
        formattedDate = intent.getStringExtra("DATE") ?: "DefaultDate"
        Log.d(TAG, "Get date: $formattedDate")
    }

    private fun setupViewModel(){
        factory = ViewModelFactory.getInstance(this)
    }

    private fun setupUser(){

    }

    private fun setupAdapter(){

    }

    private fun setupAction(){

        binding.backAction.setOnClickListener{
            onBackPressed()
        }

        binding.btnRemove.setOnClickListener {
            val mealItems = listOfNotNull(breakfastItem, lunchItem, dinnerItem)

            val nutritionIds = getNutritionIds(mealItems)
            val mealType = when {
                breakfastItem != null -> "Sarapan"
                lunchItem != null -> "Makan Siang"
                dinnerItem != null -> "Makan Malam"
                else -> ""
            }

            homeViewModel.getSession().observe(this) { user ->
                Log.d(TAG, "this function called")
                token = user.idToken
                Log.d(TAG, "Your token: ${token}")
                homeViewModel.getDataHome(token, formattedDate)
                if (!user.isLogin) {
                    Log.d(TAG, "User is not login")
                    startActivity(Intent(this, WelcomeActivity::class.java))
                    finish()
                } else {
                    val singleNutritionId = nutritionIds.firstOrNull() ?: 0
                    token = user.idToken
                    Log.d(TAG, "Your delete token: ${token}")
                    homeViewModel.deleteRecordFood(token, listOf(singleNutritionId), mealType, formattedDate) { success ->
                        if (success) {
                            Toast.makeText(this, "Record deleted successfully", Toast.LENGTH_SHORT)
                                .show()
                            startActivity(Intent(this, MainActivity::class.java))
                            finish()
                        } else {
                            Toast.makeText(this, "Failed to delete record", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                }
            }
        }

    }
    private fun getNutritionIds(mealItems: List<Any?>): List<Int> {
        return mealItems.mapNotNull { getNutritionId(it) }
    }
    private fun getNutritionId(mealItem: Any?): Int {
        return when (mealItem) {
            is BreakfastItem -> mealItem.nutrition_id
            is LunchItem -> mealItem.nutrition_id
            is DinnerItem -> mealItem.nutrition_id
            else -> 0 // Return a default value or handle the case where the mealItem is not recognized
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