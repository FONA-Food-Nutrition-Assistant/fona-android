package com.example.fonaapp.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fonaapp.data.models.UserModel
import com.example.fonaapp.data.repository.FonaRepository
import com.example.fonaapp.data.response.DataItemDetail
import com.example.fonaapp.data.response.DinnerItem
import com.example.fonaapp.data.response.GetFoodDetailResponse
import kotlinx.coroutines.launch

class SearchFoodViewModel(private val fonaRepository: FonaRepository) : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _getFoodDetailResponse =  fonaRepository.getFoodDetailResponse
    val getFoodDetailResponse = _getFoodDetailResponse

    val listFoodDetailResponse: LiveData<List<DataItemDetail>> =
        fonaRepository.listFoodDetailResponse
    //TODO LULU 4 - buat variabel buat get list food/nutrition
    fun getNutritionListResponse(firebaseToken: String, search: String): LiveData<GetFoodDetailResponse> {
        viewModelScope.launch {
            fonaRepository.getFoodDetailResponse(firebaseToken, search)
        }
        return _getFoodDetailResponse
    }




    //TODO LULU 5 - buat fungsi getSession() mirip di ResultViewModel

    fun getSession(): LiveData<UserModel> {
        return fonaRepository.getSession()
    }

}