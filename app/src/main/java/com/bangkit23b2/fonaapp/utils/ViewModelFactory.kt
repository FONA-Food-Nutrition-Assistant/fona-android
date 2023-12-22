package com.bangkit23b2.fonaapp.utils

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bangkit23b2.fonaapp.data.repository.FonaRepository
import com.bangkit23b2.fonaapp.ui.cart.CartViewModel
import com.bangkit23b2.fonaapp.ui.home.HomeViewModel
import com.bangkit23b2.fonaapp.ui.preferences.UserPreferenceViewModel
import com.bangkit23b2.fonaapp.ui.profile.ProfileViewModel
import com.bangkit23b2.fonaapp.ui.result.ResultViewModel
import com.bangkit23b2.fonaapp.ui.search.SearchFoodViewModel
import com.bangkit23b2.fonaapp.ui.update.UpdatePreferenceViewModel
import com.bangkit23b2.fonaapp.ui.upload.UploadViewModel

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
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
              HomeViewModel(repository) as T
            }
            modelClass.isAssignableFrom(UploadViewModel::class.java) -> {
                UploadViewModel(repository) as T
            }
            modelClass.isAssignableFrom(CartViewModel::class.java) -> {
               CartViewModel(repository) as T
            }
            modelClass.isAssignableFrom(SearchFoodViewModel::class.java) -> {
               SearchFoodViewModel(repository) as T
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
