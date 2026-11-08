package com.stenis.stocklisttest.feature.stocklist.presentation

import androidx.compose.animation.Crossfade
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.stenis.stocklisttest.R
import com.stenis.stocklisttest.core.presentation.UiState
import com.stenis.stocklisttest.feature.stocklist.presentation.component.StockItemCard
import com.stenis.stocklisttest.feature.stocklist.presentation.component.StockSortBottomSheet
import com.stenis.stocklisttest.feature.stocklist.presentation.component.StockValuationDialog
import com.stenis.stocklisttest.feature.stocklist.presentation.model.StockItemUiModel
import com.stenis.stocklisttest.feature.stocklist.presentation.model.StockSortOrder
import com.stenis.stocklisttest.feature.stocklist.presentation.model.StockValuationUiModel
import com.stenis.stocklisttest.feature.stocklist.presentation.preview.StockPreviewData
import com.stenis.stocklisttest.ui.theme.AppTheme
import com.stenis.stocklisttest.ui.theme.StockListTestTheme
import org.koin.compose.viewmodel.koinViewModel

/** 連接 ViewModel 的入口：收集 State、處理 Effect，並管理 dialog / bottom sheet 的顯示 */
@Composable
fun StockListRoute(
    viewModel: StockListViewModel = koinViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    var isSortSheetVisible by rememberSaveable { mutableStateOf(false) }
    var selectedValuation by remember { mutableStateOf<StockValuationUiModel?>(null) }

    // 只在畫面可見時處理 effect，背景時 effect 會留在 channel 等待
    LaunchedEffect(viewModel, lifecycleOwner) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.effect.collect { effect ->
                when (effect) {
                    StockListEffect.ShowSortBottomSheet -> isSortSheetVisible = true
                    is StockListEffect.ShowValuationDialog -> selectedValuation = effect.valuation
                    is StockListEffect.ShowErrorMessage ->
                        snackbarHostState.showSnackbar(context.getString(effect.messageRes))
                }
            }
        }
    }

    StockListScreen(
        state = state,
        snackbarHostState = snackbarHostState,
        onIntent = viewModel::onIntent,
    )

    if (isSortSheetVisible) {
        StockSortBottomSheet(
            selectedOrder = (state as? UiState.Success)?.data?.sortOrder ?: StockSortOrder.DEFAULT,
            onOrderSelected = { order ->
                isSortSheetVisible = false
                viewModel.onIntent(StockListIntent.ChangeStockOrder(order))
            },
            onDismiss = { isSortSheetVisible = false },
        )
    }

    selectedValuation?.let { valuation ->
        StockValuationDialog(
            valuation = valuation,
            onDismiss = { selectedValuation = null },
        )
    }
}

/** 無狀態畫面：只依 [state] 繪製，使用者操作一律轉成 [StockListIntent]，方便 Preview 與 UI 測試 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StockListScreen(
    state: UiState<StockListUiState>,
    snackbarHostState: SnackbarHostState,
    onIntent: (StockListIntent) -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.stock_list_title)) },
                navigationIcon = {
                    ThemeToggleButton(onThemeChange = { onIntent(StockListIntent.ChangeThemeMode(it)) })
                },
                actions = {
                    OutlinedIconButton(
                        onClick = { onIntent(StockListIntent.ShowStockOptionBottomSheet) },
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
        val contentModifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)

        when (state) {
            UiState.Loading -> Box(contentModifier, contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }

            is UiState.Success -> PullToRefreshBox(
                isRefreshing = state.data.isRefreshing,
                onRefresh = { onIntent(StockListIntent.RefreshStockItemList) },
                modifier = contentModifier,
            ) {
                if (state.data.stocks.isEmpty()) {
                    // 空清單仍可下拉刷新，需要可捲動的容器
                    LazyColumn(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
                        item {
                            Text(
                                text = stringResource(R.string.stock_list_empty),
                                modifier = Modifier.padding(top = 120.dp),
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                    }
                } else {
                    StockList(
                        stocks = state.data.stocks,
                        onStockClick = { onIntent(StockListIntent.ShowStockValuation(it)) },
                    )
                }
            }

            is UiState.Failure -> Box(contentModifier, contentAlignment = Alignment.Center) {
                MessageWithAction(
                    message = stringResource(state.messageRes),
                    actionText = stringResource(R.string.stock_list_retry),
                    onAction = { onIntent(StockListIntent.GetStockItemList) },
                )
            }
        }
    }
}

/** 深色 / 淺色切換：icon 顯示目前的模式（深色為月亮、淺色為太陽），點擊切換到另一個模式 */
@Composable
private fun ThemeToggleButton(onThemeChange: (isDarkTheme: Boolean) -> Unit) {
    val isDarkTheme = AppTheme.isDarkTheme
    IconButton(onClick = { onThemeChange(!isDarkTheme) }) {
        Crossfade(targetState = isDarkTheme, label = "ThemeIcon") { isDark ->
            Icon(
                painter = painterResource(if (isDark) R.drawable.ic_dark_mode else R.drawable.ic_light_mode),
                contentDescription = stringResource(
                    if (isDark) R.string.theme_switch_to_light else R.string.theme_switch_to_dark,
                ),
            )
        }
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
            state = UiState.Success(StockListUiState(stocks = StockPreviewData.stocks)),
            snackbarHostState = remember { SnackbarHostState() },
            onIntent = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun StockListScreenErrorPreview() {
    StockListTestTheme {
        StockListScreen(
            state = UiState.Failure(R.string.stock_list_error_load_failed),
            snackbarHostState = remember { SnackbarHostState() },
            onIntent = {},
        )
    }
}
