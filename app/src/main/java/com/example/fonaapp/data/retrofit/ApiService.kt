package com.example.fonaapp.data.retrofit

import com.example.fonaapp.data.models.User
import com.example.fonaapp.data.response.GetNutritionListResponse
import com.example.fonaapp.data.response.GetUserDataResponse
import com.example.fonaapp.data.response.UpdateUserResponse
import com.example.fonaapp.data.response.UserPreferenceResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT

interface ApiService {
    @POST("/gateway/v1/us/user")
    fun storeUserData(
        @Header("Authorization") idToken: String,
        @Body request: User
    ): Call<UserPreferenceResponse>

    @GET("/gateway/v1/us/user")
    fun getUserData(
        @Header("Authorization") idToken: String,
    ): Call<GetUserDataResponse>

    //TODO MARWAN 1 PUT USER
    @PUT("/gateway/v1/us/user")
    fun updateUserData(
        @Header("Authorization") idToken: String,
        @Body request: User
    ): Call<UpdateUserResponse>




    //TODO LULU 1 - GET LIST OF NUTRITION & NANTI BUAT RESPONSE DARI JSON
    @GET("/gateway/v1/fs/food/nutrition")
    fun getNutritionList (
        @Header("Authorization") idToken: String,
    ): Call<GetNutritionListResponse>

}