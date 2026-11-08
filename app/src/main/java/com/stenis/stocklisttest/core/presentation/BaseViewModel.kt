package com.stenis.stocklisttest.core.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * MVI 架構的 ViewModel 基底。
 *
 * 資料流：UI 呼叫 [onIntent] → 子類別在 [handleIntent] 處理 →
 * 以 [setState] 更新 [uiState]（持續性狀態）或以 [sendEffect] 發出 [effect]（一次性動作）。
 *
 * @param S 畫面狀態
 * @param I 使用者意圖
 * @param E 一次性效果
 */
abstract class BaseViewModel<S : UiState, I : UiIntent, E : UiEffect>(
    initialState: S,
) : ViewModel() {

    private val _uiState = MutableStateFlow(initialState)

    /** 目前畫面狀態，Compose 以 collectAsStateWithLifecycle 收集 */
    val uiState: StateFlow<S> = _uiState.asStateFlow()

    private val _effect = Channel<E>(Channel.BUFFERED)

    /** 一次性效果，UI 收集後執行，每個 effect 只會被收到一次 */
    val effect: Flow<E> = _effect.receiveAsFlow()

    /** 目前的狀態快照 */
    protected val currentState: S
        get() = _uiState.value

    /** UI 唯一的事件入口 */
    fun onIntent(intent: I) {
        handleIntent(intent)
    }

    /** 子類別依 intent 決定要更新狀態或發出 effect */
    protected abstract fun handleIntent(intent: I)

    /** 以目前狀態產生新狀態（執行緒安全） */
    protected fun setState(reducer: S.() -> S) {
        _uiState.update(reducer)
    }

    /** 發出一次性效果 */
    protected fun sendEffect(effect: E) {
        viewModelScope.launch { _effect.send(effect) }
    }
}
