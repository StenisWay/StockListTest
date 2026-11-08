package com.stenis.stocklisttest.feature.stocklist.presentation

import androidx.lifecycle.viewModelScope
import com.stenis.stocklisttest.R
import com.stenis.stocklisttest.core.presentation.BaseViewModel
import com.stenis.stocklisttest.core.theme.ThemeMode
import com.stenis.stocklisttest.core.theme.ThemeRepository
import com.stenis.stocklisttest.feature.stocklist.data.repository.StockRepository
import com.stenis.stocklisttest.feature.stocklist.domain.usecase.RemoveDuplicateStockCodeUseCase
import com.stenis.stocklisttest.feature.stocklist.domain.usecase.SortStockListUseCase
import com.stenis.stocklisttest.feature.stocklist.presentation.model.StockItemUiModel
import com.stenis.stocklisttest.feature.stocklist.presentation.model.StockSortOrder
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class StockListViewModel(
    private val stockRepository: StockRepository,
    private val themeRepository: ThemeRepository,
    private val removeDuplicateStockCode: RemoveDuplicateStockCodeUseCase,
    private val sortStockList: SortStockListUseCase,
) : BaseViewModel<StockListUiState, StockListIntent, StockListEffect>(StockListUiState.Loading) {

    init {
        fetchStocks(forceRefresh = false)
    }

    override fun handleIntent(intent: StockListIntent) {
        when (intent) {
            StockListIntent.GetStockItemList -> getStockItemList()
            StockListIntent.RefreshStockItemList -> refreshStockItemList()
            is StockListIntent.ChangeStockOrder -> changeStockOrder(intent.order)
            StockListIntent.ShowStockOptionBottomSheet -> showStockOptionBottomSheet()
            is StockListIntent.ShowStockValuation -> showStockValuation(intent.stock)
            is StockListIntent.ChangeThemeMode -> changeThemeMode(intent.isDarkTheme)
        }
    }

    /** 重試：只有在失敗狀態時才重新取得（有快取時使用快取） */
    private fun getStockItemList() {
        if (currentState is StockListUiState.Failure) fetchStocks(forceRefresh = false)
    }

    /** 下拉刷新：只有在已有資料且未刷新中時才重新呼叫 API */
    private fun refreshStockItemList() {
        val state = currentState
        if (state is StockListUiState.Success && !state.isRefreshing) fetchStocks(forceRefresh = true)
    }

    /**
     * 取得股票清單：repository（IO）→ 去除重複代號（Default）→ 依排序方式排序（Default）→ 更新狀態。
     * 剛取得時就排序一次，符合 bottom sheet 預設的代號降序。
     */
    private fun fetchStocks(forceRefresh: Boolean) {
        val sortOrder = (currentState as? StockListUiState.Success)?.sortOrder ?: StockSortOrder.DEFAULT

        stockRepository.getStocks(forceRefresh)
            .let { stocks -> removeDuplicateStockCode(stocks) }
            .let { stocks -> sortStockList(stocks, sortOrder) }
            .onStart {
                setState {
                    // 已有資料時保留舊資料只顯示刷新中，否則進入整頁載入
                    if (this is StockListUiState.Success) copy(isRefreshing = true) else StockListUiState.Loading
                }
            }
            .onEach { stocks -> setState { StockListUiState.Success(stocks = stocks, sortOrder = sortOrder) } }
            .catch { onLoadFailed() }
            .launchIn(viewModelScope)
    }

    private fun onLoadFailed() {
        val messageRes = R.string.stock_list_error_load_failed
        val state = currentState
        if (state is StockListUiState.Success) {
            // 已有資料：保留舊資料，以 snackbar 提示
            setState { state.copy(isRefreshing = false) }
            sendEffect(StockListEffect.ShowErrorMessage(messageRes))
        } else {
            setState { StockListUiState.Failure(messageRes) }
        }
    }

    /** 變更排序方式：先更新選取的排序，再於背景排序清單 */
    private fun changeStockOrder(order: StockSortOrder) {
        val state = currentState as? StockListUiState.Success ?: return
        if (order == state.sortOrder) return
        setState { state.copy(sortOrder = order) }

        sortStockList(flowOf(state.stocks), order)
            .onEach { sorted ->
                setState {
                    // 快速連續切換時，只套用最後選取的排序
                    if (this is StockListUiState.Success && sortOrder == order) copy(stocks = sorted) else this
                }
            }
            .launchIn(viewModelScope)
    }

    /** 請 UI 顯示排序 bottom sheet（有資料時才需要排序） */
    private fun showStockOptionBottomSheet() {
        if (currentState is StockListUiState.Success) sendEffect(StockListEffect.ShowSortBottomSheet)
    }

    /** 請 UI 顯示估值 dialog */
    private fun showStockValuation(stock: StockItemUiModel) {
        sendEffect(StockListEffect.ShowValuationDialog(stock.valuation))
    }

    /** 切換深色 / 淺色主題，並記錄到 DataStore */
    private fun changeThemeMode(isDarkTheme: Boolean) {
        viewModelScope.launch {
            themeRepository.setThemeMode(if (isDarkTheme) ThemeMode.DARK else ThemeMode.LIGHT)
        }
    }
}
