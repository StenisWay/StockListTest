package com.stenis.stocklisttest.feature.stocklist.data.mapper

import com.stenis.stocklisttest.feature.stocklist.presentation.model.PriceTrend
import com.stenis.stocklisttest.feature.stocklist.presentation.model.StockItemUiModel
import com.stenis.stocklisttest.feature.stocklist.presentation.model.StockValuationUiModel
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

/** 沒有資料時顯示的佔位符號 */
const val EMPTY_VALUE = "-"

/**
 * 以日成交資訊為主，依股票代號合併月平均價與估值資料，轉成卡片顯示資料。
 * 找不到對應資料或 API 給空字串的欄位顯示 [EMPTY_VALUE]。
 */
fun mapToStockItems(
    dayAll: List<StockDayAllDto>,
    dayAverages: List<StockDayAvgDto>,
    valuations: List<StockValuationDto>,
): List<StockItemUiModel> {
    val dayAvgByCode = dayAverages.associateBy { it.code.trim() }
    val valuationByCode = valuations.associateBy { it.code.trim() }
    // DecimalFormat 非執行緒安全，每次轉換建立一組
    val formatter = StockNumberFormatter()
    return dayAll.map { day ->
        val code = day.code.trim()
        day.toStockItem(dayAvgByCode[code], valuationByCode[code], formatter)
    }
}

private fun StockDayAllDto.toStockItem(
    dayAvg: StockDayAvgDto?,
    valuation: StockValuationDto?,
    formatter: StockNumberFormatter,
): StockItemUiModel {
    val code = code.trim()
    val name = name.trim()
    val closing = closingPrice.toDoubleOrNullSafe()
    val monthlyAverage = dayAvg?.monthlyAveragePrice.toDoubleOrNullSafe()
    val changeValue = change.toDoubleOrNullSafe()

    return StockItemUiModel(
        code = code,
        name = name,
        openingPrice = formatter.price(openingPrice.toDoubleOrNullSafe()),
        closingPrice = formatter.price(closing),
        closingPriceTrend = compareTrend(closing, monthlyAverage),
        highestPrice = formatter.price(highestPrice.toDoubleOrNullSafe()),
        lowestPrice = formatter.price(lowestPrice.toDoubleOrNullSafe()),
        change = formatter.change(changeValue),
        changeTrend = compareTrend(changeValue, 0.0),
        monthlyAveragePrice = formatter.price(monthlyAverage),
        transaction = formatter.count(transaction.toLongOrNullSafe()),
        tradeVolume = formatter.count(tradeVolume.toLongOrNullSafe()),
        tradeValue = formatter.count(tradeValue.toLongOrNullSafe()),
        valuation = StockValuationUiModel(
            code = code,
            name = name,
            peRatio = formatter.price(valuation?.peRatio.toDoubleOrNullSafe()),
            dividendYield = formatter.percent(valuation?.dividendYield.toDoubleOrNullSafe()),
            pbRatio = formatter.price(valuation?.pbRatio.toDoubleOrNullSafe()),
        ),
    )
}

/** value 高於 base 為 UP、低於為 DOWN，任一缺資料或相等為 FLAT */
internal fun compareTrend(value: Double?, base: Double?): PriceTrend {
    if (value == null || base == null) return PriceTrend.FLAT
    return when {
        value > base -> PriceTrend.UP
        value < base -> PriceTrend.DOWN
        else -> PriceTrend.FLAT
    }
}

/** API 數值為字串，可能是空字串或含千分位逗號 */
private fun String?.toDoubleOrNullSafe(): Double? =
    this?.replace(",", "")?.trim()?.toDoubleOrNull()

private fun String?.toLongOrNullSafe(): Long? =
    this?.replace(",", "")?.trim()?.toLongOrNull()

/** 數字格式：千分位，小數最多兩位且去掉多餘的 0（2135.00 → 2,135；14.50 → 14.5） */
private class StockNumberFormatter {
    private val symbols = DecimalFormatSymbols(Locale.TAIWAN)
    private val decimalFormat = DecimalFormat("#,##0.##", symbols)
    private val signedFormat = DecimalFormat("+#,##0.##;-#,##0.##", symbols)
    private val countFormat = DecimalFormat("#,##0", symbols)

    fun price(value: Double?): String = value?.let(decimalFormat::format) ?: EMPTY_VALUE

    /** 正數加 "+"，0 不加正負號 */
    fun change(value: Double?): String = when {
        value == null -> EMPTY_VALUE
        value == 0.0 -> decimalFormat.format(0.0)
        else -> signedFormat.format(value)
    }

    fun percent(value: Double?): String = value?.let { "${decimalFormat.format(it)}%" } ?: EMPTY_VALUE

    fun count(value: Long?): String = value?.let(countFormat::format) ?: EMPTY_VALUE
}
