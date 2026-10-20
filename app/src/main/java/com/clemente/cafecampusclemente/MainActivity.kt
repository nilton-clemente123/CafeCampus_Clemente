package com.clemente.cafecampusclemente

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import com.clemente.cafecampusclemente.data.prefs.PreferenciasStore
import com.clemente.cafecampusclemente.ui.navigation.NavCafe
import com.clemente.cafecampusclemente.ui.theme.CafecampusTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val preferencias = remember { PreferenciasStore(applicationContext) }
            val modoOscuro by preferencias.modoOscuro.collectAsState(initial = false)

            CafecampusTheme(darkTheme = modoOscuro) {
                NavCafe()
            }
        }
    }
}
