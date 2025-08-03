package com.example.currencyconverter.data.mapper

import com.example.currencyconverter.data.dataSource.room.account.dbo.AccountDbo
import com.example.currencyconverter.domain.entity.Account
import com.example.currencyconverter.domain.entity.Currency
import javax.inject.Inject

class AccountMapper @Inject constructor() {

    fun toDomain(dbo: AccountDbo): Account {
        return Account(
            currencyCode = Currency.valueOf(dbo.code),
            amount = dbo.amount
        )
    }

    fun toDbo(domain: Account): AccountDbo {
        return AccountDbo(
            code = domain.currencyCode.name,
            amount = domain.amount
        )
    }

    fun toDomainList(dboList: List<AccountDbo>): List<Account> {
        return dboList.map { toDomain(it) }
    }
}