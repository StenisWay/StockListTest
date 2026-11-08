package com.stenis.stocklisttest.feature.stocklist.data.mapper

import com.stenis.stocklisttest.core.extension.EMPTY_PLACEHOLDER
import com.stenis.stocklisttest.core.extension.toDecimalString
import com.stenis.stocklisttest.core.extension.toDoubleOrNullSafe
import com.stenis.stocklisttest.core.extension.toLongOrNullSafe
import com.stenis.stocklisttest.core.extension.toPercentString
import com.stenis.stocklisttest.core.extension.toSignedString
import com.stenis.stocklisttest.core.extension.toThousandsString
import com.stenis.stocklisttest.feature.stocklist.presentation.model.PriceTrend
import com.stenis.stocklisttest.feature.stocklist.presentation.model.StockItemUiModel
import com.stenis.stocklisttest.feature.stocklist.presentation.model.StockValuationUiModel

/**
 * 以日成交資訊為主，依股票代號合併月平均價與估值資料，轉成卡片顯示資料。
 * 找不到對應資料或 API 給空字串的欄位顯示 [EMPTY_PLACEHOLDER]。
 */
fun mapToStockItems(
    dayAll: List<StockDayAllDto>,
    dayAverages: List<StockDayAvgDto>,
    valuations: List<StockValuationDto>,
): List<StockItemUiModel> {
    val dayAvgByCode = dayAverages.associateBy { it.code.trim() }
    val valuationByCode = valuations.associateBy { it.code.trim() }
    return dayAll.map { day ->
        val code = day.code.trim()
        day.toStockItem(dayAvgByCode[code], valuationByCode[code])
    }
}

private fun StockDayAllDto.toStockItem(
    dayAvg: StockDayAvgDto?,
    valuation: StockValuationDto?,
): StockItemUiModel {
    val code = code.trim()
    val name = name.trim()
    val closing = closingPrice.toDoubleOrNullSafe()
    val monthlyAverage = dayAvg?.monthlyAveragePrice.toDoubleOrNullSafe()
    val changeValue = change.toDoubleOrNullSafe()

    return StockItemUiModel(
        code = code,
        name = name,
        openingPrice = openingPrice.toDoubleOrNullSafe().toDecimalString(),
        closingPrice = closing.toDecimalString(),
        closingPriceTrend = compareTrend(closing, monthlyAverage),
        highestPrice = highestPrice.toDoubleOrNullSafe().toDecimalString(),
        lowestPrice = lowestPrice.toDoubleOrNullSafe().toDecimalString(),
        change = changeValue.toSignedString(),
        changeTrend = compareTrend(changeValue, 0.0),
        monthlyAveragePrice = monthlyAverage.toDecimalString(),
        transaction = transaction.toLongOrNullSafe().toThousandsString(),
        tradeVolume = tradeVolume.toLongOrNullSafe().toThousandsString(),
        tradeValue = tradeValue.toLongOrNullSafe().toThousandsString(),
        valuation = StockValuationUiModel(
            code = code,
            name = name,
            peRatio = valuation?.peRatio.toDoubleOrNullSafe().toDecimalString(),
            dividendYield = valuation?.dividendYield.toDoubleOrNullSafe().toPercentString(),
            pbRatio = valuation?.pbRatio.toDoubleOrNullSafe().toDecimalString(),
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
