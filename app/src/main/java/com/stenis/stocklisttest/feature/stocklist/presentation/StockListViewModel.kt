package com.stenis.stocklisttest.feature.stocklist.presentation

import androidx.lifecycle.viewModelScope
import com.stenis.stocklisttest.R
import com.stenis.stocklisttest.core.presentation.BaseViewModel
import com.stenis.stocklisttest.core.theme.ThemeMode
import com.stenis.stocklisttest.core.theme.ThemeRepository
import com.stenis.stocklisttest.feature.stocklist.data.repository.StockRepository
import com.stenis.stocklisttest.feature.stocklist.presentation.model.StockItemUiModel
import com.stenis.stocklisttest.feature.stocklist.presentation.model.StockSortOrder
import com.stenis.stocklisttest.feature.stocklist.presentation.model.sortedBy
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch

class StockListViewModel(
    private val stockRepository: StockRepository,
    private val themeRepository: ThemeRepository,
) : BaseViewModel<StockListUiState, StockListIntent, StockListEffect>(StockListUiState()) {

    init {
        onIntent(StockListIntent.GetStockItemList)
    }

    override fun handleIntent(intent: StockListIntent) {
        when (intent) {
            StockListIntent.GetStockItemList -> getStockItemList()
            is StockListIntent.ChangeStockOrder -> changeStockOrder(intent.order)
            StockListIntent.ShowStockOptionBottomSheet -> showStockOptionBottomSheet()
            is StockListIntent.ShowStockValuation -> showStockValuation(intent.stock)
            is StockListIntent.ChangeThemeMode -> changeThemeMode(intent.isDarkTheme)
        }
    }

    /** 載入（或重新整理）股票清單；載入中重複呼叫會被忽略 */
    private fun getStockItemList() {
        if (currentState.isLoading) return
        setState { copy(isLoading = true, errorMessageRes = null) }

        viewModelScope.launch {
            try {
                val items = stockRepository.getStocks().sortedBy(currentState.sortOrder)
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
            if (order == sortOrder) this else copy(sortOrder = order, stocks = stocks.sortedBy(order))
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

    /** 切換深色 / 淺色主題 */
    private fun changeThemeMode(isDarkTheme: Boolean) {
        themeRepository.setThemeMode(if (isDarkTheme) ThemeMode.DARK else ThemeMode.LIGHT)
    }
}
