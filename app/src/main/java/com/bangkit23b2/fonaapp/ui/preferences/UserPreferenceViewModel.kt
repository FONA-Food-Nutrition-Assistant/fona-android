package com.bangkit23b2.fonaapp.ui.preferences

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bangkit23b2.fonaapp.data.models.User
import com.bangkit23b2.fonaapp.data.models.UserModel
import com.bangkit23b2.fonaapp.data.repository.FonaRepository
import com.bangkit23b2.fonaapp.data.response.ListAllergyResponse
import kotlinx.coroutines.launch

class UserPreferenceViewModel(private val fonaRepository: FonaRepository) : ViewModel() {

    private val _userPreferenceResponse = fonaRepository.userPreferenceResponse
    val userPreferenceResponse = _userPreferenceResponse

    private val _isDataDiri = fonaRepository.isDataDiri
    val isDataDiri = _isDataDiri

    val allergyData: LiveData<ListAllergyResponse> =
        fonaRepository.listAllergyResponse

    fun getListAllergy(firebaseToken: String): LiveData<ListAllergyResponse>{
        viewModelScope.launch{
            fonaRepository.getListAllergy(firebaseToken)
        }
        return fonaRepository.listAllergyResponse
    }

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