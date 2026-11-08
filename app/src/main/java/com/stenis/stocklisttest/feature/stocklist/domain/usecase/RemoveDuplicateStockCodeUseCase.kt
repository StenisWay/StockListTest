package com.stenis.stocklisttest.feature.stocklist.domain.usecase

import com.stenis.stocklisttest.feature.stocklist.presentation.model.StockItemUiModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

/**
 * 股票代號重複性檢查：同一代號只保留第一筆，於 [defaultDispatcher] 執行。
 *
 * 清單以股票代號作為 LazyColumn 的 key，重複的 key 會造成閃退。
 */
class RemoveDuplicateStockCodeUseCase(
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default,
) {

    operator fun invoke(stocks: Flow<List<StockItemUiModel>>): Flow<List<StockItemUiModel>> =
        stocks
            .map { list -> list.distinctBy { it.code } }
            .flowOn(defaultDispatcher)
}
