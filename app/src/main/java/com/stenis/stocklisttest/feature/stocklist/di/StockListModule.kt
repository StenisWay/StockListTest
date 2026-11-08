package com.stenis.stocklisttest.feature.stocklist.di

import com.stenis.stocklisttest.feature.stocklist.data.datasource.StockRemoteDataSource
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

/** 股票清單 feature 的依賴：之後的 repository、use case、ViewModel 也註冊在這裡 */
val stockListModule = module {
    singleOf(::StockRemoteDataSource)
}
