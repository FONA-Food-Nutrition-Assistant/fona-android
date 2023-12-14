package com.example.fonaapp.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.fonaapp.data.response.AllergiesItem
import com.example.fonaapp.data.response.DataItem
import com.example.fonaapp.data.response.GetFoodDetailResponse
import com.example.fonaapp.databinding.ContentFoodBinding
import com.example.fonaapp.databinding.ItemsAllergyBinding


class SearchAdapter (private val getFoodDetailResponse: List<DataItem>) : RecyclerView.Adapter<SearchAdapter.ViewHolder>() {

    private var searchFood: List<DataItem> = emptyList()


    inner class ViewHolder(private val binding: ContentFoodBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(searchFood: DataItem) {
            val nutrition = searchFood.nutritions
            binding.tvNamaMakanan.text = searchFood.name
            binding.edtKalori.text = nutrition.cals.toString()
            binding.edtKarbohidrat.text = nutrition.carbos.toString()
            binding.edtLemak.text = nutrition.fats.toString()
            binding.edtProtein.text = nutrition.proteins.toString()
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ContentFoodBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return searchFood.size

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(searchFood[position])

    }
}