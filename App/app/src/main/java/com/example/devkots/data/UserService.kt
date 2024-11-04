package com.example.devkots.data

import com.example.devkots.model.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface UserService {
    @GET("users")
    suspend fun getUsersByEmail(
        @Query("mail") email: String
    ): Response<List<User>>

    @POST("users")
    suspend fun registerUser(@Body user: User): Response<User>

    @PUT("users/{userId}")
    suspend fun updateUser(
        @Path("userId") userId: Int,
        @Body user: User
    ): Response<User>
}