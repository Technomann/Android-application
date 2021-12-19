package com.example.mobwebhf.network

import com.example.mobwebhf.data.api.StockData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface StockApi {
    @GET("latest")
    fun getStock(
        @Query("apikey") apikey: String?,
        @Query("base_currency") base_currency: String?
    ): Call<StockData?>?
}