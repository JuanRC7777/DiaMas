package com.diaplus.app.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

// las fechas se guardan en milisegundos porque Room no sabe guardar LocalDateTime
@Entity(tableName = "tareas")
data class Tarea(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val titulo: String,
    val fechaMillis: Long? = null, // null = sin fecha
    val aviso30: Boolean = false,
    val completada: Boolean = false,
    val completadaMillis: Long? = null, // cuando se marco, para la racha
)

fun Tarea.fecha(): LocalDateTime? = fechaMillis?.aFechaHora()

fun LocalDateTime.aMillis(): Long = atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()

fun Long.aFechaHora(): LocalDateTime = Instant.ofEpochMilli(this).atZone(ZoneId.systemDefault()).toLocalDateTime()

// solo para las vistas previas: la app arranca con la lista vacia
fun tareasDeEjemplo(ahora: LocalDateTime = LocalDateTime.now()): List<Tarea> {
    val hoy = ahora.toLocalDate()
    return listOf(
        Tarea(1, "Pagar renta", ahora.minusHours(3).aMillis()),
        Tarea(2, "Entregar ensayo de Comunicación", hoy.plusDays(1).atTime(20, 28).aMillis(), aviso30 = true),
        Tarea(3, "Estudiar para parcial de Cálculo", hoy.plusDays(2).atTime(20, 0).aMillis()),
        Tarea(
            4, "Mandar correo al profesor", hoy.minusDays(2).atTime(11, 0).aMillis(),
            completada = true, completadaMillis = ahora.minusDays(1).aMillis()
        ),
    )
}
