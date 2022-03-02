package com.android.currencyconverter.utils

import android.content.Context
import com.android.currencyconverter.R

fun getErrorMessageFromCode(context: Context, code: Int): String {
    return when (code) {
        500 -> context.getString(R.string.error_message_500)
        404 -> context.getString(R.string.error_message_404)
        400 -> context.getString(R.string.error_message_400)
        104 -> context.getString(R.string.error_message_104)
        101 -> context.getString(R.string.error_message_101)
        103 -> context.getString(R.string.error_message_103)
        105 -> context.getString(R.string.error_message_105)
        106 -> context.getString(R.string.error_message_106)
        102 -> context.getString(R.string.error_message_102)
        201 -> context.getString(R.string.error_message_201)
        202 -> context.getString(R.string.error_message_202)
        else -> {
            context.getString(R.string.error_message_any)
        }
    }
}