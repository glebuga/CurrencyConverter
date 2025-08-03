package com.example.currencyconverter.ui.screens.currencies

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.currencyconverter.R
import kotlinx.coroutines.launch
import java.math.BigDecimal

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurrenciesScreen(
    navController: NavController,
    viewModel: CurrenciesViewModel = hiltViewModel()
) {
    val state = viewModel.uiState.collectAsStateWithLifecycle().value
    val context = LocalContext.current
    val lazyListState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    // Обработчик одноразовых эффектов (навигация, тосты)
    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is CurrenciesEffect.NavigateToExchange -> {
                    navController.navigate("exchange/${effect.baseCurrencyCode}/${effect.targetCurrencyCode}/${effect.amount}")
                }
                is CurrenciesEffect.NavigateToTransactions -> {
                    navController.navigate("transactions")
                }
                is CurrenciesEffect.ShowErrorToast -> {
                    Toast.makeText(context, effect.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    // Плавный скролл к верху списка при смене базовой валюты
    LaunchedEffect(state.baseCurrency) {
        if (state.baseCurrency != null) {
            coroutineScope.launch {
                lazyListState.animateScrollToItem(index = 0)
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.сurrency_сonverter)) },
                actions = {
                    IconButton(onClick = { viewModel.setEvent(CurrenciesEvent.TransactionsButtonClicked) }) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = stringResource(R.string.transactions_history)
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (state.isLoading && state.currencies.isEmpty()) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    state = lazyListState,
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    items(state.currencies, key = { it.currency.name }) { currencyInfo ->
                        val isBase = currencyInfo.currency == state.baseCurrency

                        if (isBase && state.screenMode == ScreenMode.AMOUNT_INPUT) {
                            InputCurrencyRow(
                                currencyInfo = currencyInfo,
                                amount = state.enteredAmount,
                                onAmountChange = { newAmount ->
                                    viewModel.setEvent(CurrenciesEvent.AmountChanged(newAmount))
                                },
                                onClearClick = {
                                    viewModel.setEvent(CurrenciesEvent.ClearAmountClicked)
                                }
                            )
                        } else {
                            val amountToShow = if (isBase) {
                                state.amountAsNumber
                            } else {
                                state.amountAsNumber.multiply(BigDecimal(currencyInfo.rate))
                            }

                            DisplayCurrencyRow(
                                currencyInfo = currencyInfo,
                                amount = amountToShow,
                                isBase = isBase,
                                onItemClick = {
                                    viewModel.setEvent(CurrenciesEvent.RowClicked(currencyInfo.currency))
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}