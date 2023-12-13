package com.example.fonaapp.ui.home

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fonaapp.data.repository.FonaRepository
import com.example.fonaapp.data.response.DailyNeeds
import com.example.fonaapp.data.response.DataHome
import com.example.fonaapp.data.response.HomeResponse
import com.example.fonaapp.data.response.ResultData
import kotlinx.coroutines.launch

class HomeViewModel(private val fonaRepository: FonaRepository) : ViewModel() {
    val dailyNeeds: MutableLiveData<DailyNeeds?> = fonaRepository.dailyNeedsResponse

//    private val _dataHome = MutableLiveData<DataHome>()
//    val dataHome: LiveData<DataHome> = _dataHome

    // Observer untuk dataHome
    fun observeDataHome(): LiveData<DataHome> {
        return fonaRepository.dataHome
    }

    fun getHomeData(firebaseToken: String, date: String): LiveData<HomeResponse> {
        viewModelScope.launch {
            Log.d(ContentValues.TAG, "getUserData function called with token: $firebaseToken")
            fonaRepository.getHomeData(firebaseToken, date)
        }
        return fonaRepository.homeDataResponse
    }

    // Function to get daily needs
    fun getDailyNeeds(firebaseToken: String, date: String) {
        viewModelScope.launch {
            Log.d(ContentValues.TAG, "getDailyNeeds function called with token: $firebaseToken")
            fonaRepository.getHomeData(firebaseToken, date)
        }
    }


}