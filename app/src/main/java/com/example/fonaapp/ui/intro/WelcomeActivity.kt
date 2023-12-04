package com.example.fonaapp.ui.intro

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.viewpager2.widget.ViewPager2
import com.example.fonaapp.R
import com.example.fonaapp.databinding.ActivityWelcomeBinding
import com.example.fonaapp.ui.preferences.UserPreferenceActivity

class WelcomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWelcomeBinding

    //List Adapter of Intro
    private val introSliderAdapter = IntroSliderAdapter(
        listOf(
            IntroSlider(
                "Selamat Datang di FONA!",
                "Aplikasi yang dapat membantumu mengerti dan menghitung nutrisi makanan harian hanya dengan satu sentuhan! Rasakan kemudahan menjaga kesehatan di ujung jarimu.",
                R.drawable.ic_intro_1
            ),
            IntroSlider(
                "Catat dengan mudah",
                "Sesuaikan kebutuhan makanan dan catat makanan harianmu dengan mudah hanya di FONA.",
                R.drawable.ic_intro_2
            ),
            IntroSlider(
                "Mulai hidup sehatmu!",
                "Ayo bergabung bersama kami dan temukan keuntungan menjalani hidup sehat dengan fitur bantuan AI. Mulai detik ini, jadikan setiap langkah menuju kesehatan yang menyenangkan.",
                R.drawable.ic_intro_3
            )
        )
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Call Adapter
        binding.introSliderViewPager.adapter = introSliderAdapter

        //Call Function setupIndicators()
        setupIndicators()
        //Call function setCurrentIndicators()
        setCurrentIndicators(0)
        binding.introSliderViewPager.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                setCurrentIndicators(position)
            }
        })
        binding.btnSignUp.setOnClickListener {
            startActivity(Intent(this, UserPreferenceActivity::class.java))
        }
    }

    //Setup Indicator View
    private fun setupIndicators() {
        val indicators = arrayOfNulls<ImageView>(introSliderAdapter.itemCount)
        val layoutParams: LinearLayout.LayoutParams =
            LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
        layoutParams.setMargins(8, 0, 8, 0)
        for (i in indicators.indices) {
            indicators[i] = ImageView(applicationContext)
            indicators[i].apply {
                this?.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.indicator_inactive
                    )
                )
                this?.layoutParams = layoutParams
            }
            binding.indicatorsContainer.addView(indicators[i])
        }
    }

    //Fun setCurrentIndicators
    private fun setCurrentIndicators(index: Int) {
        val childCount = binding.indicatorsContainer.childCount
        for (i in 0 until childCount) {
            val imageView = binding.indicatorsContainer[i] as ImageView
            //Set indicator if active or not
            if (i == index) {
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.indicator_active
                    )
                )
            } else {
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.indicator_inactive
                    )
                )
            }
        }
    }
}