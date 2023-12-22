package com.example.photoexam_1.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PhotoEssay(
    val createdAt: String,
    val answerKey: String,
    val score: Int,
    val fileName: String,
    val studentName: String,
    val description: String,
    val studentAnswer: String,
    val storageUrl: String,
    val fileId: String
): Parcelable
