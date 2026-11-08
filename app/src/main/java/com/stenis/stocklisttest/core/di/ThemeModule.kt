package com.stenis.stocklisttest.core.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.stenis.stocklisttest.core.theme.ThemeRepository
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

// 同一個檔案只能有一個 DataStore 實例，以 Context 擴充屬性確保唯一
private val Context.settingsDataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

val themeModule = module {
    single<DataStore<Preferences>> { androidContext().settingsDataStore }
    singleOf(::ThemeRepository)
}
