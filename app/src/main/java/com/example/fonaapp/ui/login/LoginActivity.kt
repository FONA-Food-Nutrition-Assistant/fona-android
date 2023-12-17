package com.example.fonaapp.ui.login

import android.app.ProgressDialog
import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import com.example.fonaapp.R
import com.example.fonaapp.data.models.UserModel
import com.example.fonaapp.data.models.UserPreference
import com.example.fonaapp.databinding.ActivityLoginBinding
import com.example.fonaapp.main.MainActivity
import com.example.fonaapp.ui.preferences.UserPreferenceActivity
import com.example.fonaapp.utils.Injection
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var googleSignInClient: GoogleSignInClient
    private val RC_SIGN_IN: Int = 1
    private lateinit var auth: FirebaseAuth
    private lateinit var progressDialog: ProgressDialog
    private lateinit var userPreference: UserPreference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupView()
        setupAction()
    }
    private fun setupView(){
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Login")
        progressDialog.setMessage("Silakan tunggu...")
        auth = FirebaseAuth.getInstance()
        // Configure Google Sign In
        val gso = GoogleSignInOptions
            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)
        // Initialize Firebase Auth
        auth = Firebase.auth
        binding = ActivityLoginBinding.inflate(layoutInflater)

        userPreference = Injection.provideUserPreference(this)
        setContentView(binding.root)
    }

    private fun setupAction(){
        binding.btnMasuk.setOnClickListener {
            if(binding.edtEmail.text.isNotEmpty() && binding.edtKataSandi.text.isNotEmpty()){
                    //LAUNCH REGISTER
                    loginWithEmailPassword()
            } else{
                Toast.makeText(this, "Silakan isi semua data terlebih dahulu!!", LENGTH_SHORT).show()
            }
        }
        binding.btnMasukGoogle.setOnClickListener {
            signIn()
        }
        binding.backAction.setOnClickListener {
            onBackPressed()
        }
    }



    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            val exception=task.exception

            try {
                // Google Sign In was successful, authenticate with Firebase
                val idToken = task.getResult(ApiException::class.java)!!
                firebaseAuthWithGoogle(idToken)

            }
            catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
//                Toast.makeText(this, "Login Failed 1", Toast.LENGTH_SHORT)
//                    .show()
            }
        }
    }

    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)



        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {

                    Log.d(TAG, "signInWithCredential:success")
                    val user = auth.currentUser
                    user?.getIdToken(true)?.addOnSuccessListener { result ->
                        val idToken = result.token
                        Log.d(TAG, "GetTokenResult result = $idToken")

                        CoroutineScope(Dispatchers.IO).launch {

                            idToken?.let { userPreference.saveSession(UserModel(it, true)) }
                            updateUI(user)
                            val intent = Intent(this@LoginActivity, MainActivity::class.java)
                            intent.putExtra("ID_TOKEN", idToken)
                            startActivity(intent)
                        }


                    }
                } else {

                    Toast.makeText(this@LoginActivity, "Login Failed: ", Toast.LENGTH_SHORT).show()
                    updateUI(null)
                }
            }
    }

    private fun loginWithEmailPassword(){
        val email = binding.edtEmail.text.toString().trim { it <= ' ' }
        val password = binding.edtKataSandi.text.toString().trim { it <= ' ' }
        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                val user = auth.currentUser
                user?.getIdToken(true)?.addOnSuccessListener { result ->
                    val idToken = result.token
                    Log.d(TAG, "GetTokenResult result = $idToken")

                    CoroutineScope(Dispatchers.IO).launch {

                        idToken?.let { userPreference.saveSession(UserModel(it, true)) }
                        updateUI(user)
                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        intent.putExtra("ID_TOKEN", idToken)
                        startActivity(intent)
                    }
                }
                startActivity(Intent(this,MainActivity::class.java))
            }
            .addOnFailureListener {
                Toast.makeText(this, it.localizedMessage, LENGTH_SHORT).show()
            }
            .addOnCompleteListener {
                progressDialog.dismiss()
                if (it.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    val user = auth.currentUser
                    updateUI(user)

                    val intent= Intent(this,MainActivity::class.java)
                    startActivity(intent)
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(this@LoginActivity, "Login Failed: ", Toast.LENGTH_SHORT).show()
                    updateUI(null)
                }
            }
    }
    private fun updateUI(currentUser: FirebaseUser?) {
        if (currentUser != null){
            startActivity(Intent(this@LoginActivity, UserPreferenceActivity::class.java))
            finish()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }


    companion object {
        private const val TAG = "LoginActivity"
    }
}