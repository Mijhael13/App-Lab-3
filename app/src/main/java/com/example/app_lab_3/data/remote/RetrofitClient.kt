package com.example.app_lab_3.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Cliente Singleton para configurar y proveer la instancia de Retrofit.
 */
object RetrofitClient {
    // Para Emulador de Android Studio, 10.0.2.2 apunta al localhost de la PC:
    private const val BASE_URL = "http://10.0.2.2:3000/api/"

    // Para dispositivo físico conectado a la misma red Wi-Fi, descomenta y pon tu IP local:
    // private const val BASE_URL = "http://192.168.1.XX:3000/api/"

    val gymApiService: GymApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GymApiService::class.java)
    }
}
