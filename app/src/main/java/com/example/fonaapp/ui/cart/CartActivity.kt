package com.example.fonaapp.ui.cart

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fonaapp.R
import com.example.fonaapp.data.models.FoodItem
import com.example.fonaapp.data.models.convertToFoodItem
import com.example.fonaapp.data.models.getUniqueServingSizes
import com.example.fonaapp.data.response.DataFood
import com.example.fonaapp.data.response.UploadFoodResponse
import com.example.fonaapp.databinding.ActivityCartBinding

class CartActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCartBinding
    private lateinit var cartAdapter: CartAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupAdapter()
    }


    private fun setupAdapter(){
        // Mendapatkan data dari response UploadFoodResponse
        val uploadFoodResponse: UploadFoodResponse? = intent.getParcelableExtra("UPLOAD_RESPONSE")

        // Memastikan bahwa data ada dan tidak null
        if (uploadFoodResponse != null) {
            // Mengambil list DataFood dari response
            val dataFoodList: List<DataFood> = uploadFoodResponse.data

            // Membuat list FoodItem dari list DataFood
            val foodItemList: List<FoodItem> = dataFoodList.map { dataFood ->
                convertToFoodItem(dataFood)
            }
            val servingSizes = foodItemList.getUniqueServingSizes()
            // Inisialisasi adapter dengan data
            cartAdapter = CartAdapter(foodItemList, servingSizes)
            binding.rvPredict.layoutManager = LinearLayoutManager(this)
            cartAdapter.notifyDataSetChanged()
            // Set adapter ke RecyclerView di dalam layout XML
            binding.rvPredict.adapter = cartAdapter
        }
    }
}