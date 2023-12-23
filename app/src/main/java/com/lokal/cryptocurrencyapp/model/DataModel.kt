package com.lokal.cryptocurrencyapp.model

data class Currency(val code: String, val fullName: String, val iconUrl: String, var exchangeRate: Double)

data class CurrencyData(val name: String, val icon_url: String)

data class LiveRatesResponse(val success: Boolean, val terms: String, val privacy: String, val timestamp: Long, val target: String, val rates: Map<String, Double>)

data class CurrencyListResponse(val success: Boolean, val crypto: Map<String, CurrencyData>)

