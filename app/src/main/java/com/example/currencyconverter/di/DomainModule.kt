package com.example.currencyconverter.di

import com.example.currencyconverter.data.repository.CurrenciesRepositoryImpl
import com.example.currencyconverter.data.repository.ExchangeRepositoryImpl
import com.example.currencyconverter.data.repository.TransactionsRepositoryImpl
import com.example.currencyconverter.domain.repository.CurrenciesRepository
import com.example.currencyconverter.domain.repository.ExchangeRepository
import com.example.currencyconverter.domain.repository.TransactionsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DomainModule {

    @Binds
    @Singleton
    abstract fun bindCurrenciesRepository(
        impl: CurrenciesRepositoryImpl
    ): CurrenciesRepository

    @Binds
    @Singleton
    abstract fun bindTransactionsRepository(
        impl: TransactionsRepositoryImpl
    ): TransactionsRepository

    @Binds
    @Singleton
    abstract fun bindExchangeRepository(
        impl: ExchangeRepositoryImpl
    ): ExchangeRepository
}