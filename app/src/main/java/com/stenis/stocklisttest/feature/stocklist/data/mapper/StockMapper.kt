package com.stenis.stocklisttest.feature.stocklist.data.mapper

import com.stenis.stocklisttest.feature.stocklist.domain.model.Stock

/**
 * 以日成交資訊為主，依股票代號合併月平均價與估值資料。
 * 找不到對應資料的欄位為 null。
 */
internal fun StockDayAllDto.toStock(
    dayAvg: StockDayAvgDto?,
    valuation: StockValuationDto?,
): Stock = Stock(
    code = code.trim(),
    name = name.trim(),
    openingPrice = openingPrice.toDoubleOrNullSafe(),
    highestPrice = highestPrice.toDoubleOrNullSafe(),
    lowestPrice = lowestPrice.toDoubleOrNullSafe(),
    closingPrice = closingPrice.toDoubleOrNullSafe(),
    change = change.toDoubleOrNullSafe(),
    monthlyAveragePrice = dayAvg?.monthlyAveragePrice.toDoubleOrNullSafe(),
    transaction = transaction.toLongOrNullSafe(),
    tradeVolume = tradeVolume.toLongOrNullSafe(),
    tradeValue = tradeValue.toLongOrNullSafe(),
    peRatio = valuation?.peRatio.toDoubleOrNullSafe(),
    dividendYield = valuation?.dividendYield.toDoubleOrNullSafe(),
    pbRatio = valuation?.pbRatio.toDoubleOrNullSafe(),
)

/** API 數值為字串，可能是空字串或含千分位逗號 */
private fun String?.toDoubleOrNullSafe(): Double? =
    this?.replace(",", "")?.trim()?.toDoubleOrNull()

private fun String?.toLongOrNullSafe(): Long? =
    this?.replace(",", "")?.trim()?.toLongOrNull()
