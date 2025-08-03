package com.example.currencyconverter.ui.screens.currencies

import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.currencyconverter.R
import com.example.currencyconverter.domain.entity.Currency
import com.example.currencyconverter.domain.entity.CurrencyInfo
import com.example.currencyconverter.domain.usecase.GetCurrenciesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.math.BigDecimal
import javax.inject.Inject

@HiltViewModel
class CurrenciesViewModel @Inject constructor(
    private val getCurrenciesUseCase: GetCurrenciesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(CurrenciesState())
    val uiState: StateFlow<CurrenciesState> = _uiState.asStateFlow()

    private val _effect: Channel<CurrenciesEffect> = Channel()
    val effect: Flow<CurrenciesEffect> = _effect.receiveAsFlow()

    private val _rawCurrenciesFlow = MutableStateFlow<List<CurrencyInfo>>(emptyList())
    private var currenciesJob: Job? = null

    init {
        setEvent(CurrenciesEvent.RowClicked(Currency.USD))

        viewModelScope.launch {
            combine(
                _rawCurrenciesFlow,
                _uiState.map { it.screenMode }.distinctUntilChanged(),
                _uiState.map { it.amountAsNumber }.distinctUntilChanged(),
                _uiState.map { it.baseCurrency }.distinctUntilChanged()
            ) { rawList, screenMode, amount, baseCurrency ->
                processAndSortCurrencies(rawList, screenMode, amount, baseCurrency)
            }.collect { processedList ->
                _uiState.update { it.copy(currencies = processedList) }
            }
        }
    }

    fun setEvent(event: CurrenciesEvent) {
        viewModelScope.launch {
            when (event) {
                is CurrenciesEvent.RowClicked -> handleRowClick(event.currency)
                is CurrenciesEvent.AmountChanged -> handleAmountChange(event.amount)
                is CurrenciesEvent.ClearAmountClicked -> handleClearAmountClick()
                is CurrenciesEvent.TransactionsButtonClicked -> _effect.send(CurrenciesEffect.NavigateToTransactions)
            }
        }
    }

    private fun handleRowClick(clickedCurrency: Currency) {
        val currentState = uiState.value
        val isBaseCurrency = clickedCurrency == currentState.baseCurrency

        if (currentState.screenMode == ScreenMode.CURRENCY_LIST) {
            if (isBaseCurrency) {
                _uiState.update { it.copy(screenMode = ScreenMode.AMOUNT_INPUT, enteredAmount = "") }
            } else {
                _uiState.update { it.copy(baseCurrency = clickedCurrency) }
                subscribeToCurrenciesUpdates(clickedCurrency)
            }
        } else {
            if (!isBaseCurrency) {
                viewModelScope.launch {
                    val amount = currentState.amountAsNumber
                    if (amount > BigDecimal.ZERO) {
                        _effect.send(
                            CurrenciesEffect.NavigateToExchange(
                                baseCurrencyCode = currentState.baseCurrency?.name ?: clickedCurrency.name,
                                targetCurrencyCode = clickedCurrency.name,
                                amount = amount.toPlainString()
                            )
                        )
                    }
                }
            }
        }
    }

    private fun handleAmountChange(newAmount: String) {
        val correctedAmount = newAmount.replace(',', '.')
        if (correctedAmount.isEmpty() || correctedAmount.matches(Regex("^\\d*\\.?\\d*\$"))) {
            _uiState.update {
                it.copy(
                    enteredAmount = correctedAmount,
                    amountAsNumber = correctedAmount.toBigDecimalOrNull() ?: BigDecimal.ZERO
                )
            }
        }
    }

    private fun handleClearAmountClick() {
        _uiState.update {
            it.copy(
                screenMode = ScreenMode.CURRENCY_LIST,
                enteredAmount = "1",
                amountAsNumber = BigDecimal.ONE
            )
        }
    }

    private fun subscribeToCurrenciesUpdates(baseCurrency: Currency) {
        currenciesJob?.cancel()
        currenciesJob = viewModelScope.launch {
            getCurrenciesUseCase(baseCurrency)
                .onStart { _uiState.update { it.copy(isLoading = true) } }
                .catch { throwable ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = (throwable.message ?: R.string.unknown_network_error).toString()
                        )
                    }
                    _effect.send(CurrenciesEffect.ShowErrorToast((throwable.message ?: R.string.couldnt_update_courses).toString()))
                }
                .collect { newRawCurrencies ->
                    _rawCurrenciesFlow.value = newRawCurrencies
                    _uiState.update { it.copy(isLoading = false) }
                }
        }
    }

    private fun processAndSortCurrencies(
        rawList: List<CurrencyInfo>,
        screenMode: ScreenMode,
        amountToBuy: BigDecimal,
        baseCurrency: Currency?
    ): List<CurrencyInfo> {
        val sortedList = rawList.sortedBy { it.currency != baseCurrency }

        if (screenMode != ScreenMode.AMOUNT_INPUT || amountToBuy <= BigDecimal.ZERO) {
            return sortedList
        }

        return sortedList.filter { currencyInfo ->
            if (currencyInfo.currency == baseCurrency) return@filter true

            val cost = amountToBuy.multiply(BigDecimal(currencyInfo.rate))
            val balance = currencyInfo.balance.toBigDecimal()
            balance >= cost
        }
    }
}