package com.example.fonaapp.ui.update

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.fonaapp.R
import com.example.fonaapp.data.response.Data
import com.example.fonaapp.data.response.DataItem
import com.example.fonaapp.data.response.ListAllergyResponse
import com.example.fonaapp.databinding.ActivityUpdateUserBinding
import com.example.fonaapp.main.MainActivity
import com.example.fonaapp.utils.ViewModelFactory
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class UpdateUserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUpdateUserBinding
    private lateinit var factory: ViewModelFactory
    private val calendar = Calendar.getInstance()
    private var token = ""
    private lateinit var updateAdapter: UpdateUserAdapter
    private val updateUserViewModel: UpdatePreferenceViewModel by viewModels { factory }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupView()
        setupViewModel()
        setupUser()
        setupAdapter()
        setupAction()

    }

    private fun setupView(){
        binding = ActivityUpdateUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
        }
    }

    private fun setupViewModel(){
        factory = ViewModelFactory.getInstance(this)
        updateUserViewModel.isDataDiri.observe(this) {
            if(!it){
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }
//        updateUserViewModel.checkboxAllergyResponse.observe(this) { allergies ->
//            updateAdapter.setData(allergies)
//        }
    }

    private fun setupUser(){

        updateUserViewModel.getSession().observe(this){ user ->
            token = user.idToken
//            updateUser()
            updateUserViewModel.getListAllergy(token)
            updateUserViewModel.allergyData.observe(this) { allergies ->
                getAllergy(allergies)
            }
        }
//        updateUserViewModel.getListAllergy(token)
//        updateUserViewModel.checkboxAllergyResponse.observe(this) { allergies ->
//            updateAdapter.setData(allergies)
//        }
    }

    private fun getAllergy(allergyData: ListAllergyResponse){
        val listAllergy = ArrayList<DataItem>()
        updateAdapter.setData(allergyData.data)
        listAllergy.addAll(allergyData.data)
        updateAdapter.notifyDataSetChanged()
    }

    private fun setupAdapter(){
        val listAllergy = ArrayList<DataItem>()
        updateAdapter = UpdateUserAdapter(listAllergy)
        val layoutManager = StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
        binding.rvCbAlergi.layoutManager = layoutManager
        binding.rvCbAlergi.adapter = updateAdapter

    }

    private fun setupAction(){
        binding.edtBirthDate.setOnClickListener {
            showDatePickerDialog()
        }
        val activityLevels = resources.getStringArray(R.array.activity_levels)
        val spinner = binding.spinnerActivity

        val adapter = ArrayAdapter(this, R.layout.spinner_item, activityLevels)
        spinner.adapter = adapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Implementasi jika tidak ada yang dipilih
            }
        }
        binding.btnContinueResult.setOnClickListener {
            if (isEditTextEmpty(binding.edtBirthDate) || isEditTextEmpty(binding.edtHeight) || isEditTextEmpty(
                    binding.edtWeight
                )
            ) {
                Toast.makeText(this, "Silakan isi semua data terlebih dahulu!!", Toast.LENGTH_SHORT)
                    .show()

            } else {
                updateUser()
                updateUserViewModel.updateUserResponse.observe(this@UpdateUserActivity) { _ ->
                    val intent = Intent(
                        this@UpdateUserActivity,
                        MainActivity::class.java
                    )
                    startActivity(intent)
                }
            }
        }
    }

    private fun showDatePickerDialog() {
        val datePickerDialog = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                // Update EditText with selected date
                calendar.set(year, month, dayOfMonth)
                updateEditText()

            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.datePicker.maxDate = System.currentTimeMillis()
        datePickerDialog.show()
    }
    private fun isEditTextEmpty(editText: EditText): Boolean {
        return editText.text.toString().trim().isEmpty()
    }

    private fun updateEditText() {
        val dateFormat = "dd/MM/yyyy"
        val simpleDateFormat = SimpleDateFormat(dateFormat, Locale.US)
        binding.edtBirthDate.setText(simpleDateFormat.format(calendar.time))
    }

    private fun updateUser(){

        binding.apply{
            val height = edtHeight.text.toString().toInt()
            val weight = edtWeight.text.toString().toInt()
            val gender = when (radioGroup.checkedRadioButtonId){
                R.id.rbMale -> "Laki-laki"
                R.id.rbFemale -> "Perempuan"
                else -> ""
            }
            val dateOfBirth = convertDateFormatForBackend(edtBirthDate.text.toString())
            val activityLevel = spinnerActivity.selectedItem.toString().split(":")[0].trim()
            val selectedAllergies = updateAdapter.getSelectedIds().toMutableList()
            if (token != null) {
                updateUserViewModel.updateUserData(
                    Data(
                        height, weight, gender, dateOfBirth, activityLevel,
                        selectedAllergies
                    ), token
                )
            }

        }
    }

    private fun convertDateFormatForBackend(originalDate: String): String {
        val inputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.US)
        val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        val date = inputFormat.parse(originalDate)
        return outputFormat.format(date!!)

    }
}