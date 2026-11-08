package com.stenis.stocklisttest.feature.stocklist.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stenis.stocklisttest.R
import com.stenis.stocklisttest.core.theme.ThemeMode
import com.stenis.stocklisttest.core.theme.ThemeRepository
import com.stenis.stocklisttest.feature.stocklist.data.repository.StockRepository
import com.stenis.stocklisttest.feature.stocklist.presentation.model.StockItemUiModel
import com.stenis.stocklisttest.feature.stocklist.presentation.model.StockSortOrder
import com.stenis.stocklisttest.feature.stocklist.presentation.model.sortedBy
import com.stenis.stocklisttest.feature.stocklist.presentation.model.toStockItemUiModels
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class StockListViewModel(
    private val stockRepository: StockRepository,
    private val themeRepository: ThemeRepository,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default,
) : ViewModel() {

    private val _state = MutableStateFlow(StockListState())
    val state: StateFlow<StockListState> = _state.asStateFlow()

    private var loadJob: Job? = null

    init {
        getStockItemList()
    }

    /** 載入（或重新整理）股票清單；載入中重複呼叫會被忽略 */
    fun getStockItemList() {
        if (loadJob?.isActive == true) return
        loadJob = viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessageRes = null) }
            try {
                val stocks = stockRepository.getStocks()
                val items = withContext(defaultDispatcher) {
                    stocks.toStockItemUiModels().sortedBy(_state.value.sortOrder)
                }
                _state.update { it.copy(isLoading = false, stocks = items) }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _state.update {
                    it.copy(isLoading = false, errorMessageRes = R.string.stock_list_error_load_failed)
                }
            }
        }
    }

    /** 變更排序方式並關閉 bottom sheet */
    fun changeStockOrder(order: StockSortOrder) {
        _state.update {
            it.copy(
                sortOrder = order,
                stocks = if (order == it.sortOrder) it.stocks else it.stocks.sortedBy(order),
                isSortSheetVisible = false,
            )
        }
    }

    /** 顯示排序 bottom sheet */
    fun showStockOptionBottomSheet() {
        _state.update { it.copy(isSortSheetVisible = true) }
    }

    /** 關閉排序 bottom sheet（下滑或點擊外側） */
    fun hideStockOptionBottomSheet() {
        _state.update { it.copy(isSortSheetVisible = false) }
    }

    /** 點擊卡片，顯示估值 alert */
    fun showStockValuation(stock: StockItemUiModel) {
        _state.update { it.copy(selectedValuation = stock.valuation) }
    }

    /** 關閉估值 alert */
    fun dismissStockValuation() {
        _state.update { it.copy(selectedValuation = null) }
    }

    /** 切換深色 / 淺色主題 */
    fun changeThemeMode(isDarkTheme: Boolean) {
        themeRepository.setThemeMode(if (isDarkTheme) ThemeMode.DARK else ThemeMode.LIGHT)
    }

    /** 錯誤訊息已顯示，清除錯誤 */
    fun dismissError() {
        _state.update { it.copy(errorMessageRes = null) }
    }
}
