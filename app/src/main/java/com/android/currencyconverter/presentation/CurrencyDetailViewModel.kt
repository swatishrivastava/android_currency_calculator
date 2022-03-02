package com.android.currencyconverter.presentation

/**
 * This class is responsible for getting currency  details from network and handling UI logic
 */

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.currencyconverter.data.CurrencyHistory
import com.android.currencyconverter.data.response.RatesResponse
import com.android.currencyconverter.data.state.NetworkResult
import com.android.currencyconverter.domain.GetHistoryOfGivenCurrency
import com.android.currencyconverter.domain.GetPopularRates
import com.android.currencyconverter.utils.NETWORK_FAILURE_CODE
import com.android.currencyconverter.utils.getCurrentDate
import com.android.currencyconverter.utils.getYesterdayDate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.math.RoundingMode
import javax.inject.Inject

@HiltViewModel
class CurrencyDetailViewModel
@Inject constructor(
    private val popularRates: GetPopularRates,
    private val historyOfGivenCurrency: GetHistoryOfGivenCurrency
) : ViewModel() {

    private var list: MutableList<CurrencyHistory> = mutableListOf()
    private var mutableLiveData = MutableLiveData<Map<String, Any>>()
    val rates: LiveData<Map<String, Any>>
        get() {
            return mutableLiveData
        }

    private var mutableLiveDataHistory = MutableLiveData<List<CurrencyHistory>>()
    val currencyHistory: LiveData<List<CurrencyHistory>>
        get() {
            return mutableLiveDataHistory
        }
    private var mutableErrorLiveData = MutableLiveData<Int>()

    val errorLiveData: LiveData<Int>
        get() = mutableErrorLiveData

    init {
        getRates()
    }

    /**
     *    This is temporary logic.As we do not have paid subscription, not able to access time-series API.
    Just for demo purpose, doing call one today and other for yesterday
     */

    fun getHistoryRatesForGivenCurrency(from: String, to: String) {

        viewModelScope.launch {
            viewModelScope.async {
                historyOfGivenCurrency.currencies(from, to)
                historyOfGivenCurrency.setDate(getCurrentDate())
                handleCurrenciesHistory(historyOfGivenCurrency())
            }

            viewModelScope.async {
                historyOfGivenCurrency.currencies(from, to)
                historyOfGivenCurrency.setDate(getYesterdayDate())
                handleCurrenciesHistory(historyOfGivenCurrency())
            }
        }


    }

    private fun getRates() {
        viewModelScope.launch {
            handleCurrenciesResponse(popularRates())
        }
    }

    private fun getConvertedAmount(from: String, to: String, amount: String = "1"): String {
        val fromRate = from.toBigDecimal()
        val toRate = to.toBigDecimal()
        val amt = amount.toBigDecimal()
        val response = (amt * toRate) / fromRate
        val finalVal =
            response.toString().toBigDecimal().setScale(2, RoundingMode.UP).toDouble()
        return finalVal.toString()

    }

    private fun handleCurrenciesResponse(result: NetworkResult<RatesResponse>) {
        when (result) {
            is NetworkResult.Success -> {
                result.value.also {
                    mutableLiveData.value = it.rates
                }
            }
            is NetworkResult.Failure -> {
                result.errorCode?.also {
                    mutableErrorLiveData.value = it
                }
            }

            else -> {
                mutableErrorLiveData.value = NETWORK_FAILURE_CODE
            }
        }
    }


    private fun handleCurrenciesHistory(result: NetworkResult<RatesResponse>) {
        when (result) {
            is NetworkResult.Success -> {
                result.value.also {
                    prepareHistoryList(it)
                    mutableLiveDataHistory.value = list
                }
            }
            is NetworkResult.Failure -> {
                result.errorCode?.also {
                    mutableErrorLiveData.value = it
                }
            }

            else -> {
                mutableErrorLiveData.value = NETWORK_FAILURE_CODE
            }
        }
    }

    private fun prepareHistoryList(it: RatesResponse) {
        val first = getFromCurrency(it)
        val sec = getToCurrency(it)
        val rateForFromCurrencyForBase = it.rates.get(first)
        val rateForToCurrencyFromBase = it.rates.get(sec)
        list.add(
            getCurrencyHistory(
                first, sec, it,
                rateForFromCurrencyForBase, rateForToCurrencyFromBase
            )
        )
    }

    private fun getToCurrency(it: RatesResponse) =
        it.rates.keys.toList()[1]

    private fun getFromCurrency(it: RatesResponse) =
        it.rates.keys.toList()[0]

    private fun getCurrencyHistory(
        first: String,
        sec: String,
        it: RatesResponse,
        rateFrom: String?,
        to: String?
    ): CurrencyHistory {
        return CurrencyHistory(
            first, sec, it.date,
            getConvertedAmount(rateFrom!!, to!!)
        )
    }
}