package com.bangkit23b2.fonaapp.utils


import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.bangkit23b2.fonaapp.data.models.UserPreference
import com.bangkit23b2.fonaapp.data.repository.FonaRepository
import com.bangkit23b2.fonaapp.data.retrofit.ApiConfig

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("token")
object Injection {
    fun provideRepository(context: Context): FonaRepository {
        val apiService = ApiConfig.getApiService()
        val pref = UserPreference.getInstance(context.dataStore)
        return FonaRepository.getInstance(pref,apiService)
    }

    fun provideUserPreference(context: Context): UserPreference {
        val dataStore = context.dataStore
        return UserPreference.getInstance(dataStore)
    }
}