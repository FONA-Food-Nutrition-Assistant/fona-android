package com.bangkit23b2.fonaapp.ui.home

import android.annotation.SuppressLint
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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bangkit23b2.fonaapp.R
import com.bangkit23b2.fonaapp.data.response.BreakfastItem
import com.bangkit23b2.fonaapp.data.response.CalorieIntake
import com.bangkit23b2.fonaapp.data.response.DailyAnalysis
import com.bangkit23b2.fonaapp.data.response.DailyNeeds
import com.bangkit23b2.fonaapp.data.response.DinnerItem
import com.bangkit23b2.fonaapp.data.response.LunchItem
import com.bangkit23b2.fonaapp.databinding.FragmentHomeBinding
import com.bangkit23b2.fonaapp.ui.detail.DetailMakananActivity
import com.bangkit23b2.fonaapp.ui.home.record.RecordBreakfastAdapter
import com.bangkit23b2.fonaapp.ui.home.record.RecordDinnerAdapter
import com.bangkit23b2.fonaapp.ui.home.record.RecordLunchAdapter
import com.bangkit23b2.fonaapp.ui.home.record.RecordWaterAdapter
import com.bangkit23b2.fonaapp.ui.home.suggestion.FoodSuggestionAdapter
import com.bangkit23b2.fonaapp.ui.intro.WelcomeActivity
import com.bangkit23b2.fonaapp.ui.preferences.UserPreferenceActivity
import com.bangkit23b2.fonaapp.ui.result.ResultViewModel
import com.bangkit23b2.fonaapp.ui.upload.UploadActivity
import com.bangkit23b2.fonaapp.utils.ViewModelFactory
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
    private lateinit var gelasAdapter: RecordWaterAdapter
    private lateinit var factory: ViewModelFactory
    private val binding get() = _binding!!
    private var token = ""
    private lateinit var calorieIntake: CalorieIntake
    private lateinit var auth: FirebaseAuth
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var breakfastAdapter: RecordBreakfastAdapter
    private lateinit var lunchAdapter: RecordLunchAdapter
    private lateinit var dinnerAdapter: RecordDinnerAdapter
    private lateinit var suggestionAdapter: FoodSuggestionAdapter
    private var formattedDate: String = ""
    private val mainViewModel by activityViewModels<ResultViewModel> { factory }
    private val homeViewModel by activityViewModels<HomeViewModel> { factory }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewModel()
        val currentDate = Calendar.getInstance()
        val defaultDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val defaultFormattedDate = defaultDateFormat.format(currentDate.time)

        binding.tvDatePicker.text = defaultFormattedDate

        val formattedDate = convertDateFormatForBackend(binding.tvDatePicker.text.toString())

        binding.tvDatePicker.setOnClickListener {
            showDatePickerDialog()
        }
        homeViewModel.isLoading.observe(this) {
            showLoading(it)
        }
        binding.btnAddLunch.setOnClickListener {
            val intent = Intent(
                requireActivity(),
                UploadActivity::class.java
            )
            startActivity(intent)
        }
        binding.btnAddSarapan.setOnClickListener {
            val intent = Intent(
                requireActivity(),
                UploadActivity::class.java
            )
            startActivity(intent)
        }
        binding.btnAddDinner.setOnClickListener {
            val intent = Intent(
                requireActivity(),
                UploadActivity::class.java
            )
            startActivity(intent)
        }
        setupUser()
        setupAdapter()
        auth = Firebase.auth
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        mGoogleSignInClient= GoogleSignIn.getClient(requireActivity(),gso)

    }

    private fun setupViewModel() {
        factory = ViewModelFactory.getInstance(requireActivity())

    }

    @SuppressLint("SetTextI18n")
    private fun setupUser() {

        val formattedDate = convertDateFormatForBackend(binding.tvDatePicker.text.toString())


        mainViewModel.getSession().observe(this) { user ->
            token = user.idToken
            homeViewModel.getDataHome(token, formattedDate)
            if (!user.isLogin) {
                Log.d(TAG, "User is not login")
                startActivity(Intent(requireActivity(), WelcomeActivity::class.java))
                requireActivity().finish()
            } else {
                checkUserData(token)
                homeViewModel.getDataHome(token, formattedDate)
                mainViewModel.isLogin.observe(viewLifecycleOwner){
                    if(it){
                        Toast.makeText(requireActivity(), "Sesi anda berakhir! Silakan sign in terlebih dahulu!", Toast.LENGTH_LONG).show()
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
                homeViewModel.dailyNeeds.observe(this) { dailyNeeds ->
                    Log.d(TAG,"Daily Needs Called")
                    getDailyNeeds(dailyNeeds)
                }
                homeViewModel.dailyAnalysis.observe(this) { dailyAnalysis ->
                    Log.d(TAG,"Daily Analysis called")
                    getDailyAnalysis(dailyAnalysis)
                }
                homeViewModel.calorieTargetTakes.observe(this) { calorieTargetTakes->
                    Log.d(TAG, "calorieTargetTakes called")
                    getTargetIntakes(calorieTargetTakes)
                    calorieIntake = calorieTargetTakes
                }
                homeViewModel.listBreakfast.observe(viewLifecycleOwner) { breakfastList ->
                    Log.d(TAG, "List Breakfast called")
                    breakfastAdapter.updateData(breakfastList)
                }
                homeViewModel.listLunch.observe(viewLifecycleOwner) { lunchList ->
                    Log.d(TAG, "List Lunch called")
                    lunchAdapter.updateData(lunchList)
                }
                homeViewModel.listDinner.observe(viewLifecycleOwner) { lunchDinner ->
                    Log.d(TAG, "List Dinner called")
                    dinnerAdapter.updateData(lunchDinner)
                }
                homeViewModel.storeWaterResponse.observe(viewLifecycleOwner) { response ->
                    Log.d(TAG,"Can store")
                }
                homeViewModel.getRecordWater.observe(viewLifecycleOwner) { drink ->
                    binding.tvKaloriDrinkRemaining.text = "${drink} gelas"
                    val totalMl = drink * 25 // Sesuaikan dengan aturan konversi
                    binding.tvKaloriDrink.text = "$totalMl mL"
                }
                homeViewModel.listWater.observe(viewLifecycleOwner){
                    Log.d(TAG, "Dirnk called")
                    gelasAdapter.updateData(it)

                }

            }
        }
    }

    private fun setupAdapter(){
        binding.apply {
            // Adapter Breakfast
            breakfastAdapter = RecordBreakfastAdapter(ArrayList()) { clickedItem ->
                openDetailActivityBreakfast(clickedItem)
            }
            rcListSarapan.layoutManager = LinearLayoutManager(requireContext())
            rcListSarapan.adapter = breakfastAdapter

            breakfastAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
                override fun onChanged() {
                    super.onChanged()
                    updateTotalCalories(calorieIntake)
                }
            })

            lunchAdapter = RecordLunchAdapter(ArrayList()) { clickedItem ->
                openDetailActivityLunch(clickedItem)
            }
            rcListLunch.layoutManager = LinearLayoutManager(requireContext())
            rcListLunch.adapter = lunchAdapter

            lunchAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
                override fun onChanged() {
                    super.onChanged()
                    updateTotalCalories(calorieIntake)
                }
            })

            // Adapter Dinner
            dinnerAdapter = RecordDinnerAdapter(ArrayList()) { clickedItem ->
                openDetailActivityDinner(clickedItem)
            }
            rcListDinner.layoutManager = LinearLayoutManager(requireContext())
            rcListDinner.adapter = dinnerAdapter

            dinnerAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
                override fun onChanged() {
                    super.onChanged()
                    updateTotalCalories(calorieIntake)
                }
            })

            homeViewModel.foodSuggestion.observe(viewLifecycleOwner) { foodSuggestion ->
                val meals = listOf(foodSuggestion.breakfast, foodSuggestion.lunch, foodSuggestion.dinner)
                suggestionAdapter = FoodSuggestionAdapter(meals)
                val layoutManager = StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
                binding.rcFoodSuggestion.layoutManager = layoutManager
                binding.rcFoodSuggestion.adapter = suggestionAdapter
            }
            gelasAdapter = RecordWaterAdapter(ArrayList())
            rvDrink.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            rvDrink.adapter = gelasAdapter

        }
    }

    private fun openDetailActivityBreakfast(breakfastItem: BreakfastItem) {
        val intent = Intent(requireActivity(), DetailMakananActivity::class.java)
        intent.putExtra("BREAKFAST_ITEM_KEY", breakfastItem)
        startActivity(intent)
    }

    private fun openDetailActivityLunch(lunchItem: LunchItem) {
        val intent = Intent(requireActivity(), DetailMakananActivity::class.java)
        intent.putExtra("LUNCH_ITEM_KEY", lunchItem)
        startActivity(intent)
    }

    private fun openDetailActivityDinner(dinnerItem: DinnerItem) {
        val intent = Intent(requireActivity(), DetailMakananActivity::class.java)
        intent.putExtra("DINNER_ITEM_KEY", dinnerItem)
        startActivity(intent)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    //Fungsi cek data user
    private fun checkUserData(token: String) {
        Log.d(TAG, "fun called")
        mainViewModel.getUserDataResponse(token)
        mainViewModel.isDataDiri.observe(viewLifecycleOwner) {
            if(!it){
                Toast.makeText(requireActivity(), "Mohon isi data diri terlebih dahulu!", Toast.LENGTH_LONG).show()
                startActivity(Intent(requireActivity(), UserPreferenceActivity::class.java))
                requireActivity().finish()
            } else {
                Log.d(TAG,"User Data is filled")
            }
        }
    }

    private fun showDatePickerDialog() {
        val currentDate = Calendar.getInstance()
        val currentYear = currentDate.get(Calendar.YEAR)
        val currentMonth = currentDate.get(Calendar.MONTH)
        val currentDay = currentDate.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, year, month, day ->

                val selectedDate = Calendar.getInstance()
                selectedDate.set(year, month, day)
                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val formattedDate = dateFormat.format(selectedDate.time)

                binding.tvDatePicker.text = formattedDate
                checkUserData(token)
                homeViewModel.getDataHome(token, formattedDate)
                binding.btnAddDrink.setOnClickListener {
                    homeViewModel.recordWaterConsumption(0, token, formattedDate)
                    homeViewModel.isRecorded.observe(viewLifecycleOwner) { isRecorded ->
                        if (!isRecorded) {
                            Log.d(TAG, "Can record")
                            homeViewModel.getDataHome(token, formattedDate)
                            homeViewModel.recordWaterConsumption(1, token, formattedDate)
                        } else {
                            Log.d(TAG, "Can update")
                            val previousNumberOfCups = homeViewModel.getRecordWater.value ?: 0
                            val updatedNumberOfCups = previousNumberOfCups + 1
                            homeViewModel.updateWaterRecord(updatedNumberOfCups, token, formattedDate)
                            homeViewModel.getDataHome(token, formattedDate)
                        }
                    }
                    homeViewModel.getRecordWater.observe(viewLifecycleOwner) { drink ->
                        binding.tvKaloriDrinkRemaining.text = "${drink} gelas"
                        val totalMl = drink * 25 // Sesuaikan dengan aturan konversi
                        binding.tvKaloriDrink.text = "$totalMl mL"
                    }
                }
            },
            currentYear,
            currentMonth,
            currentDay
        )

        datePickerDialog.datePicker.maxDate = System.currentTimeMillis()

        datePickerDialog.updateDate(currentYear, currentMonth, currentDay)

        datePickerDialog.show()
    }




    private fun convertDateFormatForBackend(originalDate: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)

        try {
            val date = inputFormat.parse(originalDate)
            return outputFormat.format(date!!)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return originalDate
    }

    @SuppressLint("SetTextI18n")
    private fun getDailyNeeds(dailyNeeds: DailyNeeds){

        binding.apply {
            binding.tvCaloryTarget.text = "${dailyNeeds.tDEE.toInt()} kkal"
            tvCarboTarget.text = "${dailyNeeds.carbos.toInt()} g"
            tvFatTarget.text = "${dailyNeeds.fats.toInt()} g"
            tvProteinTarget.text = "${dailyNeeds.proteins.toInt()} g"
        }
    }

    private fun getDailyAnalysis(dailyAnalysis: DailyAnalysis){
        binding.apply{
            val stringBuilder = StringBuilder()
            with(binding) {
                tvCaloryTakes.text = stringBuilder.append(dailyAnalysis.totalCals.toInt()).append("/").toString()
                tvCarboTakes.text = stringBuilder.clear().append(dailyAnalysis.totalCarbos.toInt()).append("/").toString()
                tvFatTakes.text = stringBuilder.clear().append(dailyAnalysis.totalFats.toInt()).append("/").toString()
                tvProteinTakes.text = stringBuilder.clear().append(dailyAnalysis.totalProteins.toInt()).append("/").toString()
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun getTargetIntakes(calorieIntake: CalorieIntake){
        binding.apply{
            tvKaloriSarapanRemaining.text = "Target min: ${calorieIntake.lowestBreakfastIntake}"
            tvKaloriLunchRemaining.text = "Target min: ${calorieIntake.lowestLunchIntake}"
            tvKaloriDinnerRemaining.text = "Target min: ${calorieIntake.lowestDinnerIntake}"
        }
    }

    @SuppressLint("SetTextI18n")
    private fun updateTotalCalories(calorieIntake: CalorieIntake) {
        if (!::calorieIntake.isInitialized) {
            return
        }

        // Sarapan
        val targetCaloriesSarapan = calorieIntake.lowestBreakfastIntake
        val highestBreakfastIntake = calorieIntake.highestBreakfastIntake
        val consumedCaloriesSarapan = breakfastAdapter.getBreakfastList().sumBy { it.total_cals.toInt() }
        binding.tvKaloriSarapan.text = "$consumedCaloriesSarapan kalori"
        when {
            consumedCaloriesSarapan < targetCaloriesSarapan -> {
                binding.statusIntakesBreakfast.setImageResource(R.drawable.ic_kurang)
                binding.tvStatusSarapan.text = "Kurang"
            }
            consumedCaloriesSarapan == targetCaloriesSarapan -> {
                binding.statusIntakesBreakfast.setImageResource(R.drawable.ic_ideal)
                binding.tvStatusSarapan.text = "Cukup"
            }
            consumedCaloriesSarapan > highestBreakfastIntake -> {
                binding.statusIntakesBreakfast.setImageResource(R.drawable.ic_berlebih)
                binding.tvStatusSarapan.text = "Berlebih"
            }
            else -> {
                binding.statusIntakesBreakfast.setImageResource(R.drawable.ic_ideal)
                binding.tvStatusSarapan.text = "Cukup"
            }
        }

        // Makan Siang
        val targetCaloriesLunch = calorieIntake.lowestLunchIntake
        val highestLunchIntake = calorieIntake.highestLunchIntake
        val consumedCaloriesLunch = lunchAdapter.getLunchList().sumBy { it.total_cals.toInt() }
        binding.tvKaloriLunch.text = "$consumedCaloriesLunch kalori"
        when {
            consumedCaloriesLunch < targetCaloriesLunch -> {
                binding.statusIntakesLunch.setImageResource(R.drawable.ic_kurang)
                binding.tvStatusLunch.text = "Kurang"
            }
            consumedCaloriesLunch == targetCaloriesLunch -> {
                binding.statusIntakesLunch.setImageResource(R.drawable.ic_ideal)
                binding.tvStatusLunch.text = "Cukup"
            }
            consumedCaloriesLunch > highestLunchIntake -> {
                binding.statusIntakesLunch.setImageResource(R.drawable.ic_berlebih)
                binding.tvStatusLunch.text = "Berlebih"
            }
            else -> {
                binding.statusIntakesLunch.setImageResource(R.drawable.ic_ideal)
                binding.tvStatusLunch.text = "Cukup"
            }
        }

        // Makan Malam
        val targetCaloriesDinner = calorieIntake.lowestDinnerIntake
        val highestDinnerIntake = calorieIntake.highestDinnerIntake
        val consumedCaloriesDinner = dinnerAdapter.getDinnerList().sumBy { it.total_cals.toInt() }
        binding.tvKaloriDinner.text = "$consumedCaloriesDinner kalori"
        when {
            consumedCaloriesDinner < targetCaloriesDinner -> {
                binding.statusIntakesDinner.setImageResource(R.drawable.ic_kurang)
                binding.tvStatusDinner.text = "Kurang"
            }
            consumedCaloriesDinner == targetCaloriesDinner -> {
                binding.statusIntakesDinner.setImageResource(R.drawable.ic_ideal)
                binding.tvStatusDinner.text = "Cukup"
            }
            consumedCaloriesDinner > highestDinnerIntake -> {
                binding.statusIntakesDinner.setImageResource(R.drawable.ic_berlebih)
                binding.tvStatusDinner.text = "Berlebih"
            }
            else -> {
                binding.statusIntakesDinner.setImageResource(R.drawable.ic_ideal)
                binding.tvStatusDinner.text = "Cukup"
            }
        }
    }




    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}