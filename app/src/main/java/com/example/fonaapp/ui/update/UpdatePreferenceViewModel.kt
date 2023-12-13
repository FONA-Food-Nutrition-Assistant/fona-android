package com.example.fonaapp.ui.update

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fonaapp.data.models.User
import com.example.fonaapp.data.models.UserModel
import com.example.fonaapp.data.repository.FonaRepository
import com.example.fonaapp.data.response.Data
import com.example.fonaapp.data.response.DataItem
import com.example.fonaapp.data.response.ListAllergyResponse
import com.example.fonaapp.data.response.ResultData
import kotlinx.coroutines.launch

class UpdatePreferenceViewModel(private val fonaRepository: FonaRepository) : ViewModel() {
    private val _updateUserDataResponse = fonaRepository.updateUserResponse
    val updateUserResponse = _updateUserDataResponse

    private val _listAllergyResponse = fonaRepository.listAllergyResponse
    val listAllergyResponse = _listAllergyResponse

    private val _checkboxAllergyResponse = fonaRepository.checkboxAllergyResponse
    val checkboxAllergyResponse = _checkboxAllergyResponse

    val allergyData: LiveData<ListAllergyResponse> =
        fonaRepository.listAllergyResponse

    private val _isDataDiri = fonaRepository.isDataDiri
    val isDataDiri = _isDataDiri

    private val _isLoading = fonaRepository.isLoading
    val isLoading = _isLoading

    //Fungsi untuk update user pengguna
    fun updateUserData(user: Data, firebaseToken: String){
        viewModelScope.launch{
            fonaRepository.updateUserData(user, firebaseToken)
        }
    }

    fun getListAllergy(firebaseToken: String): LiveData<ListAllergyResponse>{
        viewModelScope.launch{
            fonaRepository.getListAllergy(firebaseToken)
        }
        return fonaRepository.listAllergyResponse
    }

    fun getSession(): LiveData<UserModel> {
        return fonaRepository.getSession()
    }
}