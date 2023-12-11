package com.example.fonaapp.data.response

import com.google.gson.annotations.SerializedName

data class GetUserDataResponse(

	@field:SerializedName("method")
	var method: String,

	@field:SerializedName("data")
	var data: ResultData,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	var status: Int
)

data class ResultData(

	@field:SerializedName("allergies")
	val allergies: MutableList<AllergiesItem>,

	@field:SerializedName("uid")
	val uid: String,

	@field:SerializedName("activity")
	val activity: String,

	@field:SerializedName("gender")
	val gender: String,

	@field:SerializedName("updated_at")
	val updatedAt: String,

	@field:SerializedName("date_of_birth")
	val dateOfBirth: String,

	@field:SerializedName("tdee")
	val tdee: Double,

	@field:SerializedName("weight")
	val weight: Int,

	@field:SerializedName("created_at")
	val createdAt: String,

	@field:SerializedName("age")
	val age: Int,

	@field:SerializedName("height")
	val height: Int,

	@field:SerializedName("bmi")
	val bmi: Double
)

data class AllergiesItem(

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("id")
	val id: Int
)
