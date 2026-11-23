package com.diaplus.app.ui.screens.calendario

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.diaplus.app.DiaPlusApp
import com.diaplus.app.data.model.Tarea
import com.diaplus.app.data.model.aMillis
import com.diaplus.app.data.model.fecha
import com.diaplus.app.data.repository.TareaRepositorio
import com.diaplus.app.domain.diaAlCambiarDeMes
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.YearMonth

// todo lo que la pantalla del calendario necesita para dibujarse.
// las tareas sin fecha no aparecen aqui: se ven en la lista
data class CalendarioEstado(
    val mes: YearMonth = YearMonth.now(),
    val seleccionado: LocalDate = LocalDate.now(),
    val tareasPorDia: Map<LocalDate, List<Tarea>> = emptyMap(),
)

// el mes que se ve y el dia elegido van juntos para que cambien a la vez
private data class Vista(val mes: YearMonth, val dia: LocalDate)

class CalendarioViewModel(private val repositorio: TareaRepositorio) : ViewModel() {

    private val vista = MutableStateFlow(Vista(YearMonth.now(), LocalDate.now()))

    val estado: StateFlow<CalendarioEstado> =
        combine(repositorio.tareas, vista) { tareas, v ->
            val porDia = tareas
                .mapNotNull { tarea -> tarea.fecha()?.let { it.toLocalDate() to tarea } }
                .groupBy({ it.first }, { it.second })
                .mapValues { (_, lista) -> lista.sortedBy { it.fechaMillis } }
            CalendarioEstado(v.mes, v.dia, porDia)
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), CalendarioEstado())

    // tocar un dia gris (de otro mes) tambien cambia de mes
    fun elegirDia(dia: LocalDate) {
        vista.value = Vista(YearMonth.from(dia), dia)
    }

    fun cambiarMes(cantidad: Int) {
        val nuevo = vista.value.mes.plusMonths(cantidad.toLong())
        vista.value = Vista(nuevo, diaAlCambiarDeMes(vista.value.dia, nuevo))
    }

    fun irAHoy() {
        vista.value = Vista(YearMonth.now(), LocalDate.now())
    }

    fun marcar(tarea: Tarea) {
        viewModelScope.launch { repositorio.marcar(tarea, !tarea.completada) }
    }

    fun guardarNueva(titulo: String, fecha: LocalDateTime?, aviso30: Boolean) {
        viewModelScope.launch {
            repositorio.guardar(Tarea(titulo = titulo, fechaMillis = fecha?.aMillis(), aviso30 = aviso30))
        }
    }

    companion object {
        val Fabrica = viewModelFactory {
            initializer {
                val app = this[APPLICATION_KEY] as DiaPlusApp
                CalendarioViewModel(app.repositorio)
            }
        }
    }
}
