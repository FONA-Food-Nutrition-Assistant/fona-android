package com.bangkit23b2.fonaapp.ui.home.record

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bangkit23b2.fonaapp.databinding.ItemsDrinksBinding

class RecordWaterAdapter :
    RecyclerView.Adapter<RecordWaterAdapter.ViewHolder>() {

    private var drink: Int = 0

    class ViewHolder(private val binding: ItemsDrinksBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val imageViewCup = binding.imageViewCup
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemsDrinksBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

    }


    override fun getItemCount(): Int {
        return drink
    }

    fun updateData(drink: Int) {
        this.drink = drink
    }
}
