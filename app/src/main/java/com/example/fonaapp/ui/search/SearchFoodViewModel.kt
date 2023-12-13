package com.example.fonaapp.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fonaapp.data.models.UserModel
import com.example.fonaapp.data.repository.UserRepository
import com.example.fonaapp.data.response.GetNutritionListResponse
import kotlinx.coroutines.launch

class ResultViewModel(private val userRepository: UserRepository) : ViewModel() {

    //TODO LULU 4 - buat variabel buat get list food/nutrition
    fun getNutritionListResponse(firebaseToken: String): LiveData<GetNutritionListResponse> {
        viewModelScope.launch {
            userRepository.getNutritionListResponse(firebaseToken)
        }
        return userRepository.getNutritionListResponse
    }


    //TODO LULU 5 - buat fungsi getSession() mirip di ResultViewModel

    fun getSession(): LiveData<UserModel> {
        return userRepository.getSession()
    }

}