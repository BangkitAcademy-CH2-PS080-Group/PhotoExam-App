	package com.example.photoexam_1.data.remote.response

import androidx.room.Entity
import com.google.gson.annotations.SerializedName

data class PhotoEssayResponse(

	@field:SerializedName("data")
	val data: List<DataItem> = emptyList(),

	@field:SerializedName("message")
	val message: String? = null
)

data class DataItem(

	@field:SerializedName("createdAt")
	val createdAt: String? = null,

	@field:SerializedName("answerKey")
	val answerKey: String? = null,

	@field:SerializedName("score")
	val score: Int? = null,

	@field:SerializedName("fileName")
	val fileName: String? = null,

	@field:SerializedName("studentName")
	val studentName: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("studentAnswer")
	val studentAnswer: String? = null,

	@field:SerializedName("storageUrl")
	val storageUrl: String? = null,

	@field:SerializedName("fileId")
	val fileId: String? = null
)
