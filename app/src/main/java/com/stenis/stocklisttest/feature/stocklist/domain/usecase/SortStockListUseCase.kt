package com.stenis.stocklisttest.feature.stocklist.domain.usecase

import com.stenis.stocklisttest.feature.stocklist.presentation.model.StockItemUiModel
import com.stenis.stocklisttest.feature.stocklist.presentation.model.StockSortOrder
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

/** 依排序方式排列股票清單，於 [defaultDispatcher] 執行 */
class SortStockListUseCase(
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default,
) {

    operator fun invoke(
        stocks: Flow<List<StockItemUiModel>>,
        order: StockSortOrder,
    ): Flow<List<StockItemUiModel>> =
        stocks
            .map { list ->
                when (order) {
                    StockSortOrder.CODE_DESCENDING -> list.sortedByDescending { it.code }
                    StockSortOrder.CODE_ASCENDING -> list.sortedBy { it.code }
                }
            }
            .flowOn(defaultDispatcher)
}
