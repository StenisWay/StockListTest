package com.stenis.stocklisttest.core.di

import com.stenis.stocklisttest.BuildConfig
import com.stenis.stocklisttest.core.network.TwseApiService
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.util.concurrent.TimeUnit

private const val TWSE_BASE_URL = "https://openapi.twse.com.tw/v1/"

/** 網路層：Json、OkHttp、Retrofit 與 API Service，全部為 singleton */
val networkModule = module {

    single {
        Json {
            ignoreUnknownKeys = true
            coerceInputValues = true
        }
    }

    single {
        OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS)
            // 全市場資料量較大，讀取時間放寬
            .readTimeout(30, TimeUnit.SECONDS)
            .apply {
                if (BuildConfig.DEBUG) {
                    addInterceptor(
                        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC)
                    )
                }
            }
            .build()
    }

    single {
        Retrofit.Builder()
            .baseUrl(TWSE_BASE_URL)
            .client(get<OkHttpClient>())
            .addConverterFactory(get<Json>().asConverterFactory("application/json".toMediaType()))
            .build()
    }

    single<TwseApiService> { get<Retrofit>().create(TwseApiService::class.java) }
}
