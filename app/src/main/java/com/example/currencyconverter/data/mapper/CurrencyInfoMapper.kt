package com.example.currencyconverter.data.mapper

import com.example.currencyconverter.data.dataSource.remote.dto.RateDto
import com.example.currencyconverter.data.dataSource.room.account.dbo.AccountDbo
import com.example.currencyconverter.domain.entity.Currency
import com.example.currencyconverter.domain.entity.CurrencyInfo
import javax.inject.Inject

class CurrencyInfoMapper @Inject constructor() {

    private companion object {
        private const val FLAG_API_BASE_URL = "https://flagcdn.com/w80/"
    }

    fun mapToCurrencyInfoList(
        rates: List<RateDto>,
        accounts: List<AccountDbo>
    ): List<CurrencyInfo> {

        val ratesMap = rates.associateBy { it.currency }
        val accountsMap = accounts.associateBy { it.code }

        return Currency.entries.map { currency ->
            val currencyCode = currency.name

            val rate = ratesMap[currencyCode]?.value ?: 1.0
            val balance = accountsMap[currencyCode]?.amount ?: 0.0

            CurrencyInfo(
                currency = currency,
                rate = rate,
                balance = balance,
                flagUrl = getFlagUrl(currency)
            )
        }
    }

    private fun getFlagUrl(currency: Currency): String {
        return "$FLAG_API_BASE_URL${currency.countryCode}.png"
    }
}

