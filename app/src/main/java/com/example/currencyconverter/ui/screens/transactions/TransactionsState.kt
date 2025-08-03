package com.example.currencyconverter.ui.screens.transactions

import com.example.currencyconverter.domain.entity.Transaction

data class TransactionsState(
    val transactions: List<Transaction> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)