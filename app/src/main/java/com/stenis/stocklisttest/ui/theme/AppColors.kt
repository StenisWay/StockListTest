package com.stenis.stocklisttest.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

/**
 * App 自訂的語意化顏色（Material ColorScheme 沒有涵蓋的部分）。
 *
 * 元件只依語意取色，淺色 / 深色由 [StockListTestTheme] 依目前主題切換。
 */
interface AppColors {
    /** 上漲 / 高於比較基準（台股慣例：紅） */
    val priceUp: Color

    /** 下跌 / 低於比較基準（台股慣例：綠） */
    val priceDown: Color

    /** 持平或無法比較 */
    val priceUnchanged: Color
}

/** 淺色主題 */
object LightAppColors : AppColors {
    override val priceUp = Red700
    override val priceDown = Green800
    override val priceUnchanged = Neutral900
}

/** 深色主題：使用較亮的色階維持對比 */
object DarkAppColors : AppColors {
    override val priceUp = Red300
    override val priceDown = Green400
    override val priceUnchanged = Neutral100
}

internal val LocalAppColors = staticCompositionLocalOf<AppColors> { LightAppColors }

internal val LocalDarkTheme = staticCompositionLocalOf { false }

/** 取得目前主題的設定，用法：`AppTheme.colors.priceUp`、`AppTheme.isDarkTheme` */
object AppTheme {
    /** 目前主題的 App 自訂顏色 */
    val colors: AppColors
        @Composable
        @ReadOnlyComposable
        get() = LocalAppColors.current

    /** 目前是否為深色主題 */
    val isDarkTheme: Boolean
        @Composable
        @ReadOnlyComposable
        get() = LocalDarkTheme.current
}
