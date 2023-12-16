package com.example.fonaapp.ui.cart

import android.R
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.fonaapp.data.models.FoodItem
import com.example.fonaapp.databinding.ItemsCartFoodBinding

class CartAdapter (private val foodList: List<FoodItem>,
                   private val servingSizes: List<String>)
    : RecyclerView.Adapter<CartAdapter.FoodViewHolder>() {

    inner class FoodViewHolder(private val binding: ItemsCartFoodBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(foodItem: FoodItem) {
            binding.apply {
                // Bind data ke view menggunakan View Binding
                namaMakanan.text = foodItem.name
                edtKalori.text = foodItem.calories.toString()
                edtKarbohidrat.text = foodItem.carbs.toString()
                edtLemak.text = foodItem.fats.toString()
                edtProtein.text = foodItem.proteins.toString()
                // Menggunakan ArrayAdapter.createFromResource untuk dropdown
                // Menggunakan ArrayAdapter untuk dropdown
                val adapter = ArrayAdapter(
                    itemView.context,
                    android.R.layout.simple_spinner_item,
                    servingSizes
                )
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinnerActivity.adapter = adapter

                // Menetapkan selected item pada dropdown sesuai serving_size dari FoodItem
                val servingSizePosition = adapter.getPosition(foodItem.serving_size)
                spinnerActivity.setSelection(servingSizePosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {
        val binding = ItemsCartFoodBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FoodViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {
        holder.bind(foodList[position])
    }

    override fun getItemCount(): Int {
        return foodList.size
    }
}