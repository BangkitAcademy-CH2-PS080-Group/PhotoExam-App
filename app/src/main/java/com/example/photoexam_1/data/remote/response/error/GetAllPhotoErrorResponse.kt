package com.example.photoexam_1.data.remote.response.error

import com.google.gson.annotations.SerializedName

data class Error(

	@field:SerializedName("error")
	val error: Error? = null
)

data class GetAllPhotoErrorResponse(

	@field:SerializedName("code")
	val code: String? = null,

	@field:SerializedName("message")
	val message: String? = null
)
