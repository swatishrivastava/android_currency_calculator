package com.android.currencyconverter.di

import com.android.currencyconverter.data.api.CurrencyConverterAPI
import com.android.currencyconverter.data.repo.CurrencyRepo
import com.android.currencyconverter.data.repo.IRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {
    @Provides
    @Singleton
    fun provideRepo(api: CurrencyConverterAPI): IRepository = CurrencyRepo(api)
}