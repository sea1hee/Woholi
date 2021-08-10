package com.example.woholi.db

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ExchangeRateAPI {
    fun getClient(baseUrl: String): Retrofit? = Retrofit.Builder()
            .baseUrl(baseUrl).client(OkHttpClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
}

object ExchangeRateService {
    private const val apiUrl = "https://www.koreaexim.go.kr/site/program/financial/"

    val client = ExchangeRateAPI().getClient(apiUrl)?.create(ExchangeRateInterface::class.java)
}