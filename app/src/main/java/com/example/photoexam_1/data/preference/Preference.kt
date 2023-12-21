package com.example.photoexam_1.data.preference


import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.photoexam_1.data.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user")

class Preference private constructor(private val dataStore: DataStore<Preferences>) {

    fun getUser(): Flow<User> {
        return dataStore.data.map { preferences ->
            User(
                preferences[IDUSER] ?: "",
                preferences[EMAIL] ?: "",
                preferences[TOKEN] ?: "",
                preferences[ISLOGIN] ?: false
            )
        }
    }

    suspend fun saveUser(user: User) {
        dataStore.edit { preferences ->
            preferences[IDUSER] = user.userId
            preferences[EMAIL] = user.email
            preferences[TOKEN] = user.token
            preferences[ISLOGIN] = true
        }
    }

    suspend fun logOut() {
        dataStore.edit { preferences -> preferences.clear() }
    }

    companion object {
        private val EMAIL = stringPreferencesKey("name")
        private val TOKEN = stringPreferencesKey("token")
        private val IDUSER = stringPreferencesKey("userId")
        private val ISLOGIN = booleanPreferencesKey("loginStatus")

        @Volatile
        private var INSTANCE: Preference? = null

        fun getInstance(dataStore: DataStore<Preferences>): Preference{
            return INSTANCE ?: synchronized(this) {
                val instance = Preference(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}