package com.example.fonaapp.ui.daftar

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import com.example.fonaapp.R
import com.example.fonaapp.data.models.UserModel
import com.example.fonaapp.data.models.UserPreference
import com.example.fonaapp.databinding.ActivityDaftarBinding
import com.example.fonaapp.main.MainActivity
import com.example.fonaapp.ui.login.LoginActivity
import com.example.fonaapp.ui.preferences.UserPreferenceActivity
import com.example.fonaapp.utils.Injection
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.auth.userProfileChangeRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DaftarActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDaftarBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var userPreference: UserPreference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_daftar)
        setupView()
        setupAction()
    }

    private fun setupView(){
        binding = ActivityDaftarBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        auth = Firebase.auth
        userPreference = Injection.provideUserPreference(this)
    }

    private fun setupAction(){
        binding.btnDaftar.setOnClickListener {
            if(isEditTextEmpty(binding.edtNama) || isEditTextEmpty(binding.edtEmail) || isEditTextEmpty(binding.edtKataSandi) || isEditTextEmpty(binding.edtKonfirmasiSandi) ){
                Toast.makeText(this, "Silakan isi semua data terlebih dahulu!!", LENGTH_SHORT).show()

            } else if (hasValidationError()) {
                // Validasi jika EditText masih memiliki Error
                Toast.makeText(this, "Mohon masukkan input dengan benar!!", LENGTH_SHORT).show()
            } else {
                if(binding.edtKataSandi.text.toString() == binding.edtKonfirmasiSandi.text.toString()) {
                    processRegister()
                } else {
                    Toast.makeText(this, "Konfirmasi kata sandi harus sama!!", LENGTH_SHORT).show()
                }

            }
        }
    }

    private fun isEditTextEmpty(editText: EditText): Boolean {
        return editText.text.toString().trim().isEmpty()
    }

    private fun hasValidationError(): Boolean {
        val nameError = binding.edtNama.isError
        val emailError = binding.edtEmail.isError
        val passwordError = binding.edtKataSandi.isError

        return nameError || emailError || passwordError
    }

    private fun processRegister(){
        val fullName = binding.edtNama.text.toString()
        val email = binding.edtEmail.text.toString()
        val password = binding.edtKataSandi.text.toString()

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener{
                if(it.isSuccessful){
                    val userUpdateProfile = userProfileChangeRequest{
                        displayName = fullName
                    }
                    val user = it.result.user
                    user!!.updateProfile(userUpdateProfile)
                        .addOnCompleteListener {
                            val user = auth.currentUser
                            user?.getIdToken(true)?.addOnSuccessListener { result ->
                                val idToken = result.token
                                Log.d(TAG, "GetTokenResult result = $idToken")

                                CoroutineScope(Dispatchers.IO).launch {
                                    idToken?.let { userPreference.saveSession(UserModel(it, true)) }
                                    val intent = Intent(this@DaftarActivity, UserPreferenceActivity::class.java)
                                    intent.putExtra("ID_TOKEN", idToken)
                                    startActivity(intent)
                                }
                            }
                            startActivity(Intent(this, UserPreferenceActivity::class.java))
                        }
                        .addOnFailureListener {
                            Toast.makeText(this, it.localizedMessage, LENGTH_SHORT).show()
                        }

                }else{
                    Toast.makeText(this,"Registration failed", LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener{
                Toast.makeText(this, it.localizedMessage, LENGTH_SHORT).show()
            }
    }
    private fun showLoading(showLoading: Boolean) {
        binding.progressBar.visibility = if (showLoading) View.VISIBLE else View.GONE

    }
}