package com.example.currencyconverter.domain.usecase

import com.example.currencyconverter.domain.entity.Currency
import com.example.currencyconverter.domain.entity.CurrencyInfo
import com.example.currencyconverter.domain.repository.CurrenciesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

// Будет получать данные для главного экрана
class GetCurrenciesUseCase @Inject constructor(
    private val repository: CurrenciesRepository
) {
    operator fun invoke(baseCurrency: Currency): Flow<List<CurrencyInfo>> {
        return repository.getCurrencies(baseCurrency)
    }
}
