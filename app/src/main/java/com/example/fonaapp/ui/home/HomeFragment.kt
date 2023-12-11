package com.example.fonaapp.ui.home

import android.app.DatePickerDialog
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.fonaapp.databinding.FragmentHomeBinding
import com.example.fonaapp.ui.intro.WelcomeActivity
import com.example.fonaapp.ui.preferences.UserPreferenceActivity
import com.example.fonaapp.ui.result.ResultUserPreferenceActivity
import com.example.fonaapp.ui.result.ResultViewModel
import com.example.fonaapp.utils.ViewModelFactory
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private lateinit var factory: ViewModelFactory
    private val binding get() = _binding!!
    private var token = ""
    private lateinit var auth: FirebaseAuth
    private val mainViewModel by activityViewModels<ResultViewModel> { factory }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        setupViewModel()
        setupUser()
        return binding.root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = Firebase.auth
        // Set onClickListener pada TextView
        binding.tvDatePicker.setOnClickListener {
            showDatePickerDialog()
        }
    }

    private fun setupViewModel() {
        factory = ViewModelFactory.getInstance(requireActivity())

    }

    private fun setupUser() {
        mainViewModel.getSession().observe(this) { user ->
            token = user.idToken
            if (!user.isLogin) {
                Log.d(TAG, "User is not login")
                Toast.makeText(requireActivity(), "Login Failed 1", Toast.LENGTH_SHORT)
                startActivity(Intent(requireActivity(), WelcomeActivity::class.java))
                requireActivity().finish()
            } else {
                //Cek apakah user sudah mengisi data diri
                checkUserData(token)
            }
        }
    }

    //Fungsi cek data user
    private fun checkUserData(token: String) {
        Log.d(TAG, "fun called")
        mainViewModel.isDataDiri.observe(viewLifecycleOwner) {
            if(!it){
                startActivity(Intent(requireActivity(), UserPreferenceActivity::class.java))
                requireActivity().finish()
            }
        }
        mainViewModel.getUserDataResponse(token)
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val currentYear = calendar.get(Calendar.YEAR)
        val currentMonth = calendar.get(Calendar.MONTH)
        val currentDay = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, year, month, day ->
                // Format tanggal sesuai kebutuhan
                val selectedDate = Calendar.getInstance()
                selectedDate.set(year, month, day)
                val dateFormat = SimpleDateFormat("YYYY/MM/dd", Locale.getDefault())
                val formattedDate = dateFormat.format(selectedDate.time)

                // Set nilai TextView dengan tanggal yang dipilih
                binding.tvDatePicker.text = formattedDate
            },
            currentYear,
            currentMonth,
            currentDay
        )
        datePickerDialog.datePicker.maxDate = System.currentTimeMillis()
        // Show DatePickerDialog
        datePickerDialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}