package com.example.photoexam_1.data.model

data class User(
    val userId: String,
    val email: String,
    val token: String,
    val isLogin: Boolean = false
)
