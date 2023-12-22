package com.bangkit23b2.fonaapp.ui.home.record

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bangkit23b2.fonaapp.data.response.DinnerItem
import com.bangkit23b2.fonaapp.databinding.ItemsRecordFoodBinding

class RecordDinnerAdapter(private var dinnerList: List<DinnerItem>, private val onItemClick: (DinnerItem) -> Unit) :
    RecyclerView.Adapter<RecordDinnerAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemsRecordFoodBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(listDinner: DinnerItem) {
            binding.tvFoodName.text = listDinner.name
            binding.tvFoodCal.text = listDinner.total_cals.toInt().toString()
            binding.root.setOnClickListener {
                onItemClick.invoke(listDinner)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecordDinnerAdapter.ViewHolder {
        val binding = ItemsRecordFoodBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecordDinnerAdapter.ViewHolder, position: Int) {
        val listDinner = dinnerList[position]
        holder.bind(listDinner)
    }

    override fun getItemCount(): Int {
        return dinnerList.size
    }

    fun updateData(newList: List<DinnerItem>) {
        dinnerList = newList
        notifyDataSetChanged()
    }

    fun getDinnerList(): List<DinnerItem> {
        return dinnerList
    }
}