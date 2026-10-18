package com.diaplus.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.diaplus.app.ui.screens.Bienvenida
import com.diaplus.app.ui.screens.PantallaPrincipal
import com.diaplus.app.ui.theme.ColoresClaros
import com.diaplus.app.ui.theme.ColoresOscuros
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                var oscuro by remember { mutableStateOf(false) }
                var mostrarBienvenida by remember { mutableStateOf(true) }

                val colores = if (oscuro) ColoresOscuros else ColoresClaros

                // la bienvenida dura un segundo y medio
                LaunchedEffect(Unit) {
                    delay(1500)
                    mostrarBienvenida = false
                }

                if (mostrarBienvenida) {
                    Bienvenida(colores)
                } else {
                    PantallaPrincipal(colores, oscuro, cambiarTema = { oscuro = !oscuro })
                }
            }
        }
    }
}
