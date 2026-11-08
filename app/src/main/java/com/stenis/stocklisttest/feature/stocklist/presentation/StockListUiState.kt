package com.stenis.stocklisttest.feature.stocklist.presentation

import androidx.compose.runtime.Immutable
import com.stenis.stocklisttest.feature.stocklist.presentation.model.StockItemUiModel
import com.stenis.stocklisttest.feature.stocklist.presentation.model.StockSortOrder

/**
 * 股票清單畫面在 UiState.Success 時的資料。
 *
 * @param stocks 股票卡片清單，已依 [sortOrder] 排序；可能為空清單
 * @param sortOrder 目前的排序方式（bottom sheet 標示選取項目）
 * @param isRefreshing 是否正在下拉刷新（保留舊資料顯示）
 */
@Immutable
data class StockListUiState(
    val stocks: List<StockItemUiModel>,
    val sortOrder: StockSortOrder = StockSortOrder.DEFAULT,
    val isRefreshing: Boolean = false,
)
