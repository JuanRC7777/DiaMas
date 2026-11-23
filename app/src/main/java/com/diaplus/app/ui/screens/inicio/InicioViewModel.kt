package com.diaplus.app.ui.screens.inicio

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.diaplus.app.DiaPlusApp
import com.diaplus.app.data.model.Tarea
import com.diaplus.app.data.model.aFechaHora
import com.diaplus.app.data.model.aMillis
import com.diaplus.app.data.repository.TareaRepositorio
import com.diaplus.app.domain.calcularRacha
import com.diaplus.app.domain.leerTextoNatural
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime

class InicioViewModel(private val repositorio: TareaRepositorio) : ViewModel() {

    private val avisoDeshacer = MutableStateFlow<TipoAviso?>(null)
    private var accionDeshacer: (suspend () -> Unit)? = null
    private var esperaDelAviso: Job? = null

    // cada vez que cambia la tabla o el aviso, se arma un estado nuevo
    val estado: StateFlow<InicioEstado> =
        combine(repositorio.tareas, avisoDeshacer) { tareas, aviso -> armarEstado(tareas, aviso) }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), InicioEstado(cargando = true))

    private fun armarEstado(tareas: List<Tarea>, aviso: TipoAviso?): InicioEstado {
        val completadas = tareas.filter { it.completada }
        val diasCompletados = completadas.mapNotNull { it.completadaMillis?.aFechaHora()?.toLocalDate() }

        return InicioEstado(
            // primero las que tienen fecha (la mas cercana arriba), despues las sin fecha
            pendientes = tareas.filter { !it.completada }
                .sortedWith(compareBy({ it.fechaMillis == null }, { it.fechaMillis }, { -it.id })),
            completadas = completadas.sortedByDescending { it.completadaMillis },
            racha = calcularRacha(diasCompletados, LocalDate.now()),
            avisoDeshacer = aviso,
        )
    }

    // lo que se escribe en "Anotar algo...": "entregar ensayo mañana 3pm"
    fun anotar(texto: String) {
        val leido = leerTextoNatural(texto, LocalDateTime.now())
        guardarNueva(leido.titulo, leido.fecha, aviso30 = false)
    }

    fun guardarNueva(titulo: String, fecha: LocalDateTime?, aviso30: Boolean) {
        viewModelScope.launch {
            repositorio.guardar(Tarea(titulo = titulo, fechaMillis = fecha?.aMillis(), aviso30 = aviso30))
        }
    }

    fun marcar(tarea: Tarea) {
        viewModelScope.launch {
            val marcada = repositorio.marcar(tarea, !tarea.completada)
            if (marcada.completada) {
                ofrecerDeshacer(TipoAviso.COMPLETADA) { repositorio.marcar(marcada, false) }
            } else {
                ocultarAviso()
            }
        }
    }

    fun borrar(tarea: Tarea) {
        viewModelScope.launch {
            repositorio.borrar(tarea)
            // al guardarla con su mismo id vuelve tal como estaba
            ofrecerDeshacer(TipoAviso.BORRADA) { repositorio.guardar(tarea) }
        }
    }

    fun deshacer() {
        val accion = accionDeshacer
        ocultarAviso()
        viewModelScope.launch { accion?.invoke() }
    }

    // el aviso se va solo a los 4 segundos
    private fun ofrecerDeshacer(tipo: TipoAviso, accion: suspend () -> Unit) {
        accionDeshacer = accion
        avisoDeshacer.value = tipo
        esperaDelAviso?.cancel()
        esperaDelAviso = viewModelScope.launch {
            delay(4000)
            ocultarAviso()
        }
    }

    private fun ocultarAviso() {
        accionDeshacer = null
        avisoDeshacer.value = null
    }

    companion object {
        val Fabrica = viewModelFactory {
            initializer {
                val app = this[APPLICATION_KEY] as DiaPlusApp
                InicioViewModel(app.repositorio)
            }
        }
    }
}
