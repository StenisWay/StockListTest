package com.stenis.stocklisttest.feature.stocklist.presentation.model

import androidx.compose.runtime.Immutable

/**
 * 股票清單中一張卡片的顯示資料。
 *
 * 價格與數量都是已格式化好的顯示字串，沒有資料時由 mapper 填入佔位符號（例如 "-"）。
 */
@Immutable
data class StockItemUiModel(
    /** 股票代號 */
    val code: String,
    /** 股票名稱 */
    val name: String,
    /** 開盤價 */
    val openingPrice: String,
    /** 收盤價 */
    val closingPrice: String,
    /** 收盤價與月平均價比較：高於為 UP（紅字），低於為 DOWN（綠字） */
    val closingPriceTrend: PriceTrend,
    /** 最高價 */
    val highestPrice: String,
    /** 最低價 */
    val lowestPrice: String,
    /** 漲跌價差 */
    val change: String,
    /** 漲跌價差正負：正為 UP（紅字），負為 DOWN（綠字） */
    val changeTrend: PriceTrend,
    /** 月平均價 */
    val monthlyAveragePrice: String,
    /** 成交筆數 */
    val transaction: String,
    /** 成交股數 */
    val tradeVolume: String,
    /** 成交金額 */
    val tradeValue: String,
    /** 點擊卡片後 alert 要顯示的估值資訊 */
    val valuation: StockValuationUiModel,
)
