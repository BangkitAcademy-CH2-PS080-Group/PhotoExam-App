package com.example.photoexam_1.data.remote.response.error

import com.google.gson.annotations.SerializedName

data class ErrorLoginResponse(

	@field:SerializedName("error")
	val error: List<LoginErrorResponse?>? = null
)

data class LoginErrorResponse(

	@field:SerializedName("code")
	val code: String? = null,

	@field:SerializedName("message")
	val message: String? = null
)
