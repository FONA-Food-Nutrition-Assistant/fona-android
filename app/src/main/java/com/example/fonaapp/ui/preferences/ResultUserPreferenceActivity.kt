package com.example.fonaapp.ui.preferences

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.fonaapp.databinding.ActivityResultUserPreferenceBinding
import com.example.fonaapp.main.MainActivity

class ResultUserPreferenceActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultUserPreferenceBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultUserPreferenceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSaveResult.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
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