package com.example.photoexam_1.data.remote.response.error

import com.google.gson.annotations.SerializedName

data class ErrorItem(

	@field:SerializedName("error")
	val error: List<DeleteErrorResponse?>? = null
)

data class DeleteErrorResponse(

	@field:SerializedName("code")
	val code: String? = null,

	@field:SerializedName("message")
	val message: String? = null
)
