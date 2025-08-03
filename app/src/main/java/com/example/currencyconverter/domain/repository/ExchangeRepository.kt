package com.example.currencyconverter.domain.repository

import com.example.currencyconverter.domain.entity.Currency

interface ExchangeRepository {
    suspend fun performExchange(
        fromCurrency: Currency,
        fromAmount: Double,
        toCurrency: Currency,
        toAmount: Double
    )
}