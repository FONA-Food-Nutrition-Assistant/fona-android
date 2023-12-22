package com.bangkit23b2.fonaapp.ui.upload

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bangkit23b2.fonaapp.data.models.UserModel
import com.bangkit23b2.fonaapp.data.repository.FonaRepository
import com.bangkit23b2.fonaapp.data.response.UploadFoodResponse
import kotlinx.coroutines.launch
import okhttp3.MultipartBody

class UploadViewModel(private val fonaRepository: FonaRepository) : ViewModel()  {
    val uploadFoodResponse: LiveData<UploadFoodResponse> = fonaRepository.uploadFoodResponse

    val isError:LiveData<Boolean> = fonaRepository.isError

    fun uploadFood(firebaseToken: String, image: MultipartBody.Part): LiveData<UploadFoodResponse> {
        viewModelScope.launch {
            Log.d(ContentValues.TAG, "upload Food function called with token: $firebaseToken")
            fonaRepository.uploadFood(firebaseToken, image)
        }
        return fonaRepository.uploadFoodResponse
    }

    fun getSession(): LiveData<UserModel> {
        return fonaRepository.getSession()
    }

    private val _isLoading = fonaRepository.isLoading
    val isLoading = _isLoading



}