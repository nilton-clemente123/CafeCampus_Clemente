package com.clemente.cafecampusclemente

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.clemente.cafecampusclemente.ui.navigation.NavCafe
import com.clemente.cafecampusclemente.ui.theme.CafecampusTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            CafecampusTheme {
                NavCafe()
            }
        }
    }
}
