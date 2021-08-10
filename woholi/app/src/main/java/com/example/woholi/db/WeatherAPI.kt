package com.example.woholi.db

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class WeatherAPI {
    fun getClient(baseUrl: String): Retrofit? = Retrofit.Builder()
            .baseUrl(baseUrl).client(OkHttpClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
}

object WeatherService {
    private const val apiUrl = "api.openweathermap.org/data/2.5/"

    val client = WeatherAPI().getClient(apiUrl)?.create(WeatherInterface::class.java)
}

}