package com.example.currencyconverter.ui.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.currencyconverter.R
import com.example.currencyconverter.domain.entity.Currency

@Composable
fun Currency.fullName(): String {
    val resId = currencyUiDataMap[this]?.fullNameResId ?: R.string.currency_unknown
    return stringResource(id = resId)
}

@Composable
fun Currency.symbol(): String {
    val resId = currencyUiDataMap[this]?.symbolResId ?: R.string.symbol_unknown
    return stringResource(id = resId)
}

