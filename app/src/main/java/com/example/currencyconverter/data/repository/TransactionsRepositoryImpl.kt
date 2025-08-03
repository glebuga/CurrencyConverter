package com.example.currencyconverter.data.repository

import com.example.currencyconverter.data.dataSource.room.transaction.dao.TransactionDao
import com.example.currencyconverter.data.mapper.TransactionMapper
import com.example.currencyconverter.domain.entity.Transaction
import com.example.currencyconverter.domain.repository.TransactionsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TransactionsRepositoryImpl @Inject constructor(
    private val transactionDao: TransactionDao,
    private val transactionMapper: TransactionMapper
) : TransactionsRepository {

    override fun getTransactions(): Flow<List<Transaction>> {
        return transactionDao.getAllAsFlow().map { transactionDboList ->
            transactionMapper.toDomainList(transactionDboList)
        }
    }
}