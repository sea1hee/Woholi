package com.example.woholi.db

import com.example.woholi.db.weather.Weather
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherInterface {

    @GET("weather")
    fun getweather(
        @Query("q") q: String,
        @Query("appid") appid: String
    ): Weather
}