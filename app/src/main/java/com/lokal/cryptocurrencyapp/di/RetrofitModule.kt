package com.lokal.cryptocurrencyapp.di

import com.lokal.cryptocurrencyapp.api.CryptoApiService
import com.lokal.cryptocurrencyapp.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class RetrofitModule {

    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder().baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun provideFakerApi(retrofit: Retrofit):CryptoApiService{
        return retrofit.create(CryptoApiService::class.java)
    }
}