package com.example.mobwebhf.network

import com.example.mobwebhf.data.api.StockData
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetworkManager {
    private val retrofit: Retrofit
    private val stockApi: StockApi

    private const val SERVICE_URL = "https://freecurrencyapi.net/api/v2/"
    private const val API_KEY = "d4cf0060-510b-11ec-98fa-715ecd1e0db9"

    init{
        retrofit = Retrofit.Builder()
            .baseUrl(SERVICE_URL)
            .client(OkHttpClient.Builder().build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        stockApi = retrofit.create(StockApi::class.java)
    }

    fun getStock(base: String?): Call<StockData?>?{
        return stockApi.getStock(API_KEY, base)
    }
}