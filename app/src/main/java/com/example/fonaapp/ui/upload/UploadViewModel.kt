package com.example.fonaapp.ui.upload

import android.Manifest
import android.content.ContentValues
import android.content.pm.PackageManager
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fonaapp.data.models.UserModel
import com.example.fonaapp.data.repository.FonaRepository
import com.example.fonaapp.data.response.ResultData
import com.example.fonaapp.data.response.UploadFoodResponse
import com.example.fonaapp.main.MainActivity
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody

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