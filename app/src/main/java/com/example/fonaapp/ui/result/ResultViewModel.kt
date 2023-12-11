package com.example.fonaapp.ui.result

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fonaapp.data.models.User
import com.example.fonaapp.data.models.UserModel
import com.example.fonaapp.data.repository.UserRepository
import com.example.fonaapp.data.response.GetUserDataResponse
import com.example.fonaapp.data.response.ResultData
import kotlinx.coroutines.launch

class ResultViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _isDataDiri = userRepository.isDataDiri
    val isDataDiri = _isDataDiri

    fun getUserData(firebaseToken: String): LiveData<ResultData> {
        viewModelScope.launch {
            Log.d(TAG, "getUserData function called with token: $firebaseToken")
            userRepository.getUserData(firebaseToken)
        }
        return userRepository.userDataResponse
    }

    fun getUserDataResponse(firebaseToken: String): LiveData<GetUserDataResponse>{
        viewModelScope.launch {
            userRepository.getUserDataResponse(firebaseToken)
        }
        return userRepository.getUserDataResponse
    }

    fun getSession(): LiveData<UserModel> {
        return userRepository.getSession()
    }

    fun logout() {
        viewModelScope.launch {
            userRepository.logout()
        }
    }

//    val userData: LiveData<ResultData> =
//        userRepository.userDataResponse
    val isLoading = userRepository.isLoading

    fun getIsDataDiri(): LiveData<Boolean> {
        return userRepository.isDataDiri
    }

    val getUserDataResponse: LiveData<GetUserDataResponse> = userRepository.getUserDataResponse
}