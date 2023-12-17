package com.example.fonaapp.ui.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.fonaapp.R
import com.example.fonaapp.data.response.DataItemDetail
import com.example.fonaapp.databinding.ContentCheckboxAllergyBinding
import com.example.fonaapp.databinding.ItemsFoodBinding
import com.example.fonaapp.databinding.ItemsSuggestionFoodBinding
import com.example.fonaapp.ui.update.UpdateUserAdapter

class SearchAdapter(private val foodDetailList: List<DataItemDetail>) :
    RecyclerView.Adapter<SearchAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemsFoodBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(foodDetail: DataItemDetail) {
            with(binding) {
                binding.namaMakanan.text = foodDetail.name
                binding.edtKalori.text = foodDetail.nutritions.cals.toString()
                binding.edtKarbohidrat.text = foodDetail.nutritions.carbos.toString()
                binding.edtLemak.text = foodDetail.nutritions.fats.toString()
                binding.edtProtein.text = foodDetail.nutritions.proteins.toString()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchAdapter.ViewHolder {
        val binding =
            ItemsFoodBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val foodDetail = foodDetailList[position]
        holder.bind(foodDetail)
    }

    override fun getItemCount(): Int {
        return foodDetailList.size
    }
}