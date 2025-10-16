package com.example.core_network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private val logging = HttpLoggingInterceptor().apply {
        setLevel(HttpLoggingInterceptor.Level.BODY)
    }
    
    private val client = OkHttpClient.Builder()
        .addInterceptor(logging)
        .build()
    
    val api: UsersApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://fake-json-api.mock.beeceptor.com/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(UsersApi::class.java)
    }
}