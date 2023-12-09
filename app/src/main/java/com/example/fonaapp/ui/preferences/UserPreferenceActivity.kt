package com.example.fonaapp.ui.preferences

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
import com.example.fonaapp.R
import com.example.fonaapp.data.models.User
import com.example.fonaapp.databinding.ActivityUserPreferenceBinding
import com.example.fonaapp.utils.ViewModelFactory
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class UserPreferenceActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserPreferenceBinding
    private lateinit var factory: ViewModelFactory
    private val calendar = Calendar.getInstance()
    private val userPreferenceViewModel: UserPreferenceViewModel by viewModels { factory }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupView()
        setupAction()
        setupViewModel()
    }

    private fun setupView() {
        binding = ActivityUserPreferenceBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.apply {
            title = getString(R.string.title_user_preference)
            setDisplayHomeAsUpEnabled(true)
        }
        binding.edtBirthDate.setOnClickListener {
            showDatePickerDialog()
        }
    }
    private fun setupViewModel() {
        factory = ViewModelFactory.getInstance(this)
    }

    private fun setupAction() {
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

        // Navigasi ke ResultUserPreferenceActivity saat tombol lanjut diklik
        binding.btnContinueResult.setOnClickListener {
            if (isEditTextEmpty(binding.edtBirthDate) || isEditTextEmpty(binding.edtHeight) || isEditTextEmpty(
                    binding.edtWeight
                )
            ) {
                Toast.makeText(this, "Silakan isi semua data terlebih dahulu!!", Toast.LENGTH_SHORT)
                    .show()

            } else {
                postText()
                userPreferenceViewModel.userPreferenceResponse.observe(this@UserPreferenceActivity) { _ ->
                    val intent = Intent(
                        this@UserPreferenceActivity,
                        ResultUserPreferenceActivity::class.java
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

    private fun postText() {
        binding.apply {
            val height = edtHeight.text.toString().toInt()
            val weight = edtWeight.text.toString().toInt()
            // Mengambil gender dari radio button yang dipilih
            val gender = when (radioGroup.checkedRadioButtonId) {
                R.id.rbMale -> "Male"
                R.id.rbFemale -> "Female"
                else -> "" // Handle jika tidak ada yang dipilih
            }
            val dateOfBirth = convertDateFormatForBackend(edtBirthDate.text.toString())
            // Mengambil tingkat aktivitas dari spinner
            val activityLevel = spinnerActivity.selectedItem.toString().split(":")[0].trim()
            // Mengambil data alergi dari checkbox yang dipilih
            val allergies = mutableListOf<Int>()
            if (checkBox.isChecked) {
                allergies.add(5) // Menyimpan kode alergi sesuai dengan kebutuhan Anda
            }
            if (checkBox2.isChecked) {
                allergies.add(6)
            }
            if (checkBox3.isChecked) {
                allergies.add(7)
            }
            val idToken = intent.getStringExtra("ID_TOKEN")

            if (idToken != null) {
                userPreferenceViewModel.storeUserData(
                    User(
                        height,
                        weight,
                        gender,
                        dateOfBirth,
                        activityLevel,
                        allergies
                    ), idToken
                )
            }
        }
    }

    private fun updateEditText() {
        val dateFormat = "dd/MM/yyyy"
        val simpleDateFormat = SimpleDateFormat(dateFormat, Locale.US)
        binding.edtBirthDate.setText(simpleDateFormat.format(calendar.time))
    }

    private fun convertDateFormatForBackend(originalDate: String): String {
        val inputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.US)
        val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        val date = inputFormat.parse(originalDate)
        return outputFormat.format(date!!)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}