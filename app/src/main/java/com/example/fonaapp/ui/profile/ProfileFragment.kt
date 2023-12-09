package com.example.fonaapp.ui.profile

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.fonaapp.R
import com.example.fonaapp.databinding.FragmentProfileBinding
import com.example.fonaapp.ui.intro.WelcomeActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class ProfileFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
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
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        mGoogleSignInClient= GoogleSignIn.getClient(requireActivity(),gso)
        // Set onClickListener pada TextView
        binding.btnKeluar.setOnClickListener {
            signOut()
        }
    }

    private fun signOut() {
        auth.signOut()
        // Sign out from Google
        mGoogleSignInClient.signOut().addOnCompleteListener {
            // Start the WelcomeActivity after signing out
            startActivity(Intent(requireActivity(), WelcomeActivity::class.java))
            requireActivity().finish()
        }
    }
}