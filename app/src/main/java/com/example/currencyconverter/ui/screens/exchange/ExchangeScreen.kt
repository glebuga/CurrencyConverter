package com.example.currencyconverter.ui.screens.exchange

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.currencyconverter.R
import com.example.currencyconverter.domain.entity.Currency
import com.example.currencyconverter.domain.entity.CurrencyInfo
import com.example.currencyconverter.ui.theme.CurrencyConverterTheme
import com.example.currencyconverter.ui.util.fullName
import com.example.currencyconverter.ui.util.rememberAmountFormatter
import com.example.currencyconverter.ui.util.symbol
import java.math.BigDecimal

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExchangeScreen(
    navController: NavController,
    viewModel: ExchangeViewModel = hiltViewModel()
) {
    val state = viewModel.uiState.collectAsStateWithLifecycle().value
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is ExchangeEffect.NavigateBack -> navController.popBackStack()
                is ExchangeEffect.ShowErrorToast -> Toast.makeText(context, effect.message, Toast.LENGTH_LONG).show()
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.exchange_title)) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back_button_description)
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        if (state.isLoading && state.fromCurrencyInfo == null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (state.fromCurrencyInfo != null && state.toCurrencyInfo != null) {
            ExchangeContent(
                modifier = Modifier.padding(paddingValues),
                state = state,
                onConfirmClick = { viewModel.setEvent(ExchangeEvent.ExchangeButtonClicked) }
            )
        }
    }
}

@Composable
private fun ExchangeContent(
    modifier: Modifier = Modifier,
    state: ExchangeState,
    onConfirmClick: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ExchangeRateText(
            fromCurrency = state.rateFrom,
            toCurrency = state.rateTo,
            rateValue = state.rateValue
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Карточка "Покупаем" (Получаем)
        CurrencyCard(
            currencyInfo = state.toCurrencyInfo,
            amount = state.toAmount,
            sign = "+",
            amountColor = Color(0xFF008000)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Карточка "Продаем" (Отдаем)
        CurrencyCard(
            currencyInfo = state.fromCurrencyInfo,
            amount = state.fromAmount,
            sign = "-",
            amountColor = Color.Red
        )

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = onConfirmClick,
            modifier = Modifier.fillMaxWidth(),
            enabled = !state.isLoading
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            } else {
                Text(text = stringResource(R.string.confirm_exchange), fontSize = 16.sp)
            }
        }
    }
}

@Composable
fun ExchangeRateText(
    fromCurrency: Currency?,
    toCurrency: Currency?,
    rateValue: BigDecimal?
) {
    val formatter = rememberAmountFormatter()
    val text = if (fromCurrency != null && toCurrency != null && rateValue != null) {
        "1 ${toCurrency.name} = ${formatter.format(rateValue)} ${fromCurrency.name}"
    } else {
        "..."
    }
    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
}

@Composable
fun CurrencyCard(
    currencyInfo: CurrencyInfo?,
    amount: BigDecimal,
    sign: String,
    amountColor: Color
) {
    val formatter = rememberAmountFormatter()

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = currencyInfo?.flagUrl,
                contentDescription = "Flag of ${currencyInfo?.currency?.name}",
                modifier = Modifier
                    .size(40.dp),
                placeholder = painterResource(id = R.drawable.ic_flag_plug),
                error = painterResource(id = R.drawable.ic_flag_plug)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = currencyInfo?.currency?.name ?: "...", style = MaterialTheme.typography.bodyLarge)
                Text(
                    text = currencyInfo?.currency?.fullName() ?: "Loading...",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = stringResource(
                        id = R.string.balance_format,
                        currencyInfo?.currency?.symbol() ?: "",
                        formatter.format(currencyInfo?.balance ?: 0.0)
                    ),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Text(
                text = "$sign${formatter.format(amount)}",
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                color = amountColor
            )
        }
    }
}

@Preview(showBackground = true, name = "Exchange Screen Content")
@Composable
private fun ExchangeContentPreview() {
    CurrencyConverterTheme {
        ExchangeContent(
            state = ExchangeState(
                fromCurrencyInfo = CurrencyInfo(Currency.USD, 1.0, 1000.0, ""),
                toCurrencyInfo = CurrencyInfo(Currency.EUR, 1.0 / 1.08, 500.0, ""),
                fromAmount = BigDecimal("108.00"),
                toAmount = BigDecimal("100.00"),
                rateFrom = Currency.USD,
                rateTo = Currency.EUR,
                rateValue = BigDecimal("1.08")
            ),
            onConfirmClick = {}
        )
    }
}