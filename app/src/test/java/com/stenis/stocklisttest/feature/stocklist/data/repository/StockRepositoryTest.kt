package com.stenis.stocklisttest.feature.stocklist.data.repository

import com.stenis.stocklisttest.core.network.TwseApiService
import com.stenis.stocklisttest.feature.stocklist.data.datasource.StockRemoteDataSource
import com.stenis.stocklisttest.feature.stocklist.data.dto.StockDayAllDto
import com.stenis.stocklisttest.feature.stocklist.data.dto.StockDayAvgDto
import com.stenis.stocklisttest.feature.stocklist.data.dto.StockValuationDto
import com.stenis.stocklisttest.feature.stocklist.presentation.model.PriceTrend
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class StockRepositoryTest {

    private val api = FakeTwseApiService()

    @Test
    fun `second call uses cache without calling api again`() = runTest {
        val repository = StockRepository(StockRemoteDataSource(api), StandardTestDispatcher(testScheduler))

        repository.getStocks().first()
        repository.getStocks().first()

        assertEquals(1, api.dayAllCalls)
    }

    @Test
    fun `force refresh calls api again`() = runTest {
        val repository = StockRepository(StockRemoteDataSource(api), StandardTestDispatcher(testScheduler))

        repository.getStocks().first()
        repository.getStocks(forceRefresh = true).first()

        assertEquals(2, api.dayAllCalls)
    }

    @Test
    fun `dto is merged by code and converted to ui data`() = runTest {
        val repository = StockRepository(StockRemoteDataSource(api), StandardTestDispatcher(testScheduler))

        val stock = repository.getStocks().first().single()

        assertEquals("2330", stock.code)
        assertEquals("2,135", stock.closingPrice)
        assertEquals(PriceTrend.UP, stock.closingPriceTrend) // 2135 > 月均 2052
        assertEquals("+35", stock.change)
        assertEquals("2,052", stock.monthlyAveragePrice)
        assertEquals("1.45%", stock.valuation.dividendYield)
        assertEquals("-", stock.valuation.peRatio) // API 空字串
    }

    private class FakeTwseApiService : TwseApiService {
        var dayAllCalls = 0

        override suspend fun getStockDayAll(): List<StockDayAllDto> {
            dayAllCalls++
            return listOf(
                StockDayAllDto(
                    code = "2330", name = "台積電", tradeVolume = "32154879", tradeValue = "68650665000",
                    openingPrice = "2100.00", highestPrice = "2150.00", lowestPrice = "2095.00",
                    closingPrice = "2135.00", change = "35.0000", transaction = "98765",
                ),
            )
        }

        override suspend fun getStockDayAverages(): List<StockDayAvgDto> = listOf(
            StockDayAvgDto(code = "2330", name = "台積電", closingPrice = "2135.00", monthlyAveragePrice = "2052.00"),
        )

        override suspend fun getStockValuations(): List<StockValuationDto> = listOf(
            StockValuationDto(code = "2330", name = "台積電", peRatio = "", dividendYield = "1.45", pbRatio = "7.85"),
        )
    }
}
