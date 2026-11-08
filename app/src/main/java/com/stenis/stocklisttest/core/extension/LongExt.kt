package com.stenis.stocklisttest.core.extension

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

// DecimalFormat 非執行緒安全，每個執行緒各自持有一份
private val thousandsFormat = object : ThreadLocal<DecimalFormat>() {
    override fun initialValue() = DecimalFormat("#,##0", DecimalFormatSymbols(Locale.TAIWAN))
}

/**
 * 千分位整數。
 * 例：68650665000 → "68,650,665,000"；null → "-"
 */
fun Long?.toThousandsString(): String =
    this?.let { thousandsFormat.get()!!.format(it) } ?: EMPTY_PLACEHOLDER
