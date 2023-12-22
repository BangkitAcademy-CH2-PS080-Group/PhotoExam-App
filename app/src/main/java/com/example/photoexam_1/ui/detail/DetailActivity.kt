package com.example.photoexam_1.ui.detail

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.photoexam_1.R
import com.example.photoexam_1.data.model.PhotoEssay
import com.example.photoexam_1.databinding.ActivityDetailBinding
import com.example.photoexam_1.ui.ViewModelFactory
import com.example.photoexam_1.ui.authentication.AuthViewModel
import com.example.photoexam_1.ui.main.MainActivity
import com.example.photoexam_1.utils.formattedDate
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private val viewModel by viewModels<DetailViewModel> { ViewModelFactory.getInstance(this) }
    private var token: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.getUser().observe(this) {
            token = it?.token
        }

        val detailPhoto = if(Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra(EXTRA_DETAIL_STORY, PhotoEssay::class.java)
        } else  {
            @Suppress("DEPRECATTION")
            intent.getParcelableExtra(EXTRA_DETAIL_STORY)
        }
        val score = detailPhoto?.score
        with(binding) {
            txtAnswerKey.text = detailPhoto?.answerKey
            txtScore.text = resources.getString(R.string.score, detailPhoto?.score?.toInt().toString())
            txtStudentName.text = detailPhoto?.studentName
            txtDescDetail.text = detailPhoto?.description
            txtStudentAnswer.text = detailPhoto?.studentAnswer
            if (score == 1) {
                txtResult.text = resources.getString(R.string.correct)
            } else if (score == 0) { txtResult.text = resources.getString(R.string.wrong) }
            Glide.with(applicationContext)
                .load(detailPhoto?.storageUrl)
                .into(imageDetail)
        }
        binding.btnDelete.setOnClickListener {
            AlertDialog.Builder(this).apply {
                setTitle(R.string.deleteTitle)
                setMessage(R.string.deleteMsg)
                setNegativeButton(R.string.no, null)
                setPositiveButton(R.string.yes) { _, _ ->
                    detailPhoto?.fileId?.let { fileId ->
                        token?.let { viewModel.deletePhoto(it, fileId) }
                        viewModel.getAllPhoto(token!!)
                    }
                    val intent = Intent(context, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                }
                show()
            }
        }
        binding.toolbarDetail.setNavigationOnClickListener { finish() }

        viewModel.loading.observe(this) {
            showLoading(it)
        }

        viewModel.deleteSuccess.observe(this) {
            finish()
        }

        viewModel.errorDelete.observe(this) {
            it.let { errorResponse ->
                Toast.makeText(this, errorResponse.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.detailProgressBar.visibility = if (isLoading) View.VISIBLE else View.INVISIBLE
    }

    companion object {
        const val EXTRA_DETAIL_STORY = "extra_detail_story"
    }
}