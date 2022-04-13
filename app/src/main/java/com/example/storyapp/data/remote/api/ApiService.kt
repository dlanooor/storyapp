package com.example.storyapp.data.remote.api

import com.example.storyapp.data.remote.pojo.AddNewStory
import com.example.storyapp.data.remote.pojo.GetAllStories
import com.example.storyapp.data.remote.pojo.Login
import com.example.storyapp.data.remote.pojo.Register
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @FormUrlEncoded
    @POST("register")
    fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<Register>

    @FormUrlEncoded
    @POST("login")
    fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<Login>

    @Multipart
    @POST("stories")
    fun uploadStories(
        @Header("Authorization") token: String,
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody
    ): Call<AddNewStory>

    @GET("stories")
    fun getAllStories(
        @Header("Authorization") token: String
    ): Call<GetAllStories>
}