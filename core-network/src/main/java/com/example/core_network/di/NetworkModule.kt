package com.example.core_network.di

import com.example.core_network.UsersApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import javax.inject.Singleton
import okhttp3.MediaType.Companion.toMediaType


@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    
    
    private const val BASE_URL = "https://fake-json-api.mock.beeceptor.com/"
    
    
    @Provides @Singleton
    fun provideJson(): Json = Json { ignoreUnknownKeys = true }
    
    
    @Provides @Singleton
    fun provideOkHttp(): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY })
        .build()
    
    
    @Provides @Singleton
    fun provideRetrofit(json: Json, client: OkHttpClient): Retrofit {
        val contentType = "application/json".toMediaType()
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
           //.addConverterFactory(json.asConverterFactory(contentType))
            .client(client)
            .build()
    }
    
    
    @Provides @Singleton
    fun provideUsersApi(retrofit: Retrofit): UsersApi = retrofit.create(UsersApi::class.java)
}