package com.stenis.stocklisttest.feature.stocklist.presentation.component

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.stenis.stocklisttest.R
import com.stenis.stocklisttest.feature.stocklist.data.mock.StockMockData
import com.stenis.stocklisttest.feature.stocklist.presentation.model.PriceTrend
import com.stenis.stocklisttest.feature.stocklist.presentation.model.StockItemUiModel
import com.stenis.stocklisttest.feature.stocklist.presentation.model.toStockItemUiModels
import com.stenis.stocklisttest.ui.theme.StockListTestTheme

/** 股票卡片：代號、名稱、價格資訊與成交資訊，點擊後顯示估值 */
@Composable
fun StockItemCard(
    stock: StockItemUiModel,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
        ),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = stock.code,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Text(
                text = stock.name,
                style = MaterialTheme.typography.titleLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Spacer(Modifier.height(12.dp))

            PriceRow(
                startLabel = R.string.stock_opening_price,
                startValue = stock.openingPrice,
                endLabel = R.string.stock_closing_price,
                endValue = stock.closingPrice,
                endColor = stock.closingPriceTrend.toColor(),
            )
            PriceRow(
                startLabel = R.string.stock_highest_price,
                startValue = stock.highestPrice,
                endLabel = R.string.stock_lowest_price,
                endValue = stock.lowestPrice,
            )
            PriceRow(
                startLabel = R.string.stock_change,
                startValue = stock.change,
                startColor = stock.changeTrend.toColor(),
                endLabel = R.string.stock_monthly_average_price,
                endValue = stock.monthlyAveragePrice,
            )

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 12.dp),
                color = MaterialTheme.colorScheme.outlineVariant,
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                TradeInfo(R.string.stock_transaction, stock.transaction, Modifier.weight(1f))
                TradeInfo(R.string.stock_trade_volume, stock.tradeVolume, Modifier.weight(1f))
                TradeInfo(R.string.stock_trade_value, stock.tradeValue, Modifier.weight(1.2f))
            }
        }
    }
}

@Composable
private fun PriceRow(
    @StringRes startLabel: Int,
    startValue: String,
    @StringRes endLabel: Int,
    endValue: String,
    startColor: Color = MaterialTheme.colorScheme.onSurface,
    endColor: Color = MaterialTheme.colorScheme.onSurface,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
    ) {
        PriceCell(startLabel, startValue, startColor, Modifier.weight(1f))
        Spacer(Modifier.width(16.dp))
        PriceCell(endLabel, endValue, endColor, Modifier.weight(1f))
    }
}

@Composable
private fun PriceCell(
    @StringRes label: Int,
    value: String,
    valueColor: Color,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(label),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(
            text = value,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Medium,
            color = valueColor,
            textAlign = TextAlign.End,
            maxLines = 1,
        )
    }
}

@Composable
private fun TradeInfo(
    @StringRes label: Int,
    value: String,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Text(
            text = stringResource(label),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun StockItemCardPreview() {
    StockListTestTheme {
        StockItemCard(
            stock = StockMockData.stocks.toStockItemUiModels().first(),
            onClick = {},
            modifier = Modifier.padding(16.dp),
        )
    }
}
