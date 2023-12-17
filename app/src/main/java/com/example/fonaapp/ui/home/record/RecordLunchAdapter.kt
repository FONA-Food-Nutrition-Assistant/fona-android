package com.example.fonaapp.ui.home.record

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.fonaapp.data.response.LunchItem
import com.example.fonaapp.databinding.ItemsRecordFoodBinding

class RecordLunchAdapter(private var lunchList: List<LunchItem>, private val onItemClick: (LunchItem) -> Unit) :
    RecyclerView.Adapter<RecordLunchAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemsRecordFoodBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(listLunch: LunchItem) {
            binding.tvFoodName.text = listLunch.name
            binding.tvFoodCal.text = listLunch.total_cals.toInt().toString()
            binding.root.setOnClickListener {
                onItemClick.invoke(listLunch)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecordLunchAdapter.ViewHolder {
        val binding = ItemsRecordFoodBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecordLunchAdapter.ViewHolder, position: Int) {
        val listLunch = lunchList[position]
        holder.bind(listLunch)
    }

    override fun getItemCount(): Int {
        return lunchList.size
    }

    fun updateData(newList: List<LunchItem>) {
        lunchList = newList
        notifyDataSetChanged()
    }

    fun getLunchList(): List<LunchItem> {
        return lunchList
    }
}