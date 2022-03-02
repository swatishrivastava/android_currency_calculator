package com.android.currencyconverter.domain

import com.android.currencyconverter.data.repo.IRepository
import com.android.currencyconverter.data.response.CurrencyRateResponse
import com.android.currencyconverter.data.state.NetworkResult
import javax.inject.Inject

class GetAllCurrenciesRates @Inject
constructor(private val repo: IRepository) {
    suspend operator fun invoke(): NetworkResult<CurrencyRateResponse> {
        var result = repo.getAllCurrencyRates()
        return if (result.success) {
            NetworkResult.Success(result)
        } else {
            NetworkResult.Failure(false, result.error.code, null)
        }
    }
}