package com.stenis.stocklisttest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.stenis.stocklisttest.feature.stocklist.presentation.StockListRoute
import com.stenis.stocklisttest.ui.theme.StockListTestTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            StockListTestTheme {
                StockListRoute()
            }
        }
    }
}
