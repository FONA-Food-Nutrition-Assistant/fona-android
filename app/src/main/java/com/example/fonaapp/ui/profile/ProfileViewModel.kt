package com.example.fonaapp.ui.profile

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fonaapp.data.models.UserModel
import com.example.fonaapp.data.repository.FonaRepository
import com.example.fonaapp.data.response.GetUserDataResponse
import com.example.fonaapp.data.response.ResultData
import kotlinx.coroutines.launch

class ProfileViewModel(private val fonaRepository: FonaRepository) : ViewModel()  {
    private val _isDataDiri = fonaRepository.isDataDiri
    val isDataDiri = _isDataDiri

    fun getUserData(firebaseToken: String): LiveData<ResultData> {
        viewModelScope.launch {
            Log.d(ContentValues.TAG, "getUserData function called with token: $firebaseToken")
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
}