package com.stenis.stocklisttest.feature.stocklist.domain.usecase

import com.stenis.stocklisttest.feature.stocklist.presentation.model.StockItemUiModel

/**
 * 股票代號重複性檢查：同一代號只保留第一筆。
 *
 * 清單以股票代號作為 LazyColumn 的 key，重複的 key 會造成閃退。
 */
class RemoveDuplicateStockCodeUseCase {

    operator fun invoke(stocks: List<StockItemUiModel>): List<StockItemUiModel> =
        stocks.distinctBy { it.code }
}
