package com.example.fonaapp.ui.search

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import com.example.fonaapp.R
import com.example.fonaapp.databinding.ActivitySearchFoodBinding

//TODO LULU - 6 setup binding, buat layout nya = Constraint - SearchBar (mirip di github) - recyclerview - progressbar
class SearchFoodActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchFoodBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchFoodBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupAction()
    }

    //TODO LULU - 7 ikutin di activity lain contohnya
    private fun setupView(){
        val searchView = binding.searchView

        //searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener)

    }

    //ini nanti dlu nunggu pasti dari API
    private fun setupViewModel(){

    }

    private fun setupAction(){
        binding.searchBar.setOnClickListener {

        }
        binding.searchView.setOnSearchClickListener {

        }
    }
}