package com.example.fonaapp.ui.preferences

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.fonaapp.databinding.ActivityResultUserPreferenceBinding

class ResultUserPreferenceActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultUserPreferenceBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultUserPreferenceBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }


    companion object {
        const val EXTRA_GENDER = "extra_gender"
        const val EXTRA_AGE = "extra_age"
        const val EXTRA_WEIGHT = "extra_weight"
        const val EXTRA_HEIGHT = "extra_height"
        const val EXTRA_BMI = "extra_bmi"
        const val EXTRA_ACTIVITY_LEVEL = "extra_activity_level"
    }
}