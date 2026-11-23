package com.diaplus.app

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.diaplus.app.navigation.NavegacionApp
import com.diaplus.app.ui.screens.Bienvenida
import com.diaplus.app.ui.theme.ColoresClaros
import com.diaplus.app.ui.theme.ColoresOscuros
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val preferencias = (application as DiaPlusApp).preferencias

        setContent {
            // el modo oscuro se guarda en DataStore; es null una fracción de segundo mientras se lee el archivo
            val guardado by preferencias.oscuro.collectAsState(initial = null)
            val alcance = rememberCoroutineScope()
            var mostrarBienvenida by remember { mutableStateOf(true) }

            // la bienvenida dura un segundo y medio
            LaunchedEffect(Unit) {
                delay(1500)
                mostrarBienvenida = false
            }

            val oscuro = guardado
            if (oscuro != null) {
                // los iconos de la barra de estado se eligen según el tema de la app, no el del teléfono
                DisposableEffect(oscuro) {
                    val estilo = SystemBarStyle.auto(Color.TRANSPARENT, Color.TRANSPARENT, detectDarkMode = { oscuro })
                    enableEdgeToEdge(statusBarStyle = estilo, navigationBarStyle = estilo)
                    onDispose {}
                }

                val colores = if (oscuro) ColoresOscuros else ColoresClaros

                MaterialTheme {
                    if (mostrarBienvenida) {
                        Bienvenida(colores)
                    } else {
                        NavegacionApp(
                            colores,
                            oscuro,
                            cambiarTema = { alcance.launch { preferencias.guardarOscuro(!oscuro) } }
                        )
                    }
                }
            }
        }
    }
}
