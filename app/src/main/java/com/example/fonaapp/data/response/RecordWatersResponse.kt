package com.example.fonaapp.data.response

import com.google.gson.annotations.SerializedName

data class RecordWatersResponse(

	@field:SerializedName("method")
	val method: String,

	@field:SerializedName("data")
	val data: List<Any>,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: Int
)
