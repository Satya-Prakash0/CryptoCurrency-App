package com.lokal.cryptocurrencyapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class CryptoCurrencyApplication: Application() {

    override fun onCreate() {
        super.onCreate()
    }
}