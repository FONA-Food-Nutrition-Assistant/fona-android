package com.example.fonaapp.ui.search

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.example.fonaapp.R
import com.example.fonaapp.databinding.ActivitySearchFoodBinding
import com.example.fonaapp.ui.upload.UploadViewModel
import com.example.fonaapp.utils.ViewModelFactory

//TODO LULU - 6 setup binding, buat layout nya = Constraint - SearchBar (mirip di github) - recyclerview - progressbar
class SearchFoodActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchFoodBinding
    private val searchFoodViewModel: SearchFoodViewModel by viewModels { factory }
    private lateinit var factory: ViewModelFactory
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setupView()
        setupAction()
    }

    //TODO LULU - 7 ikutin di activity lain contohnya
    private fun setupView(){
        binding = ActivitySearchFoodBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val searchView = binding.searchView

        //searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener)

    }

    //ini nanti dlu nunggu pasti dari API
    private fun setupViewModel(){

    }

    private fun setupAction(){
        binding.searchBar.setOnClickListener {

        }

    }
}