package com.example.fonaapp.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import com.example.fonaapp.data.models.User
import com.example.fonaapp.data.models.UserModel
import com.example.fonaapp.data.models.UserPreference
import com.example.fonaapp.data.response.Data
import com.example.fonaapp.data.response.DataItem
import com.example.fonaapp.data.response.GetUserDataResponse
import com.example.fonaapp.data.response.ListAllergyResponse
import com.example.fonaapp.data.response.ResultData
import com.example.fonaapp.data.response.UpdateUserResponse
import com.example.fonaapp.data.response.UserPreferenceResponse
import com.example.fonaapp.data.retrofit.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FonaRepository(private val userPreference: UserPreference, private val apiService: ApiService) {



    private val _userPreferenceResponse = MutableLiveData<UserPreferenceResponse>()
    val userPreferenceResponse: LiveData<UserPreferenceResponse> = _userPreferenceResponse

    private val _getUserDataResponse = MutableLiveData<GetUserDataResponse>()
    val getUserDataResponse: LiveData<GetUserDataResponse> = _getUserDataResponse

    private val _userDataResponse = MutableLiveData<ResultData>()
    val userDataResponse: LiveData<ResultData> = _userDataResponse

    private val _updateUserDataResponse = MutableLiveData<UpdateUserResponse>()
    val updateUserResponse: LiveData<UpdateUserResponse> = _updateUserDataResponse

    private val _listAllergyResponse = MutableLiveData<ListAllergyResponse>()
    val listAllergyResponse: LiveData<ListAllergyResponse> = _listAllergyResponse

    private val _checkboxAllergyResponse = MutableLiveData<List<ListAllergyResponse>>()
    val checkboxAllergyResponse: LiveData<List<ListAllergyResponse>> = _checkboxAllergyResponse

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isDataDiri = MutableLiveData<Boolean>()
    val isDataDiri: LiveData<Boolean> = _isDataDiri

    private val _isLogin = MutableLiveData<Boolean>()
    val isLogin : LiveData<Boolean> = _isLogin



    //TODO LULU 2 - Buat variabel food/nutrition response

    fun storeUserData(user: User, firebaseToken: String) {
        _isLoading.value = true
        val call = apiService.storeUserData("Bearer $firebaseToken", user)

        call.enqueue(object : Callback<UserPreferenceResponse> {
            override fun onResponse(
                call: Call<UserPreferenceResponse>, response: Response<UserPreferenceResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful && response.body() != null) {
                    _isDataDiri.value = true
                    _userPreferenceResponse.value = response.body()
                } else {
                    _isDataDiri.value = false
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<UserPreferenceResponse>, t: Throwable) {
                _isLoading.value = false
                _isDataDiri.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    fun getUserData(firebaseToken: String){
        _isLoading.value = true
        val call = apiService.getUserData("Bearer $firebaseToken")
        call.enqueue(object : Callback<GetUserDataResponse> {
            override fun onResponse(
                call: Call<GetUserDataResponse>,
                response: Response<GetUserDataResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful && response.body() != null) {
                    _userDataResponse.value = response.body()?.data
                    Log.d(TAG, "User data response: ${response.body()?.data}")
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<GetUserDataResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }
    fun getUserDataResponse(firebaseToken: String){
        _isLoading.value = true
        val call = apiService.getUserData("Bearer $firebaseToken")
        call.enqueue(object : Callback<GetUserDataResponse> {
            override fun onResponse(
                call: Call<GetUserDataResponse>,
                response: Response<GetUserDataResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful && response.body() != null) {
                    _getUserDataResponse.value = response.body()
                    _isDataDiri.value = true
                    _isLogin.value = false
                } else if(response.code() == 401) {
                    _isDataDiri.value = false
                    _isLogin.value = true
                } else{
                    Log.e(TAG, "onFailure: gagal 2")
                    _isDataDiri.value = false
                    _isLogin.value = false
                }
            }
            override fun onFailure(call: Call<GetUserDataResponse>, t: Throwable) {
                _isLoading.value = false
                _isDataDiri.value = false
                Log.e(TAG, "onFailure: gagal 1")
            }
        })
    }

    fun updateUserData(user: Data, firebaseToken: String){
        _isLoading.value = true
        val call = apiService.updateUserData("Bearer $firebaseToken", user)

        call.enqueue(object : Callback<UpdateUserResponse> {
            override fun onResponse(
                call: Call<UpdateUserResponse>, response: Response<UpdateUserResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful && response.body() != null) {
                    _isDataDiri.value = true
                    _updateUserDataResponse.value = response.body()
                } else {
                    _isDataDiri.value = false
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<UpdateUserResponse>, t: Throwable) {
                _isLoading.value = false
                _isDataDiri.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    fun getListAllergy(firebaseToken: String){
        _isLoading.value = true
        val call = apiService.getListAllergy("Bearer $firebaseToken")
        call.enqueue(object : Callback<ListAllergyResponse> {
            override fun onResponse(
                call: Call<ListAllergyResponse>,
                response: Response<ListAllergyResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful && response.body() != null) {
                    _listAllergyResponse.value = response.body()
                    _isDataDiri.value = true
                } else {
                    Log.e(TAG, "onFailure: gagal 2")
                    _isDataDiri.value = false
                }
            }
            override fun onFailure(call: Call<ListAllergyResponse>, t: Throwable) {
                _isLoading.value = false
                _isDataDiri.value = false
                Log.e(TAG, "onFailure: gagal 1")
            }
        })
    }

    //TODO LULU 3 - Buat fungsi get list food


    fun getSession(): LiveData<UserModel> {
        return userPreference.getSession().asLiveData()
    }

    suspend fun logout() {
        userPreference.logout()
    }



    companion object {
        private const val TAG = "FonaRepository"

        @Volatile
        private var instance: FonaRepository? = null
        fun getInstance(
            userPreference: UserPreference, apiService: ApiService
        ): FonaRepository = instance ?: synchronized(this) {
            instance ?: FonaRepository(userPreference,apiService)
        }.also { instance = it }
    }
}