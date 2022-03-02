package com.android.currencyconverter.utils

import java.time.LocalDateTime
import java.time.Period
import java.time.format.DateTimeFormatter
import kotlin.time.Duration.Companion.days

const val DATE_FORMAT = "yyyy-MM-dd"

fun getListOfCurrencySymbols(it: Map<String, String>): List<String>? {
    return it?.keys?.toList()
}

fun getCurrentDate(): String {
    val current = LocalDateTime.now()
    val formatter = DateTimeFormatter.ofPattern(DATE_FORMAT)
    return current.format(formatter)
}

fun getYesterdayDate(): String {
    val today = LocalDateTime.now()
    var yesterday = today.minus(Period.ofDays(1));
    val formatter = DateTimeFormatter.ofPattern(DATE_FORMAT)
    return yesterday.format(formatter)
}
