package com.bangkit23b2.fonaapp.ui.result

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bangkit23b2.fonaapp.data.response.AllergiesItem
import com.bangkit23b2.fonaapp.databinding.ItemsAllergyBinding

class ResultUserAdapter(private var allergiesItem: List<AllergiesItem>) : RecyclerView.Adapter<ResultUserAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemsAllergyBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(allergy: AllergiesItem) {
            binding.tvAllergyName.text = allergy.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemsAllergyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//        val params = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
//        binding.root.layoutParams = params
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val allergy = allergiesItem[position]
        holder.bind(allergy)
    }

    override fun getItemCount(): Int {
        return allergiesItem.size
    }

    fun updateData(newList: List<AllergiesItem>) {
        allergiesItem = newList
        notifyDataSetChanged()
    }
}
