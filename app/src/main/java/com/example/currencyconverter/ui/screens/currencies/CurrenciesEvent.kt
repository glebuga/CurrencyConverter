package com.example.currencyconverter.ui.screens.currencies

import com.example.currencyconverter.domain.entity.Currency

sealed interface CurrenciesEvent {
    // Пользователь нажал на какую-то строку в списке. Какую именно - решаем в ViewModel.
    data class RowClicked(val currency: Currency) : CurrenciesEvent
    // Пользователь изменил текст в поле ввода.
    data class AmountChanged(val amount: String) : CurrenciesEvent
    // Пользователь нажал кнопку "очистить" / "назад" из режима ввода.
    object ClearAmountClicked : CurrenciesEvent
    // Пользователь нажал на кнопку истории транзакций.
    object TransactionsButtonClicked : CurrenciesEvent
}