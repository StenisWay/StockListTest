package com.stenis.stocklisttest.feature.stocklist.presentation.component

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.stenis.stocklisttest.R
import com.stenis.stocklisttest.feature.stocklist.presentation.model.StockSortOrder
import kotlinx.coroutines.launch

/** 排序選項 bottom sheet，選取後以動畫收起再回呼 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StockSortBottomSheet(
    selectedOrder: StockSortOrder,
    onOrderSelected: (StockSortOrder) -> Unit,
    onDismiss: () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
    ) {
        Column(
            modifier = Modifier
                .selectableGroup()
                .navigationBarsPadding()
                .padding(bottom = 16.dp),
        ) {
            StockSortOrder.entries.forEach { order ->
                val isSelected = order == selectedOrder
                ListItem(
                    headlineContent = { Text(stringResource(order.labelRes)) },
                    trailingContent = if (isSelected) {
                        {
                            Icon(
                                painter = painterResource(R.drawable.ic_check),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                            )
                        }
                    } else {
                        null
                    },
                    colors = ListItemDefaults.colors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow),
                    modifier = Modifier.selectable(
                        selected = isSelected,
                        role = Role.RadioButton,
                        onClick = {
                            scope.launch { sheetState.hide() }.invokeOnCompletion {
                                onOrderSelected(order)
                            }
                        },
                    ),
                )
            }
        }
    }
}

@get:StringRes
private val StockSortOrder.labelRes: Int
    get() = when (this) {
        StockSortOrder.CODE_DESCENDING -> R.string.stock_sort_code_descending
        StockSortOrder.CODE_ASCENDING -> R.string.stock_sort_code_ascending
    }
