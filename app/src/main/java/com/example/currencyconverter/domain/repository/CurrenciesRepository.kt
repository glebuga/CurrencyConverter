package com.example.currencyconverter.domain.repository

import com.example.currencyconverter.domain.entity.Currency
import com.example.currencyconverter.domain.entity.CurrencyInfo
import kotlinx.coroutines.flow.Flow

interface CurrenciesRepository {
    fun getCurrencies(baseCurrency: Currency): Flow<List<CurrencyInfo>>
}