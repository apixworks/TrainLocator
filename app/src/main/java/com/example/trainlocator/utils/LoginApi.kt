package com.example.trainlocator.utils

import com.example.trainlocator.models.UserResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST


interface LoginApi {

    @FormUrlEncoded
    @POST("register/user")
    fun registerUser(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<String>

    @FormUrlEncoded
    @POST("login/user")
    fun loginUser(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<UserResponse>
}