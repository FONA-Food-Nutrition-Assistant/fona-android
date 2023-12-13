package com.example.fonaapp.ui.preferences

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fonaapp.data.models.User
import com.example.fonaapp.data.models.UserModel
import com.example.fonaapp.data.repository.FonaRepository
import kotlinx.coroutines.launch

class UserPreferenceViewModel(private val fonaRepository: FonaRepository) : ViewModel() {

    private val _userPreferenceResponse = fonaRepository.userPreferenceResponse
    val userPreferenceResponse = _userPreferenceResponse

    private val _isDataDiri = fonaRepository.isDataDiri
    val isDataDiri = _isDataDiri

    private val _isLoading = fonaRepository.isLoading
    val isLoading = _isLoading

    // Fungsi untuk menyimpan data pengguna
    fun storeUserData(user: User, firebaseToken: String) {
        viewModelScope.launch {
            fonaRepository.storeUserData(user, firebaseToken)
        }
    }

    fun getSession(): LiveData<UserModel> {
        return fonaRepository.getSession()
    }
}