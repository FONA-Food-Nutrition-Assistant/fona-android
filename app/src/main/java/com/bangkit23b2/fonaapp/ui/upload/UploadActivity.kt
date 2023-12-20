package com.bangkit23b2.fonaapp.ui.upload

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.OrientationEventListener
import android.view.Surface
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.bangkit23b2.fonaapp.databinding.ActivityUploadBinding
import com.bangkit23b2.fonaapp.ui.cart.CartActivity
import com.bangkit23b2.fonaapp.ui.search.SearchFoodActivity
import com.bangkit23b2.fonaapp.utils.ViewModelFactory
import com.bangkit23b2.fonaapp.utils.createCustomTempFile
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody

class UploadActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUploadBinding
    private var imageCapture: ImageCapture? = null
    private lateinit var factory: ViewModelFactory
    private var token = ""
    private var isProcessing = false
    private lateinit var imageMultipart: MultipartBody.Part
    private var cameraSelector: CameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
    private val uploadFoodViewModel: UploadViewModel by viewModels { factory }
    private fun allPermissionsGranted() =
        ContextCompat.checkSelfPermission(
            this,
            UploadActivity.REQUIRED_PERMISSION
        ) == PackageManager.PERMISSION_GRANTED
    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(this, "Permission request granted", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Permission request denied", Toast.LENGTH_LONG).show()
            }
        }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (!allPermissionsGranted()) {
            requestPermissionLauncher.launch(UploadActivity.REQUIRED_PERMISSION)
        }
        setupViewModel()
        setupUser()
        setupAction()
        uploadFoodViewModel.isLoading.observe(this@UploadActivity) {
            showLoading(it)
        }
    }
    private fun setupUser(){

    }
    private fun setupViewModel(){
        factory = ViewModelFactory.getInstance(this)
    }

    private fun setupAction(){
        binding.switchCamera.setOnClickListener {
            cameraSelector =
                if (cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA) CameraSelector.DEFAULT_FRONT_CAMERA
                else CameraSelector.DEFAULT_BACK_CAMERA
            startCamera()
        }
        binding.captureImage.setOnClickListener {
            takePhoto()
        }
        binding.searchFood.setOnClickListener {
            val intent = Intent(this@UploadActivity, SearchFoodActivity::class.java)
            startActivity(intent)
        }
        binding.galleryImage.setOnClickListener { startGallery() }
        //TODO LULU buat logika klo dia mencet logo ic_search, dia intent ke SearchFoodActivity()
    }

    private fun startGallery() {
        launcherGallery.launch("image/jpeg")
    }

    private val launcherGallery = registerForActivityResult(ActivityResultContracts.GetContent()) {
        handleSelectedImage(it)
    }

    private fun handleSelectedImage(uri: Uri?) {
        uri?.let {
            val filePath: String? = uri.path
            if (!filePath.isNullOrEmpty()) {
                val photoFile = createCustomTempFile(application)
                val requestFile = photoFile.asRequestBody("image/jpeg".toMediaType())
                imageMultipart = MultipartBody.Part.createFormData(
                    "image",
                    photoFile.name,
                    requestFile
                )
                uploadFoodViewModel.getSession().observe(this@UploadActivity) { user ->
                    Log.d(TAG,"this function gallery called")
                    token = user.idToken
                    Log.d(TAG,"Your token: ${token}")
                    uploadFoodViewModel.uploadFood(user.idToken, imageMultipart)
                    uploadFoodViewModel.isError.observe(this@UploadActivity){
                        if(it){
                            Toast.makeText(this@UploadActivity, "Internal server error", Toast.LENGTH_LONG).show()
                        }

                    }
                    uploadFoodViewModel.uploadFoodResponse.observe(this@UploadActivity) {
                        Log.d(TAG,"Gambar gallery terkirim")
                        Toast.makeText(this@UploadActivity, "Gambar berhasil terkirim", Toast.LENGTH_LONG).show()
                        val uploadFoodResponse = it

                        val intent = Intent(this@UploadActivity, CartActivity::class.java)

                        intent.putExtra("UPLOAD_RESPONSE", uploadFoodResponse)

                        startActivity(intent)

                        finish()
                    }
                }
            }
        }
    }

    private fun takePhoto() {

        val imageCapture = imageCapture ?: return

        if (!isProcessing) {
            isProcessing = true
            val photoFile = createCustomTempFile(application)
            val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()
            val requestFile = photoFile.asRequestBody("image/jpeg".toMediaType())
            imageMultipart = MultipartBody.Part.createFormData(
                "image",
                photoFile.name,
                requestFile
            )

            imageCapture.takePicture(

                outputOptions,
                ContextCompat.getMainExecutor(this),
                object : ImageCapture.OnImageSavedCallback {
                    override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                        uploadFoodViewModel.getSession().observe(this@UploadActivity) { user ->
                            Log.d(TAG, "this function called")
                            token = user.idToken
                            Log.d(TAG, "Your token: ${token}")
                            uploadFoodViewModel.uploadFood(user.idToken, imageMultipart)
                            uploadFoodViewModel.uploadFoodResponse.observe(this@UploadActivity) {
                                Log.d(TAG, "Gambar terkirim")
                                val uploadFoodResponse = it

                                val intent = Intent(this@UploadActivity, CartActivity::class.java)

                                intent.putExtra("UPLOAD_RESPONSE", uploadFoodResponse)

                                startActivity(intent)

                                finish()
                            }
                            uploadFoodViewModel.isError.observe(this@UploadActivity) {
                                if (it) {
                                    Toast.makeText(
                                        this@UploadActivity,
                                        "Internal server error",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }

                            }
                        }
                    }

                    override fun onError(exc: ImageCaptureException) {
                        Toast.makeText(
                            this@UploadActivity,
                            "Gagal mengambil gambar.",
                            Toast.LENGTH_SHORT
                        ).show()
                        Log.e(TAG, "onError: ${exc.message}")
                    }
                }
            )
        }

    }

    private fun moveActivity() {
        val intent = Intent(this@UploadActivity, CartActivity::class.java)
        startActivity(intent)
        finish()
    }
    private val orientationEventListener by lazy {
        object : OrientationEventListener(this) {
            override fun onOrientationChanged(orientation: Int) {
                if (orientation == ORIENTATION_UNKNOWN) {
                    return
                }
                val rotation = when (orientation) {
                    in 45 until 135 -> Surface.ROTATION_270
                    in 135 until 225 -> Surface.ROTATION_180
                    in 225 until 315 -> Surface.ROTATION_90
                    else -> Surface.ROTATION_0
                }
                imageCapture?.targetRotation = rotation
            }
        }
    }

    public override fun onResume() {
        super.onResume()
        hideSystemUI()
        startCamera()
    }

    override fun onStart() {
        super.onStart()
        orientationEventListener.enable()
    }

    override fun onStop() {
        super.onStop()
        orientationEventListener.disable()
    }

    private fun hideSystemUI() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.previewView.surfaceProvider)
                }
            imageCapture = ImageCapture.Builder().build()

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    this,
                    cameraSelector,
                    preview,
                    imageCapture
                )
            } catch (exc: Exception) {
                Toast.makeText(
                    this@UploadActivity,
                    "Gagal memunculkan kamera.",
                    Toast.LENGTH_SHORT
                ).show()
                Log.e(TAG, "startCamera: ${exc.message}")
            }
        }, ContextCompat.getMainExecutor(this))
    }

    companion object {
        private const val TAG = "UploadActivity"
        const val EXTRA_CAMERAX_IMAGE = "CameraX Image"
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
        const val CAMERAX_RESULT = 200
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}