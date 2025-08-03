package com.example.currencyconverter

import com.example.currencyconverter.domain.entity.Currency
import com.example.currencyconverter.domain.entity.CurrencyInfo
import com.example.currencyconverter.domain.repository.CurrenciesRepository
import com.example.currencyconverter.domain.usecase.GetCurrenciesUseCase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@RunWith(MockitoJUnitRunner::class)
class GetCurrenciesUseCaseTest {

    @Mock
    private lateinit var repository: CurrenciesRepository

    private lateinit var useCase: GetCurrenciesUseCase

    @Before
    fun setUp() {
        useCase = GetCurrenciesUseCase(repository)
    }

    @Test
    fun `invoke should return currencies flow from repository for given base currency`() = runTest {

        val baseCurrency = Currency.EUR
        val testCurrencies = listOf(
            CurrencyInfo(Currency.RUB, 0.9, 500.0, ""), CurrencyInfo(Currency.INR, 0.8, 200.0, "")
        )
        val expectedFlow = flowOf(testCurrencies)

        whenever(repository.getCurrencies(baseCurrency)).thenReturn(expectedFlow)

        val resultFlow = useCase(baseCurrency)

        val actualCurrencies = resultFlow.first()

        assertEquals(testCurrencies, actualCurrencies)

        verify(repository).getCurrencies(baseCurrency)
    }
}