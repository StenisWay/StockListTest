package com.stenis.stocklisttest.feature.stocklist.presentation

import androidx.annotation.StringRes
import com.stenis.stocklisttest.core.presentation.UiEffect
import com.stenis.stocklisttest.feature.stocklist.presentation.model.StockValuationUiModel

/**
 * 由 ViewModel 發起、請 UI 執行的一次性動作（顯示 dialog、bottom sheet、snackbar、跳轉等）。
 * 每個 effect 只會被消費一次，不會因畫面重組或旋轉而重複觸發。
 */
sealed interface StockListEffect : UiEffect {

    /** 顯示排序 bottom sheet */
    data object ShowSortBottomSheet : StockListEffect

    /** 顯示估值 dialog（本益比、殖利率、股價淨值比） */
    data class ShowValuationDialog(val valuation: StockValuationUiModel) : StockListEffect

    /** 已有資料時重新整理失敗，以 snackbar 提示 */
    data class ShowErrorMessage(@StringRes val messageRes: Int) : StockListEffect
}
