package com.diaplus.app.ui.screens.tarea

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.diaplus.app.DiaPlusApp
import com.diaplus.app.data.model.Tarea
import com.diaplus.app.data.model.aMillis
import com.diaplus.app.data.repository.TareaRepositorio
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class TareaViewModel(private val repositorio: TareaRepositorio, id: Long) : ViewModel() {

    // null mientras se lee de la base de datos
    private val _tarea = MutableStateFlow<Tarea?>(null)
    val tarea: StateFlow<Tarea?> = _tarea

    init {
        viewModelScope.launch { _tarea.value = repositorio.buscar(id) }
    }

    fun guardar(titulo: String, fecha: LocalDateTime?, aviso30: Boolean, alTerminar: () -> Unit) {
        val actual = _tarea.value ?: return
        viewModelScope.launch {
            repositorio.guardar(actual.copy(titulo = titulo, fechaMillis = fecha?.aMillis(), aviso30 = aviso30))
            alTerminar()
        }
    }

    companion object {
        fun fabrica(id: Long) = viewModelFactory {
            initializer {
                val app = this[APPLICATION_KEY] as DiaPlusApp
                TareaViewModel(app.repositorio, id)
            }
        }
    }
}
