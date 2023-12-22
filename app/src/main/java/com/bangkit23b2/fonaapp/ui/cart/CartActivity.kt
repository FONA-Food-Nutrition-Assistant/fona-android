package com.bangkit23b2.fonaapp.ui.cart

import android.app.DatePickerDialog
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit23b2.fonaapp.R
import com.bangkit23b2.fonaapp.data.models.FoodItem
import com.bangkit23b2.fonaapp.data.models.FoodRecordItem
import com.bangkit23b2.fonaapp.data.models.RecordedFoodsRequest
import com.bangkit23b2.fonaapp.data.models.convertToFoodItem
import com.bangkit23b2.fonaapp.data.models.convertToNutritionItem
import com.bangkit23b2.fonaapp.data.response.DataFood
import com.bangkit23b2.fonaapp.data.response.NutritionsItem
import com.bangkit23b2.fonaapp.data.response.UploadFoodResponse
import com.bangkit23b2.fonaapp.databinding.ActivityCartBinding
import com.bangkit23b2.fonaapp.main.MainActivity
import com.bangkit23b2.fonaapp.utils.ViewModelFactory
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class CartActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCartBinding
    private lateinit var cartAdapter: CartAdapter
    private lateinit var selectedMealTime: String
    private val calendar = Calendar.getInstance()
    private lateinit var nutritionsItems: List<NutritionsItem>
    private lateinit var quantities: MutableList<Int>
    private lateinit var foodItemList: MutableList<FoodItem>
    private lateinit var factory: ViewModelFactory
    private var token = ""
    private val cartViewModel: CartViewModel by viewModels { factory }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupView()
        setupViewModel()
        setupAdapter()
        setupAction()
    }

    private fun setupView() {
        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun setupViewModel() {
        factory = ViewModelFactory.getInstance(this)
    }


    private fun setupAdapter() {
        val uploadFoodResponse: UploadFoodResponse? = intent.getParcelableExtra("UPLOAD_RESPONSE")

        if (uploadFoodResponse != null) {

            val dataFoodList: List<DataFood> = uploadFoodResponse.data

            foodItemList = dataFoodList.map { dataFood ->
                convertToFoodItem(dataFood)
            }.toMutableList()
            nutritionsItems = dataFoodList.convertToNutritionItem().sortedBy { it.foodId }

            cartAdapter = CartAdapter(foodItemList, binding.totalCalories, nutritionsItems)
            binding.rvPredict.layoutManager = LinearLayoutManager(this)
            binding.rvPredict.adapter = cartAdapter
            cartAdapter.notifyDataSetChanged()

            val waktuMakanAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.waktu_makan,
                android.R.layout.simple_spinner_item
            )
            quantities = cartAdapter.getQuantities().toMutableList()

            waktuMakanAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinner.adapter = waktuMakanAdapter

            binding.spinner.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parentView: AdapterView<*>,
                    selectedItemView: View?,
                    position: Int,
                    id: Long
                ) {
                    selectedMealTime = parentView.getItemAtPosition(position).toString()
                }

                override fun onNothingSelected(parentView: AdapterView<*>) {

                }
            })

            binding.btnAddFood.setOnClickListener {
                Toast.makeText(this, "Mohon maaf, fitur ini belum tersedia", Toast.LENGTH_SHORT)
                    .show()
            }

            binding.totalCalories.text = cartAdapter.getTotalCalories().toString()
            cartAdapter.updateTotalCaloriesTextView()
        }
    }


    private fun setupAction() {
        cartAdapter.setOnMinusClickListener { position ->
            val removedItem = cartAdapter.removeItem(position)
            quantities.removeAt(position)

            binding.totalCalories.text = cartAdapter.getTotalCalories().toString()

            if (removedItem != null && removedItem.quantity == 0) {
                val removedNutritionId = removedItem.nutritionId
                foodItemList.removeAll { it.nutritionId == removedNutritionId }
                Log.d(TAG, "Nutrition id terhapus")
            }

            Toast.makeText(this, "Item berhasil dihapus", Toast.LENGTH_SHORT).show()
            Log.d(TAG, "Berhasil dihapus")
        }
        binding.edtDate.setOnClickListener {
            showDatePickerDialog()
        }
        binding.backAction.setOnClickListener {
            onBackPressed()
            clearDataAndFinish()
        }
        binding.btnSave.setOnClickListener {
            cartViewModel.getSession().observe(this) { user ->
                token = user.idToken
                val idToken = token
                idToken?.let {
                    if (::foodItemList.isInitialized) {
                        val totalCalories = cartAdapter.getTotalCalories()
                        val mealTime = selectedMealTime
                        val date = convertDateFormatForBackend(binding.edtDate.text.toString())
                        val quantities = cartAdapter.getQuantities()
                        val foodsArray = mutableListOf<FoodRecordItem>()

                        for ((index, foodItem) in foodItemList.withIndex()) {
                            val nutritionId = foodItem.nutritionId
                            val quantity = quantities[index]
                            val foodObject = FoodRecordItem(nutritionId, quantity)
                            foodsArray.add(foodObject)
                        }

                        cartViewModel.storeRecordFood(idToken, RecordedFoodsRequest(foodsArray, mealTime, date)) { success ->
                            if (success) {
                                Toast.makeText(this, "Data berhasil disimpan", Toast.LENGTH_SHORT).show()
                                foodItemList.clear()
                                cartAdapter.notifyDataSetChanged()
                                clearDataAndFinish()
                                startActivity(Intent(this, MainActivity::class.java))
                                finish()
                            } else {
                                cartViewModel.updateRecordFood(idToken, RecordedFoodsRequest(foodsArray, mealTime, date))
                                foodItemList.clear()
                                cartAdapter.notifyDataSetChanged()
                                clearDataAndFinish()
                                startActivity(Intent(this, MainActivity::class.java))
                            }
                        }
                    } else {
                        Toast.makeText(this, "FoodItemList belum diinisialisasi", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun convertDateFormatForBackend(originalDate: String): String {
        val inputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.US)
        val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        val date = inputFormat.parse(originalDate)
        return outputFormat.format(date!!)
    }

    private fun showDatePickerDialog() {
        val datePickerDialog = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                // Update EditText with selected date
                calendar.set(year, month, dayOfMonth)
                updateEditText()

            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.datePicker.maxDate = System.currentTimeMillis()
        datePickerDialog.show()
    }

    override fun onBackPressed() {
        val intent = Intent(
            this@CartActivity,
            MainActivity::class.java
        )
        startActivity(intent)
        finish()
    }

    private fun clearDataAndFinish() {
        foodItemList.clear()
        cartAdapter.notifyDataSetChanged()
        finish()
    }

    private fun updateEditText() {
        val dateFormat = "dd/MM/yyyy"
        val simpleDateFormat = SimpleDateFormat(dateFormat, Locale.US)
        binding.edtDate.setText(simpleDateFormat.format(calendar.time))
    }
}