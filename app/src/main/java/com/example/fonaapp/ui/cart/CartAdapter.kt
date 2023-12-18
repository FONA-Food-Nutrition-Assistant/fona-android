package com.example.fonaapp.ui.cart

import android.content.ContentValues.TAG
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.fonaapp.data.models.FoodItem
import com.example.fonaapp.data.response.NutritionsItem
import com.example.fonaapp.databinding.ItemsCartFoodBinding

class CartAdapter(
    private val foodList: MutableList<FoodItem>,
    private val servingSizes: List<String>,
    private val totalCaloriesTextView: TextView,
    private val nutritionsItems: List<NutritionsItem>
) : RecyclerView.Adapter<CartAdapter.FoodViewHolder>() {

    fun getQuantities(): List<Int> {
        return foodList.map { it.quantity }
    }

    private var onMinusClickListener: ((Int) -> Unit)? = null

    fun setOnMinusClickListener(listener: (Int) -> Unit) {
        onMinusClickListener = listener
        Log.d(TAG, "onMinusClickListener set with listener: $listener")
    }

    private var totalCalories: Double = 0.0

    private fun calculateTotalCalories() {
        totalCalories = 0.0
        for (foodItem in foodList) {
            totalCalories += foodItem.calories * foodItem.quantity
        }
    }


    fun getTotalCalories(): Double {
        calculateTotalCalories()
        return totalCalories
    }

    fun removeItem(position: Int): FoodItem? {
        if (position < foodList.size) {
            val removedItem = foodList.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, itemCount)
            notifyDataSetChanged()
            calculateTotalCalories()
            updateTotalCaloriesTextView()
            return removedItem
        }
        return null
    }

    inner class FoodViewHolder(private val binding: ItemsCartFoodBinding) : RecyclerView.ViewHolder(binding.root) {
        private var currentPosition: Int = -1
        private lateinit var currentNutrition: NutritionsItem // Sesuaikan dengan kelas objek nutrisi Anda
        fun setQuantity(position: Int, quantity: Int) {
            binding.tvQuantity.text = quantity.toString()
        }
        fun updateNutritionByServingSize(position: Int, servingSize: String) {
            val foodItem = foodList[position]
            val selectedNutrition = nutritionsItems.find { it.serving_size == servingSize }
            selectedNutrition?.let {
                currentNutrition = it
                binding.edtKalori.text = (it.cals * foodItem.quantity).toString()
                binding.edtKarbohidrat.text = (it.carbos * foodItem.quantity).toString()
                binding.edtLemak.text = (it.fats * foodItem.quantity).toString()
                binding.edtProtein.text = (it.proteins * foodItem.quantity).toString()

                calculateTotalCalories()
                updateTotalCaloriesTextView()
            }
        }



        fun bind(foodItem: FoodItem) {
            binding.apply {
                namaMakanan.text = foodItem.name
                edtKalori.text = (foodItem.calories * foodItem.quantity).toString() // Hitung total kalori
                edtKarbohidrat.text = (foodItem.carbs * foodItem.quantity).toString()
                edtLemak.text = (foodItem.fats * foodItem.quantity).toString()
                edtProtein.text = (foodItem.proteins * foodItem.quantity).toString()

                val servingSizesAdapter = ArrayAdapter(
                    itemView.context,
                    android.R.layout.simple_spinner_item,
                    foodItem.servingSizes
                )
                servingSizesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinnerActivity.adapter = servingSizesAdapter

                val servingSizePosition = findPositionByServingSize(
                    foodItem.serving_size,
                    foodItem.servingSizes
                )
                spinnerActivity.setSelection(servingSizePosition)

            }
        }

        init {
            binding.ivPlus.setOnClickListener {
                val position = adapterPosition
                if (position < foodList.size) {
                    foodList[position].quantity += 1
                    setQuantity(position, foodList[position].quantity)
                    notifyItemChanged(position)
                    calculateTotalCalories()
                    updateTotalCaloriesTextView()
                    updateNutritionByServingSize(position, servingSizes[currentPosition])
                    Log.d(TAG, "Item berhasil ditambah")
                }
            }

            binding.ivMinus.setOnClickListener {
                val position = adapterPosition
                if (position < foodList.size && foodList[position].quantity > 0) {
                    if (foodList[position].quantity == 1) {
                        onMinusClickListener?.invoke(position)
                        Toast.makeText(itemView.context, "Item berhasil dihapus", Toast.LENGTH_SHORT).show()
                        Log.d(TAG, "Berhasil dihapus, Position: $position")
                        calculateTotalCalories()
                        updateTotalCaloriesTextView()
                    } else {
                        foodList[position].quantity -= 1
                        setQuantity(position, foodList[position].quantity)
                        notifyItemChanged(position)
                        updateTotalCaloriesTextView()
                        updateNutritionByServingSize(position, servingSizes[currentPosition])
                        Log.d(TAG, "Item berhasil dikurangi")
                    }
                }
            }
            binding.spinnerActivity.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parentView: AdapterView<*>, selectedItemView: View?, position: Int, id: Long) {
                    if (position != currentPosition) {
                        currentPosition = position
                        updateNutritionByServingSize(adapterPosition, servingSizes[position])
                    }
                }
                override fun onNothingSelected(parentView: AdapterView<*>) {
                    // Tidak melakukan apa-apa saat tidak ada yang dipilih
                }
            }
        }

    }

    private fun findPositionByServingSize(servingSize: String, servingSizes: List<String>): Int {
        for ((index, size) in servingSizes.withIndex()) {
            if (size.equals(servingSize, ignoreCase = true)) {
                return index
            }
        }
        return 0
    }

    fun updateTotalCaloriesTextView() {
        totalCaloriesTextView.text = getTotalCalories().toString()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {
        val binding = ItemsCartFoodBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FoodViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {
        holder.bind(foodList[position])
        holder.setQuantity(position, foodList[position].quantity)
    }

    override fun getItemCount(): Int {
        return foodList.size
    }

    fun clearData() {
        foodList.clear()
        notifyDataSetChanged()
        calculateTotalCalories()
        updateTotalCaloriesTextView()
    }


}
