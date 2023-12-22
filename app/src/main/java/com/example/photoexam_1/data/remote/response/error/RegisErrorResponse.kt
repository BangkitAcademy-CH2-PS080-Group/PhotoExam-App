package com.example.photoexam_1.data.remote.response.error

import com.google.gson.annotations.SerializedName

data class ErrorRegisItem(

	@field:SerializedName("error")
	val error: List<RegisErrorResponse?>? = null
)

data class RegisErrorResponse(

	@field:SerializedName("code")
	val code: String? = null,

	@field:SerializedName("message")
	val message: String? = null
)
