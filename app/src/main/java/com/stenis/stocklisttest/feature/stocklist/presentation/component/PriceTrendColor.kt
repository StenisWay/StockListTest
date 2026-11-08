package com.stenis.stocklisttest.feature.stocklist.presentation.component

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.stenis.stocklisttest.feature.stocklist.presentation.model.PriceTrend

/** 紅漲綠跌，持平使用一般文字色 */
@Composable
fun PriceTrend.toColor(): Color {
    val isDark = isSystemInDarkTheme()
    return when (this) {
        PriceTrend.UP -> if (isDark) Color(0xFFFF6B6B) else Color(0xFFD32F2F)
        PriceTrend.DOWN -> if (isDark) Color(0xFF66BB6A) else Color(0xFF2E7D32)
        PriceTrend.FLAT -> MaterialTheme.colorScheme.onSurface
    }
}
