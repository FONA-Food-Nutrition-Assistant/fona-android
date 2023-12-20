package com.bangkit23b2.fonaapp.data.models

import com.bangkit23b2.fonaapp.R

data class WaterRecord(
    val number_of_cups: Int,
    val date: String
) {
    // Fungsi untuk mendapatkan referensi gambar sesuai dengan jumlah cangkir
    fun getCupImageResource(): Int {
        return R.drawable.ic_cup_drink // Ganti dengan gambar yang sesuai
    }
}