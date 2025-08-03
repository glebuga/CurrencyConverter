package com.example.currencyconverter.domain.usecase

import com.example.currencyconverter.domain.entity.Transaction
import com.example.currencyconverter.domain.repository.TransactionsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

// Будет получать историю транзакций
class GetTransactionsUseCase @Inject constructor(
    private val repository: TransactionsRepository
) {
    operator fun invoke(): Flow<List<Transaction>> {
        return repository.getTransactions()
    }
}