package com.stenis.stocklisttest.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import com.stenis.stocklisttest.appModules
import com.stenis.stocklisttest.core.network.TwseApiService
import com.stenis.stocklisttest.core.theme.ThemeRepository
import com.stenis.stocklisttest.feature.stocklist.data.datasource.StockRemoteDataSource
import com.stenis.stocklisttest.feature.stocklist.data.repository.StockRepository
import com.stenis.stocklisttest.feature.stocklist.domain.usecase.RemoveDuplicateStockCodeUseCase
import com.stenis.stocklisttest.feature.stocklist.domain.usecase.SortStockListUseCase
import org.junit.After
import org.junit.Assert.assertNotNull
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.verify.verify
import java.io.File

/** 確認所有 Koin 依賴都有註冊並能實際建立，避免執行時才發現漏註冊 */
class KoinModulesTest {

    @get:Rule
    val tempFolder = TemporaryFolder()

    @After
    fun tearDown() {
        stopKoin()
    }

    @OptIn(KoinExperimentalAPI::class)
    @Test
    fun `all constructor dependencies are declared`() {
        module { includes(appModules) }.verify()
    }

    @Test
    fun `all definitions can be created`() {
        // DataStore 需要 Android Context，測試時改用暫存檔
        val testDataStoreModule = module {
            single<DataStore<Preferences>> {
                PreferenceDataStoreFactory.create { File(tempFolder.root, "settings.preferences_pb") }
            }
        }
        val koin = startKoin {
            allowOverride(true)
            modules(appModules + testDataStoreModule)
        }.koin

        assertNotNull(koin.get<TwseApiService>())
        assertNotNull(koin.get<StockRemoteDataSource>())
        assertNotNull(koin.get<StockRepository>())
        assertNotNull(koin.get<RemoveDuplicateStockCodeUseCase>())
        assertNotNull(koin.get<SortStockListUseCase>())
        assertNotNull(koin.get<ThemeRepository>())
        // ViewModel 建立時會立即呼叫 API，只以 verify() 檢查其依賴
    }
}
