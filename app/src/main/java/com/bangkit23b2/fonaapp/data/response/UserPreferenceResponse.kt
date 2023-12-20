package com.bangkit23b2.fonaapp.data.response

import com.google.gson.annotations.SerializedName

data class UserPreferenceResponse(

	@field:SerializedName("method")
	val method: String,

	@field:SerializedName("data")
	val data: UserData,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: Int
)

data class UserData(
	val any: Any? = null
)
