package com.android.currencyconverter.data.repo

import com.android.currencyconverter.data.response.CurrencyRateResponse
import com.android.currencyconverter.data.response.CurrencySymbolResponse
import com.android.currencyconverter.data.response.RatesResponse
import com.android.currencyconverter.data.state.NetworkResult

interface IRepository {
    suspend fun getAllCurrencySymbols(): CurrencySymbolResponse
    suspend fun getAllCurrencyRates(): CurrencyRateResponse
    suspend fun getRatesForPopularCurrencies(dateInUrl: String,popularCurrency:String): RatesResponse

}