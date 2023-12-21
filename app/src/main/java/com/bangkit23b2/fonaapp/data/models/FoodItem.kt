package com.bangkit23b2.fonaapp.data.models

import com.bangkit23b2.fonaapp.data.response.DataFood
import com.bangkit23b2.fonaapp.data.response.NutritionsItem


data class FoodItem(
    val id: Int,
    val name: String,
    var calories: Double,
    val carbs: Double,
    val proteins: Double,
    val fibers: Double,
    val fats: Double,
    val glucoses: Double,
    val sodiums: Double,
    val caliums: Double,
    var serving_size: String,
    val createdAt: String?,
    val updatedAt: String?,
    val servingSizes: List<String>,
    var nutritionId: Int,
    var quantity: Int,
) {
}

fun convertToFoodItem(dataFood: DataFood): FoodItem {

    val nutritionsItems = dataFood.nutritions

    val servingSizes = nutritionsItems.map { it.serving_size }

    val nutritionsItem = nutritionsItems.first()
    return FoodItem(
        id = dataFood.id,
        name = dataFood.name,
        calories = nutritionsItem.cals,
        carbs = nutritionsItem.carbos,
        proteins = nutritionsItem.proteins,
        fibers = nutritionsItem.fibers,
        fats = nutritionsItem.fats,
        glucoses = nutritionsItem.glucoses,
        sodiums = nutritionsItem.sodiums,
        caliums = nutritionsItem.caliums,
        serving_size = nutritionsItem.serving_size,
        createdAt = nutritionsItem.createdAt,
        updatedAt = nutritionsItem.updatedAt,
        servingSizes = servingSizes,
        nutritionId = nutritionsItem.id,
        quantity = 1
    )
}

fun List<DataFood>.convertToNutritionItem(): List<NutritionsItem> {
    return this.flatMap { food ->
        food.nutritions.map { nutrition ->
            NutritionsItem(
                nutrition.fibers,
                nutrition.carbos,
                nutrition.createdAt,
                nutrition.cals,
                food.id,
                nutrition.glucoses,
                nutrition.caliums,
                nutrition.sodiums,
                nutrition.updatedAt,
                nutrition.fats,
                nutrition.proteins,
                nutrition.serving_size,
                nutrition.id
            )
        }
    }
}

fun List<FoodItem>.getUniqueServingSizes(): List<String> {
    val servingSizes = mutableListOf<String>()
    val uniqueServingSizes = mutableListOf<String>()

    for (foodItem in this) {
        if (foodItem.serving_size.isNotBlank() && !servingSizes.contains(foodItem.serving_size)) {
            servingSizes.add(foodItem.serving_size)
            uniqueServingSizes.add(foodItem.serving_size)
        }
    }
    return uniqueServingSizes
}