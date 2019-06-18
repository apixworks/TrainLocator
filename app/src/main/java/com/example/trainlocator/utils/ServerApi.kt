package com.example.trainlocator.utils

import com.example.trainlocator.models.BookingResponse
import com.example.trainlocator.models.RouteResponse
import com.example.trainlocator.models.UserResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST


interface ServerApi {

    @FormUrlEncoded
    @POST("register/user")
    fun registerUser(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("phone") phone: String
    ): Call<String>

    @FormUrlEncoded
    @POST("login/user")
    fun loginUser(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<UserResponse>

    @FormUrlEncoded
    @POST("booking/user")
    fun userBooking(
        @Field("from") from: String,
        @Field("to") to: String,
        @Field("class") seatClass: String,
        @Field("date") date: String,
        @Field("first_name") first_name: String,
        @Field("last_name") last_name: String,
        @Field("phone") phone: String
    ): Call<BookingResponse>

    @GET("view/routes")
    abstract fun getRoutes(): Call<RouteResponse>
}