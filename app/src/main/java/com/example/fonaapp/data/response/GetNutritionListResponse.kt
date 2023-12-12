package com.example.fonaapp.data.response

import com.google.gson.annotations.SerializedName

data class GetNutritionListResponse(

	@field:SerializedName("method")
	val method: String,

	@field:SerializedName("data")
	val data: List<DataItem>,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: Int
)

data class DataItem(

	@field:SerializedName("fibers")
	val fibers: Int,

	@field:SerializedName("carbos")
	val carbos: Any,

	@field:SerializedName("created_at")
	val createdAt: String,

	@field:SerializedName("cals")
	val cals: Int,

	@field:SerializedName("food_id")
	val foodId: Int,

	@field:SerializedName("glucoses")
	val glucoses: Int,

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
