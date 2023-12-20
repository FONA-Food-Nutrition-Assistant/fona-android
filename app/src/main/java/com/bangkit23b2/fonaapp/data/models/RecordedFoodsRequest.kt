package com.bangkit23b2.fonaapp.data.models

data class FoodRecordItem(
    val nutrition_id: Int,
    val quantity: Int
)

data class RecordedFoodsRequest(
    val foods: List<FoodRecordItem>,
    val meal_time: String,
    val date: String
)