package com.android.currencyconverter.domain

import com.android.currencyconverter.data.repo.IRepository
import com.android.currencyconverter.data.response.CurrencySymbolResponse
import com.android.currencyconverter.data.state.NetworkResult
import javax.inject.Inject

class GetAllCurrencies @Inject
constructor(private val repo: IRepository) {
    suspend operator fun invoke(): NetworkResult<CurrencySymbolResponse> {
        var result = repo.getAllCurrencySymbols()
        return if (result.success) {
            NetworkResult.Success(result)
        } else {
            NetworkResult.Failure(false, result.error.code, null)
        }
    }
}