package com.example.uploader.network

import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @Multipart
    @POST("/upload")
    suspend fun uploadMedia(@Part media: List<MultipartBody.Part>): Response<Map<String, List<String>>>

    @GET("/media")
    suspend fun getMediaList(): Response<List<String>>

    @POST("/delete")
    suspend fun deleteMedia(@Body files: Map<String, List<String>>): Response<Map<String, List<String>>>
}
