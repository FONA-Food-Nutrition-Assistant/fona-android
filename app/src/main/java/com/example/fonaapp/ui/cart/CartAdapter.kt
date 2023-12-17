package com.example.fonaapp.ui.cart

import android.content.ContentValues.TAG
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.fonaapp.data.models.FoodItem
import com.example.fonaapp.databinding.ItemsCartFoodBinding
import kotlin.math.min

class CartAdapter(
    private val foodList: MutableList<FoodItem>,
    private val servingSizes: List<String>,
    private val totalCaloriesTextView: TextView
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
            // Menggunakan quantity dari setiap item di foodList
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
        fun setQuantity(position: Int, quantity: Int) {
            binding.tvQuantity.text = quantity.toString()
            // ...
        }


        fun bind(foodItem: FoodItem) {
            binding.apply {
                namaMakanan.text = foodItem.name
                edtKalori.text = (foodItem.calories * foodItem.quantity).toString() // Hitung total kalori
                edtKarbohidrat.text = (foodItem.carbs * foodItem.quantity).toString()
                edtLemak.text = (foodItem.fats * foodItem.quantity).toString()
                edtProtein.text = (foodItem.proteins * foodItem.quantity).toString()

                val adapter = ArrayAdapter(
                    itemView.context,
                    android.R.layout.simple_spinner_item,
                    servingSizes
                )
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinnerActivity.adapter = adapter
                
                val servingSizePosition = findPositionByServingSize(foodItem.serving_size)
                spinnerActivity.setSelection(servingSizePosition)

                updateTotalCaloriesTextView()
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
                    Log.d(TAG,"Item berhasil ditambah")
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
                        Log.d(TAG,"Item berhasil dikurangi")
                    }
                }
            }
        }

    }

    private fun findPositionByServingSize(servingSize: String): Int {
        for ((index, size) in servingSizes.withIndex()) {
            if (size.equals(servingSize, ignoreCase = true)) {
                return index
            }
        }
        return 0 // Set default position jika tidak ditemukan
    }

    private fun updateTotalCaloriesTextView() {
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
}
