package com.stenis.stocklisttest.feature.stocklist.presentation

import com.stenis.stocklisttest.core.presentation.UiIntent
import com.stenis.stocklisttest.feature.stocklist.presentation.model.StockItemUiModel
import com.stenis.stocklisttest.feature.stocklist.presentation.model.StockSortOrder

/** 股票清單畫面的使用者意圖 */
sealed interface StockListIntent : UiIntent {

    /** 載入股票清單（首次進入、重試），有快取時使用快取 */
    data object GetStockItemList : StockListIntent

    /** 下拉刷新：忽略快取重新呼叫 API */
    data object RefreshStockItemList : StockListIntent

    /** 變更排序方式 */
    data class ChangeStockOrder(val order: StockSortOrder) : StockListIntent

    /** 點擊右上列表 icon，開啟排序選項 */
    data object ShowStockOptionBottomSheet : StockListIntent

    /** 點擊股票卡片，顯示估值資訊 */
    data class ShowStockValuation(val stock: StockItemUiModel) : StockListIntent

    /** 切換深色 / 淺色主題 */
    data class ChangeThemeMode(val isDarkTheme: Boolean) : StockListIntent
}
