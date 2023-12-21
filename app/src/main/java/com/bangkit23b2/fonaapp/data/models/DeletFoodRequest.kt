package com.bangkit23b2.fonaapp.data.models

data class DeleteFoodRequest(
    val nutrition_ids: List<Int>,
    val meal_time: String,
    val date: String
)