package com.example.currencyconverter.ui.screens.currencies

import com.example.currencyconverter.domain.entity.Currency
import com.example.currencyconverter.domain.entity.CurrencyInfo
import java.math.BigDecimal

data class CurrenciesState(
    val currencies: List<CurrencyInfo> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val baseCurrency: Currency? = null,
    val screenMode: ScreenMode = ScreenMode.CURRENCY_LIST,
    val enteredAmount: String = "0.00",
    val amountAsNumber: BigDecimal = BigDecimal.ONE
)

// Режим экрана
enum class ScreenMode {
    CURRENCY_LIST,  // Режим отображения списка валют
    AMOUNT_INPUT    // Режим ввода суммы для конвертации
}