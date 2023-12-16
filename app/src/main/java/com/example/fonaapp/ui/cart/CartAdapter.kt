package com.example.fonaapp.ui.cart

import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.example.fonaapp.data.models.FoodItem
import com.example.fonaapp.databinding.ItemsCartFoodBinding

class CartAdapter (private val foodList: MutableList<FoodItem>,
                   private val servingSizes: List<String>)
    : RecyclerView.Adapter<CartAdapter.FoodViewHolder>() {

    // Fungsi untuk mengatur click listener pada tombol minus
    private var onMinusClickListener: ((Int) -> Unit)? = null

    fun setOnMinusClickListener(listener: (Int) -> Unit) {
        onMinusClickListener = listener
    }

    fun removeItem(position: Int) {
        // Hapus item dari list dan beri tahu adapter
        foodList.removeAt(position)
        notifyItemRemoved(position)
    }

    inner class FoodViewHolder(private val binding: ItemsCartFoodBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(foodItem: FoodItem) {
            binding.apply {
                // Bind data ke view menggunakan View Binding
                namaMakanan.text = foodItem.name
                edtKalori.text = foodItem.calories.toString()
                edtKarbohidrat.text = foodItem.carbs.toString()
                edtLemak.text = foodItem.fats.toString()
                edtProtein.text = foodItem.proteins.toString()

                // Menggunakan ArrayAdapter.createFromResource untuk dropdown
                // Menggunakan ArrayAdapter untuk dropdown
                val adapter = ArrayAdapter(
                    itemView.context,
                    android.R.layout.simple_spinner_item,
                    servingSizes
                )
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinnerActivity.adapter = adapter

                // Menetapkan selected item pada dropdown sesuai serving_size dari FoodItem
                val servingSizePosition = adapter.getPosition(foodItem.serving_size)
                spinnerActivity.setSelection(servingSizePosition)

                // Menambahkan click listener pada tombol minus
                ivMinus.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        // Memanggil fungsi setOnMinusClickListener jika telah diatur
                        onMinusClickListener?.invoke(position)
                        // Tampilkan Toast bahwa item berhasil dihapus
                        Toast.makeText(itemView.context, "Item berhasil dihapus", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {
        val binding = ItemsCartFoodBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FoodViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {
        holder.bind(foodList[position])
    }

    override fun getItemCount(): Int {
        return foodList.size
    }
}
