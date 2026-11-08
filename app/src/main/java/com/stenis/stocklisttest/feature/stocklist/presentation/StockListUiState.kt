package com.stenis.stocklisttest.feature.stocklist.presentation

import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import com.stenis.stocklisttest.core.presentation.UiState
import com.stenis.stocklisttest.feature.stocklist.presentation.model.StockItemUiModel
import com.stenis.stocklisttest.feature.stocklist.presentation.model.StockSortOrder

/**
 * 股票清單畫面狀態，Compose 以 `when` 決定要顯示什麼。
 *
 * 一次性的 UI 動作（顯示 dialog、bottom sheet、snackbar）放在 [StockListEffect]。
 */
sealed interface StockListUiState : UiState {

    /** 首次下載資料中（尚無任何資料） */
    data object Loading : StockListUiState

    /**
     * 已取得資料。
     *
     * @param stocks 股票卡片清單，已依 [sortOrder] 排序；可能為空清單
     * @param sortOrder 目前的排序方式
     * @param isRefreshing 是否正在下拉刷新（保留舊資料顯示）
     */
    @Immutable
    data class Success(
        val stocks: List<StockItemUiModel>,
        val sortOrder: StockSortOrder = StockSortOrder.DEFAULT,
        val isRefreshing: Boolean = false,
    ) : StockListUiState

    /** 沒有任何資料且載入失敗，整頁顯示錯誤與重試 */
    data class Failure(@StringRes val messageRes: Int) : StockListUiState
}
