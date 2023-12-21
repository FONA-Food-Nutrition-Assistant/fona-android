package com.bangkit23b2.fonaapp.data.models

import com.bangkit23b2.fonaapp.R

data class WaterRecord(
    val number_of_cups: Int,
    val date: String
) {
    fun getCupImageResource(): Int {
        return R.drawable.ic_cup_drink
    }
}