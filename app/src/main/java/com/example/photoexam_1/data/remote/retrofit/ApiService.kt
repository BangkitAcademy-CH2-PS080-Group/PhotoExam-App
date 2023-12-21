package com.example.photoexam_1.data.remote.retrofit

import com.example.photoexam_1.data.remote.response.DeleteResponse
import com.example.photoexam_1.data.remote.response.LoginResponse
import com.example.photoexam_1.data.remote.response.PhotoEssayResponse
import com.example.photoexam_1.data.remote.response.RegisResponse
import com.example.photoexam_1.data.remote.response.UploadResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface ApiService {

    @FormUrlEncoded
    @POST("api/user/register")
    suspend fun register(
        @Field("email") email: String,
        @Field("password") password: String
    ): RegisResponse

    @FormUrlEncoded
    @POST("api/user/login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): LoginResponse

    @GET("api/files")
    suspend fun getAllPhoto(): PhotoEssayResponse

    @Multipart
    @POST("api/files")
    suspend fun uploadPhoto(
        @Part documents: MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Part("studentName") studentName: RequestBody,
        @Part("keyAnswer") answerKey: RequestBody
    ): UploadResponse

    @DELETE("api/files/{fileId}")
    suspend fun deletePhoto(
        @Path("fileId") fileId: String
    ): DeleteResponse
}