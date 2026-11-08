package com.stenis.stocklisttest.di

import com.stenis.stocklisttest.appModules
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.dsl.module
import org.koin.test.verify.verify
import org.junit.Test

/** 確認所有 Koin module 的依賴都能被解析，避免執行時才發現漏註冊 */
class KoinModulesTest {

    @OptIn(KoinExperimentalAPI::class)
    @Test
    fun `all koin modules are resolvable`() {
        module { includes(appModules) }.verify()
    }
}
