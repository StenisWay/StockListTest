package com.stenis.stocklisttest.feature.stocklist.data.repository

import com.stenis.stocklisttest.core.extension.toDecimalString
import com.stenis.stocklisttest.core.extension.toDoubleOrNullSafe
import com.stenis.stocklisttest.core.extension.toLongOrNullSafe
import com.stenis.stocklisttest.core.extension.toPercentString
import com.stenis.stocklisttest.core.extension.toSignedString
import com.stenis.stocklisttest.core.extension.toThousandsString
import com.stenis.stocklisttest.feature.stocklist.data.datasource.StockRemoteDataSource
import com.stenis.stocklisttest.feature.stocklist.data.dto.StockDayAllDto
import com.stenis.stocklisttest.feature.stocklist.data.dto.StockDayAvgDto
import com.stenis.stocklisttest.feature.stocklist.data.dto.StockValuationDto
import com.stenis.stocklisttest.feature.stocklist.presentation.model.PriceTrend
import com.stenis.stocklisttest.feature.stocklist.presentation.model.StockItemUiModel
import com.stenis.stocklisttest.feature.stocklist.presentation.model.StockValuationUiModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

/**
 * 上市個股資料來源。
 *
 * 不需要即時報價，取得 API 資料並轉換後快取在記憶體（Koin singleton，App 存活期間有效），
 * 之後有快取就直接回傳，只有 [getStocks] 帶 forceRefresh 時才重新呼叫 API。
 * API 呼叫、DTO 轉換與快取讀寫都在 [ioDispatcher] 執行。
 */
class StockRepository(
    private val remoteDataSource: StockRemoteDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
) {

    // 避免同時多次呼叫 API，也確保快取讀寫一致
    private val mutex = Mutex()
    private var cachedStocks: List<StockItemUiModel>? = null

    /**
     * 取得最近一個交易日的上市個股卡片資料。
     *
     * @param forceRefresh true 時忽略快取重新呼叫 API（下拉刷新）
     */
    fun getStocks(forceRefresh: Boolean = false): Flow<List<StockItemUiModel>> = flow {
        val stocks = mutex.withLock {
            val cached = cachedStocks
            if (!forceRefresh && cached != null) {
                cached
            } else {
                fetchStocksFromApi().also { cachedStocks = it }
            }
        }
        emit(stocks)
    }.flowOn(ioDispatcher)

    /** 同時呼叫三支 API，將 DTO 依股票代號合併並轉成卡片資料 */
    private suspend fun fetchStocksFromApi(): List<StockItemUiModel> = coroutineScope {
        // 三支 API 互不相依，同時發出
        val dayAll = async { remoteDataSource.getStockDayAll() }
        val dayAverages = async { remoteDataSource.getStockDayAverages() }
        val valuations = async { remoteDataSource.getStockValuations() }

        mergeToStockItems(dayAll.await(), dayAverages.await(), valuations.await())
    }

    /**
     * 以日成交資訊為主，依股票代號合併月平均價與估值資料。
     * 找不到對應資料或 API 給空字串的欄位顯示佔位符號。
     */
    private fun mergeToStockItems(
        dayAll: List<StockDayAllDto>,
        dayAverages: List<StockDayAvgDto>,
        valuations: List<StockValuationDto>,
    ): List<StockItemUiModel> {
        val dayAvgByCode = dayAverages.associateBy { it.code.trim() }
        val valuationByCode = valuations.associateBy { it.code.trim() }
        return dayAll.map { day ->
            val code = day.code.trim()
            day.toStockItem(dayAvgByCode[code], valuationByCode[code])
        }
    }

    private fun StockDayAllDto.toStockItem(
        dayAvg: StockDayAvgDto?,
        valuation: StockValuationDto?,
    ): StockItemUiModel {
        val code = code.trim()
        val name = name.trim()
        val closing = closingPrice.toDoubleOrNullSafe()
        val monthlyAverage = dayAvg?.monthlyAveragePrice.toDoubleOrNullSafe()
        val changeValue = change.toDoubleOrNullSafe()

        return StockItemUiModel(
            code = code,
            name = name,
            openingPrice = openingPrice.toDoubleOrNullSafe().toDecimalString(),
            closingPrice = closing.toDecimalString(),
            // 收盤價高於月平均價紅字、低於綠字
            closingPriceTrend = compareTrend(closing, monthlyAverage),
            highestPrice = highestPrice.toDoubleOrNullSafe().toDecimalString(),
            lowestPrice = lowestPrice.toDoubleOrNullSafe().toDecimalString(),
            change = changeValue.toSignedString(),
            // 漲跌價差正紅、負綠
            changeTrend = compareTrend(changeValue, 0.0),
            monthlyAveragePrice = monthlyAverage.toDecimalString(),
            transaction = transaction.toLongOrNullSafe().toThousandsString(),
            tradeVolume = tradeVolume.toLongOrNullSafe().toThousandsString(),
            tradeValue = tradeValue.toLongOrNullSafe().toThousandsString(),
            valuation = StockValuationUiModel(
                code = code,
                name = name,
                peRatio = valuation?.peRatio.toDoubleOrNullSafe().toDecimalString(),
                dividendYield = valuation?.dividendYield.toDoubleOrNullSafe().toPercentString(),
                pbRatio = valuation?.pbRatio.toDoubleOrNullSafe().toDecimalString(),
            ),
        )
    }

    /** value 高於 base 為 UP、低於為 DOWN，任一缺資料或相等為 FLAT */
    private fun compareTrend(value: Double?, base: Double?): PriceTrend {
        if (value == null || base == null) return PriceTrend.FLAT
        return when {
            value > base -> PriceTrend.UP
            value < base -> PriceTrend.DOWN
            else -> PriceTrend.FLAT
        }
    }
}
