package com.bangkit23b2.fonaapp.ui.home.record

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bangkit23b2.fonaapp.data.models.WaterRecord
import com.bangkit23b2.fonaapp.databinding.ItemsDrinksBinding

class RecordWaterAdapter(private val dataList: MutableList<WaterRecord>) :
    RecyclerView.Adapter<RecordWaterAdapter.ViewHolder>() {

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
        val waterRecord = dataList[position]

        // Clear existing views (if any)
        holder.imageViewCup.removeAllViews()

        // Tambahkan gambar sesuai dengan jumlah cangkir
        for (i in 0 until waterRecord.number_of_cups) {
            val imageView = ImageView(holder.itemView.context)
            imageView.setImageResource(waterRecord.getCupImageResource())
            holder.imageViewCup.addView(imageView)
        }
    }


    override fun getItemCount(): Int {
        return dataList.size
    }

    fun updateData(newDataList: List<WaterRecord>) {
        dataList.clear()
        dataList.addAll(newDataList)
        notifyDataSetChanged()
    }
}
