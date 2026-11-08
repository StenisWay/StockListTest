package com.stenis.stocklisttest.feature.stocklist.data.mock

import com.stenis.stocklisttest.feature.stocklist.data.mapper.StockDayAllDto
import com.stenis.stocklisttest.feature.stocklist.data.mapper.StockDayAvgDto
import com.stenis.stocklisttest.feature.stocklist.data.mapper.StockValuationDto
import com.stenis.stocklisttest.feature.stocklist.data.mapper.mapToStockItems
import com.stenis.stocklisttest.feature.stocklist.presentation.model.StockItemUiModel

/**
 * 開發畫面用的假資料（不呼叫 API），格式與三支 API 的回應相同（數值皆為字串）。
 * 涵蓋上漲 / 下跌 / 平盤、收盤價高於 / 低於月均價、虧損無本益比、ETF 無估值、無成交等情境。
 */
object StockMockData {

    private const val DATE = "1151106"

    /** STOCK_DAY_ALL：代號、名稱、成交股數、成交金額、開、高、低、收、漲跌、成交筆數 */
    val dayAll: List<StockDayAllDto> = listOf(
        // 上漲、收盤高於月均
        dayAll("2330", "台積電", "32154879", "68650665000", "2100.00", "2150.00", "2095.00", "2135.00", "35.0000", "98765"),
        // 下跌、收盤低於月均
        dayAll("2317", "鴻海", "45678912", "10163558000", "228.50", "230.00", "221.00", "222.50", "-6.0000", "65432"),
        // 平盤、收盤等於月均
        dayAll("2454", "聯發科", "3456789", "5029628000", "1450.00", "1465.00", "1440.00", "1455.00", "0.0000", "12345"),
        // 上漲、收盤低於月均
        dayAll("2412", "中華電", "9876543", "1274074000", "128.00", "129.50", "127.50", "129.00", "1.0000", "8765"),
        // 下跌、收盤高於月均
        dayAll("2882", "國泰金", "34567890", "2350617000", "68.90", "69.20", "67.80", "68.00", "-0.9000", "23456"),
        // 虧損公司：本益比為空字串
        dayAll("2409", "友達", "87654321", "1271000000", "14.20", "14.60", "14.10", "14.55", "0.3500", "34567"),
        dayAll("1101", "台泥", "12345678", "415432000", "33.40", "33.80", "33.20", "33.65", "0.2500", "9876"),
        dayAll("2603", "長榮", "23456789", "4832100000", "210.00", "212.50", "205.50", "206.00", "-4.5000", "45678"),
        // ETF：沒有估值資料
        dayAll("0050", "元大台灣50", "21098765", "4216589000", "198.50", "200.20", "198.00", "199.85", "1.6000", "54321"),
        dayAll("00878", "國泰永續高股息", "98765432", "2182716000", "22.15", "22.30", "22.05", "22.10", "-0.0500", "76543"),
        // 當日無成交：價量皆為空字串
        dayAll("00625K", "富邦上証+R", "", "", "", "", "", "", "0.0000", ""),
        dayAll("6505", "台塑化", "4567890", "204184000", "45.00", "45.35", "44.60", "44.70", "-0.3000", "6543"),
    )

    /** STOCK_DAY_AVG_ALL：代號、名稱、收盤價、月平均價 */
    val dayAverages: List<StockDayAvgDto> = listOf(
        dayAvg("2330", "台積電", "2135.00", "2052.00"),
        dayAvg("2317", "鴻海", "222.50", "231.40"),
        dayAvg("2454", "聯發科", "1455.00", "1455.00"),
        dayAvg("2412", "中華電", "129.00", "130.20"),
        dayAvg("2882", "國泰金", "68.00", "66.50"),
        dayAvg("2409", "友達", "14.55", "14.10"),
        dayAvg("1101", "台泥", "33.65", "33.10"),
        dayAvg("2603", "長榮", "206.00", "215.80"),
        dayAvg("0050", "元大台灣50", "199.85", "195.30"),
        dayAvg("00878", "國泰永續高股息", "22.10", "22.40"),
        dayAvg("00625K", "富邦上証+R", "", "18.20"),
        dayAvg("6505", "台塑化", "44.70", "46.10"),
    )

    /** BWIBBU_ALL：代號、名稱、本益比、殖利率(%)、股價淨值比（ETF 不在此清單） */
    val valuations: List<StockValuationDto> = listOf(
        valuation("2330", "台積電", "25.32", "1.45", "7.85"),
        valuation("2317", "鴻海", "15.61", "2.47", "2.31"),
        valuation("2454", "聯發科", "20.12", "3.78", "5.42"),
        valuation("2412", "中華電", "26.48", "3.73", "2.68"),
        valuation("2882", "國泰金", "9.87", "4.41", "1.32"),
        valuation("2409", "友達", "", "2.07", "0.65"),
        valuation("1101", "台泥", "", "2.99", "0.87"),
        valuation("2603", "長榮", "4.12", "9.71", "1.08"),
        valuation("6505", "台塑化", "38.53", "1.12", "1.75"),
    )

    /** 已轉換好的卡片資料，供 Compose Preview 使用 */
    val stockItems: List<StockItemUiModel> by lazy { mapToStockItems(dayAll, dayAverages, valuations) }

    private fun dayAll(
        code: String,
        name: String,
        tradeVolume: String,
        tradeValue: String,
        openingPrice: String,
        highestPrice: String,
        lowestPrice: String,
        closingPrice: String,
        change: String,
        transaction: String,
    ) = StockDayAllDto(
        date = DATE,
        code = code,
        name = name,
        tradeVolume = tradeVolume,
        tradeValue = tradeValue,
        openingPrice = openingPrice,
        highestPrice = highestPrice,
        lowestPrice = lowestPrice,
        closingPrice = closingPrice,
        change = change,
        transaction = transaction,
    )

    private fun dayAvg(code: String, name: String, closingPrice: String, monthlyAveragePrice: String) =
        StockDayAvgDto(
            date = DATE,
            code = code,
            name = name,
            closingPrice = closingPrice,
            monthlyAveragePrice = monthlyAveragePrice,
        )

    private fun valuation(code: String, name: String, peRatio: String, dividendYield: String, pbRatio: String) =
        StockValuationDto(
            date = DATE,
            code = code,
            name = name,
            peRatio = peRatio,
            dividendYield = dividendYield,
            pbRatio = pbRatio,
        )
}
