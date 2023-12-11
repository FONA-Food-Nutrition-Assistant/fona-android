package com.example.fonaapp.ui.result

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.fonaapp.databinding.ActivityResultUserPreferenceBinding
import com.example.fonaapp.main.MainActivity
import com.example.fonaapp.utils.ViewModelFactory

class ResultUserPreferenceActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultUserPreferenceBinding
    private lateinit var factory: ViewModelFactory
    private var token = ""
    private val resultViewModel: ResultViewModel by viewModels { factory }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



    }

    private fun setupView(){
        binding = ActivityResultUserPreferenceBinding.inflate(layoutInflater)
        setContentView(binding.root)
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
        }
    }

    private fun setupViewModel(){
        factory = ViewModelFactory.getInstance(this)
    }

    private fun setupAction(){
        binding.btnSaveResult.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}