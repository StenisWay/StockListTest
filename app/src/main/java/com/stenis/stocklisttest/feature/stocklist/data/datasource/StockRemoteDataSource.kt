package com.stenis.stocklisttest.feature.stocklist.data.datasource

import com.stenis.stocklisttest.core.network.TwseApiService
import com.stenis.stocklisttest.feature.stocklist.data.dto.StockDayAllDto
import com.stenis.stocklisttest.feature.stocklist.data.dto.StockDayAvgDto
import com.stenis.stocklisttest.feature.stocklist.data.dto.StockValuationDto

/**
 * 股票清單的遠端資料來源，只負責呼叫臺灣證券交易所 OpenAPI，不做資料轉換或合併。
 *
 * - Base URL：https://openapi.twse.com.tw/v1/
 * - 三支 API 皆為 GET、無任何 request 參數（無 path / query / header / body），
 *   每次回傳「最近一個交易日」的全部上市個股資料。
 * - 回傳的數值欄位皆為字串，可能為空字串，轉換交給 mapper 處理。
 */
class StockRemoteDataSource(
    private val apiService: TwseApiService,
) {

    /**
     * 上市個股日本益比、殖利率及股價淨值比
     *
     * - Method / Path：GET /exchangeReport/BWIBBU_ALL
     * - Request 欄位：無
     * - 用途：取得每檔上市股票的本益比、殖利率(%)、股價淨值比，用於股票清單的估值資訊。
     *   沒有獲利的公司本益比會是空字串。
     */
    suspend fun getStockValuations(): List<StockValuationDto> =
        apiService.getStockValuations()

    /**
     * 上市個股日收盤價及月平均價
     *
     * - Method / Path：GET /exchangeReport/STOCK_DAY_AVG_ALL
     * - Request 欄位：無
     * - 用途：取得每檔上市證券的當日收盤價與當月平均價，用於比較股價與月均價的相對位置。
     */
    suspend fun getStockDayAverages(): List<StockDayAvgDto> =
        apiService.getStockDayAverages()

    /**
     * 上市個股日成交資訊
     *
     * - Method / Path：GET /exchangeReport/STOCK_DAY_ALL
     * - Request 欄位：無
     * - 用途：取得每檔上市證券當日的開高低收、漲跌價差、成交股數、成交金額與成交筆數，
     *   作為股票清單的主要報價資料。當日無成交的證券，價量欄位會是空字串。
     */
    suspend fun getStockDayAll(): List<StockDayAllDto> =
        apiService.getStockDayAll()
}
