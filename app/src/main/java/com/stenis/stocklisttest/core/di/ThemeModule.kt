package com.stenis.stocklisttest.core.di

import com.stenis.stocklisttest.core.theme.ThemeRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val themeModule = module {
    singleOf(::ThemeRepository)
}
