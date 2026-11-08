package com.stenis.stocklisttest.feature.stocklist.presentation.preview

import com.stenis.stocklisttest.feature.stocklist.presentation.model.PriceTrend
import com.stenis.stocklisttest.feature.stocklist.presentation.model.StockItemUiModel
import com.stenis.stocklisttest.feature.stocklist.presentation.model.StockValuationUiModel

/** 僅供 Compose Preview 使用的範例資料，App 執行時不會用到 */
internal object StockPreviewData {

    val stocks: List<StockItemUiModel> = listOf(
        StockItemUiModel(
            code = "2330",
            name = "台積電",
            openingPrice = "2,100",
            closingPrice = "2,135",
            closingPriceTrend = PriceTrend.UP,
            highestPrice = "2,150",
            lowestPrice = "2,095",
            change = "+35",
            changeTrend = PriceTrend.UP,
            monthlyAveragePrice = "2,052",
            transaction = "98,765",
            tradeVolume = "32,154,879",
            tradeValue = "68,650,665,000",
            valuation = StockValuationUiModel("2330", "台積電", "25.32", "1.45%", "7.85"),
        ),
        StockItemUiModel(
            code = "2317",
            name = "鴻海",
            openingPrice = "228.5",
            closingPrice = "222.5",
            closingPriceTrend = PriceTrend.DOWN,
            highestPrice = "230",
            lowestPrice = "221",
            change = "-6",
            changeTrend = PriceTrend.DOWN,
            monthlyAveragePrice = "231.4",
            transaction = "65,432",
            tradeVolume = "45,678,912",
            tradeValue = "10,163,558,000",
            valuation = StockValuationUiModel("2317", "鴻海", "15.61", "2.47%", "2.31"),
        ),
        StockItemUiModel(
            code = "0050",
            name = "元大台灣50",
            openingPrice = "198.5",
            closingPrice = "199.85",
            closingPriceTrend = PriceTrend.UP,
            highestPrice = "200.2",
            lowestPrice = "198",
            change = "0",
            changeTrend = PriceTrend.FLAT,
            monthlyAveragePrice = "195.3",
            transaction = "54,321",
            tradeVolume = "21,098,765",
            tradeValue = "4,216,589,000",
            valuation = StockValuationUiModel("0050", "元大台灣50", "-", "-", "-"),
        ),
    )
}
