package com.example.fonaapp.ui.update

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.fonaapp.data.response.DataItem
import com.example.fonaapp.data.response.ListAllergyResponse
import com.example.fonaapp.databinding.ContentCheckboxAllergyBinding

class UpdateUserAdapter(private var dataItem: List<DataItem>) : RecyclerView.Adapter<UpdateUserAdapter.ViewHolder>() {
    inner class ViewHolder(private val binding: ContentCheckboxAllergyBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(dataItem: DataItem) {
            binding.checkBox.text = dataItem.name
            binding.checkBox.isChecked = selectedIds.contains(dataItem.id)

            binding.checkBox.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    selectedIds.add(dataItem.id)
                } else {
                    selectedIds.remove(dataItem.id)
                }
            }
        }
    }

    private var selectedIds: MutableList<Int> = mutableListOf()

    fun getSelectedIds():   List<Int> {
        return selectedIds
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ContentCheckboxAllergyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return dataItem.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dataItem = dataItem[position]
        holder.bind(dataItem)

        holder.itemView.setOnClickListener {
            if (selectedIds.contains(dataItem.id)) {
                selectedIds.remove(dataItem.id)
            } else {
                selectedIds.add(dataItem.id)
            }
            notifyItemChanged(position)
        }
    }

    fun setData(newData: List<DataItem>) {
        dataItem = newData
        notifyDataSetChanged()
    }
}