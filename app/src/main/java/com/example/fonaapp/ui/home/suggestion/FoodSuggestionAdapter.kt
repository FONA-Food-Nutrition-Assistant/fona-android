package com.example.fonaapp.ui.home.suggestion

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.fonaapp.data.response.FoodSuggestion
import com.example.fonaapp.data.response.Meal
import com.example.fonaapp.databinding.ItemsSuggestionFoodBinding

class FoodSuggestionAdapter(private var meals: List<Meal>) :
    RecyclerView.Adapter<FoodSuggestionAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemsSuggestionFoodBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(meals[position])
    }

    override fun getItemCount(): Int {
        return meals.size
    }

    inner class ViewHolder(private val binding: ItemsSuggestionFoodBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(meal: Meal) {
            with(binding) {
                Glide.with(root.context)
                    .load(meal.imageUrl)
                    .into(ivFood)
            }
            binding.tvMealTime.text = getMealTime(position)
            // Set package title
            binding.tvPaket.text = meal.name
            binding.tvMakanan.text = meal.foods.joinToString { it.name }
            // Set total calories
            binding.tvKalori.text = "${meal.totalCals} kalori"
        }
    }
    private fun getMealTime(position: Int): String {
        return when (position) {
            0 -> "Sarapan"
            1 -> "Makan Siang"
            2 -> "Makan Malam"
            else -> "Waktu Makan Tidak Diketahui"
        }
    }
}
