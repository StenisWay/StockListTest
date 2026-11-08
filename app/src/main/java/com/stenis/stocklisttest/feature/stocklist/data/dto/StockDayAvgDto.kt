package com.stenis.stocklisttest.feature.stocklist.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** STOCK_DAY_AVG_ALL：上市個股日收盤價及月平均價（API 數值皆為字串，可能為空字串） */
@Serializable
data class StockDayAvgDto(
    /** 日期（民國年，例：1151106） */
    @SerialName("Date") val date: String = "",
    /** 證券代號 */
    @SerialName("Code") val code: String = "",
    /** 證券名稱 */
    @SerialName("Name") val name: String = "",
    /** 收盤價 */
    @SerialName("ClosingPrice") val closingPrice: String = "",
    /** 月平均價 */
    @SerialName("MonthlyAveragePrice") val monthlyAveragePrice: String = "",
)
