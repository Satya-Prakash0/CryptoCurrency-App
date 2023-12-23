package com.lokal.cryptocurrencyapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lokal.cryptocurrencyapp.model.CurrencyListResponse
import com.lokal.cryptocurrencyapp.model.LiveRatesResponse
import com.lokal.cryptocurrencyapp.repository.CurrencyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: CurrencyRepository):ViewModel() {

    private val _dataFetchComplete = MutableLiveData<Unit>()
    val dataFetchComplete: LiveData<Unit>
        get() = _dataFetchComplete

    fun fetchData() {

        viewModelScope.launch(Dispatchers.IO) {
            repository.getCurrencies()
        }
        viewModelScope.launch(Dispatchers.IO) {
            repository.getLiveRates()
        }
        // Notify the UI that data fetch is complete
        _dataFetchComplete.postValue(Unit)
    }

    val cryptoCurrency:LiveData<CurrencyListResponse>
    get() = repository.currencies

    val liveRate:LiveData<LiveRatesResponse>
    get() = repository.liveRate

}