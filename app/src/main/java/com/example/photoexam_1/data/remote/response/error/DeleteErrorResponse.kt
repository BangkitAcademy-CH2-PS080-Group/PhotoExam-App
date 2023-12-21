package com.example.photoexam_1.data.remote.response.error

import com.google.gson.annotations.SerializedName

data class DeleteErrorResponse(

	@field:SerializedName("error")
	val error: List<ErrorItem?>? = null
)

data class ErrorItem(

	@field:SerializedName("code")
	val code: String? = null,

	@field:SerializedName("message")
	val message: String? = null
)
