package com.android.currencyconverter.domain

import com.android.currencyconverter.data.repo.IRepository
import com.android.currencyconverter.data.response.RatesResponse
import com.android.currencyconverter.data.state.NetworkResult
import javax.inject.Inject

class GetHistoryOfGivenCurrency @Inject
constructor(private val repo: IRepository) {
    private lateinit var currencies: String
    private lateinit var dateInUrl: String

    fun setDate(date: String) {
        this.dateInUrl = date
    }

    fun currencies(from: String, to: String) {
        currencies = "$from, $to"

    }

    suspend operator fun invoke(): NetworkResult<RatesResponse> {
        var result = repo.getRatesForPopularCurrencies(dateInUrl, currencies)
        return if (result.success) {
            NetworkResult.Success(result)
        } else {
            NetworkResult.Failure(false, result.error.code, null)
        }
    }

}

