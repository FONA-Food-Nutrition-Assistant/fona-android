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
    @field:SerializedName("height")
    val height: Int,

    @field:SerializedName("weight")
    val weight: Int,

    @field:SerializedName("gender")
    val gender: String,

    @field:SerializedName("date_of_birth")
    val dateOfBirth: String,

    @field:SerializedName("activity")
    val activity: String,

    @field:SerializedName("allergies")
    val allergies: MutableList<Int>,
)
