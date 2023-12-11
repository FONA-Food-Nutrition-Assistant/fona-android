package com.example.fonaapp.data.response

import com.google.gson.annotations.SerializedName

data class UpdateUserResponse(

	@field:SerializedName("method")
	val method: String,

	@field:SerializedName("data")
	val data: Data,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: Int
)

data class Data(

	@field:SerializedName("allergies")
	val allergies: List<AllergiesItem>,

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

	@field:SerializedName("weight")
	val weight: Int,

	@field:SerializedName("created_at")
	val createdAt: String,

	@field:SerializedName("height")
	val height: Int
)
