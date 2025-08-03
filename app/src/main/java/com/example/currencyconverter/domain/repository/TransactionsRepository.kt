package com.example.currencyconverter.domain.repository

import com.example.currencyconverter.domain.entity.Transaction
import kotlinx.coroutines.flow.Flow

interface TransactionsRepository {
    fun getTransactions(): Flow<List<Transaction>>
}