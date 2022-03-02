package com.android.currencyconverter.data

data class CurrencyHistory(
    var from: String,
    var to: String,
    var date: String,
    var rate: String,
)
