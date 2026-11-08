package com.stenis.stocklisttest.core.extension

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

// DecimalFormat 非執行緒安全，每個執行緒各自持有一份
private val decimalFormat = threadLocalFormat("#,##0.##")
private val signedFormat = threadLocalFormat("+#,##0.##;-#,##0.##")

private fun threadLocalFormat(pattern: String) = object : ThreadLocal<DecimalFormat>() {
    override fun initialValue() = DecimalFormat(pattern, DecimalFormatSymbols(Locale.TAIWAN))
}

/**
 * 千分位、小數最多兩位並去掉多餘的 0。
 * 例：2135.0 → "2,135"；14.5 → "14.5"；33.65 → "33.65"；null → "-"
 */
fun Double?.toDecimalString(): String =
    this?.let { decimalFormat.get()!!.format(it) } ?: EMPTY_PLACEHOLDER

/**
 * 帶正負號的數值，0 不加正負號。
 * 例：35.0 → "+35"；-0.05 → "-0.05"；0.0 → "0"；null → "-"
 */
fun Double?.toSignedString(): String = when {
    this == null -> EMPTY_PLACEHOLDER
    this == 0.0 -> decimalFormat.get()!!.format(0.0)
    else -> signedFormat.get()!!.format(this)
}

/**
 * 百分比（數值本身已是百分比，只加上 "%"）。
 * 例：2.99 → "2.99%"；3.9 → "3.9%"；null → "-"
 */
fun Double?.toPercentString(): String =
    this?.let { "${decimalFormat.get()!!.format(it)}%" } ?: EMPTY_PLACEHOLDER
