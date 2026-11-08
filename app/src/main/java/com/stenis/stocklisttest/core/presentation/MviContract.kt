package com.stenis.stocklisttest.core.presentation

/** 畫面狀態：Compose 只依 UiState 決定當前要顯示什麼，必須是不可變的 */
interface UiState

/** 使用者意圖：UI 發生的事件（點擊、下拉刷新等），交給 ViewModel 處理 */
interface UiIntent

/** 一次性效果：ViewModel 請 UI 執行的動作（顯示 dialog、snackbar、跳轉等），只會被消費一次 */
interface UiEffect
