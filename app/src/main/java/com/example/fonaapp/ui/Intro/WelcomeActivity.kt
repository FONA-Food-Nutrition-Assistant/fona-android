package com.example.fonaapp.ui.Intro

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import com.example.fonaapp.R
import com.example.fonaapp.databinding.ActivityWelcomeBinding

class WelcomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWelcomeBinding

    private val introSliderAdapter = IntroSliderAdapter(
        listOf(
            IntroSlider(
                "Selamat Datang di FONA!",
                "Aplikasi yang dapat membantumu mengerti dan menghitung nutrisi makanan harian hanya dengan satu sentuhan! Rasakan kemudahan menjaga kesehatan di ujung jarimu.",
                R.drawable.ic_intro_1
            ),
            IntroSlider("Catat dengan mudah", "Sesuaikan kebutuhan makanan dan catat makanan harianmu dengan mudah hanya di FONA.", R.drawable.ic_intro_2),
            IntroSlider("Mulai hidup sehatmu!", "Ayo bergabung bersama kami dan temukan keuntungan menjalani hidup sehat dengan fitur bantuan AI. Mulai detik ini, jadikan setiap langkah menuju kesehatan yang menyenangkan.", R.drawable.ic_intro_3)
        )
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.introSliderViewPager.adapter = introSliderAdapter


    }
}