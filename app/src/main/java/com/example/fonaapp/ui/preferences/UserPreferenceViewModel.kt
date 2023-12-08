package com.example.fonaapp.ui.preferences

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fonaapp.data.models.User
import com.example.fonaapp.data.repository.UserRepository
import kotlinx.coroutines.launch

class UserPreferenceViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _userPreferenceResponse = userRepository.userPreferenceResponse
    val userPreferenceResponse = _userPreferenceResponse

    private val _isLoading = userRepository.isLoading
    val isLoading = _isLoading

    // Fungsi untuk menyimpan data pengguna
    fun storeUserData(user: User, firebaseToken: String) {
        viewModelScope.launch {
            userRepository.storeUserData(user, firebaseToken)
        }
    }
}