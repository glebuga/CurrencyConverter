package com.example.currencyconverter.ui.screens.currencies

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.currencyconverter.R
import com.example.currencyconverter.domain.entity.Currency
import com.example.currencyconverter.domain.entity.CurrencyInfo
import com.example.currencyconverter.ui.theme.CurrencyConverterTheme
import com.example.currencyconverter.ui.util.fullName
import com.example.currencyconverter.ui.util.rememberAmountFormatter
import com.example.currencyconverter.ui.util.symbol
import java.math.BigDecimal

/**
 * Ряд для отображения валюты в режиме ввода (только для базовой валюты).
 */
@Composable
fun InputCurrencyRow(
    currencyInfo: CurrencyInfo,
    amount: String,
    onAmountChange: (String) -> Unit,
    onClearClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    CurrencyRowLayout(
        modifier = modifier,
        currencyInfo = currencyInfo,
        isBase = true,
        onClick = { }
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            BasicTextField(
                value = amount,
                onValueChange = onAmountChange,
                textStyle = MaterialTheme.typography.titleMedium.copy(textAlign = TextAlign.End),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                singleLine = true,
                modifier = Modifier
                    .focusRequester(focusRequester)
                    .weight(1f)
            )
            Spacer(Modifier.width(4.dp))
            Text(text = currencyInfo.currency.symbol())
            IconButton(
                modifier = Modifier.size(26.dp),
                onClick = onClearClick
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = stringResource(R.string.clear_amount_description)
                )
            }
        }
    }
}

/**
 * Ряд для отображения валюты в режиме списка.
 */
@Composable
fun DisplayCurrencyRow(
    currencyInfo: CurrencyInfo,
    amount: BigDecimal,
    isBase: Boolean,
    onItemClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val formatter = rememberAmountFormatter()

    CurrencyRowLayout(
        modifier = modifier,
        currencyInfo = currencyInfo,
        isBase = isBase,
        onClick = onItemClick
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = formatter.format(amount),
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.End,
            )
            Spacer(Modifier.width(4.dp))
            Text(text = currencyInfo.currency.symbol())
        }
    }
}

/**
 * Общий лейаут для обоих видов рядов, чтобы избежать дублирования кода.
 * Содержит Card, иконку, название и баланс.
 */
@Composable
private fun CurrencyRowLayout(
    currencyInfo: CurrencyInfo,
    isBase: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val formatter = rememberAmountFormatter()

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isBase) {
                MaterialTheme.colorScheme.surfaceVariant
            } else {
                MaterialTheme.colorScheme.surface
            }
        )
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = currencyInfo.flagUrl,
                contentDescription = "Flag of ${currencyInfo.currency.name}",
                modifier = Modifier
                    .size(40.dp),
                placeholder = painterResource(id = R.drawable.ic_flag_plug),
                error = painterResource(id = R.drawable.ic_flag_plug)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = currencyInfo.currency.name,
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = currencyInfo.currency.fullName(),
                    style = MaterialTheme.typography.bodySmall
                )
                Row {
                    Text(
                        text = stringResource(
                            id = R.string.balance_format,
                            currencyInfo.currency.symbol(),
                            formatter.format(currencyInfo.balance)
                        ),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            content()
        }
    }
}

// Previews
val sampleCurrencyInfo = CurrencyInfo(currency = Currency.USD, rate = 1.0, balance = 1000.0, flagUrl = "")

@Preview(showBackground = true, name = "Display Row (Base)")
@Composable
private fun DisplayCurrencyRowBasePreview() {
    CurrencyConverterTheme {
        DisplayCurrencyRow(
            currencyInfo = sampleCurrencyInfo,
            amount = BigDecimal("1"),
            isBase = true,
            onItemClick = {}
        )
    }
}

@Preview(showBackground = true, name = "Display Row (Not Base)")
@Composable
private fun DisplayCurrencyRowNotBasePreview() {
    CurrencyConverterTheme {
        DisplayCurrencyRow(
            currencyInfo = sampleCurrencyInfo.copy(currency = Currency.EUR, rate = 0.93),
            amount = BigDecimal("93.00"),
            isBase = false,
            onItemClick = {}
        )
    }
}

@Preview(showBackground = true, name = "Input Row")
@Composable
private fun InputCurrencyRowPreview() {
    CurrencyConverterTheme {
        InputCurrencyRow(
            currencyInfo = sampleCurrencyInfo,
            amount = "100,00",
            onAmountChange = {},
            onClearClick = {}
        )
    }
}