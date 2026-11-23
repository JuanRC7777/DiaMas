package com.diaplus.app.domain

import java.time.LocalDate

// dias seguidos en los que se completo al menos una tarea.
// si hoy todavia no se completo nada, la racha de ayer sigue contando
fun calcularRacha(diasCompletados: Collection<LocalDate>, hoy: LocalDate): Int {
    val dias = diasCompletados.toSet()

    var dia = if (hoy in dias) hoy else hoy.minusDays(1)
    var racha = 0
    while (dia in dias) {
        racha++
        dia = dia.minusDays(1)
    }
    return racha
}
