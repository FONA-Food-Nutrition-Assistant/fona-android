package com.example.fonaapp.ui.home.record

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.fonaapp.data.response.BreakfastItem
import com.example.fonaapp.databinding.ItemsRecordFoodBinding

class RecordBreakfastAdapter(private var breakfastList: List<BreakfastItem>, private val onItemClick: (BreakfastItem) -> Unit) :
    RecyclerView.Adapter<RecordBreakfastAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemsRecordFoodBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(listBreakfast: BreakfastItem) {
            binding.tvFoodName.text = listBreakfast.name
            binding.tvFoodCal.text = listBreakfast.total_cals.toInt().toString()
            binding.root.setOnClickListener {
                onItemClick.invoke(listBreakfast)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecordBreakfastAdapter.ViewHolder {
        val binding = ItemsRecordFoodBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//        val params = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
//        binding.root.layoutParams = params
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecordBreakfastAdapter.ViewHolder, position: Int) {
        val listBreakfast = breakfastList[position]
        holder.bind(listBreakfast)
    }

    override fun getItemCount(): Int {
        return breakfastList.size
    }

    fun updateData(newList: List<BreakfastItem>) {
        breakfastList = newList
        notifyDataSetChanged()
    }

    fun getBreakfastList(): List<BreakfastItem> {
        return breakfastList
    }
}
