package com.stenis.stocklisttest.feature.stocklist.data.repository

import com.stenis.stocklisttest.feature.stocklist.data.datasource.StockRemoteDataSource
import com.stenis.stocklisttest.feature.stocklist.data.mapper.mapToStockItems
import com.stenis.stocklisttest.feature.stocklist.data.mock.StockMockData
import com.stenis.stocklisttest.feature.stocklist.presentation.model.StockItemUiModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

class StockRepository(
    private val remoteDataSource: StockRemoteDataSource,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default,
) {

    /**
     * 取得最近一個交易日的上市個股卡片資料。
     *
     * 目前回傳假資料；串接 API 時改為 `return fetchStocksFromApi()`。
     */
    suspend fun getStocks(): List<StockItemUiModel> {
        // 模擬網路延遲，方便看到 loading
        delay(MOCK_DELAY_MILLIS)
        return withContext(dispatcher) {
            mapToStockItems(StockMockData.dayAll, StockMockData.dayAverages, StockMockData.valuations)
        }
    }

    /** 同時呼叫三支 API，以日成交資訊為主依股票代號合併 */
    @Suppress("unused") // 串接 API 時使用
    private suspend fun fetchStocksFromApi(): List<StockItemUiModel> = coroutineScope {
        // 三支 API 互不相依，同時發出
        val dayAll = async { remoteDataSource.getStockDayAll() }
        val dayAverages = async { remoteDataSource.getStockDayAverages() }
        val valuations = async { remoteDataSource.getStockValuations() }

        // 月平均價約 2.8 萬筆（含權證），合併放到背景執行緒
        withContext(dispatcher) {
            mapToStockItems(dayAll.await(), dayAverages.await(), valuations.await())
        }
    }

    private companion object {
        const val MOCK_DELAY_MILLIS = 800L
    }
}
