package com.bangkit23b2.fonaapp.ui.profile

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.Glide
import com.bangkit23b2.fonaapp.R
import com.bangkit23b2.fonaapp.data.response.AllergiesItem
import com.bangkit23b2.fonaapp.data.response.ResultData
import com.bangkit23b2.fonaapp.databinding.FragmentProfileBinding
import com.bangkit23b2.fonaapp.ui.intro.WelcomeActivity
import com.bangkit23b2.fonaapp.ui.result.ResultUserAdapter
import com.bangkit23b2.fonaapp.ui.update.UpdateUserActivity
import com.bangkit23b2.fonaapp.utils.ViewModelFactory
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth

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
        setupView()
        setupViewModel()
        setupUser()
        setupAdapter()
        setupAction()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        // Simpan data yang ingin dipertahankan di sini
        outState.putString("nama", binding.tvNama.text.toString())
        outState.putString("email", binding.tvEmail.text.toString())
        // Tambahkan penyimpanan data lainnya sesuai kebutuhan
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        // Pulihkan data yang telah disimpan saat fragment dibuat kembali
        if (savedInstanceState != null) {
            binding.tvNama.text = savedInstanceState.getString("nama")
            binding.tvEmail.text = savedInstanceState.getString("email")
            // Pulihkan data lainnya sesuai kebutuhan
        }
    }

    private fun setupView(){
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        mGoogleSignInClient= GoogleSignIn.getClient(requireActivity(),gso)


    }

    private fun setupAction(){
        binding.btnEditProfile.setOnClickListener {
            startActivity(Intent(requireActivity(), UpdateUserActivity::class.java))
        }
        binding.btnKeluar.setOnClickListener {
            signOut()
        }
    }

    private fun setupViewModel() {
        factory = ViewModelFactory.getInstance(requireActivity())
        profileViewModel.isLoading.observe(this) {
            showLoading(it)
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
        profileViewModel.getSession().observe(this){ user ->
            token = user.idToken
            profileViewModel.getUserData(token)
            profileViewModel.userData.observe(this){ userData ->
                Log.d(TAG,"userData observed")
                getUserData(userData)
            }
            profileViewModel.isLogin.observe(viewLifecycleOwner){
                if(it){
                    Toast.makeText(requireActivity(), "Sesi anda berakhir! Silakan sign in terlebih dahulu!", Toast.LENGTH_LONG).show()
                    profileViewModel.logout()
                    auth.signOut()
                    // Sign out from Google
                    mGoogleSignInClient.signOut().addOnCompleteListener {
                        // Start the WelcomeActivity after signing out
                        startActivity(Intent(requireActivity(), WelcomeActivity::class.java))
                        requireActivity().finish()
                    }
                } else {
                    Log.d(TAG, "User is Login")
                }
            }
        }

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

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}