package com.example.currencyconverter

import com.example.currencyconverter.domain.entity.Currency
import com.example.currencyconverter.domain.repository.ExchangeRepository
import com.example.currencyconverter.domain.usecase.PerformExchangeUseCase
import kotlinx.coroutines.test.runTest
import org.junit.runner.RunWith
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.verify
import org.mockito.kotlin.times

@RunWith(MockitoJUnitRunner::class)
class PerformExchangeUseCaseTest {

    @Mock
    private lateinit var repository: ExchangeRepository

    private lateinit var useCase: PerformExchangeUseCase

    @Before
    fun setUp() {
        useCase = PerformExchangeUseCase(repository)
    }

    @Test
    fun `invoke should call performExchange on repository with correct parameters`() = runTest {
        val fromCurrency = Currency.USD
        val fromAmount = 100.0
        val toCurrency = Currency.RUB
        val toAmount = 95.0

        useCase(fromCurrency, fromAmount, toCurrency, toAmount)

        verify(repository, times(1)).performExchange(
            fromCurrency, fromAmount, toCurrency, toAmount
        )
    }
}