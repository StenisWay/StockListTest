package com.stenis.stocklisttest.feature.stocklist.di

import com.stenis.stocklisttest.feature.stocklist.data.datasource.StockRemoteDataSource
import com.stenis.stocklisttest.feature.stocklist.data.repository.StockRepository
import com.stenis.stocklisttest.feature.stocklist.presentation.StockListViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val stockListModule = module {
    singleOf(::StockRemoteDataSource)
    single { StockRepository(get()) }
    viewModel { StockListViewModel(get(), get()) }
}
