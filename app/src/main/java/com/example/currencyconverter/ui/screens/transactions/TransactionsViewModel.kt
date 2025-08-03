package com.example.currencyconverter.ui.screens.transactions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.currencyconverter.domain.usecase.GetTransactionsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TransactionsViewModel @Inject constructor(
    private val getTransactionsUseCase: GetTransactionsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(TransactionsState())
    val uiState: StateFlow<TransactionsState> = _uiState.asStateFlow()

    init {
        loadTransactions()
    }

    private fun loadTransactions() {
        viewModelScope.launch {
            getTransactionsUseCase()
                .onStart { _uiState.update { it.copy(isLoading = true) } }
                .catch { throwable ->
                    _uiState.update { it.copy(isLoading = false, error = throwable.message) }
                }
                .collect { transactions ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            transactions = transactions
                        )
                    }
                }
        }
    }
}