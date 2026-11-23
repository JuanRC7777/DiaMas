package com.diaplus.app.ui.screens.ajustes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.diaplus.app.DiaPlusApp
import com.diaplus.app.data.local.Preferencias
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

// lo que la pantalla de ajustes necesita dibujar
data class AjustesEstado(
    val oscuro: Boolean = false,
    val sonido: Boolean = true,
)

class AjustesViewModel(private val preferencias: Preferencias) : ViewModel() {

    val estado: StateFlow<AjustesEstado> =
        combine(preferencias.oscuro, preferencias.sonido) { oscuro, sonido -> AjustesEstado(oscuro, sonido) }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), AjustesEstado())

    fun cambiarOscuro(valor: Boolean) {
        viewModelScope.launch { preferencias.guardarOscuro(valor) }
    }

    fun cambiarSonido(valor: Boolean) {
        viewModelScope.launch { preferencias.guardarSonido(valor) }
    }

    companion object {
        val Fabrica = viewModelFactory {
            initializer {
                val app = this[APPLICATION_KEY] as DiaPlusApp
                AjustesViewModel(app.preferencias)
            }
        }
    }
}
