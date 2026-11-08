package com.stenis.stocklisttest.feature.stocklist.domain.usecase

import com.stenis.stocklisttest.feature.stocklist.presentation.model.StockSortOrder
import com.stenis.stocklisttest.feature.stocklist.testStock
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class SortStockListUseCaseTest {

    private val useCase = SortStockListUseCase()
    private val stocks = listOf(testStock("2330"), testStock("00878"), testStock("1101"), testStock("0050"))

    @Test
    fun `code descending`() = runTest {
        val result = useCase(flowOf(stocks), StockSortOrder.CODE_DESCENDING).first()

        assertEquals(listOf("2330", "1101", "00878", "0050"), result.map { it.code })
    }

    @Test
    fun `code ascending`() = runTest {
        val result = useCase(flowOf(stocks), StockSortOrder.CODE_ASCENDING).first()

        assertEquals(listOf("0050", "00878", "1101", "2330"), result.map { it.code })
    }
}
