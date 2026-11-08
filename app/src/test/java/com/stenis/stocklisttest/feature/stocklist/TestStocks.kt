package com.stenis.stocklisttest.feature.stocklist

import com.stenis.stocklisttest.feature.stocklist.presentation.model.PriceTrend
import com.stenis.stocklisttest.feature.stocklist.presentation.model.StockItemUiModel
import com.stenis.stocklisttest.feature.stocklist.presentation.model.StockValuationUiModel

/** 測試用：只關心代號與名稱的卡片資料 */
fun testStock(code: String, name: String = "股票$code") = StockItemUiModel(
    code = code,
    name = name,
    openingPrice = "-",
    closingPrice = "-",
    closingPriceTrend = PriceTrend.FLAT,
    highestPrice = "-",
    lowestPrice = "-",
    change = "-",
    changeTrend = PriceTrend.FLAT,
    monthlyAveragePrice = "-",
    transaction = "-",
    tradeVolume = "-",
    tradeValue = "-",
    valuation = StockValuationUiModel(code, name, "-", "-", "-"),
)
