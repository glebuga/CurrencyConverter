package com.example.currencyconverter.ui.screens.exchange

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.currencyconverter.domain.entity.Currency
import com.example.currencyconverter.domain.usecase.GetCurrenciesUseCase
import com.example.currencyconverter.domain.usecase.PerformExchangeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.math.RoundingMode
import javax.inject.Inject

@HiltViewModel
class ExchangeViewModel @Inject constructor(
    private val performExchangeUseCase: PerformExchangeUseCase,
    private val getCurrenciesUseCase: GetCurrenciesUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(ExchangeState())
    val uiState: StateFlow<ExchangeState> = _uiState.asStateFlow()

    private val _effect = Channel<ExchangeEffect>()
    val effect: Flow<ExchangeEffect> = _effect.receiveAsFlow()

    private val baseCurrencyCode: String? = savedStateHandle["baseCurrencyCode"]
    private val targetCurrencyCode: String? = savedStateHandle["targetCurrencyCode"]
    private val amountString: String? = savedStateHandle["amount"]

    init {
        loadInitialData()
    }

    fun setEvent(event: ExchangeEvent) {
        when (event) {
            is ExchangeEvent.ExchangeButtonClicked -> performExchange()
        }
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val toCurrency = Currency.valueOf(baseCurrencyCode ?: "")
            val fromCurrency = Currency.valueOf(targetCurrencyCode ?: "")
            val amountToReceive = amountString?.toBigDecimalOrNull()

            if (amountToReceive == null || amountToReceive <= BigDecimal.ZERO) {
                _effect.send(ExchangeEffect.ShowErrorToast("Invalid amount to exchange"))
                _effect.send(ExchangeEffect.NavigateBack)
                return@launch
            }

            try {
                val rates = getCurrenciesUseCase(fromCurrency).first()

                val fromInfo = rates.first { it.currency == fromCurrency }
                val toInfo = rates.first { it.currency == toCurrency }

                val rate = toInfo.rate.toBigDecimal()
                val amountToSell = amountToReceive.divide(rate, 2, RoundingMode.HALF_UP)

                val displayRateValue = BigDecimal.ONE.divide(rate, 4, RoundingMode.HALF_UP)

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        fromCurrencyInfo = fromInfo,
                        toCurrencyInfo = toInfo,
                        fromAmount = amountToSell,
                        toAmount = amountToReceive,
                        rateFrom = fromCurrency,
                        rateTo = toCurrency,
                        rateValue = displayRateValue
                    )
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = e.message) }
                _effect.send(ExchangeEffect.ShowErrorToast(e.message ?: "Failed to load data"))
                _effect.send(ExchangeEffect.NavigateBack)
            }
        }
    }

    private fun performExchange() {
        viewModelScope.launch {
            val currentState = _uiState.value
            val fromInfo = currentState.fromCurrencyInfo ?: return@launch
            val toInfo = currentState.toCurrencyInfo ?: return@launch

            _uiState.update { it.copy(isLoading = true) }
            try {
                performExchangeUseCase(
                    fromCurrency = fromInfo.currency,
                    fromAmount = currentState.fromAmount.toDouble(),
                    toCurrency = toInfo.currency,
                    toAmount = currentState.toAmount.toDouble()
                )
                _effect.send(ExchangeEffect.NavigateBack)
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = e.message) }
                _effect.send(ExchangeEffect.ShowErrorToast(e.message ?: "Exchange failed"))
            }
        }
    }
}