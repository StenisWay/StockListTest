package com.stenis.stocklisttest.feature.stocklist.domain.usecase

import com.stenis.stocklisttest.feature.stocklist.testStock
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class RemoveDuplicateStockCodeUseCaseTest {

    private val testScope = TestScope()
    private val useCase = RemoveDuplicateStockCodeUseCase(StandardTestDispatcher(testScope.testScheduler))

    @Test
    fun `duplicate codes keep only the first item`() = testScope.runTest {
        val stocks = listOf(testStock("2330", "第一筆"), testStock("2317"), testStock("2330", "第二筆"))

        val result = useCase(flowOf(stocks)).first()

        assertEquals(listOf("2330", "2317"), result.map { it.code })
        assertEquals("第一筆", result.first().name)
    }

    @Test
    fun `no duplicates returns same items in same order`() = testScope.runTest {
        val stocks = listOf(testStock("1101"), testStock("2330"), testStock("0050"))

        assertEquals(stocks, useCase(flowOf(stocks)).first())
    }
}
