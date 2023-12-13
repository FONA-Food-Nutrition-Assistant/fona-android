package com.example.fonaapp.utils

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.fonaapp.data.repository.FonaRepository
import com.example.fonaapp.ui.preferences.UserPreferenceViewModel
import com.example.fonaapp.ui.profile.ProfileViewModel
import com.example.fonaapp.ui.result.ResultViewModel
import com.example.fonaapp.ui.update.UpdatePreferenceViewModel

class ViewModelFactory private constructor(private val repository: FonaRepository) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T: ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(UserPreferenceViewModel::class.java) -> {
                UserPreferenceViewModel(repository) as T
            }
            modelClass.isAssignableFrom(ResultViewModel::class.java) -> {
               ResultViewModel(repository) as T
            }
            modelClass.isAssignableFrom(ProfileViewModel::class.java) -> {
              ProfileViewModel(repository) as T
            }
            modelClass.isAssignableFrom(UpdatePreferenceViewModel::class.java) -> {
              UpdatePreferenceViewModel(repository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null
        @JvmStatic
        fun getInstance(context: Context): ViewModelFactory {
            if (INSTANCE == null) {
                synchronized(ViewModelFactory::class.java) {
                    INSTANCE = ViewModelFactory(Injection.provideRepository(context))
                }
            }
            return INSTANCE as ViewModelFactory
        }
    }
}
