package com.bangkit23b2.fonaapp.data.response

import com.google.gson.annotations.SerializedName

data class ListAllergyResponse(

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

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("id")
	val id: Int
)
