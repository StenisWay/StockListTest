package com.stenis.stocklisttest.feature.stocklist.data.mapper

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** BWIBBU_ALL：上市個股日本益比、殖利率及股價淨值比（API 數值皆為字串，可能為空字串） */
@Serializable
data class StockValuationDto(
    /** 日期（民國年，例：1151106） */
    @SerialName("Date") val date: String = "",
    /** 證券代號 */
    @SerialName("Code") val code: String = "",
    /** 證券名稱 */
    @SerialName("Name") val name: String = "",
    /** 本益比 */
    @SerialName("PEratio") val peRatio: String = "",
    /** 殖利率(%) */
    @SerialName("DividendYield") val dividendYield: String = "",
    /** 股價淨值比 */
    @SerialName("PBratio") val pbRatio: String = "",
)
