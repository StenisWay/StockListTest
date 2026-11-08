package com.stenis.stocklisttest.core.presentation

import androidx.annotation.StringRes

/**
 * 畫面狀態：Compose 以 `when` 決定當前要顯示什麼。
 *
 * @param T [Success] 時畫面需要的資料
 */
sealed interface UiState<out T> {

    /** 下載資料中（尚無任何資料） */
    data object Loading : UiState<Nothing>

    /** 已取得資料 */
    data class Success<out T>(val data: T) : UiState<T>

    /** 沒有資料且載入失敗 */
    data class Failure(@StringRes val messageRes: Int) : UiState<Nothing>
}

/** 使用者意圖：UI 發生的事件（點擊、下拉刷新等），交給 ViewModel 處理 */
interface UiIntent

/** 一次性效果：ViewModel 請 UI 執行的動作（顯示 dialog、snackbar、跳轉等），只會被消費一次 */
interface UiEffect
