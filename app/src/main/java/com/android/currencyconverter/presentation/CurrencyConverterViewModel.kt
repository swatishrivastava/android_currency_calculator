package com.android.currencyconverter.presentation
/**
 * This class is responsible for getting currency  data from network and handling conversion logic
 */
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.currencyconverter.data.response.CurrencyRateResponse
import com.android.currencyconverter.data.response.CurrencySymbolResponse
import com.android.currencyconverter.data.state.NetworkResult
import com.android.currencyconverter.domain.GetAllCurrencies
import com.android.currencyconverter.domain.GetAllCurrenciesRates
import com.android.currencyconverter.utils.BAD_INPUT_CODE
import com.android.currencyconverter.utils.NETWORK_FAILURE_CODE
import com.android.currencyconverter.utils.getListOfCurrencySymbols
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.math.RoundingMode
import javax.inject.Inject

@HiltViewModel
class CurrencyConverterViewModel
@Inject constructor(
    private val currencies: GetAllCurrencies,
    private val currenciesRates: GetAllCurrenciesRates
) : ViewModel() {

    var currencyRatesMap: Map<String, Any>? = null

    private var mutableLiveData = MutableLiveData<List<String>>()
    val symbols: LiveData<List<String>>
        get() {
            return mutableLiveData
        }

    private var mutableLiveDataConvertedAmt = MutableLiveData<String>()
    val amount: LiveData<String>
        get() {
            return mutableLiveDataConvertedAmt
        }

    private var mutableErrorLiveData = MutableLiveData<Int>()
    val errorLiveData: LiveData<Int>
        get() = mutableErrorLiveData

    init {
        getAllCurrencyRates()
    }

    fun getCurrencySymbols() {
        viewModelScope.launch {
            handleCurrenciesResponse(currencies())
        }
    }

    private fun getAllCurrencyRates() {
        viewModelScope.launch {
            handleCurrencyRatesResponse(currenciesRates())
        }
    }

    private fun handleCurrenciesResponse(result: NetworkResult<CurrencySymbolResponse>) {
        when (result) {
            is NetworkResult.Success -> {
                result.value.symbols.also {
                    mutableLiveData.value = getListOfCurrencySymbols(it)!!
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

    private fun handleCurrencyRatesResponse(result: NetworkResult<CurrencyRateResponse>) {
        when (result) {
            is NetworkResult.Success -> {
                result.value.also {
                    currencyRatesMap = it.rates
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

    fun getConvertedAmount(from: String, to: String, amount: String) {
        if (amount.isEmpty()) {
            mutableErrorLiveData.value = BAD_INPUT_CODE
            return
        }
        viewModelScope.launch {
            val fromRate = currencyRatesMap?.get(from).toString().toBigDecimal()
            val toRate = currencyRatesMap?.get(to).toString().toBigDecimal()
            val amt = amount.toBigDecimal()
            val response = (amt * toRate) / fromRate
            val finalVal =
                response.toString().toBigDecimal().setScale(2, RoundingMode.UP).toDouble()
            mutableLiveDataConvertedAmt.value = finalVal.toString()

        }

    }


}