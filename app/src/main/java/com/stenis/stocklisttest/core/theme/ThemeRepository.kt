package com.stenis.stocklisttest.core.theme

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/** 主題模式 */
enum class ThemeMode {
    /** 跟隨系統設定（預設） */
    SYSTEM,
    LIGHT,
    DARK,
}

/** App 全域的主題模式，由 Koin 以 singleton 提供 */
class ThemeRepository {

    private val _themeMode = MutableStateFlow(ThemeMode.SYSTEM)
    val themeMode: StateFlow<ThemeMode> = _themeMode.asStateFlow()

    fun setThemeMode(mode: ThemeMode) {
        _themeMode.value = mode
    }
}
