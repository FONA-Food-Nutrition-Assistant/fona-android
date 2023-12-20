package com.example.fonaapp.data.models

import com.example.fonaapp.R

data class WaterRecord(
    val number_of_cups: Int,
    val date: String
) {
    // Fungsi untuk mendapatkan referensi gambar sesuai dengan jumlah cangkir
    fun getCupImageResource(): Int {
        return R.drawable.ic_cup_drink // Ganti dengan gambar yang sesuai
    }
}