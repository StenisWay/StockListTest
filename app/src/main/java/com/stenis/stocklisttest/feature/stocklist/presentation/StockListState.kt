package com.stenis.stocklisttest.feature.stocklist.presentation

import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import com.stenis.stocklisttest.feature.stocklist.presentation.model.StockItemUiModel
import com.stenis.stocklisttest.feature.stocklist.presentation.model.StockSortOrder
import com.stenis.stocklisttest.feature.stocklist.presentation.model.StockValuationUiModel

/**
 * 股票清單畫面的 MVI State，畫面只依這個 State 繪製。
 */
@Immutable
data class StockListState(
    /** 是否正在載入資料 */
    val isLoading: Boolean = false,
    /** 股票卡片清單，已依 [sortOrder] 排序 */
    val stocks: List<StockItemUiModel> = emptyList(),
    /** 目前的排序方式（預設依股票代號降序） */
    val sortOrder: StockSortOrder = StockSortOrder.CODE_DESCENDING,
    /** 是否顯示排序 bottom sheet（點擊右上 filter icon 開啟） */
    val isSortSheetVisible: Boolean = false,
    /** 點擊卡片後要在 alert 顯示的估值資訊；null 表示不顯示 alert */
    val selectedValuation: StockValuationUiModel? = null,
    /** 載入失敗的錯誤訊息字串資源；null 表示沒有錯誤 */
    @StringRes val errorMessageRes: Int? = null,
)
