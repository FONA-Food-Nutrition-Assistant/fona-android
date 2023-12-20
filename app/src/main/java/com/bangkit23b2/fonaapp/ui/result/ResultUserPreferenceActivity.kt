package com.bangkit23b2.fonaapp.ui.result


import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bangkit23b2.fonaapp.R
import com.bangkit23b2.fonaapp.data.response.AllergiesItem
import com.bangkit23b2.fonaapp.data.response.ResultData
import com.bangkit23b2.fonaapp.databinding.ActivityResultUserPreferenceBinding
import com.bangkit23b2.fonaapp.main.MainActivity
import com.bangkit23b2.fonaapp.utils.ViewModelFactory

class ResultUserPreferenceActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultUserPreferenceBinding
    private lateinit var factory: ViewModelFactory
    private var token = ""
    private lateinit var resultAdapter: ResultUserAdapter
    private val resultViewModel: ResultViewModel by viewModels { factory }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupView()
        setupViewModel()
        setupUser()
        setupAdapter()
        setupAction()

        resultViewModel.isLoading.observe(this) {
            showLoading(it)
        }

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
        val listAllergy = ArrayList<AllergiesItem>()
        resultAdapter = ResultUserAdapter(listAllergy)
        val layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        binding.rvAllergy.layoutManager = layoutManager
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

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
    private fun getUserData(userData: ResultData){
        val listAllergy = ArrayList<AllergiesItem>()
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
            listAllergy.clear()
            listAllergy.addAll(userData.allergies)
            resultAdapter.updateData(userData.allergies)
            resultAdapter.notifyDataSetChanged()
        }
        val adapter = ResultUserAdapter(listAllergy)
        binding.rvAllergy.adapter = adapter
    }
}