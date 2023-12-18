package com.example.fonaapp.ui.cart

import android.app.DatePickerDialog
import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fonaapp.R
import com.example.fonaapp.data.models.FoodItem
import com.example.fonaapp.data.models.FoodRecordItem
import com.example.fonaapp.data.models.Nutrition
import com.example.fonaapp.data.models.RecordedFoodsRequest
import com.example.fonaapp.data.models.User
import com.example.fonaapp.data.models.convertToFoodItem
import com.example.fonaapp.data.models.getUniqueServingSizes
import com.example.fonaapp.data.response.DataFood
import com.example.fonaapp.data.response.FoodsItem
import com.example.fonaapp.data.response.NutritionsItem
import com.example.fonaapp.data.response.UploadFoodResponse
import com.example.fonaapp.databinding.ActivityCartBinding
import com.example.fonaapp.main.MainActivity
import com.example.fonaapp.ui.preferences.UserPreferenceViewModel
import com.example.fonaapp.ui.result.ResultUserPreferenceActivity
import com.example.fonaapp.utils.ViewModelFactory
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
            val uploadFoodResponse: UploadFoodResponse? = intent.getParcelableExtra("UPLOAD_RESPONSE")
            nutritionsItems = dataFoodList.flatMap { it.nutritions } // Menggunakan flatMap
            val servingSizes = foodItemList.getUniqueServingSizes()

            cartAdapter = CartAdapter(foodItemList, servingSizes, binding.totalCalories, nutritionsItems)
            binding.rvPredict.layoutManager = LinearLayoutManager(this)
            cartAdapter.notifyDataSetChanged()

            binding.rvPredict.adapter = cartAdapter

            val waktuMakanAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.waktu_makan,
                android.R.layout.simple_spinner_item
            )
            quantities = cartAdapter.getQuantities().toMutableList()

            waktuMakanAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinner.adapter = waktuMakanAdapter

            // Tambahkan listener untuk mendeteksi perubahan pilihan
            binding.spinner.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parentView: AdapterView<*>,
                    selectedItemView: View?,
                    position: Int,
                    id: Long
                ) {
                    // Simpan pilihan waktu makan yang dipilih
                    selectedMealTime = parentView.getItemAtPosition(position).toString()
                }

                override fun onNothingSelected(parentView: AdapterView<*>) {

                }
            })

            binding.totalCalories.text = cartAdapter.getTotalCalories().toString()
            cartAdapter.updateTotalCaloriesTextView() // Tambahkan ini
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
                Log.d(TAG,"Nutrition id terhapus")
            }

            Toast.makeText(this, "Item berhasil dihapus", Toast.LENGTH_SHORT).show()
            Log.d(TAG, "Berhasil dihapus")
        }
        binding.edtDate.setOnClickListener {
            showDatePickerDialog()
        }
        binding.backAction.setOnClickListener {
            onBackPressed()
        }
        binding.btnSave.setOnClickListener {
            cartViewModel.getSession().observe(this) { user ->
                token = user.idToken
                val idToken = token
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
                    if (idToken != null) {
                        cartViewModel.storeRecordFood(
                            idToken,
                            RecordedFoodsRequest(
                                foodsArray,
                                mealTime,
                                date
                            ),
                        )
                        cartViewModel.storeRecordFood.observe(this@CartActivity) { _ ->
                            val intent = Intent(
                                this@CartActivity,
                                MainActivity::class.java
                            )
                            startActivity(intent)
                        }
                    }

                    Toast.makeText(this, "Data berhasil disimpan", Toast.LENGTH_SHORT).show()
                } else {

                    Toast.makeText(this, "FoodItemList belum diinisialisasi", Toast.LENGTH_SHORT).show()
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
        clearData()
        super.onBackPressed()
        finish()
    }

    override fun onDestroy() {
        clearData()
        super.onDestroy()
    }

    private fun clearData() {
        // Bersihkan data yang perlu dihapus
        // Misalnya, reset data pada adapter atau hapus data di ViewModel
        if (::cartAdapter.isInitialized) {
            cartAdapter.clearData()
        }
        // Hapus data lainnya sesuai kebutuhan
    }

    private fun updateEditText() {
        val dateFormat = "dd/MM/yyyy"
        val simpleDateFormat = SimpleDateFormat(dateFormat, Locale.US)
        binding.edtDate.setText(simpleDateFormat.format(calendar.time))
    }
}