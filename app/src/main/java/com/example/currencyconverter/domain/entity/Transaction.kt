package com.example.currencyconverter.domain.entity

data class Transaction(
    val fromCurrency: Currency,
    val fromAmount: Double,
    val toCurrency: Currency,
    val toAmount: Double,
    val date: Long
)
