package com.stenis.stocklisttest.feature.stocklist.presentation

import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import com.stenis.stocklisttest.core.presentation.UiState
import com.stenis.stocklisttest.feature.stocklist.presentation.model.StockItemUiModel
import com.stenis.stocklisttest.feature.stocklist.presentation.model.StockSortOrder

/**
 * 股票清單畫面狀態，Compose 只依這個狀態決定要顯示什麼。
 *
 * 一次性的 UI 動作（顯示 dialog、bottom sheet、snackbar）放在 [StockListEffect]。
 */
@Immutable
data class StockListUiState(
    /** 是否正在載入資料 */
    val isLoading: Boolean = false,
    /** 股票卡片清單，已依 [sortOrder] 排序 */
    val stocks: List<StockItemUiModel> = emptyList(),
    /** 目前的排序方式（預設依股票代號降序） */
    val sortOrder: StockSortOrder = StockSortOrder.CODE_DESCENDING,
    /** 清單為空且載入失敗時的錯誤訊息字串資源；null 表示沒有錯誤 */
    @StringRes val errorMessageRes: Int? = null,
) : UiState
