package com.example.currencyconverter.data.mapper

import com.example.currencyconverter.data.dataSource.room.transaction.dbo.TransactionDbo
import com.example.currencyconverter.domain.entity.Currency
import com.example.currencyconverter.domain.entity.Transaction
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import javax.inject.Inject

class TransactionMapper @Inject constructor() {

    private fun toDomain(dbo: TransactionDbo): Transaction {
        return Transaction(
            fromCurrency = Currency.valueOf(dbo.from),
            fromAmount = dbo.fromAmount,
            toCurrency = Currency.valueOf(dbo.to),
            toAmount = dbo.toAmount,
            date = dbo.dateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
        )
    }

    fun fromDomainToDbo(domain: Transaction): TransactionDbo {
        return TransactionDbo(
            id = 0,
            from = domain.fromCurrency.name,
            to = domain.toCurrency.name,
            fromAmount = domain.fromAmount,
            toAmount = domain.toAmount,
            dateTime = LocalDateTime.ofInstant(
                Instant.ofEpochMilli(domain.date), ZoneId.systemDefault()
            )
        )
    }

    fun toDomainList(dboList: List<TransactionDbo>): List<Transaction> {
        return dboList.map { toDomain(it) }
    }
}