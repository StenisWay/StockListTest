package com.stenis.stocklisttest.feature.stocklist.presentation.model

/** 排序 bottom sheet 的選項 */
enum class StockSortOrder {
    /** 依股票代號降序（預設） */
    CODE_DESCENDING,

    /** 依股票代號升序 */
    CODE_ASCENDING,
}

/** 依排序方式排列卡片 */
fun List<StockItemUiModel>.sortedBy(order: StockSortOrder): List<StockItemUiModel> =
    when (order) {
        StockSortOrder.CODE_DESCENDING -> sortedByDescending { it.code }
        StockSortOrder.CODE_ASCENDING -> sortedBy { it.code }
    }
