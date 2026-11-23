package com.diaplus.app.domain

import java.time.Duration
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

// una tarea esta vencida si su fecha ya paso
fun estaVencida(fecha: LocalDateTime?, ahora: LocalDateTime): Boolean {
    return fecha != null && fecha.isBefore(ahora)
}

// el texto que sale debajo del titulo: "en 2h · 20:00", "mañana · 15:00", "hace 3d · 11:00"
fun textoDeTiempo(fecha: LocalDateTime?, ahora: LocalDateTime): String {
    if (fecha == null) return "sin fecha"

    val minutos = Duration.between(ahora, fecha).toMinutes()
    val dias = ChronoUnit.DAYS.between(ahora.toLocalDate(), fecha.toLocalDate())

    val cuando = when {
        minutos == 0L -> "ahora"
        // lo que falta
        minutos in 1..59 -> "en ${minutos}min"
        minutos > 0 && dias == 0L -> "en ${minutos / 60}h"
        minutos > 0 && dias == 1L -> "mañana"
        minutos > 0 -> "en ${dias}d"
        // lo que ya paso
        minutos > -60 -> "hace ${-minutos}min"
        minutos > -24 * 60 -> "hace ${-minutos / 60}h"
        else -> "hace ${-minutos / (24 * 60)}d"
    }

    val hora = "%02d:%02d".format(fecha.hour, fecha.minute)
    return "$cuando · $hora"
}
