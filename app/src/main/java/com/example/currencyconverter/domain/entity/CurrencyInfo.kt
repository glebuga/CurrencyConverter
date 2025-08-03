package com.example.currencyconverter.domain.entity

data class CurrencyInfo(
    val currency: Currency,
    val rate: Double,
    val balance: Double,
    val flagUrl: String
)
