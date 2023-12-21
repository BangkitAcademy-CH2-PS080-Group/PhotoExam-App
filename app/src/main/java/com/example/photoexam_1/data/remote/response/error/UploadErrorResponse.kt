package com.example.photoexam_1.data.remote.response.error

import com.google.gson.annotations.SerializedName

data class UploadErrorResponse(

	@field:SerializedName("error")
	val error: List<ErrorUploadItem?>? = null
)

data class ErrorUploadItem(

	@field:SerializedName("code")
	val code: String? = null,

	@field:SerializedName("message")
	val message: String? = null
)
