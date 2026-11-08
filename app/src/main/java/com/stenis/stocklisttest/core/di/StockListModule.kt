package com.stenis.stocklisttest.core.di

import com.stenis.stocklisttest.feature.stocklist.data.datasource.StockRemoteDataSource
import com.stenis.stocklisttest.feature.stocklist.data.repository.StockRepository
import com.stenis.stocklisttest.feature.stocklist.domain.usecase.RemoveDuplicateStockCodeUseCase
import com.stenis.stocklisttest.feature.stocklist.domain.usecase.SortStockListUseCase
import com.stenis.stocklisttest.feature.stocklist.presentation.StockListViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val stockListModule = module {
    singleOf(::StockRemoteDataSource)
    // 快取在 repository，必須是 singleton
    single { StockRepository(get()) }
    factoryOf(::RemoveDuplicateStockCodeUseCase)
    factoryOf(::SortStockListUseCase)
    viewModelOf(::StockListViewModel)
}
