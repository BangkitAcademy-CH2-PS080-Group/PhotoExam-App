package com.example.photoexam_1.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PhotoEssay(
    val createdAt: String,
    val answerKey: String? = null,
    val score: Int? = null,
    val fileName: String,
    val studentName: String,
    val description: String,
    val studentAnswer: String? = null,
    val storageUrl: String,
    val fileId: String
): Parcelable
