package com.diaplus.app.data

// "cuando" es el texto que sale debajo del titulo
data class Tarea(
    val titulo: String,
    val cuando: String,
    val vencida: Boolean = false,
    val aviso30: Boolean = false,
    val completada: Boolean = false,
)

val tareasDeEjemplo = listOf(
    Tarea("Pagar renta", "hace 3h · 14:00", vencida = true),
    Tarea("Entregar ensayo de Comunicación", "mañana · 20:28", aviso30 = true),
    Tarea("Estudiar para parcial de Cálculo", "en 2d · 20:00"),
    Tarea("Mandar correo al profesor", "hace 2d · 11:00", completada = true),
)
