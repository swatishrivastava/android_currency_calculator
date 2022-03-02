package com.android.currencyconverter.domain

import com.android.currencyconverter.data.repo.IRepository
import com.android.currencyconverter.data.response.RatesResponse
import com.android.currencyconverter.data.state.NetworkResult
import com.android.currencyconverter.utils.POPULAR_CURRENCIES
import com.android.currencyconverter.utils.getCurrentDate
import javax.inject.Inject

class GetPopularRates @Inject
constructor(private val repo: IRepository) {
    suspend operator fun invoke(): NetworkResult<RatesResponse> {
        var result = repo.getRatesForPopularCurrencies(getCurrentDate(), POPULAR_CURRENCIES)
        return if (result.success) {
            NetworkResult.Success(result)
        } else {
            NetworkResult.Failure(false, result.error.code, null)
        }

    }

}