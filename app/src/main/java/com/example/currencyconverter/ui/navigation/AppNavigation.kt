package com.example.currencyconverter.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.currencyconverter.ui.screens.currencies.CurrenciesScreen
import com.example.currencyconverter.ui.screens.exchange.ExchangeScreen
import com.example.currencyconverter.ui.screens.transactions.TransactionsScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "currencies") {
        // Экран "Валюты" (главный)
        composable("currencies") {
            CurrenciesScreen(navController = navController)
        }

        // Экран "Обмен"
        composable("exchange/{baseCurrencyCode}/{targetCurrencyCode}/{amount}") { backStackEntry ->
            ExchangeScreen(navController = navController)
        }

        // Экран "Транзакции"
        composable("transactions") {
            TransactionsScreen(
                navController = navController
            )
        }
    }
}