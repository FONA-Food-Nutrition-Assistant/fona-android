package com.example.fonaapp.ui.home

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fonaapp.data.models.User
import com.example.fonaapp.data.models.WaterRecord
import com.example.fonaapp.data.repository.FonaRepository
import com.example.fonaapp.data.response.BreakfastItem
import com.example.fonaapp.data.response.CalorieIntake
import com.example.fonaapp.data.response.DailyAnalysis
import com.example.fonaapp.data.response.DailyNeeds
import com.example.fonaapp.data.response.DataResponse
import com.example.fonaapp.data.response.DinnerItem
import com.example.fonaapp.data.response.FoodSuggestion
import com.example.fonaapp.data.response.HomeResponse
import com.example.fonaapp.data.response.LunchItem
import com.example.fonaapp.data.response.ResultData
import kotlinx.coroutines.launch

class HomeViewModel(private val fonaRepository: FonaRepository) : ViewModel() {


    fun getDataHome(firebaseToken: String, date: String): LiveData<DailyNeeds> {
        viewModelScope.launch {
            Log.d(ContentValues.TAG, "getUserData function called with token: $firebaseToken")
            fonaRepository.getDataHome(firebaseToken, date)
        }
        return fonaRepository.dailyNeeds
    }

    fun storeUserData(water: WaterRecord, firebaseToken: String) {
        viewModelScope.launch {
            fonaRepository.storeWater(water, firebaseToken)
        }
    }

    fun recordWaterConsumption(numberOfCups: Int, firebaseToken: String, date: String) {
        val waterRecord = WaterRecord(numberOfCups, date)
        storeUserData(waterRecord, firebaseToken)
    }

    fun updateWater(water: WaterRecord, firebaseToken: String){
        viewModelScope.launch{
            fonaRepository.updateWater(water, firebaseToken)
        }
    }
    fun updateWaterRecord(numberOfCups: Int, firebaseToken: String, date: String) {
        val waterRecords = WaterRecord(numberOfCups, date)
        updateWater(waterRecords, firebaseToken)
    }


    val dailyNeeds: LiveData<DailyNeeds> =
        fonaRepository.dailyNeeds

    val dailyAnalysis: LiveData<DailyAnalysis> =
        fonaRepository.dailyAnalysis

    val calorieTargetTakes: LiveData<CalorieIntake> =
        fonaRepository.calorieTargetTakes

    val listBreakfast: LiveData<List<BreakfastItem>> =
        fonaRepository.listBreakfast

    val listLunch: LiveData<List<LunchItem>> =
        fonaRepository.listLunch

    val listDinner: LiveData<List<DinnerItem>> =
        fonaRepository.listDinner

    val getRecordWater: LiveData<Int> =
        fonaRepository.getRecordWater

    val foodSuggestion: LiveData<FoodSuggestion> =
        fonaRepository.foodSuggestion

    private val _isRecorded = fonaRepository.isRecorded
    val isRecorded = _isRecorded


    private val _storeWaterResponse = fonaRepository.storeWaterResponse
    val storeWaterResponse = _storeWaterResponse


}