package com.example.fonaapp.ui.home.record

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.fonaapp.R
import com.example.fonaapp.data.models.WaterRecord
import com.example.fonaapp.databinding.ItemsDrinksBinding

class RecordWaterAdapter(private val gelasList: MutableList<WaterRecord>) :
    RecyclerView.Adapter<RecordWaterAdapter.GelasViewHolder>() {

    class GelasViewHolder(val binding: ItemsDrinksBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GelasViewHolder {
        val binding = ItemsDrinksBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GelasViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GelasViewHolder, position: Int) {
        // Mengambil jumlah gelas dari WaterRecord
        val numberOfCups = gelasList[position].number_of_cups

        // Set jumlah gambar gelas sesuai dengan numberOfCups
        for (i in 0 until numberOfCups) {
            val ivGelas = ImageView(holder.itemView.context)
            ivGelas.setImageResource(R.drawable.ic_cup_drink)
            holder.binding.llGelasContainer.addView(ivGelas)
        }
    }

    override fun getItemCount(): Int {
        return gelasList.size
    }
    fun updateData(newList: List<WaterRecord>) {
        gelasList.clear()
        gelasList.addAll(newList)
        notifyDataSetChanged()
    }
}
