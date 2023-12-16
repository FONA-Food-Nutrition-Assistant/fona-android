package com.example.fonaapp.ui.cart

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fonaapp.data.models.RecordedFoodsRequest
import com.example.fonaapp.data.models.User
import com.example.fonaapp.data.models.UserModel
import com.example.fonaapp.data.repository.FonaRepository
import kotlinx.coroutines.launch

class CartViewModel(private val fonaRepository: FonaRepository) : ViewModel() {

    private val _storeRecordFood = fonaRepository.storeRecordFood
    val storeRecordFood = _storeRecordFood

    private val _isLoading = fonaRepository.isLoading
    val isLoading = _isLoading

    // Fungsi untuk menyimpan data pengguna
    fun storeRecordFood(firebaseToken: String, food: RecordedFoodsRequest) {
        viewModelScope.launch {
            fonaRepository.storeRecordFood(firebaseToken, food)
        }
    }
    fun getSession(): LiveData<UserModel> {
        return fonaRepository.getSession()
    }
}