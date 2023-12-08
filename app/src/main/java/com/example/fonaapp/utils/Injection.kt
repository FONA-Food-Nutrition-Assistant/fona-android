package com.example.fonaapp.utils


import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.fonaapp.data.models.UserPreference
import com.example.fonaapp.data.repository.UserRepository
import com.example.fonaapp.data.retrofit.ApiConfig

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("token")
object Injection {
    fun provideRepository(context: Context): UserRepository {
        val apiService = ApiConfig.getApiService()
        val pref = UserPreference.getInstance(context.dataStore)
        return UserRepository.getInstance(pref,apiService)
    }

    fun provideUserPreference(context: Context): UserPreference {
        val dataStore = context.dataStore
        return UserPreference.getInstance(dataStore)
    }
}