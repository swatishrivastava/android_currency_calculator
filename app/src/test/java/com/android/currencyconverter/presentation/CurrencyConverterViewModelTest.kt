package com.android.currencyconverter.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.android.currencyconverter.data.state.NetworkResult
import com.android.currencyconverter.domain.GetAllCurrencies
import com.android.currencyconverter.domain.GetAllCurrenciesRates
import com.android.currencyconverter.utils.BAD_INPUT_CODE
import com.android.currencyconverter.utils.InstantExecutorExtension
import com.android.currencyconverter.utils.MainCoroutineRule
import getCurrencyMap
import getCurrencyResponseEmptyTestData
import getCurrencyResponseTestData
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExperimentalCoroutinesApi
@ExtendWith(InstantExecutorExtension::class)
class CurrencyConverterViewModelTest {
    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @MockK
    private lateinit var currencyConverterViewModel: CurrencyConverterViewModel

    @MockK
    private lateinit var currencies: GetAllCurrencies

    @MockK
    private lateinit var currenciesRates: GetAllCurrenciesRates

    @Before
    fun setup() {
        MockKAnnotations.init(this)


    }

    @Test
    fun `test currency converter amount `() {
        currencyConverterViewModel = CurrencyConverterViewModel(currencies, currenciesRates)
        currencyConverterViewModel.currencyRatesMap = getCurrencyMap()
        currencyConverterViewModel.getConvertedAmount("AED", "INR", "20")
        currencyConverterViewModel.amount.observeForever {}
        assertEquals("410.83", currencyConverterViewModel.amount.value)
    }


    @Test
    fun `test currency converter when amount is empty `() {
        currencyConverterViewModel = CurrencyConverterViewModel(currencies, currenciesRates)
        currencyConverterViewModel.currencyRatesMap = getCurrencyMap()
        currencyConverterViewModel.getConvertedAmount("AED", "INR", "")
        currencyConverterViewModel.errorLiveData.observeForever {}
        assertEquals(BAD_INPUT_CODE, currencyConverterViewModel.errorLiveData.value)
    }

    @Test
    fun `test get all currencies`() {
        var currencySymbolResponse = getCurrencyResponseTestData()
        coEvery { currencies() } returns NetworkResult.Success(currencySymbolResponse)
        currencyConverterViewModel = CurrencyConverterViewModel(currencies, currenciesRates)
        currencyConverterViewModel.getCurrencySymbols()
        currencyConverterViewModel.symbols.observeForever {}
        assertEquals(
            currencyConverterViewModel.symbols.value?.size,
            currencySymbolResponse.symbols.size
        )
        assertEquals(false, currencyConverterViewModel.symbols.value?.isEmpty())
    }


    @Test
    fun `test when received empty currency list`() {
        var currencySymbolResponseEmptyHashMap = getCurrencyResponseEmptyTestData()
        coEvery { currencies() } returns NetworkResult.Success(currencySymbolResponseEmptyHashMap)
        currencyConverterViewModel = CurrencyConverterViewModel(currencies, currenciesRates)
        currencyConverterViewModel.getCurrencySymbols()
        currencyConverterViewModel.symbols.observeForever {}
        assertEquals(true, currencyConverterViewModel.symbols.value?.isEmpty())
    }

    @Test
    fun `test when server unreachable and received error`() {
        coEvery { currencies() } returns NetworkResult.Failure(
            true, 500,
            null
        )
        currencyConverterViewModel = CurrencyConverterViewModel(currencies, currenciesRates)
        currencyConverterViewModel.getCurrencySymbols()
        currencyConverterViewModel.errorLiveData.observeForever {}
        assertEquals(500, currencyConverterViewModel.errorLiveData.value)
    }

    @Test
    fun `test when user subscription expired`() {
        coEvery { currencies() } returns NetworkResult.Failure(
            false, 104,
            null
        )
        currencyConverterViewModel = CurrencyConverterViewModel(currencies, currenciesRates)
        currencyConverterViewModel.getCurrencySymbols()
        currencyConverterViewModel.errorLiveData.observeForever {}
        assertEquals(104, currencyConverterViewModel.errorLiveData.value)
    }


}