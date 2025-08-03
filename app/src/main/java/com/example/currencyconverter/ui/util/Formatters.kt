package com.example.currencyconverter.ui.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import java.text.DecimalFormat


@Composable
fun rememberAmountFormatter(): DecimalFormat {
    return remember {
        DecimalFormat("#,##0.00")
    }
}