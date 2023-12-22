package com.bangkit23b2.fonaapp.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import com.bangkit23b2.fonaapp.data.models.DeleteFoodRequest
import com.bangkit23b2.fonaapp.data.models.RecordedFoodsRequest
import com.bangkit23b2.fonaapp.data.models.User
import com.bangkit23b2.fonaapp.data.models.UserModel
import com.bangkit23b2.fonaapp.data.models.UserPreference
import com.bangkit23b2.fonaapp.data.models.WaterRecord
import com.bangkit23b2.fonaapp.data.response.BreakfastItem
import com.bangkit23b2.fonaapp.data.response.CalorieIntake
import com.bangkit23b2.fonaapp.data.response.DailyAnalysis
import com.bangkit23b2.fonaapp.data.response.DailyNeeds
import com.bangkit23b2.fonaapp.data.response.Data
import com.bangkit23b2.fonaapp.data.response.DataItemDetail
import com.bangkit23b2.fonaapp.data.response.DeleteFoodResponse
import com.bangkit23b2.fonaapp.data.response.DinnerItem
import com.bangkit23b2.fonaapp.data.response.FoodSuggestion
import com.bangkit23b2.fonaapp.data.response.GetFoodDetailResponse
import com.bangkit23b2.fonaapp.data.response.GetUserDataResponse
import com.bangkit23b2.fonaapp.data.response.HomeResponse
import com.bangkit23b2.fonaapp.data.response.ListAllergyResponse
import com.bangkit23b2.fonaapp.data.response.LunchItem
import com.bangkit23b2.fonaapp.data.response.RecordWatersResponse
import com.bangkit23b2.fonaapp.data.response.ResultData
import com.bangkit23b2.fonaapp.data.response.StoreRecordResponse
import com.bangkit23b2.fonaapp.data.response.UpdateRecordWatersResponse
import com.bangkit23b2.fonaapp.data.response.UpdateUserResponse
import com.bangkit23b2.fonaapp.data.response.UploadFoodResponse
import com.bangkit23b2.fonaapp.data.response.UserPreferenceResponse
import com.bangkit23b2.fonaapp.data.retrofit.ApiService
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FonaRepository(private val userPreference: UserPreference, private val apiService: ApiService) {



    private val _userPreferenceResponse = MutableLiveData<UserPreferenceResponse>()
    val userPreferenceResponse: LiveData<UserPreferenceResponse> = _userPreferenceResponse

    private val _getUserDataResponse = MutableLiveData<GetUserDataResponse>()
    val getUserDataResponse: LiveData<GetUserDataResponse> = _getUserDataResponse

    private val _foodSuggestion = MutableLiveData<FoodSuggestion>()
    val foodSuggestion: LiveData<FoodSuggestion> = _foodSuggestion

    private val _userDataResponse = MutableLiveData<ResultData>()
    val userDataResponse: LiveData<ResultData> = _userDataResponse

    private val _updateUserDataResponse = MutableLiveData<UpdateUserResponse>()
    val updateUserResponse: LiveData<UpdateUserResponse> = _updateUserDataResponse

    private val _listAllergyResponse = MutableLiveData<ListAllergyResponse>()
    val listAllergyResponse: LiveData<ListAllergyResponse> = _listAllergyResponse

    private val _homeDataResponse = MutableLiveData<HomeResponse>()
    val homeDataResponse: LiveData<HomeResponse> = _homeDataResponse

    private val _dailyNeeds = MutableLiveData<DailyNeeds>()
    val dailyNeeds: LiveData<DailyNeeds> = _dailyNeeds

    private val _dailyAnalysis = MutableLiveData<DailyAnalysis>()
    val dailyAnalysis: LiveData<DailyAnalysis> = _dailyAnalysis

    private val _calorieTargetTakes = MutableLiveData<CalorieIntake>()
    val calorieTargetTakes: LiveData<CalorieIntake> = _calorieTargetTakes

    private val _listBreakfast = MutableLiveData<List<BreakfastItem>>()
    val listBreakfast: LiveData<List<BreakfastItem>> = _listBreakfast

    private val _listLunch = MutableLiveData<List<LunchItem>>()
    val listLunch: LiveData<List<LunchItem>> = _listLunch

    private val _listDinner = MutableLiveData<List<DinnerItem>>()
    val listDinner: LiveData<List<DinnerItem>> = _listDinner

    private val _storeWaterResponse = MutableLiveData<RecordWatersResponse>()
    val storeWaterResponse: LiveData<RecordWatersResponse> = _storeWaterResponse

    private val _updateWaterResponse = MutableLiveData<UpdateRecordWatersResponse>()
    val updateWaterResponse: LiveData<UpdateRecordWatersResponse> = _updateWaterResponse

    private val _uploadFoodResponse = MutableLiveData<UploadFoodResponse>()
    val uploadFoodResponse: LiveData<UploadFoodResponse> = _uploadFoodResponse

    private val _storeRecordFood = MutableLiveData<StoreRecordResponse>()
    val storeRecordFood: LiveData<StoreRecordResponse> = _storeRecordFood

    private val _updateRecordFood = MutableLiveData<StoreRecordResponse>()
    val updateRecordFood: LiveData<StoreRecordResponse> = _updateRecordFood

    private val _getFoodDetailResponse = MutableLiveData<GetFoodDetailResponse>()
    val getFoodDetailResponse: LiveData<GetFoodDetailResponse> = _getFoodDetailResponse

    private val _listFoodDetailResponse = MutableLiveData<List<DataItemDetail>>()
    val listFoodDetailResponse: LiveData<List<DataItemDetail>> = _listFoodDetailResponse

    private val _deleteFoodResponse = MutableLiveData<DeleteFoodResponse>()
    val deleteFoodResponse: LiveData<DeleteFoodResponse> = _deleteFoodResponse

    private val _getRecordWater = MutableLiveData<Int>()
    val getRecordWater: LiveData<Int> = _getRecordWater

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isRecorded = MutableLiveData<Boolean>()
    val isRecorded: LiveData<Boolean> = _isRecorded

    private val _isDataDiri = MutableLiveData<Boolean>()
    val isDataDiri: LiveData<Boolean> = _isDataDiri

    private val _isLogin = MutableLiveData<Boolean>()
    val isLogin : LiveData<Boolean> = _isLogin

    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> = _isError


    fun storeUserData(user: User, firebaseToken: String) {
        _isLoading.value = true
        _isError.value = false
        val call = apiService.storeUserData("Bearer $firebaseToken", user)

        call.enqueue(object : Callback<UserPreferenceResponse> {
            override fun onResponse(
                call: Call<UserPreferenceResponse>, response: Response<UserPreferenceResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful && response.body() != null) {
                    _isDataDiri.value = true
                    _isError.value = false
                    _userPreferenceResponse.value = response.body()
                } else {
                    _isDataDiri.value = false
                    _isError.value = false
                    Log.e(TAG, "onFailure: ${response.body()?.message.toString()}, response fail")
                }
            }

            override fun onFailure(call: Call<UserPreferenceResponse>, t: Throwable) {
                _isLoading.value = false
                _isDataDiri.value = false
                _isError.value = true
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    fun getUserData(firebaseToken: String){
        _isLoading.value = true
        _isError.value = false
        val call = apiService.getUserData("Bearer $firebaseToken")
        call.enqueue(object : Callback<GetUserDataResponse> {
            override fun onResponse(
                call: Call<GetUserDataResponse>,
                response: Response<GetUserDataResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful && response.body() != null) {
                    _userDataResponse.value = response.body()?.data
                    _isError.value = false
                    Log.d(TAG, "User data response: ${response.body()?.data}")
                } else {
                    _isError.value = false
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<GetUserDataResponse>, t: Throwable) {
                _isLoading.value = false
                _isError.value = true
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }
    fun getUserDataResponse(firebaseToken: String){
        _isLoading.value = true
        val call = apiService.getUserData("Bearer $firebaseToken")
        call.enqueue(object : Callback<GetUserDataResponse> {
            override fun onResponse(
                call: Call<GetUserDataResponse>,
                response: Response<GetUserDataResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful && response.body() != null) {
                    _getUserDataResponse.value = response.body()
                    _isDataDiri.value = true
                    _isLogin.value = false
                } else if(response.code() == 401) {
                    _isLogin.value = true
                } else{
                    Log.e(TAG, "onFailure: gagal getUserData Response atau 400 dan 500")
                    _isDataDiri.value = false
                }
            }
            override fun onFailure(call: Call<GetUserDataResponse>, t: Throwable) {
                _isLoading.value = false
                _isLogin.value = true
                Log.e(TAG, "onFailure: gagal getUserData Response")
            }
        })
    }

    fun updateUserData(user: Data, firebaseToken: String){
        _isLoading.value = true
        val call = apiService.updateUserData("Bearer $firebaseToken", user)

        call.enqueue(object : Callback<UpdateUserResponse> {
            override fun onResponse(
                call: Call<UpdateUserResponse>, response: Response<UpdateUserResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful && response.body() != null) {
                    _isDataDiri.value = true
                    _updateUserDataResponse.value = response.body()
                } else {
                    _isDataDiri.value = false
                    Log.e(TAG, "onFailure: ${response.message()}, ${response.body()?.message.toString()}")
                }
            }

            override fun onFailure(call: Call<UpdateUserResponse>, t: Throwable) {
                _isLoading.value = false
                _isDataDiri.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    fun getListAllergy(firebaseToken: String){
        _isLoading.value = true
        val call = apiService.getListAllergy("Bearer $firebaseToken")
        call.enqueue(object : Callback<ListAllergyResponse> {
            override fun onResponse(
                call: Call<ListAllergyResponse>,
                response: Response<ListAllergyResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful && response.body() != null) {
                    _listAllergyResponse.value = response.body()
                } else {
                    Log.e(TAG, "onFailure: ${response.body()?.message.toString()},  gagal get Allergy")
                }
            }
            override fun onFailure(call: Call<ListAllergyResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: gagal list allergy")
            }
        })
    }

    fun getDataHome(firebaseToken: String, date: String){
        _isLoading.value = true
        val call = apiService.getHomeData("Bearer $firebaseToken", date)
        call.enqueue(object : Callback<HomeResponse> {
            override fun onResponse(
                call: Call<HomeResponse>,
                response: Response<HomeResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful && response.body() != null) {
                    _calorieTargetTakes.value = response.body()?.data?.calorieIntake
                    _dailyNeeds.value = response.body()?.data?.dailyNeeds
                    _dailyAnalysis.value = response.body()?.data?.dailyAnalysis
                    _listBreakfast.value = response.body()?.data?.recordFoods?.breakfast
                    _listLunch.value = response.body()?.data?.recordFoods?.lunch
                    _listDinner.value = response.body()?.data?.recordFoods?.dinner
                    _getRecordWater.value = response.body()?.data?.recordWater
                    _foodSuggestion.value = response.body()?.data?.foodSuggestion

                } else {
                    Log.e(TAG, "onFailure: ${response.body()?.message.toString()}, gagal dapet data home")
                }
            }
            override fun onFailure(call: Call<HomeResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: gagal get Data Home {$t}", t)
            }
        })
    }

    fun getFoodDetailResponse(firebaseToken: String, search: String) {
        _isLoading.value = true
        val call = apiService.getFoodetail("Bearer ${firebaseToken}", search)
        call.enqueue(object : Callback<GetFoodDetailResponse> {
            override fun onResponse(
                call: Call<GetFoodDetailResponse>,
                response: Response<GetFoodDetailResponse>,
            ) {
                _isLoading.value = false
                if (response.isSuccessful && response.body() != null) {
                    _getFoodDetailResponse.value = response.body()
                    _listFoodDetailResponse.value = response.body()?.data
//                    _nutritionList.value = true
                } else {
                    Log.e(TAG,"onFailure: gagal 2")
//                    _nutritionList.value = false
                }
            }

            override fun onFailure(call: Call<GetFoodDetailResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}", t)
            }


        })
    }

    fun storeWater(water: WaterRecord, firebaseToken: String) {
        _isLoading.value = true
        val call = apiService.storeWaterRecord("Bearer $firebaseToken", water)
        call.enqueue(object : Callback<RecordWatersResponse> {
            override fun onResponse(
                call: Call<RecordWatersResponse>, response: Response<RecordWatersResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful && response.body() != null) {
                    _isRecorded.value = false
                    _storeWaterResponse.value = response.body()
                } else {
                    _isRecorded.value = true
                    Log.e(TAG, "onFailure: ${response.message()}, ${response.body()?.message.toString()} gagal store water 400/401")
                }
            }

            override fun onFailure(call: Call<RecordWatersResponse>, t: Throwable) {
                _isRecorded.value = false
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message} Internal Server error")
            }
        })
    }
    fun updateWater(water: WaterRecord, firebaseToken: String) {
        _isLoading.value = true
        val call = apiService.updateWaterRecord("Bearer $firebaseToken", water)
        call.enqueue(object : Callback<UpdateRecordWatersResponse> {
            override fun onResponse(
                call: Call<UpdateRecordWatersResponse>, response: Response<UpdateRecordWatersResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful && response.body() != null) {
                    _updateWaterResponse.value = response.body()
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}, ${response.body()?.message.toString()} gagal update water 400/401")
                }
            }

            override fun onFailure(call: Call<UpdateRecordWatersResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message} Internal Server error")
            }
        })
    }

    fun uploadFood(firebaseToken: String, image: MultipartBody.Part) {
        _isLoading.value = true
        _isError.value = false
        val client = apiService.uploadFood("Bearer $firebaseToken", image)

        client.enqueue(object : Callback<UploadFoodResponse> {
            override fun onResponse(
                call: Call<UploadFoodResponse>, response: Response<UploadFoodResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful && response.body() != null) {
                    _uploadFoodResponse.value = response.body()
                } else {
                    Log.e(
                        TAG,
                        "onFailure: ${response.message()}, ${response.body()?.message.toString()} 401/400"
                    )
                    _isError.value = true
                }
            }
            override fun onFailure(call: Call<UploadFoodResponse>, t: Throwable) {
                Log.d("error upload", t.message.toString())
                _isError.value = true
            }

        })
    }

    fun storeRecordFood(firebaseToken: String, food: RecordedFoodsRequest,  callback: (Boolean) -> Unit) {
        _isLoading.value = true
        _isError.value = false
        val call = apiService.storeRecordFood("Bearer $firebaseToken", food)

        call.enqueue(object : Callback<StoreRecordResponse> {
            override fun onResponse(
                call: Call<StoreRecordResponse>, response: Response<StoreRecordResponse>
            ) {
                _isLoading.value = false
                _isError.value = false
                if (response.isSuccessful && response.body() != null) {
                    _storeRecordFood.value = response.body()
                    _isError.value = true
                    callback(true)
                } else {
                    Log.e(TAG, "onFailure: ${response.body()?.message.toString()}, response fail")
                    callback(false)
                }
            }

            override fun onFailure(call: Call<StoreRecordResponse>, t: Throwable) {
                _isLoading.value = false
                _isError.value = true
                callback(false)
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    fun updateRecordFood(firebaseToken: String, food: RecordedFoodsRequest) {
        _isLoading.value = true
        _isError.value = false
        val call = apiService.updateRecordFood("Bearer $firebaseToken", food)

        call.enqueue(object : Callback<StoreRecordResponse> {
            override fun onResponse(
                call: Call<StoreRecordResponse>, response: Response<StoreRecordResponse>
            ) {
                _isLoading.value = false
                _isError.value = false
                if (response.isSuccessful && response.body() != null) {
                    _updateRecordFood.value = response.body()
                    _isError.value = true
                } else {
                    Log.e(TAG, "onFailure: ${response.body()?.message.toString()}, response fail")
                }
            }

            override fun onFailure(call: Call<StoreRecordResponse>, t: Throwable) {
                _isLoading.value = false
                _isError.value = true
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    fun deleteRecordFood(
        idToken: String,
        nutritionIds: List<Int>,
        mealTime: String,
        date: String,
        callback: (Boolean) -> Unit
    ) {
        apiService.deleteRecordFood("Bearer $idToken", DeleteFoodRequest(nutritionIds, mealTime, date))
            .enqueue(object : Callback<DeleteFoodResponse> {
                override fun onResponse(
                    call: Call<DeleteFoodResponse>,
                    response: Response<DeleteFoodResponse>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        // Handle success
                        callback(true)
                        _deleteFoodResponse.value = response.body()
                    } else {
                        // Handle failure
                        callback(false)
                    }
                }

                override fun onFailure(call: Call<DeleteFoodResponse>, t: Throwable) {
                    // Handle failure
                    callback(false)
                }
            })
    }


    fun getSession(): LiveData<UserModel> {
        return userPreference.getSession().asLiveData()
    }

    suspend fun logout() {
        userPreference.logout()
    }



    companion object {
        private const val TAG = "FonaRepository"

        @Volatile
        private var instance: FonaRepository? = null
        fun getInstance(
            userPreference: UserPreference, apiService: ApiService
        ): FonaRepository = instance ?: synchronized(this) {
            instance ?: FonaRepository(userPreference,apiService)
        }.also { instance = it }
    }
}