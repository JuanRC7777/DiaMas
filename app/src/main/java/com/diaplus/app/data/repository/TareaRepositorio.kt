package com.diaplus.app.data.repository

import com.diaplus.app.data.local.TareaDao
import com.diaplus.app.data.model.Tarea
import com.diaplus.app.notifications.ProgramadorAvisos
import kotlinx.coroutines.flow.Flow

// unico punto por el que la app pide o guarda tareas.
// cada cambio deja tambien al dia los recordatorios de esa tarea
class TareaRepositorio(
    private val dao: TareaDao,
    private val avisos: ProgramadorAvisos,
) {
    val tareas: Flow<List<Tarea>> = dao.observarTodas()

    suspend fun buscar(id: Long): Tarea? = dao.buscar(id)

    // devuelve la tarea con el id que le puso la base de datos
    suspend fun guardar(tarea: Tarea): Tarea {
        val id = dao.guardar(tarea)
        val guardada = tarea.copy(id = id)
        avisos.cancelar(id)
        avisos.programar(guardada)
        return guardada
    }

    suspend fun marcar(tarea: Tarea, completada: Boolean): Tarea {
        val cuando = if (completada) System.currentTimeMillis() else null
        return guardar(tarea.copy(completada = completada, completadaMillis = cuando))
    }

    suspend fun borrar(tarea: Tarea) {
        dao.borrar(tarea)
        avisos.cancelar(tarea.id)
    }

    // al reiniciar el telefono se pierden las alarmas
    suspend fun reprogramarAvisos() {
        for (tarea in dao.pendientesConFecha()) {
            avisos.programar(tarea)
        }
    }
}
