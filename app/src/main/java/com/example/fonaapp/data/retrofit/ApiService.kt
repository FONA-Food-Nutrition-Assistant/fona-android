package com.example.fonaapp.data.retrofit

import com.example.fonaapp.data.models.User
import com.example.fonaapp.data.models.WaterRecord
import com.example.fonaapp.data.response.Data
import com.example.fonaapp.data.response.GetUserDataResponse
import com.example.fonaapp.data.response.HomeResponse
import com.example.fonaapp.data.response.ListAllergyResponse
import com.example.fonaapp.data.response.RecordWatersResponse
import com.example.fonaapp.data.response.UpdateRecordWatersResponse
import com.example.fonaapp.data.response.UpdateUserResponse
import com.example.fonaapp.data.response.UploadFoodResponse
import com.example.fonaapp.data.response.UserPreferenceResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Query

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
        @Body request: Data
    ): Call<UpdateUserResponse>

    @GET("/gateway/v1/fs/allergy")
    fun getListAllergy(
        @Header("Authorization") idToken: String,
    ): Call<ListAllergyResponse>

    @GET("/gateway/v1/fs/home")
    fun getHomeData(
        @Header("Authorization") idToken: String,
        @Query("date") date: String
    ): Call<HomeResponse>

    @POST("/gateway/v1/fs/water")
    fun storeWaterRecord(
        @Header("Authorization") idToken: String,
        @Body record: WaterRecord
    ): Call<RecordWatersResponse>

    @PUT("/gateway/v1/fs/water")
    fun updateWaterRecord(
        @Header("Authorization") idToken: String,
        @Body record: WaterRecord
    ): Call<UpdateRecordWatersResponse>

    @Multipart
    @POST("/gateway/v1/is/identification/predict")
    fun uploadFood(
        @Header("Authorization") idToken: String,
        @Part image: MultipartBody.Part,
    ) : Call<UploadFoodResponse>




    //TODO LULU 1 - GET LIST OF NUTRITION & NANTI BUAT RESPONSE DARI JSON

}