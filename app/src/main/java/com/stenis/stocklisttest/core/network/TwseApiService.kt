package com.stenis.stocklisttest.core.network

import com.stenis.stocklisttest.feature.stocklist.data.dto.StockDayAllDto
import com.stenis.stocklisttest.feature.stocklist.data.dto.StockDayAvgDto
import com.stenis.stocklisttest.feature.stocklist.data.dto.StockValuationDto
import retrofit2.http.GET

/** 臺灣證券交易所 OpenAPI：https://openapi.twse.com.tw/ */
interface TwseApiService {

    /** 上市個股日本益比、殖利率及股價淨值比 */
    @GET("exchangeReport/BWIBBU_ALL")
    suspend fun getStockValuations(): List<StockValuationDto>

    /** 上市個股日收盤價及月平均價 */
    @GET("exchangeReport/STOCK_DAY_AVG_ALL")
    suspend fun getStockDayAverages(): List<StockDayAvgDto>

    /** 上市個股日成交資訊 */
    @GET("exchangeReport/STOCK_DAY_ALL")
    suspend fun getStockDayAll(): List<StockDayAllDto>
}
