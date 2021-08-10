package com.example.woholi.db

import com.example.woholi.db.exchange.ExchangeRate
import retrofit2.http.GET
import retrofit2.http.Query

interface ExchangeRateInterface {
    @GET("exchangeJSON")
    suspend fun getExchangeRate(
            @Query("authkey") authkey: String,
            @Query("data") data: String = "AP01"
    ): ExchangeRate
}