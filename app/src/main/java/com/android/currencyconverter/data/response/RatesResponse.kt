package com.android.currencyconverter.data.response

data class RatesResponse(
    val base: String,
    val date: String,
    val historical: Boolean,
    val rates: Map<String, String>,
    val success: Boolean,
    val timestamp: Int,
    val error: CurrencyError

)