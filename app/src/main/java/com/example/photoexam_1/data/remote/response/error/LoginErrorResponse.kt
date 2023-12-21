package com.example.photoexam_1.data.remote.response.error

import com.google.gson.annotations.SerializedName

data class ErrorLoginResponse(

	@field:SerializedName("error")
	val error: List<ErrorLoginItem?>? = null
)

data class ErrorLoginItem(

	@field:SerializedName("code")
	val code: String? = null,

	@field:SerializedName("message")
	val message: String? = null
)
