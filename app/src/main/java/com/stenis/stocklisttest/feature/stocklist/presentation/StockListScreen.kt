package com.stenis.stocklisttest.feature.stocklist.presentation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.stenis.stocklisttest.R
import com.stenis.stocklisttest.feature.stocklist.data.mock.StockMockData
import com.stenis.stocklisttest.feature.stocklist.presentation.component.StockItemCard
import com.stenis.stocklisttest.feature.stocklist.presentation.component.StockSortBottomSheet
import com.stenis.stocklisttest.feature.stocklist.presentation.component.StockValuationDialog
import com.stenis.stocklisttest.feature.stocklist.presentation.model.StockItemUiModel
import com.stenis.stocklisttest.feature.stocklist.presentation.model.StockSortOrder
import com.stenis.stocklisttest.feature.stocklist.presentation.model.sortedBy
import com.stenis.stocklisttest.feature.stocklist.presentation.model.toStockItemUiModels
import com.stenis.stocklisttest.ui.theme.StockListTestTheme
import org.koin.compose.viewmodel.koinViewModel

/** 連接 ViewModel 的入口 */
@Composable
fun StockListRoute(
    viewModel: StockListViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    StockListScreen(
        state = state,
        onRefresh = viewModel::getStockItemList,
        onSortClick = viewModel::showStockOptionBottomSheet,
        onSortDismiss = viewModel::hideStockOptionBottomSheet,
        onOrderSelected = viewModel::changeStockOrder,
        onStockClick = viewModel::showStockValuation,
        onValuationDismiss = viewModel::dismissStockValuation,
        onErrorShown = viewModel::dismissError,
    )
}

/** 無狀態畫面，只依 [state] 繪製，方便 Preview 與 UI 測試 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StockListScreen(
    state: StockListState,
    onRefresh: () -> Unit,
    onSortClick: () -> Unit,
    onSortDismiss: () -> Unit,
    onOrderSelected: (StockSortOrder) -> Unit,
    onStockClick: (StockItemUiModel) -> Unit,
    onValuationDismiss: () -> Unit,
    onErrorShown: () -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    // 已有資料時，載入失敗以 Snackbar 提示，保留舊資料
    LaunchedEffect(state.errorMessageRes, state.stocks.isEmpty()) {
        val messageRes = state.errorMessageRes
        if (messageRes != null && state.stocks.isNotEmpty()) {
            snackbarHostState.showSnackbar(context.getString(messageRes))
            onErrorShown()
        }
    }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.stock_list_title)) },
                actions = {
                    OutlinedIconButton(
                        onClick = onSortClick,
                        modifier = Modifier.padding(end = 8.dp),
                        shape = RoundedCornerShape(3.dp),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_list),
                            contentDescription = stringResource(R.string.stock_list_sort),
                        )
                    }
                },
                scrollBehavior = scrollBehavior,
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { innerPadding ->
        PullToRefreshBox(
            isRefreshing = state.isLoading && state.stocks.isNotEmpty(),
            onRefresh = onRefresh,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            when {
                state.stocks.isNotEmpty() -> StockList(
                    stocks = state.stocks,
                    onStockClick = onStockClick,
                )

                state.isLoading -> CircularProgressIndicator(Modifier.align(Alignment.Center))

                state.errorMessageRes != null -> MessageWithAction(
                    message = stringResource(state.errorMessageRes),
                    actionText = stringResource(R.string.stock_list_retry),
                    onAction = onRefresh,
                    modifier = Modifier.align(Alignment.Center),
                )

                else -> MessageWithAction(
                    message = stringResource(R.string.stock_list_empty),
                    actionText = stringResource(R.string.stock_list_retry),
                    onAction = onRefresh,
                    modifier = Modifier.align(Alignment.Center),
                )
            }
        }
    }

    if (state.isSortSheetVisible) {
        StockSortBottomSheet(
            selectedOrder = state.sortOrder,
            onOrderSelected = onOrderSelected,
            onDismiss = onSortDismiss,
        )
    }

    state.selectedValuation?.let { valuation ->
        StockValuationDialog(
            valuation = valuation,
            onDismiss = onValuationDismiss,
        )
    }
}

@Composable
private fun StockList(
    stocks: List<StockItemUiModel>,
    onStockClick: (StockItemUiModel) -> Unit,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        items(items = stocks, key = { it.code }) { stock ->
            StockItemCard(
                stock = stock,
                onClick = { onStockClick(stock) },
                // 排序變更時卡片以動畫移動到新位置
                modifier = Modifier.animateItem(),
            )
        }
    }
}

@Composable
private fun MessageWithAction(
    message: String,
    actionText: String,
    onAction: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Button(onClick = onAction) { Text(actionText) }
    }
}

@Preview(showBackground = true)
@Composable
private fun StockListScreenPreview() {
    StockListTestTheme {
        StockListScreen(
            state = StockListState(
                stocks = StockMockData.stocks
                    .toStockItemUiModels()
                    .sortedBy(StockSortOrder.CODE_DESCENDING),
            ),
            onRefresh = {},
            onSortClick = {},
            onSortDismiss = {},
            onOrderSelected = {},
            onStockClick = {},
            onValuationDismiss = {},
            onErrorShown = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun StockListScreenErrorPreview() {
    StockListTestTheme {
        Box {
            StockListScreen(
                state = StockListState(errorMessageRes = R.string.stock_list_error_load_failed),
                onRefresh = {},
                onSortClick = {},
                onSortDismiss = {},
                onOrderSelected = {},
                onStockClick = {},
                onValuationDismiss = {},
                onErrorShown = {},
            )
        }
    }
}
