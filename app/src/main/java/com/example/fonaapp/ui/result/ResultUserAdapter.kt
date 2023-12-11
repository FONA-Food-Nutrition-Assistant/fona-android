package com.example.fonaapp.ui.result

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.fonaapp.data.response.AllergiesItem
import com.example.fonaapp.databinding.ItemsAllergyBinding

class ResultUserAdapter(private val allergiesItem: List<AllergiesItem>) : RecyclerView.Adapter<ResultUserAdapter.ViewHolder>() {

    private var allergies: List<AllergiesItem> = emptyList()

    inner class ViewHolder(private val binding: ItemsAllergyBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(allergy: AllergiesItem) {
            binding.tvAllergyName.text = allergy.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemsAllergyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(allergies[position])
    }

    override fun getItemCount(): Int {
        return allergies.size
    }

}
