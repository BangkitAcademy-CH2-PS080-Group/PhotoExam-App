package com.example.photoexam_1.data

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.photoexam_1.data.model.User
import com.example.photoexam_1.data.preference.Preference
import com.example.photoexam_1.data.remote.response.LoginResponse
import com.example.photoexam_1.data.remote.response.PhotoEssayResponse
import com.example.photoexam_1.data.remote.response.RegisResponse
import com.example.photoexam_1.data.remote.response.UploadResponse
import com.example.photoexam_1.data.remote.retrofit.ApiConfig
import com.example.photoexam_1.data.remote.retrofit.ApiService
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody

class Repository(
    private val apiService: ApiService,
    private val preference: Preference
) {

    val loading = MutableLiveData<Boolean>()
    val registerSuccessfull = MutableLiveData<RegisResponse>()
    val loginSuccessfull = MutableLiveData<LoginResponse>()
    val uploadSuccessfull = MutableLiveData<UploadResponse>()
    val photoSuccessfull = MutableLiveData<PhotoEssayResponse>()

    suspend fun register(email: String, password: String) {
        loading.value = true
        try {
            val regisSuccess = apiService.register(email, password)
            loading.value = false
            registerSuccessfull.value = regisSuccess
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("Repository Register", "Error : $e")
            loading.value = false
        }
    }

    suspend fun login(email: String, password: String){
        loading.value = true
        try {
            val apiLogin = apiService.login(email, password)

            val loginSuccess = apiLogin.data
            val userId = loginSuccess?.userId
            val email = loginSuccess?.email
            val token = loginSuccess?.token

            val userIn = User(userId!!, email!!, token!!)
            preference.saveUser(userIn)
            loading.value = false
            loginSuccessfull.value = apiLogin
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("Repository Login", "Error : $e")
            loading.value = false
        }
    }

    suspend fun saveUser(user: User) {
        preference.saveUser(user)
    }

    fun getSession(): Flow<User> {
        return preference.getUser()
    }

    suspend fun logOut(){
        preference.logOut()
    }

    suspend fun getAllPhoto(token: String) {
        loading.value = true
        try {
            val getPhotoSuccess= ApiConfig.getApiService(token).getAllPhoto()
            loading.value = false
            photoSuccessfull.value = getPhotoSuccess
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("Repository Get Photo", "Error : $e")
            loading.value = false
        }
    }

    suspend fun uploadPhoto(
        token: String,
        multipartBody: MultipartBody.Part,
        description: RequestBody,
        studentName: RequestBody,
        answerKey: RequestBody
    ) {
        loading.value = true
        try {
            val uploadSuccess = ApiConfig.getApiService(token).uploadPhoto(multipartBody, description, studentName, answerKey)
            loading.value = false
            uploadSuccessfull.value = uploadSuccess
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("Repository Upload", "Error : $e")
            loading.value = false
        }
    }

    companion object {
        @Volatile
        private var instance: Repository? = null

        fun getInstance(
            apiService: ApiService,
            preference: Preference
        ): Repository =
            instance ?: synchronized(this) {
                instance ?: Repository(apiService, preference)
            }.also { instance = it }
    }
}