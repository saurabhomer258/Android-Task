package com.example.core_network

import com.anz.core.model.User
import retrofit2.http.GET


interface UsersApi {
    @GET("users")
    suspend fun getUsers(): List<User>
}