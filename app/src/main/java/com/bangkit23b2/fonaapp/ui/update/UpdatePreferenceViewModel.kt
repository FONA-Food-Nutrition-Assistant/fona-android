package com.bangkit23b2.fonaapp.ui.update

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bangkit23b2.fonaapp.data.models.UserModel
import com.bangkit23b2.fonaapp.data.repository.FonaRepository
import com.bangkit23b2.fonaapp.data.response.Data
import com.bangkit23b2.fonaapp.data.response.ListAllergyResponse
import kotlinx.coroutines.launch

class UpdatePreferenceViewModel(private val fonaRepository: FonaRepository) : ViewModel() {
    private val _updateUserDataResponse = fonaRepository.updateUserResponse
    val updateUserResponse = _updateUserDataResponse

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