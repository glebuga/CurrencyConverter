package com.example.currencyconverter.ui.screens.currencies

sealed interface CurrenciesEffect {

    // Команда для перехода на экран "Обмен"
    data class NavigateToExchange(
        val baseCurrencyCode: String,
        val targetCurrencyCode: String,
        val amount: String
    ) : CurrenciesEffect

    // Команда для перехода на экран "Транзакции".
    object NavigateToTransactions : CurrenciesEffect

    // Команда для отображения всплывающего сообщения.
    data class ShowErrorToast(val message: String) : CurrenciesEffect
}