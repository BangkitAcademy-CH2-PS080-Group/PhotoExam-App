package com.example.photoexam_1.data.remote.response.error

import com.google.gson.annotations.SerializedName

data class ErrorUploadItem(

	@field:SerializedName("error")
	val error: List<UploadErrorResponse?>? = null
)

data class UploadErrorResponse(

	@field:SerializedName("code")
	val code: String? = null,

	@field:SerializedName("message")
	val message: String? = null
)
