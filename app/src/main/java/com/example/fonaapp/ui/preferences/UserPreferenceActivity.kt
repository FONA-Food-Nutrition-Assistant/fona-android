package com.example.fonaapp.ui.preferences

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.example.fonaapp.R
import com.example.fonaapp.databinding.ActivityUserPreferenceBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class UserPreferenceActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserPreferenceBinding
    private val calendar = Calendar.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupView()

        binding.edtBirthDate.setOnClickListener {
            showDatePickerDialog()
        }


        //access item activity list
        val activityLevels = resources.getStringArray(R.array.activity_levels)

        //access the spinner
        val spinner = binding.spinnerActivity
        if (spinner != null) {
            val adapter = ArrayAdapter(this, R.layout.spinner_item, activityLevels)
            spinner.adapter = adapter
            spinner.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View, position: Int, id: Long
                ) {
                }
                override fun onNothingSelected(parent: AdapterView<*>) {
                    // write code to perform some action
                }
            }
        }

        binding.btnContinueResult.setOnClickListener {
            val intent = Intent(this, ResultUserPreferenceActivity::class.java)
            intent.putExtra("gender", getSelectedGender())
            intent.putExtra(ResultUserPreferenceActivity.EXTRA_AGE, 22)
            intent.putExtra(ResultUserPreferenceActivity.EXTRA_WEIGHT, 55)
            intent.putExtra(ResultUserPreferenceActivity.EXTRA_HEIGHT, 165)
            intent.putExtra(ResultUserPreferenceActivity.EXTRA_BMI, 20.20)
            startActivity(intent)

        }

    }
    private fun setupView(){
        binding = ActivityUserPreferenceBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.apply {
            title = getString(R.string.title_user_preference)
            setDisplayHomeAsUpEnabled(true)
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

    private fun updateEditText() {
        val dateFormat = "YYYY/MM/dd"
        val simpleDateFormat = SimpleDateFormat(dateFormat, Locale.US)
        binding.edtBirthDate.setText(simpleDateFormat.format(calendar.time))
    }

    private fun getSelectedAllergies(): ArrayList<String> {
        val allergies = ArrayList<String>()
        if (binding.checkBox.isChecked) allergies.add("Seafood")
        if (binding.checkBox2.isChecked) allergies.add("Telur")
        if (binding.checkBox3.isChecked) allergies.add("Lactose")
        return allergies
    }

    private fun getSelectedGender(): String {
        // Mengambil jenis kelamin yang dipilih dari radio button
        return if (binding.rbMale.isChecked) "Pria" else "Perempuan"
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}