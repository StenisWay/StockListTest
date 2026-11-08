package com.stenis.stocklisttest

import android.app.Application
import com.stenis.stocklisttest.core.di.networkModule
import com.stenis.stocklisttest.core.di.themeModule
import com.stenis.stocklisttest.core.util.ResourceProvider
import com.stenis.stocklisttest.core.di.stockListModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class StockListApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        ResourceProvider.init(this)
        startKoin {
            androidLogger(if (BuildConfig.DEBUG) Level.INFO else Level.NONE)
            androidContext(this@StockListApplication)
            modules(appModules)
        }
    }
}

/** App 所有 Koin module，新增 feature 時加在這裡 */
val appModules = listOf(
    networkModule,
    themeModule,
    stockListModule,
)
