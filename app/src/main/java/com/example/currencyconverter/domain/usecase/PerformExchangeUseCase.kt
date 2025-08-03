package com.example.currencyconverter.domain.usecase

import com.example.currencyconverter.domain.entity.Currency
import com.example.currencyconverter.domain.repository.ExchangeRepository
import javax.inject.Inject

// Будет выполнять логику обмена
class PerformExchangeUseCase @Inject constructor(
    private val repository: ExchangeRepository
) {
    suspend operator fun invoke(fromCurrency: Currency, fromAmount: Double, toCurrency: Currency, toAmount: Double) {
        return repository.performExchange(fromCurrency, fromAmount, toCurrency, toAmount)
    }
}