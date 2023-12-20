package com.bangkit23b2.fonaapp.data.models

data class User(
    val height: Int,
    val weight: Int,
    val gender: String,
    val date_of_birth: String,
    val activity: String,
    val allergies: MutableList<Int>
)