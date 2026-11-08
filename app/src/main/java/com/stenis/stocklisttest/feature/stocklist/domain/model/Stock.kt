package com.stenis.stocklisttest.feature.stocklist.domain.model

/**
 * 上市個股資料（合併日成交資訊、月平均價、估值三支 API）。
 *
 * 數值欄位為 null 代表 API 未提供（空字串或無對應資料）。
 */
data class Stock(
    val code: String,
    val name: String,
    val openingPrice: Double?,
    val highestPrice: Double?,
    val lowestPrice: Double?,
    val closingPrice: Double?,
    /** 漲跌價差 */
    val change: Double?,
    val monthlyAveragePrice: Double?,
    /** 成交筆數 */
    val transaction: Long?,
    /** 成交股數 */
    val tradeVolume: Long?,
    /** 成交金額 */
    val tradeValue: Long?,
    /** 本益比 */
    val peRatio: Double?,
    /** 殖利率(%) */
    val dividendYield: Double?,
    /** 股價淨值比 */
    val pbRatio: Double?,
)
