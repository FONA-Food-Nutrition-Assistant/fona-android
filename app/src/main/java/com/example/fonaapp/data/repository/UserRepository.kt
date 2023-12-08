package com.example.fonaapp.data.repository

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.fonaapp.data.models.User
import com.example.fonaapp.data.models.UserPreference
import com.example.fonaapp.data.response.UserPreferenceResponse
import com.example.fonaapp.data.retrofit.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserRepository(private val apiService: ApiService) {

    private val _userPreferenceResponse = MutableLiveData<UserPreferenceResponse>()
    val userPreferenceResponse: LiveData<UserPreferenceResponse> = _userPreferenceResponse

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    suspend fun storeUserData(user: User, firebaseToken: String) {
        _isLoading.value = true
        val call = apiService.storeUserData("Bearer $firebaseToken", user)

        call.enqueue(object : Callback<UserPreferenceResponse> {
            override fun onResponse(
                call: Call<UserPreferenceResponse>, response: Response<UserPreferenceResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful && response.body() != null) {
                    _userPreferenceResponse.value = response.body()
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<UserPreferenceResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    companion object {
        private const val TAG = "UserRepository"

        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            userPreference: UserPreference, apiService: ApiService
        ): UserRepository = instance ?: synchronized(this) {
            instance ?: UserRepository(apiService)
        }.also { instance = it }
    }
}