package com.diaplus.app.navigation

// los nombres de las pantallas en un solo lugar
object Rutas {
    const val INICIO = "inicio"
    const val TAREA = "tarea/{id}"
    const val AJUSTES = "ajustes"
    const val CALENDARIO = "calendario"

    fun tarea(id: Long) = "tarea/$id"
}
