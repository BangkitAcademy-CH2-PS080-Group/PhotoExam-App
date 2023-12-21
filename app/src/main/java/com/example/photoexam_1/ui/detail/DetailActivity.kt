package com.example.photoexam_1.ui.detail

import android.app.Dialog
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.photoexam_1.R
import com.example.photoexam_1.data.model.PhotoEssay
import com.example.photoexam_1.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val detailPhoto = if(Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra(EXTRA_DETAIL_STORY, PhotoEssay::class.java)
        } else  {
            @Suppress("DEPRECATTION")
            intent.getParcelableExtra(EXTRA_DETAIL_STORY)
        }
        with(binding) {
            Glide.with(applicationContext)
                .load(detailPhoto?.storageUrl)
                .into(imageDetail)
            txtStudentName.text = detailPhoto?.studentName
            txtDescDetail.text = detailPhoto?.description
            txtAnswerKey.text = detailPhoto?.answerKey
            txtStudentAnswer.text = detailPhoto?.studentAnswer
            txtScore.text = resources.getString(R.string.score, detailPhoto?.score?.toInt().toString())
        }
        binding.imageDetail.setOnClickListener { showFullScreenImage(detailPhoto?.storageUrl) }
        binding.toolbarDetail.setNavigationOnClickListener { finish() }
    }

    private fun showFullScreenImage(imageUrl: String?) {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        dialog.setContentView(R.layout.dialog_fullscreen_image)
        val imageView = dialog.findViewById<ImageView>(R.id.imageDetail)
        Glide.with(this)
            .load(imageUrl)
            .into(imageView!!)
        dialog.show()
    }

    companion object {
        const val EXTRA_DETAIL_STORY = "extra_detail_story"
    }
}