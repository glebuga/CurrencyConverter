package com.example.currencyconverter.data.repository

import com.example.currencyconverter.data.dataSource.remote.RemoteRatesServiceImpl
import com.example.currencyconverter.data.dataSource.room.account.dao.AccountDao
import com.example.currencyconverter.data.mapper.CurrencyInfoMapper
import com.example.currencyconverter.domain.entity.Currency
import com.example.currencyconverter.domain.entity.CurrencyInfo
import com.example.currencyconverter.domain.repository.CurrenciesRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CurrenciesRepositoryImpl @Inject constructor(
    private val accountDao: AccountDao,
    private val remoteRatesService: RemoteRatesServiceImpl,
    private val currencyInfoMapper: CurrencyInfoMapper
) : CurrenciesRepository {

    override fun getCurrencies(baseCurrency: Currency): Flow<List<CurrencyInfo>> {
        val remoteRatesFlow = flow {
            while (true) {
                val ratesDto = remoteRatesService.getRates(baseCurrency.name, amount = 1.0)
                emit(ratesDto)
                delay(1000)
            }
        }

        val accountsFlow = accountDao.getAllAsFlow()

        return remoteRatesFlow.combine(accountsFlow) { ratesDto, accountsDbo ->
            currencyInfoMapper.mapToCurrencyInfoList(ratesDto, accountsDbo)
        }
    }
}