package com.stenis.stocklisttest.feature.stocklist.presentation.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.Color
import com.stenis.stocklisttest.feature.stocklist.presentation.model.PriceTrend
import com.stenis.stocklisttest.ui.theme.AppTheme

/** 依價格趨勢取得目前主題對應的顏色 */
@Composable
@ReadOnlyComposable
fun PriceTrend.toColor(): Color = when (this) {
    PriceTrend.UP -> AppTheme.colors.priceUp
    PriceTrend.DOWN -> AppTheme.colors.priceDown
    PriceTrend.FLAT -> AppTheme.colors.priceUnchanged
}
