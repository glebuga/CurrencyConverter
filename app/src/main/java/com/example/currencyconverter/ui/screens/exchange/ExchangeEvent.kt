package com.example.currencyconverter.ui.screens.exchange

sealed class ExchangeEvent {
    // Событие нажатия на кнопку "Обменять"
    object ExchangeButtonClicked : ExchangeEvent()
}