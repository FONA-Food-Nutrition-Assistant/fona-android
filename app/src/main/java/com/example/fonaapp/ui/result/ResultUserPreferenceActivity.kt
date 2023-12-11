package com.example.fonaapp.ui.result

import ResultAdapter
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fonaapp.R
import com.example.fonaapp.data.response.GetUserDataResponse
import com.example.fonaapp.data.response.ResultData
import com.example.fonaapp.databinding.ActivityResultUserPreferenceBinding
import com.example.fonaapp.main.MainActivity
import com.example.fonaapp.utils.ViewModelFactory

class ResultUserPreferenceActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultUserPreferenceBinding
    private lateinit var factory: ViewModelFactory
    private var token = ""
    private lateinit var resultAdapter: ResultAdapter
    private val resultViewModel: ResultViewModel by viewModels { factory }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupView()
        setupViewModel()
        setupUser()
        setupAdapter()
        setupAction()



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

    private fun setupAdapter(){
        resultAdapter = ResultAdapter()
        val layoutManager = LinearLayoutManager(this)
        binding.rvAllergy.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvAllergy.addItemDecoration(itemDecoration)
        binding.rvAllergy.adapter = resultAdapter
    }

    private fun setupUser(){
        resultViewModel.getSession().observe(this){ user ->
            token = user.idToken
            resultViewModel.getUserData(token)
            resultViewModel.userData.observe(this){ userData ->
                getUserData(userData)
            }
        }
    }
    private fun getUserData(userData: ResultData){
        binding.apply{
            tvGender.text = userData.gender
            tvAge.text = userData.age.toString()
            tvWeight.text = userData.weight.toString()
            tvHeight.text = userData.height.toString()
            tvBmiNumber.text = userData.bmi.toString()
            tvBmiCondition.text = userData.bmi_status
            when (userData.bmi_status) {
                "Kurang" -> itemBmiLevel.setImageResource(R.drawable.ic_kurang)
                "Ideal" -> itemBmiLevel.setImageResource(R.drawable.ic_ideal)
                "Berlebih" -> itemBmiLevel.setImageResource(R.drawable.ic_berlebih)
                else -> itemBmiLevel.setImageResource(R.drawable.ic_obestitas)
            }
            tvActivity.text = userData.activity
            tvTdee.text = userData.tdee.toString()


        }
    }
}