package com.example.photoexam_1.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.photoexam_1.data.Repository
import com.example.photoexam_1.data.model.User
import kotlinx.coroutines.launch

class MainViewModel (private val repository: Repository) : ViewModel() {

    val photoSuccess = repository.photoSuccessfull
    val loading = repository.loading
    val errorGetPhoto = repository.errorGet

    fun logOut() {
        viewModelScope.launch {
            repository.logOut()
        }
    }

    fun getUser(): LiveData<User> {
        return repository.getSession().asLiveData()
    }

    fun getAllPhoto(token: String){
        viewModelScope.launch {
            repository.getAllPhoto(token)
        }
    }
}