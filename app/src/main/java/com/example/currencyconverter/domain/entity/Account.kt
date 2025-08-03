package com.example.currencyconverter.domain.entity

data class Account(
    val currencyCode: Currency,
    val amount: Double
)