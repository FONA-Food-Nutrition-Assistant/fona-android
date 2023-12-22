package com.bangkit23b2.fonaapp.data.response

import com.google.gson.annotations.SerializedName

data class UpdateRecordWatersResponse(

	@field:SerializedName("method")
	val method: String,

	@field:SerializedName("data")
	val data: DataRecord,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: Int
)

data class DataRecord(

	@field:SerializedName("date")
	val date: String,

	@field:SerializedName("updated_at")
	val updatedAt: String,

	@field:SerializedName("user_id")
	val userId: String,

	@field:SerializedName("created_at")
	val createdAt: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("number_of_cups")
	val numberOfCups: Int
)
