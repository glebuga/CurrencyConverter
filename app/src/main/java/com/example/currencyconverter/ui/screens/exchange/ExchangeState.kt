package com.example.currencyconverter.ui.screens.exchange


import com.example.currencyconverter.domain.entity.Currency
import com.example.currencyconverter.domain.entity.CurrencyInfo
import java.math.BigDecimal

data class ExchangeState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val fromCurrencyInfo: CurrencyInfo? = null,
    val toCurrencyInfo: CurrencyInfo? = null,
    val fromAmount: BigDecimal = BigDecimal.ZERO,
    val toAmount: BigDecimal = BigDecimal.ZERO,
    val rateFrom: Currency? = null,
    val rateTo: Currency? = null,
    val rateValue: BigDecimal? = null
)