package com.stenis.stocklisttest.feature.stocklist.presentation.model

import com.stenis.stocklisttest.feature.stocklist.domain.model.Stock
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

/** 沒有資料時顯示的佔位符號 */
const val EMPTY_VALUE = "-"

/** 將 domain model 轉成畫面顯示用的 UI model（格式化數字、判斷紅綠） */
fun List<Stock>.toStockItemUiModels(): List<StockItemUiModel> {
    // DecimalFormat 非執行緒安全，每次轉換建立一組
    val formatter = StockNumberFormatter()
    return map { it.toStockItemUiModel(formatter) }
}

private fun Stock.toStockItemUiModel(formatter: StockNumberFormatter) = StockItemUiModel(
    code = code,
    name = name,
    openingPrice = formatter.price(openingPrice),
    closingPrice = formatter.price(closingPrice),
    closingPriceTrend = compareTrend(closingPrice, monthlyAveragePrice),
    highestPrice = formatter.price(highestPrice),
    lowestPrice = formatter.price(lowestPrice),
    change = formatter.change(change),
    changeTrend = compareTrend(change, 0.0),
    monthlyAveragePrice = formatter.price(monthlyAveragePrice),
    transaction = formatter.count(transaction),
    tradeVolume = formatter.count(tradeVolume),
    tradeValue = formatter.count(tradeValue),
    valuation = StockValuationUiModel(
        code = code,
        name = name,
        peRatio = formatter.price(peRatio),
        dividendYield = formatter.price(dividendYield),
        pbRatio = formatter.price(pbRatio),
    ),
)

/** value 高於 base 為 UP、低於為 DOWN，任一缺資料或相等為 FLAT */
internal fun compareTrend(value: Double?, base: Double?): PriceTrend {
    if (value == null || base == null) return PriceTrend.FLAT
    return when {
        value > base -> PriceTrend.UP
        value < base -> PriceTrend.DOWN
        else -> PriceTrend.FLAT
    }
}

private class StockNumberFormatter {
    private val symbols = DecimalFormatSymbols(Locale.TAIWAN)
    private val priceFormat = DecimalFormat("#,##0.00", symbols)
    private val changeFormat = DecimalFormat("+#,##0.00;-#,##0.00", symbols)
    private val countFormat = DecimalFormat("#,##0", symbols)

    fun price(value: Double?): String = value?.let(priceFormat::format) ?: EMPTY_VALUE

    /** 正數加 "+"，0 不加正負號 */
    fun change(value: Double?): String = when {
        value == null -> EMPTY_VALUE
        value == 0.0 -> priceFormat.format(0.0)
        else -> changeFormat.format(value)
    }

    fun count(value: Long?): String = value?.let(countFormat::format) ?: EMPTY_VALUE
}
