package com.bangkit23b2.fonaapp.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bangkit23b2.fonaapp.data.response.DataItemDetail
import com.bangkit23b2.fonaapp.databinding.ItemsFoodBinding

class SearchAdapter :
    ListAdapter<DataItemDetail, SearchAdapter.ViewHolder>(FoodDetailDiffCallback()) {

    inner class ViewHolder(private val binding: ItemsFoodBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(foodDetail: DataItemDetail) {
            with(binding) {
                binding.namaMakanan.text = foodDetail.name

                val nutritionsList = foodDetail.nutritions

                if (nutritionsList.isNotEmpty()) {
                    val firstNutritionItem = nutritionsList[0]

                    binding.edtKalori.text = firstNutritionItem.cals.toString()
                    binding.edtKarbohidrat.text = firstNutritionItem.carbos.toString()
                    binding.edtLemak.text = firstNutritionItem.fats.toString()
                    binding.edtProtein.text = firstNutritionItem.proteins.toString()
                } else {

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
