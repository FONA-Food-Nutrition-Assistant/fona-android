package com.example.fonaapp.data.response

import com.google.gson.annotations.SerializedName

data class GetFoodDetailResponse(

	@field:SerializedName("method")
	val method: String,

	@field:SerializedName("data")
	val data: List<DataItem>,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: Int
)

data class NutritionsItem(

	@field:SerializedName("fibers")
	val fibers: Any,

	@field:SerializedName("carbos")
	val carbos: Any,

	@field:SerializedName("created_at")
	val createdAt: String,

	@field:SerializedName("cals")
	val cals: Int,

	@field:SerializedName("food_id")
	val foodId: Int,

	@field:SerializedName("glucoses")
	val glucoses: Any,

	@field:SerializedName("caliums")
	val caliums: Any,

	@field:SerializedName("sodiums")
	val sodiums: Any,

	@field:SerializedName("updated_at")
	val updatedAt: String,

	@field:SerializedName("fats")
	val fats: Any,

	@field:SerializedName("proteins")
	val proteins: Any,

	@field:SerializedName("serving_size")
	val servingSize: String,

	@field:SerializedName("id")
	val id: Int
)

data class DataItem(

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("is_user_allergy")
	val isUserAllergy: Boolean,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("nutritions")
	val nutritions: NutritionsItem
)
