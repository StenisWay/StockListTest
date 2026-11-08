package com.stenis.stocklisttest.feature.stocklist.domain.usecase

import com.stenis.stocklisttest.feature.stocklist.presentation.model.StockItemUiModel
import com.stenis.stocklisttest.feature.stocklist.presentation.model.StockSortOrder

/** 依排序方式排列股票清單 */
class SortStockListUseCase {

    operator fun invoke(
        stocks: List<StockItemUiModel>,
        order: StockSortOrder,
    ): List<StockItemUiModel> = when (order) {
        StockSortOrder.CODE_DESCENDING -> stocks.sortedByDescending { it.code }
        StockSortOrder.CODE_ASCENDING -> stocks.sortedBy { it.code }
    }
}
