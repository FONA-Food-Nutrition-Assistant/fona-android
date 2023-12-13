package com.example.fonaapp.ui.result

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fonaapp.data.models.UserModel
import com.example.fonaapp.data.repository.FonaRepository
import com.example.fonaapp.data.response.GetUserDataResponse
import com.example.fonaapp.data.response.ResultData
import kotlinx.coroutines.launch

class ResultViewModel(private val fonaRepository: FonaRepository) : ViewModel() {

    private val _isDataDiri = fonaRepository.isDataDiri
    val isDataDiri = _isDataDiri

    private val _isLogin = fonaRepository.isLogin
    val isLogin = _isLogin

    fun getUserData(firebaseToken: String): LiveData<ResultData> {
        viewModelScope.launch {
            Log.d(TAG, "getUserData function called with token: $firebaseToken")
            fonaRepository.getUserData(firebaseToken)
        }
        return fonaRepository.userDataResponse
    }

    fun getUserDataResponse(firebaseToken: String): LiveData<GetUserDataResponse>{
        viewModelScope.launch {
            fonaRepository.getUserDataResponse(firebaseToken)
        }
        return fonaRepository.getUserDataResponse
    }

    fun getSession(): LiveData<UserModel> {
        return fonaRepository.getSession()
    }

    fun logout() {
        viewModelScope.launch {
            fonaRepository.logout()
        }
    }

    val userData: LiveData<ResultData> =
        fonaRepository.userDataResponse
    val isLoading = fonaRepository.isLoading

    fun getIsDataDiri(): LiveData<Boolean> {
        return fonaRepository.isDataDiri
    }

    val getUserDataResponse: LiveData<GetUserDataResponse> = fonaRepository.getUserDataResponse
}