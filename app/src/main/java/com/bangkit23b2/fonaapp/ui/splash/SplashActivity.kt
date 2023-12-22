//package com.example.fonaapp.ui.splash
//
//import android.content.Intent
//import androidx.appcompat.app.AppCompatActivity
//import android.os.Bundle
//import android.os.Handler
//import com.example.fonaapp.databinding.ActivitySplashBinding
//import com.example.fonaapp.ui.Intro.WelcomeActivity
//
//class SplashActivity : AppCompatActivity() {
//
//    private lateinit var binding : ActivitySplashBinding
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivitySplashBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        supportActionBar?.hide()
//
//        val handler = Handler()
//        handler.postDelayed({
//            val intent = Intent(this@SplashActivity, WelcomeActivity::class.java)
//            startActivity(intent)
//            finish()
//        }, timer)
//    }
//
//    companion object {
//        const val timer = 2000L
//    }
//
//}