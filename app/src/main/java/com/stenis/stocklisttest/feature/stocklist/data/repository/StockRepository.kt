package com.stenis.stocklisttest.feature.stocklist.data.repository

import com.stenis.stocklisttest.feature.stocklist.data.datasource.StockRemoteDataSource
import com.stenis.stocklisttest.feature.stocklist.data.mapper.mapToStockItems
import com.stenis.stocklisttest.feature.stocklist.presentation.model.StockItemUiModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext

/**
 * 上市個股資料來源。
 *
 * 不需要即時報價，取得 API 資料並轉換後快取在記憶體（Koin singleton，App 存活期間有效），
 * 之後有快取就直接回傳，只有 [forceRefresh] 時才重新呼叫 API。
 */
class StockRepository(
    private val remoteDataSource: StockRemoteDataSource,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default,
) {

    // 避免同時多次呼叫 API，也確保快取讀寫一致
    private val mutex = Mutex()
    private var cachedStocks: List<StockItemUiModel>? = null

    /**
     * 取得最近一個交易日的上市個股卡片資料。
     *
     * @param forceRefresh true 時忽略快取重新呼叫 API（下拉刷新）
     */
    suspend fun getStocks(forceRefresh: Boolean = false): List<StockItemUiModel> = mutex.withLock {
        if (!forceRefresh) {
            cachedStocks?.let { return@withLock it }
        }
        fetchStocksFromApi().also { cachedStocks = it }
    }

    /** 同時呼叫三支 API，將 DTO 依股票代號合併並轉成卡片資料 */
    private suspend fun fetchStocksFromApi(): List<StockItemUiModel> = coroutineScope {
        // 三支 API 互不相依，同時發出
        val dayAll = async { remoteDataSource.getStockDayAll() }
        val dayAverages = async { remoteDataSource.getStockDayAverages() }
        val valuations = async { remoteDataSource.getStockValuations() }

        // 月平均價約 2.8 萬筆（含權證），轉換放到背景執行緒
        withContext(dispatcher) {
            mapToStockItems(dayAll.await(), dayAverages.await(), valuations.await())
        }
    }
}
