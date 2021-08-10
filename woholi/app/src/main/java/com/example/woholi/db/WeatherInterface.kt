package com.example.woholi.db

import com.example.woholi.db.weather.Weather
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherInterface {

    @GET("weather")
    suspend fun getweather(
        @Query("q") q: String,
        @Query("appid") appid: String,
        @Query("units") units: String = "metric"
    ): Weather
}