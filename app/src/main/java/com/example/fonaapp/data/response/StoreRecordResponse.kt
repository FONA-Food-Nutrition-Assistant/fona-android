package com.example.fonaapp.data.response

import com.google.gson.annotations.SerializedName

data class StoreRecordResponse(

	@field:SerializedName("method")
	val method: String,

	@field:SerializedName("data")
	val data: DataFoodRecord,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: Int
)

data class DataFoodRecord(

	@field:SerializedName("data")
	val data: List<DataRecordedFood>,

	@field:SerializedName("is_foods_contain_allergies")
	val isFoodsContainAllergies: Boolean,

	@field:SerializedName("foods_contain_allergies")
	val foodsContainAllergies: List<Any>
)

data class DataRecordedFood(

	@field:SerializedName("date")
	val date: String,

	@field:SerializedName("quantity")
	val quantity: Int,

	@field:SerializedName("nutrition_id")
	val nutritionId: Int,

	@field:SerializedName("user_id")
	val userId: String,

	@field:SerializedName("meal_time")
	val mealTime: String,

	@field:SerializedName("id")
	val id: Int
)
