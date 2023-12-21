package com.example.photoexam_1.ui.detail

import android.app.Dialog
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
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
        with(binding) {
            txtAnswerKey.text = detailPhoto?.answerKey
            txtScore.text = resources.getString(R.string.score, detailPhoto?.score?.toInt().toString())
            txtStudentName.text = detailPhoto?.studentName
            txtDescDetail.text = detailPhoto?.description
            txtStudentAnswer.text = detailPhoto?.studentAnswer
            txtFileDate.text = detailPhoto?.createdAt?.formattedDate()
            Glide.with(applicationContext)
                .load(detailPhoto?.storageUrl)
                .into(imageDetail)
        }
        binding.btnDelete.setOnClickListener {
            detailPhoto?.fileId?.let { fileId ->
                token?.let { viewModel.deletePhoto(it, fileId) }
            }
        }
        binding.toolbarDetail.setNavigationOnClickListener { finish() }

        viewModel.deleteSuccess.observe(this) {
            finish()
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    companion object {
        const val EXTRA_DETAIL_STORY = "extra_detail_story"
    }
}