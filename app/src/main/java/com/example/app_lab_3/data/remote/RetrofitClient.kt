package com.example.app_lab_3.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Cliente Singleton para configurar y proveer la instancia de Retrofit.
 */
object RetrofitClient {
    private const val BASE_URL = "https://tu-api-ejemplo.com/api/" // Reemplaza con tu URL real

    val gymApiService: GymApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GymApiService::class.java)
    }
}
