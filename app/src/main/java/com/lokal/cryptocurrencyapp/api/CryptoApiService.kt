package com.lokal.cryptocurrencyapp.api

import com.lokal.cryptocurrencyapp.model.CurrencyListResponse
import com.lokal.cryptocurrencyapp.model.LiveRatesResponse
import retrofit2.Response
import retrofit2.http.GET

interface CryptoApiService {

    @GET("/live?access_key=6c4770477c9f95f8a2668ff48b3e34ce")
    suspend fun getLiveRates(): Response<LiveRatesResponse>

    @GET("/list?access_key=6c4770477c9f95f8a2668ff48b3e34ce")
    suspend fun getCurrencies(): Response<CurrencyListResponse>
}