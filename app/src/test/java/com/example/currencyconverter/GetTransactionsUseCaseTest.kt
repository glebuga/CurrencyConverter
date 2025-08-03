package com.example.currencyconverter

import com.example.currencyconverter.domain.entity.Currency
import com.example.currencyconverter.domain.entity.Transaction
import com.example.currencyconverter.domain.repository.TransactionsRepository
import com.example.currencyconverter.domain.usecase.GetTransactionsUseCase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.whenever
import java.time.LocalDate
import java.time.ZoneId

@RunWith(MockitoJUnitRunner::class)
class GetTransactionsUseCaseTest {

    val specificDateAsLong =
        LocalDate.of(2025, 6, 20).atStartOfDay(ZoneId.of("UTC")).toInstant().toEpochMilli()

    @Mock
    private lateinit var repository: TransactionsRepository

    private lateinit var useCase: GetTransactionsUseCase

    @Before
    fun setUp() {
        useCase = GetTransactionsUseCase(repository)
    }

    @Test
    fun `invoke should return transactions flow from repository`() = runTest {
        val testTransactions = listOf(
            Transaction(Currency.USD, 100.0, Currency.EUR, 90.0, specificDateAsLong)
        )

        val expectedFlow = flowOf(testTransactions)

        whenever(repository.getTransactions()).thenReturn(expectedFlow)

        val resultFlow = useCase()

        val actualTransactions = resultFlow.first()

        assertEquals(expectedFlow, resultFlow)
        assertEquals(testTransactions, actualTransactions)
        assertEquals(1, actualTransactions.size)
        assertEquals(Currency.USD, actualTransactions[0].fromCurrency)
    }
}