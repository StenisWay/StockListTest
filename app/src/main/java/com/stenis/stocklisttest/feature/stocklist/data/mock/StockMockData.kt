package com.stenis.stocklisttest.feature.stocklist.data.mock

import com.stenis.stocklisttest.feature.stocklist.domain.model.Stock

/**
 * 開發畫面用的假資料（不呼叫 API），也供 Compose Preview 使用。
 * 涵蓋上漲 / 下跌 / 平盤、收盤價高於 / 低於月均價、缺估值（ETF）、無成交等情境。
 */
object StockMockData {

    val stocks: List<Stock> = listOf(
        // 上漲、收盤高於月均
        mockStock("2330", "台積電", 2100.0, 2150.0, 2095.0, 2135.0, 35.0, 2052.0, 98_765, 32_154_879, 68_650_665_000, 25.32, 1.45, 7.85),
        // 下跌、收盤低於月均
        mockStock("2317", "鴻海", 228.5, 230.0, 221.0, 222.5, -6.0, 231.4, 65_432, 45_678_912, 10_163_558_000, 15.61, 2.47, 2.31),
        // 平盤、收盤等於月均
        mockStock("2454", "聯發科", 1450.0, 1465.0, 1440.0, 1455.0, 0.0, 1455.0, 12_345, 3_456_789, 5_029_628_000, 20.12, 3.78, 5.42),
        // 上漲、收盤低於月均
        mockStock("2412", "中華電", 128.0, 129.5, 127.5, 129.0, 1.0, 130.2, 8_765, 9_876_543, 1_274_074_000, 26.48, 3.73, 2.68),
        // 下跌、收盤高於月均
        mockStock("2882", "國泰金", 68.9, 69.2, 67.8, 68.0, -0.9, 66.5, 23_456, 34_567_890, 2_350_617_000, 9.87, 4.41, 1.32),
        // 虧損公司：沒有本益比
        mockStock("2409", "友達", 14.2, 14.6, 14.1, 14.5, 0.35, 14.1, 34_567, 87_654_321, 1_271_000_000, null, 2.07, 0.65),
        mockStock("1101", "台泥", 33.4, 33.8, 33.2, 33.65, 0.25, 33.1, 9_876, 12_345_678, 415_432_000, null, 2.99, 0.87),
        mockStock("2603", "長榮", 210.0, 212.5, 205.5, 206.0, -4.5, 215.8, 45_678, 23_456_789, 4_832_100_000, 4.12, 9.71, 1.08),
        // ETF：沒有估值資料
        mockStock("0050", "元大台灣50", 198.5, 200.2, 198.0, 199.85, 1.6, 195.3, 54_321, 21_098_765, 4_216_589_000, null, null, null),
        mockStock("00878", "國泰永續高股息", 22.15, 22.3, 22.05, 22.1, -0.05, 22.4, 76_543, 98_765_432, 2_182_716_000, null, null, null),
        // 當日無成交：價量皆無資料
        mockStock("00625K", "富邦上証+R", null, null, null, null, 0.0, 18.2, null, null, null, null, null, null),
        mockStock("6505", "台塑化", 45.0, 45.35, 44.6, 44.7, -0.3, 46.1, 6_543, 4_567_890, 204_184_000, 38.53, 1.12, 1.75),
    )

    private fun mockStock(
        code: String,
        name: String,
        openingPrice: Double?,
        highestPrice: Double?,
        lowestPrice: Double?,
        closingPrice: Double?,
        change: Double?,
        monthlyAveragePrice: Double?,
        transaction: Long?,
        tradeVolume: Long?,
        tradeValue: Long?,
        peRatio: Double?,
        dividendYield: Double?,
        pbRatio: Double?,
    ) = Stock(
        code = code,
        name = name,
        openingPrice = openingPrice,
        highestPrice = highestPrice,
        lowestPrice = lowestPrice,
        closingPrice = closingPrice,
        change = change,
        monthlyAveragePrice = monthlyAveragePrice,
        transaction = transaction,
        tradeVolume = tradeVolume,
        tradeValue = tradeValue,
        peRatio = peRatio,
        dividendYield = dividendYield,
        pbRatio = pbRatio,
    )
}
