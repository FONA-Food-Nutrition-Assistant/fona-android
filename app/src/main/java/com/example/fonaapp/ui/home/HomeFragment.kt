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
import com.example.fonaapp.R
import com.example.fonaapp.data.response.DailyNeeds
import com.example.fonaapp.data.response.DataHome
import com.example.fonaapp.databinding.FragmentHomeBinding
import com.example.fonaapp.ui.intro.WelcomeActivity
import com.example.fonaapp.ui.login.LoginActivity
import com.example.fonaapp.ui.preferences.UserPreferenceActivity
import com.example.fonaapp.ui.result.ResultUserPreferenceActivity
import com.example.fonaapp.ui.result.ResultViewModel
import com.example.fonaapp.utils.ViewModelFactory
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private lateinit var factory: ViewModelFactory
    private val binding get() = _binding!!
    private var token = ""
    private lateinit var auth: FirebaseAuth
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private val mainViewModel by activityViewModels<ResultViewModel> { factory }
    private val homeViewModel by activityViewModels<HomeViewModel> { factory }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewModel()
        setupUser()
        auth = Firebase.auth
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        mGoogleSignInClient= GoogleSignIn.getClient(requireActivity(),gso)
        // Set onClickListener pada TextView
        binding.tvDatePicker.setOnClickListener {
            showDatePickerDialog()
        }
    }

    private fun setupViewModel() {
        factory = ViewModelFactory.getInstance(requireActivity())

    }

    private fun setupUser() {

        val staticYear = 2023
        val staticMonth = Calendar.SEPTEMBER  // Bulan dimulai dari 0, September adalah 8
        val staticDay = 27
        val selectedDate = Calendar.getInstance()
        selectedDate.set(staticYear, staticMonth, staticDay)
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val formattedDate = dateFormat.format(selectedDate.time)
        binding.tvDatePicker.text = formattedDate


        mainViewModel.getSession().observe(this) { user ->
            token = user.idToken
            if (!user.isLogin && token != null) {
                Log.d(TAG, "User is not login")
                Toast.makeText(requireActivity(), "Login Failed 1", Toast.LENGTH_SHORT)
                startActivity(Intent(requireActivity(), WelcomeActivity::class.java))
                requireActivity().finish()
            } else {
                //Cek apakah user sudah mengisi data diri
                checkUserData(token)
                homeViewModel.getHomeData(token, formattedDate)
                homeViewModel.getDailyNeeds(token, formattedDate)
                homeViewModel.observeDataHome().observe(viewLifecycleOwner) { dataHome ->
                    getDailyNeeds(dataHome)
                }
            }
        }
    }

    //Fungsi cek data user
    private fun checkUserData(token: String) {
        Log.d(TAG, "fun called")
        mainViewModel.getUserDataResponse(token)
        mainViewModel.isDataDiri.observe(viewLifecycleOwner) {
            if(!it){
                startActivity(Intent(requireActivity(), UserPreferenceActivity::class.java))
                requireActivity().finish()
            } else {
                Log.d(TAG,"User Data is filled")
            }
        }
        mainViewModel.isLogin.observe(viewLifecycleOwner){
            if(it){
                mainViewModel.logout()
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

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val currentYear = calendar.get(Calendar.YEAR)
        val currentMonth = calendar.get(Calendar.MONTH)
        val currentDay = calendar.get(Calendar.DAY_OF_MONTH)

        // Ganti tanggal default menjadi "2023-09-27"
        val defaultYear = 2023
        val defaultMonth = Calendar.SEPTEMBER  // Bulan dimulai dari 0, September adalah 8
        val defaultDay = 27

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, year, month, day ->
                // Format tanggal sesuai kebutuhan
                val selectedDate = Calendar.getInstance()
                selectedDate.set(year, month, day)
                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val formattedDate = dateFormat.format(selectedDate.time)

                // Set nilai TextView dengan tanggal yang dipilih
                binding.tvDatePicker.text = formattedDate
            },
            currentYear,
            currentMonth,
            currentDay
        )

        // Set tanggal default
        datePickerDialog.updateDate(defaultYear, defaultMonth, defaultDay)

        // Batasi tanggal maksimum ke hari ini
        datePickerDialog.datePicker.maxDate = System.currentTimeMillis()

        // Show DatePickerDialog
        datePickerDialog.show()
    }


//    private fun convertDateFormatForBackend(originalDate: String): String {
//        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
//        val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
//
//        try {
//            val date = inputFormat.parse(originalDate)
//            return outputFormat.format(date!!)
//        } catch (e: ParseException) {
//            e.printStackTrace()
//        }
//
//        // Return original date if parsing fails
//        return originalDate
//    }

    private fun getDailyNeeds(dataHome: DataHome){

        val dailyNeeds = dataHome.dailyNeeds
        binding.apply {
            tvCaloryTarget.text = dailyNeeds.tDEE.toString()
            tvCarboTarget.text = dailyNeeds.carbos.toString()
            tvFatTarget.text = dailyNeeds.fats.toString()
            tvProteinTarget.text = dailyNeeds.proteins.toString()
        }

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}