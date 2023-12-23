package com.lokal.cryptocurrencyapp.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.lokal.cryptocurrencyapp.api.CryptoApiService
import com.lokal.cryptocurrencyapp.model.CurrencyListResponse
import com.lokal.cryptocurrencyapp.model.LiveRatesResponse
import javax.inject.Inject

class CurrencyRepository @Inject constructor(private val apiService: CryptoApiService) {

    private val currenciesLiveData = MutableLiveData<CurrencyListResponse>()
    private val liveRateData=MutableLiveData<LiveRatesResponse>()

    val currencies:LiveData<CurrencyListResponse>
    get() = currenciesLiveData

    val liveRate:LiveData<LiveRatesResponse>
    get() = liveRateData

    suspend fun getCurrencies(){
        val result=apiService.getCurrencies()
        if(result.body() !=null){
            currenciesLiveData.postValue(result.body())
        }
    }

    suspend fun getLiveRates(){
        val result=apiService.getLiveRates()
        if(result.body() !=null){
            liveRateData.postValue(result.body())
        }
    }

}
