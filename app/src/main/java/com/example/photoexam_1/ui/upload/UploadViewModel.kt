package com.example.photoexam_1.ui.upload

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.photoexam_1.data.Repository
import com.example.photoexam_1.data.model.User
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody

class UploadViewModel(private val repository: Repository) : ViewModel() {

    val loading = repository.loading
    val uploadSuccess = repository.uploadSuccessfull
    val errorUpload = repository.errorUpload

    fun uploadPhoto(
        token: String,
        multipartBody: MultipartBody.Part,
        description: RequestBody,
        studentName: RequestBody,
        answerKey: RequestBody) {
        viewModelScope.launch {
            repository.uploadPhoto(token, multipartBody,description, studentName, answerKey)
        }
    }

    fun getUser(): LiveData<User> {
        return repository.getSession().asLiveData()
    }
}