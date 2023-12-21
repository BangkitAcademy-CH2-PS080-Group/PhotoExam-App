package com.example.photoexam_1.ui.authentication

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.photoexam_1.data.Repository
import com.example.photoexam_1.data.model.User
import kotlinx.coroutines.launch

class AuthViewModel(private val repository: Repository): ViewModel() {

    val loginSuccess = repository.loginSuccessfull
    val regisSuccess = repository.registerSuccessfull
    val loading = repository.loading

    fun getUser(): LiveData<User> {
        return repository.getSession().asLiveData()
    }

    fun register(email: String, password: String) {
        viewModelScope.launch { repository.register(email, password) }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch{ repository.login(email, password) }
    }
}