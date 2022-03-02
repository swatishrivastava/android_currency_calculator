package com.android.currencyconverter.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.android.currencyconverter.data.state.NetworkResult
import com.android.currencyconverter.domain.GetHistoryOfGivenCurrency
import com.android.currencyconverter.domain.GetPopularRates
import com.android.currencyconverter.utils.InstantExecutorExtension
import com.android.currencyconverter.utils.MainCoroutineRule
import getEmptyPopularCurrencyRatesTestData
import getPopularCurrencyRatesTestData
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExperimentalCoroutinesApi
@ExtendWith(InstantExecutorExtension::class)
class CurrencyDetailViewModelTest {
    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()


    @MockK
    private lateinit var popularRates: GetPopularRates

    @MockK
    private lateinit var historyOfGivenCurrency: GetHistoryOfGivenCurrency

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `test get all popular currencies rates`() {
        var rateResponse = getPopularCurrencyRatesTestData()
        coEvery { popularRates() } returns NetworkResult.Success(rateResponse)
        var currencyDetailViewModel = CurrencyDetailViewModel(popularRates, historyOfGivenCurrency)
        currencyDetailViewModel.rates.observeForever {}
        Assert.assertEquals(
            currencyDetailViewModel.rates.value?.size,
            rateResponse.rates.size
        )
        Assert.assertEquals(false, currencyDetailViewModel.rates.value?.isEmpty())
    }

    @Test
    fun `test when received empty rate list`() {
        var rateResponse = getEmptyPopularCurrencyRatesTestData()
        coEvery { popularRates() } returns NetworkResult.Success(rateResponse)
        var currencyDetailViewModel = CurrencyDetailViewModel(popularRates,historyOfGivenCurrency)
        currencyDetailViewModel.rates.observeForever {}
        Assert.assertEquals(
            currencyDetailViewModel.rates.value?.size,
            rateResponse.rates.size
        )
        Assert.assertEquals(true, currencyDetailViewModel.rates.value?.isEmpty())
    }

    @Test
    fun `test when server unreachable and received error`() {
        coEvery { popularRates() } returns NetworkResult.Failure(
            true, 500,
            null
        )
        var currenyDetailViewModel = CurrencyDetailViewModel(popularRates,historyOfGivenCurrency)
        currenyDetailViewModel.errorLiveData.observeForever {}
        Assert.assertEquals(500, currenyDetailViewModel.errorLiveData.value)
    }

}