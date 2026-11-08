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
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch

class StockListViewModel(
    private val stockRepository: StockRepository,
    private val themeRepository: ThemeRepository,
    private val removeDuplicateStockCode: RemoveDuplicateStockCodeUseCase,
    private val sortStockList: SortStockListUseCase,
) : BaseViewModel<StockListUiState, StockListIntent, StockListEffect>(StockListUiState()) {

    init {
        onIntent(StockListIntent.GetStockItemList)
    }

    override fun handleIntent(intent: StockListIntent) {
        when (intent) {
            StockListIntent.GetStockItemList -> getStockItemList(forceRefresh = false)
            StockListIntent.RefreshStockItemList -> getStockItemList(forceRefresh = true)
            is StockListIntent.ChangeStockOrder -> changeStockOrder(intent.order)
            StockListIntent.ShowStockOptionBottomSheet -> showStockOptionBottomSheet()
            is StockListIntent.ShowStockValuation -> showStockValuation(intent.stock)
            is StockListIntent.ChangeThemeMode -> changeThemeMode(intent.isDarkTheme)
        }
    }

    /**
     * 取得股票清單：去除重複代號後，依目前排序方式（預設代號降序）排序一次。
     * 載入中重複呼叫會被忽略。
     */
    private fun getStockItemList(forceRefresh: Boolean) {
        if (currentState.isLoading) return
        setState { copy(isLoading = true, errorMessageRes = null) }

        viewModelScope.launch {
            try {
                val stocks = stockRepository.getStocks(forceRefresh)
                val items = sortStockList(removeDuplicateStockCode(stocks), currentState.sortOrder)
                setState { copy(isLoading = false, stocks = items) }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                val messageRes = R.string.stock_list_error_load_failed
                if (currentState.stocks.isEmpty()) {
                    // 沒有資料：整頁顯示錯誤與重試
                    setState { copy(isLoading = false, errorMessageRes = messageRes) }
                } else {
                    // 已有資料：保留舊資料，以 snackbar 提示
                    setState { copy(isLoading = false) }
                    sendEffect(StockListEffect.ShowErrorMessage(messageRes))
                }
            }
        }
    }

    /** 變更排序方式 */
    private fun changeStockOrder(order: StockSortOrder) {
        setState {
            if (order == sortOrder) this else copy(sortOrder = order, stocks = sortStockList(stocks, order))
        }
    }

    /** 請 UI 顯示排序 bottom sheet */
    private fun showStockOptionBottomSheet() {
        sendEffect(StockListEffect.ShowSortBottomSheet)
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
