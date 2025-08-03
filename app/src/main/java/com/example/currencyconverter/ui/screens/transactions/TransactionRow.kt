package com.example.currencyconverter.ui.screens.transactions

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.currencyconverter.domain.entity.Transaction
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun TransactionRow(transaction: Transaction) {
    val amountFormatter = remember { DecimalFormat("#,##0.00") }
    val dateFormatter = remember { SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault()) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Exchange ${transaction.fromCurrency.name} to ${transaction.toCurrency.name}",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "-${amountFormatter.format(transaction.fromAmount)} ${transaction.fromCurrency.name}",
                    color = Color.Red,
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = "+${amountFormatter.format(transaction.toAmount)} ${transaction.toCurrency.name}",
                    color = Color(0xFF008000),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = dateFormatter.format(Date(transaction.date)),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
