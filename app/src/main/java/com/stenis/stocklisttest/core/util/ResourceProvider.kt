package com.stenis.stocklisttest.core.util

import android.content.Context
import androidx.annotation.StringRes

/**
 * 全域的資源存取工具，以 applicationContext 取得字串等需要 Context 的資源。
 *
 * 不依賴畫面的 Context，可在 effect 處理、ViewModel 等沒有 Context 的地方使用，
 * 也不會因持有 Activity 而造成記憶體洩漏。需在 Application.onCreate 呼叫 [init]。
 */
object ResourceProvider {

    private lateinit var appContext: Context

    fun init(context: Context) {
        appContext = context.applicationContext
    }

    fun getString(@StringRes resId: Int): String = appContext.getString(resId)

    fun getString(@StringRes resId: Int, vararg formatArgs: Any): String =
        appContext.getString(resId, *formatArgs)
}
