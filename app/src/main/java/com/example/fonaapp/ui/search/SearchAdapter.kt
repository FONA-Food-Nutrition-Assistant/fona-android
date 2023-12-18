package com.example.fonaapp.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.fonaapp.data.response.DataItemDetail
import com.example.fonaapp.databinding.ItemsFoodBinding

class SearchAdapter :
    ListAdapter<DataItemDetail, SearchAdapter.ViewHolder>(FoodDetailDiffCallback()) {

    inner class ViewHolder(private val binding: ItemsFoodBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(foodDetail: DataItemDetail) {
            with(binding) {
                binding.namaMakanan.text = foodDetail.name

                // Example: Assuming nutritions is a list
                val nutritionsList = foodDetail.nutritions

                // Handle the case where nutritionsList is not empty
                if (nutritionsList.isNotEmpty()) {
                    val firstNutritionItem = nutritionsList[0] // Access the first item for demonstration

                    // Use the properties of NutritionsItems
                    binding.edtKalori.text = firstNutritionItem.cals.toString()
                    binding.edtKarbohidrat.text = firstNutritionItem.carbos.toString()
                    binding.edtLemak.text = firstNutritionItem.fats.toString()
                    binding.edtProtein.text = firstNutritionItem.proteins.toString()
                } else {
                    // Handle the case where nutritionsList is empty
                    // You may want to set default values or handle this case based on your logic
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemsFoodBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val foodDetail = getItem(position)
        holder.bind(foodDetail)
    }

    private class FoodDetailDiffCallback : DiffUtil.ItemCallback<DataItemDetail>() {
        override fun areItemsTheSame(oldItem: DataItemDetail, newItem: DataItemDetail): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: DataItemDetail, newItem: DataItemDetail): Boolean {
            return oldItem == newItem
        }
    }
}
