package com.bangkit23b2.fonaapp.ui.search

import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit23b2.fonaapp.data.response.DataItemDetail
import com.bangkit23b2.fonaapp.databinding.ActivitySearchFoodBinding
import com.bangkit23b2.fonaapp.utils.ViewModelFactory

class SearchFoodActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchFoodBinding
    private val searchFoodViewModel: SearchFoodViewModel by viewModels { factory }
    private lateinit var factory: ViewModelFactory
    private lateinit var searchAdapter: SearchAdapter
    private var token = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setupView()
        setupViewModel()
        setupUser()
        setupAction()
    }
    private fun setupView(){
        binding = ActivitySearchFoodBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }

    private fun setupUser() {
        searchFoodViewModel.getSession().observe(this) { user ->
            token = user.idToken
            searchFoodViewModel.getNutritionListResponse(token, " ")

            with(binding) {
                searchView.setupWithSearchBar(searchBar)
                searchView
                    .editText
                    .setOnEditorActionListener { _, _, _ ->
                        searchBar.text = searchView.text
                        val query = searchView.text.toString().trim()

                        if (query.isEmpty()) {
                            searchFoodViewModel.isLoading.observe(this@SearchFoodActivity) {
                                showLoading(it)
                            }
                            searchFoodViewModel.getNutritionListResponse(token, "a")
                            searchFoodViewModel.listFoodDetailResponse.observe(this@SearchFoodActivity) {
                                setupAdapter(it)
                                if (it != null) {
                                    Log.d(TAG, "List Food Detail Response: $it")
                                } else {
                                    Log.e(TAG, "List Food Detail Response is null")
                                }
                            }
                        } else {
                            searchFoodViewModel.isLoading.observe(this@SearchFoodActivity) {
                                showLoading(it)
                            }
                            searchFoodViewModel.getNutritionListResponse(token, query)
                            searchFoodViewModel.listFoodDetailResponse.observe(this@SearchFoodActivity) {
                                setupAdapter(it)
                                if (it != null) {
                                    Log.d(TAG, "List Food Detail Response: $it")
                                } else {
                                    Log.e(TAG, "List Food Detail Response is null")
                                }
                            }
                        }

                        Toast.makeText(this@SearchFoodActivity, query, Toast.LENGTH_SHORT).show()
                        searchView.hide()
                        false
                    }
            }

            // Initialize the adapter only once
            searchAdapter = SearchAdapter()
            binding.rvRecordFood.adapter = searchAdapter

            searchFoodViewModel.listFoodDetailResponse.observe(this@SearchFoodActivity) {
                setupAdapter(it)
                if (it != null) {
                    Log.d(TAG, "List Food Detail Response: $it")
                } else {
                    Log.e(TAG, "List Food Detail Response is null")
                }
            }

            val layoutManager = LinearLayoutManager(this)
            binding.rvRecordFood.layoutManager = layoutManager
            val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
            binding.rvRecordFood.addItemDecoration(itemDecoration)

        }
    }



    //ini nanti dlu nunggu pasti dari API
    private fun setupViewModel(){
        factory = ViewModelFactory.getInstance(this)
    }

    private fun setupAction(){
        binding.searchBar.setOnClickListener {

        }

    }

    private fun setupAdapter(searchAdapter: List<DataItemDetail>) {
        val adapter = SearchAdapter()
        binding.rvRecordFood.adapter = adapter
        adapter.submitList(searchAdapter)
    }


    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}