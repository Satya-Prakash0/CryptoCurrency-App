package com.lokal.cryptocurrencyapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.lokal.cryptocurrencyapp.repository.CurrencyRepository


class MainViewModelFactory(private val repository: CurrencyRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel(repository) as T
    }
}