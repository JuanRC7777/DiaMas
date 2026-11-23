package com.diaplus.app.ui.screens.inicio

import com.diaplus.app.data.model.Tarea

// qué se acaba de hacer; la pantalla elige el texto del aviso «Deshacer»
enum class TipoAviso { COMPLETADA, BORRADA }

// todo lo que la pantalla principal necesita para dibujarse
data class InicioEstado(
    val pendientes: List<Tarea> = emptyList(),
    val completadas: List<Tarea> = emptyList(),
    val racha: Int = 0,
    // null = no se muestra el aviso «Deshacer»
    val avisoDeshacer: TipoAviso? = null,
    // true mientras llega la primera lectura de la base de datos
    val cargando: Boolean = false,
)
