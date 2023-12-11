package com.example.fonaapp.ui.profile

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.fonaapp.R
import com.example.fonaapp.data.response.AllergiesItem
import com.example.fonaapp.data.response.ResultData
import com.example.fonaapp.databinding.FragmentProfileBinding
import com.example.fonaapp.ui.intro.WelcomeActivity
import com.example.fonaapp.ui.result.ResultUserAdapter
import com.example.fonaapp.ui.result.ResultViewModel
import com.example.fonaapp.utils.ViewModelFactory
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class ProfileFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    private var token = ""
    private lateinit var factory: ViewModelFactory
    private lateinit var resultAdapter: ResultUserAdapter
    private val profileViewModel by activityViewModels<ProfileViewModel> { factory }
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var binding: FragmentProfileBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        val firebaseUser= auth.currentUser

        if(firebaseUser!=null){
            if (firebaseUser.displayName != null) {
                binding.tvNama.text = firebaseUser.displayName
            } else {
                // Set a default value or handle the case when displayName is null
                binding.tvNama.text = "User"
            }
            binding.tvEmail.text = firebaseUser.email
            // Check if user has a photo URL
            if (firebaseUser.photoUrl != null) {
                // Load and display the user's photo using the Glide library or any other image loading library
                // Example using Glide:
                Glide.with(this)
                    .load(firebaseUser.photoUrl)
                    .into(binding.imageView)
            } else {
                // If the user doesn't have a photo, you can set a placeholder image
                binding.imageView.setImageResource(R.drawable.ic_about)
            }
        } else {
            binding.tvNama.text = "null"
            binding.tvEmail.text = "null"
        }
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewModel()
        setupAdapter()
        setupUser()
        setupAction()
    }

    private fun setupView(){
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        mGoogleSignInClient= GoogleSignIn.getClient(requireActivity(),gso)
    }

    private fun setupAction(){
        binding.btnKeluar.setOnClickListener {
            signOut()
        }
    }

    private fun setupViewModel() {
        factory = ViewModelFactory.getInstance(requireActivity())

    }

    private fun setupAdapter(){
        val listAllergy = ArrayList<AllergiesItem>()
        resultAdapter = ResultUserAdapter(listAllergy)
        val layoutManager = LinearLayoutManager(requireActivity())
        binding.rvAllergy.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(requireActivity(), layoutManager.orientation)
        binding.rvAllergy.addItemDecoration(itemDecoration)
    }

    private fun setupUser(){
        profileViewModel.getSession().observe(this){ user ->
            token = user.idToken
            profileViewModel.getUserData(token)
            profileViewModel.userData.observe(this){ userData ->
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

    private fun signOut() {
        profileViewModel.logout()
        auth.signOut()
        // Sign out from Google
        mGoogleSignInClient.signOut().addOnCompleteListener {
            // Start the WelcomeActivity after signing out
            startActivity(Intent(requireActivity(), WelcomeActivity::class.java))
            requireActivity().finish()
        }
    }
}