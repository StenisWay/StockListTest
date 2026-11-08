package com.stenis.stocklisttest.feature.stocklist.data.mapper

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** STOCK_DAY_ALL：上市個股日成交資訊（API 數值皆為字串，可能為空字串） */
@Serializable
data class  StockDayAllDto(
    /** 日期（民國年，例：1151106） */
    @SerialName("Date") val date: String = "",
    /** 證券代號 */
    @SerialName("Code") val code: String = "",
    /** 證券名稱 */
    @SerialName("Name") val name: String = "",
    /** 成交股數 */
    @SerialName("TradeVolume") val tradeVolume: String = "",
    /** 成交金額 */
    @SerialName("TradeValue") val tradeValue: String = "",
    /** 開盤價 */
    @SerialName("OpeningPrice") val openingPrice: String = "",
    /** 最高價 */
    @SerialName("HighestPrice") val highestPrice: String = "",
    /** 最低價 */
    @SerialName("LowestPrice") val lowestPrice: String = "",
    /** 收盤價 */
    @SerialName("ClosingPrice") val closingPrice: String = "",
    /** 漲跌價差 */
    @SerialName("Change") val change: String = "",
    /** 成交筆數 */
    @SerialName("Transaction") val transaction: String = "",
)
