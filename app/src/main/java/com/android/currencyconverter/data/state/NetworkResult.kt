package com.android.currencyconverter.data.state

import okhttp3.ResponseBody

sealed class NetworkResult<out T> {
    data class Loading(val isLoading: Boolean) : NetworkResult<Nothing>()
    data class Success<out T>(val value: T) : NetworkResult<T>()
    data class Failure(
        val isNetworkError: Boolean,
        val errorCode: Int?,
        val errorBody: ResponseBody?
    ) : NetworkResult<Nothing>()
}