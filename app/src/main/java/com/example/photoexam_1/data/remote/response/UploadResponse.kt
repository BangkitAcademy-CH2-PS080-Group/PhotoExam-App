package com.example.photoexam_1.data.remote.response

import com.google.gson.annotations.SerializedName

data class UploadResponse(

	@field:SerializedName("message")
	val message: String? = null
)
