package com.stenis.stocklisttest.core.extension

/**
 * 將 API 回傳的數值字串轉成 Double。
 * 允許千分位逗號與前後空白；null、空字串或格式錯誤回傳 null。
 */
fun String?.toDoubleOrNullSafe(): Double? =
    this?.replace(",", "")?.trim()?.toDoubleOrNull()

/**
 * 將 API 回傳的數值字串轉成 Long。
 * 允許千分位逗號與前後空白；null、空字串或格式錯誤回傳 null。
 */
fun String?.toLongOrNullSafe(): Long? =
    this?.replace(",", "")?.trim()?.toLongOrNull()
