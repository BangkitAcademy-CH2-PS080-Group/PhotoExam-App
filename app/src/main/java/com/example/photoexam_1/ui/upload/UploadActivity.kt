package com.example.photoexam_1.ui.upload

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import com.example.photoexam_1.R
import com.example.photoexam_1.databinding.ActivityUploadBinding
import com.example.photoexam_1.ui.ViewModelFactory
import com.example.photoexam_1.ui.camera.CameraActivity
import com.example.photoexam_1.ui.main.MainActivity
import com.example.photoexam_1.utils.reduceFileImage
import com.example.photoexam_1.utils.uriToFile
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody

class UploadActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUploadBinding
    private var currentImageUri: Uri? = null
    private var token: String? = null
    private var showSuccess = false
    private val viewModel by viewModels<UploadViewModel>{ ViewModelFactory.getInstance(this) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!allPermissionGranted()) {
            requestPermissionLauncher.launch(REQUIRED_PERMISSION)
        }
        viewModel.getUser().observe(this){user ->
            token = user.token
        }

        binding.btnGallery.setOnClickListener{ startGallery() }
        binding.btnCamera.setOnClickListener { startCameraX() }
        binding.btnSave.setOnClickListener { savePhoto() }
        binding.toolbarUpload.setNavigationOnClickListener { finish() }

        viewModel.loading.observe(this) { showLoading(it) }
        viewModel.uploadSuccess.observe(this) {
            finish()
        }
    }

    private fun uploadAlertDialog() {
        AlertDialog.Builder(this).apply {
            setTitle(R.string.success)
            setMessage(R.string.msgUpload)
            setPositiveButton(R.string.next) { _, _ ->
                val intent = Intent(context, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            }
            show()
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.uploadProgressBar.visibility = if (isLoading) View.VISIBLE else View.INVISIBLE
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    //FUNGSI UPLOAD/SAVE
    private fun savePhoto() {
        if (currentImageUri == null) {
            MaterialAlertDialogBuilder(this)
                .setMessage(R.string.emptyPhoto)
                .setPositiveButton("Ok", null)
                .show()
        } else if (binding.txtDescPhoto.text.toString().isEmpty()){
            MaterialAlertDialogBuilder(this)
                .setMessage(R.string.emptyDesc)
                .setPositiveButton("Ok", null)
                .show()
        } else if (binding.txtStudentName.text.toString().isEmpty()) {
            MaterialAlertDialogBuilder(this)
                .setMessage(R.string.emptyStudentName)
                .setPositiveButton("Ok", null)
                .show()
        } else if (binding.txtAnswerKey.text.toString().isEmpty()) {
            MaterialAlertDialogBuilder(this)
                .setMessage(R.string.emptyAnswerKey)
                .setPositiveButton("Ok", null)
                .show()
        }
        currentImageUri?.let { uri ->
            val imageFile = uriToFile(uri, this).reduceFileImage()
            val description = binding.txtDescPhoto.text.toString()
            val studentName = binding.txtStudentName.text.toString()
            val answerKey = binding.txtAnswerKey.text.toString()

            val descRequestBody = description.toRequestBody("text/plain".toMediaType())
            val studentNameRequestBody = studentName.toRequestBody("text/plain".toMediaType())
            val answerKeyRequestBody = answerKey.toRequestBody("text/plain".toMediaType())
            val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
            val multipartBody = MultipartBody.Part.createFormData(
                "photo",
                imageFile.name,
                requestImageFile
            )
            viewModel.uploadPhoto(token!!, multipartBody, descRequestBody, studentNameRequestBody, answerKeyRequestBody)
            showSuccess = true
        } ?: showToast(getString(R.string.empty_image_warning))
    }

    //FUNGSI CAMERAX
    private fun startCameraX() {
        val intent = Intent(this, CameraActivity::class.java)
        launcherIntentCameraX.launch(intent)
    }

    private fun allPermissionGranted() =
        ContextCompat.checkSelfPermission(
            this,
            REQUIRED_PERMISSION
        ) == PackageManager.PERMISSION_GRANTED

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) {isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(this, "Permission request granted", Toast.LENGTH_LONG).show()
            } else { Toast.makeText(this, "Permission request denied", Toast.LENGTH_LONG).show() }
        }

    private val launcherIntentCameraX =  registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CameraActivity.CAMERAX_RESULT) {
            currentImageUri = it.data?.getStringExtra(CameraActivity.EXTRA_CAMERAX_IMAGE)?.toUri()
            showImage()
        }
    }

    // FUNGSI GALLERY
    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            showImage()
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }

    // SHOW IMAGE
    private fun showImage() {
        currentImageUri?.let {  uri ->
            Log.d("Image URI", "showImage: $uri")
            binding.imgUpload.setImageURI(uri)
        }
    }

    companion object {
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
    }
}