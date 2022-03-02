package com.android.currencyconverter.data.response

data class CurrencyRateResponse(
    val base: String,
    val date: String,
    val rates: Map<String, Any>,
    val success: Boolean,
    val timestamp: Int,
    val error:CurrencyError
)
