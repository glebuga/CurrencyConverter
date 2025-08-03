package com.example.currencyconverter.ui.screens.exchange

sealed class ExchangeEffect {
    // Команда для возврата на предыдущий экран
    object NavigateBack : ExchangeEffect()
    // Команда для показа сообщения об ошибке
    data class ShowErrorToast(val message: String) : ExchangeEffect()
}