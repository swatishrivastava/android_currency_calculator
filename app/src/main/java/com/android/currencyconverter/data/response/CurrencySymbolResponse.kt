package com.android.currencyconverter.data.response

data class CurrencySymbolResponse(
    val success: Boolean,
    val symbols: Map<String, String>,
    val error: CurrencyError
)