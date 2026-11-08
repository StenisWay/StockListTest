package com.stenis.stocklisttest

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.stenis.stocklisttest.core.theme.ThemeMode
import com.stenis.stocklisttest.core.theme.ThemeRepository
import com.stenis.stocklisttest.feature.stocklist.presentation.StockListRoute
import com.stenis.stocklisttest.ui.theme.StockListTestTheme
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {

    private val themeRepository: ThemeRepository by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val themeMode by themeRepository.themeMode.collectAsStateWithLifecycle()
            val isDarkTheme = when (themeMode) {
                ThemeMode.SYSTEM -> isSystemInDarkTheme()
                ThemeMode.LIGHT -> false
                ThemeMode.DARK -> true
            }

            // 主題與系統設定不同時，狀態列 / 導覽列圖示顏色也要跟著 App 主題
            DisposableEffect(isDarkTheme) {
                enableEdgeToEdge(
                    statusBarStyle = SystemBarStyle.auto(
                        lightScrim = Color.TRANSPARENT,
                        darkScrim = Color.TRANSPARENT,
                        detectDarkMode = { isDarkTheme },
                    ),
                    navigationBarStyle = SystemBarStyle.auto(
                        lightScrim = LIGHT_NAV_SCRIM,
                        darkScrim = DARK_NAV_SCRIM,
                        detectDarkMode = { isDarkTheme },
                    ),
                )
                onDispose {}
            }

            StockListTestTheme(darkTheme = isDarkTheme) {
                StockListRoute()
            }
        }
    }

    private companion object {
        // 與 androidx.activity 預設的導覽列遮罩相同
        val LIGHT_NAV_SCRIM = Color.argb(0xe6, 0xFF, 0xFF, 0xFF)
        val DARK_NAV_SCRIM = Color.argb(0x80, 0x1b, 0x1b, 0x1b)
    }
}
