package com.example.currencyconverter.data.repository

import androidx.room.withTransaction
import com.example.currencyconverter.data.dataSource.room.ConverterDatabase
import com.example.currencyconverter.data.dataSource.room.account.dao.AccountDao
import com.example.currencyconverter.data.dataSource.room.account.dbo.AccountDbo
import com.example.currencyconverter.data.dataSource.room.transaction.dao.TransactionDao
import com.example.currencyconverter.data.mapper.TransactionMapper
import com.example.currencyconverter.domain.entity.Currency
import com.example.currencyconverter.domain.entity.Transaction
import com.example.currencyconverter.domain.repository.ExchangeRepository
import javax.inject.Inject

class ExchangeRepositoryImpl @Inject constructor(
    private val accountDao: AccountDao,
    private val transactionDao: TransactionDao,
    private val transactionMapper: TransactionMapper,
    private val database: ConverterDatabase
) : ExchangeRepository {

    override suspend fun performExchange(
        fromCurrency: Currency,
        fromAmount: Double,
        toCurrency: Currency,
        toAmount: Double
    ) {
        database.withTransaction {
            // 1. Получаем текущий баланс счета, с которого списываем
            val fromAccount = accountDao.getByCode(fromCurrency.name)
                ?: throw IllegalStateException("Account ${fromCurrency.name} not found")

            // Проверка, достаточно ли средств
            if (fromAccount.amount < fromAmount) {
                throw IllegalStateException("Not enough funds in ${fromCurrency.name}")
            }

            // 2. Получаем текущий баланс счета, на который зачисляем
            val toAccount = accountDao.getByCode(toCurrency.name)

            // 3. Обновляем балансы
            val updatedFromAccount = AccountDbo(
                code = fromCurrency.name,
                amount = fromAccount.amount - fromAmount
            )
            accountDao.upsert(updatedFromAccount)

            // Обновляем или создаем счет, на который зачисляем
            val updatedToAccount = AccountDbo(
                code = toCurrency.name,
                amount = (toAccount?.amount ?: 0.0) + toAmount
            )
            accountDao.upsert(updatedToAccount)

            // 4. Записываем транзакцию в историю
            val transaction = Transaction(
                fromCurrency = fromCurrency,
                fromAmount = fromAmount,
                toCurrency = toCurrency,
                toAmount = toAmount,
                date = System.currentTimeMillis()
            )
            transactionDao.insertAll(transactionMapper.fromDomainToDbo(transaction))
        }
    }
}