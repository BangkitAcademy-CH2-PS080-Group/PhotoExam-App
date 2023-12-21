package com.example.photoexam_1.di

import android.content.Context
import com.example.photoexam_1.data.Repository
import com.example.photoexam_1.data.preference.Preference
import com.example.photoexam_1.data.preference.dataStore
import com.example.photoexam_1.data.remote.retrofit.ApiConfig
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideRepository(context: Context): Repository {
        val preferences = Preference.getInstance(context.dataStore)
        val user = runBlocking { preferences.getUser().first() }
        val apiService= ApiConfig.getApiService(user.token)

        return Repository.getInstance(apiService,preferences)
    }
}