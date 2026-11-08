package com.stenis.stocklisttest.feature.stocklist.presentation.component

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.stenis.stocklisttest.R
import com.stenis.stocklisttest.feature.stocklist.presentation.model.StockValuationUiModel

/** 點擊卡片後顯示：本益比、殖利率(%)、股價淨值比 */
@Composable
fun StockValuationDialog(
    valuation: StockValuationUiModel,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.stock_valuation_title, valuation.code, valuation.name)) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                ValuationRow(R.string.stock_pe_ratio, valuation.peRatio)
                ValuationRow(R.string.stock_dividend_yield, valuation.dividendYield)
                ValuationRow(R.string.stock_pb_ratio, valuation.pbRatio)
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.stock_valuation_confirm))
            }
        },
    )
}

@Composable
private fun ValuationRow(@StringRes label: Int, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = stringResource(label),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.SemiBold,
        )
    }
}
