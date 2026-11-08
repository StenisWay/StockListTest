package com.stenis.stocklisttest.feature.stocklist.presentation.model

/** 價格趨勢，畫面依此決定文字顏色 */
enum class PriceTrend {
    /** 上漲 / 高於比較基準：紅字 */
    UP,

    /** 下跌 / 低於比較基準：綠字 */
    DOWN,

    /** 持平或無法比較（缺資料）：預設文字色 */
    FLAT,
}
