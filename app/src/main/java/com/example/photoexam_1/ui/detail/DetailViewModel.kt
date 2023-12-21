package com.example.photoexam_1.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.photoexam_1.data.Repository
import com.example.photoexam_1.data.model.User
import com.example.photoexam_1.data.remote.response.DeleteResponse
import kotlinx.coroutines.launch

class DetailViewModel(private val repository: Repository): ViewModel() {

    val deleteSuccess: LiveData<DeleteResponse> = repository.deleteSuccessfull
    val loading = repository.loading

    fun deletePhoto(token: String, fileId: String) {
        viewModelScope.launch {
            repository.deletePhoto(token,fileId)
        }
    }

    fun getUser(): LiveData<User> {
        return repository.getSession().asLiveData()
    }
}