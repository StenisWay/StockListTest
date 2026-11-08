package com.stenis.stocklisttest.feature.stocklist.presentation.model

import androidx.compose.runtime.Immutable

/**
 * 點擊股票卡片後，alert 顯示的估值資訊（來源：BWIBBU_ALL）。
 *
 * 沒有資料時由 mapper 填入佔位符號（例如虧損公司沒有本益比）。
 */
@Immutable
data class StockValuationUiModel(
    /** 股票代號（alert 標題用） */
    val code: String,
    /** 股票名稱（alert 標題用） */
    val name: String,
    /** 本益比 */
    val peRatio: String,
    /** 殖利率(%) */
    val dividendYield: String,
    /** 股價淨值比 */
    val pbRatio: String,
)
