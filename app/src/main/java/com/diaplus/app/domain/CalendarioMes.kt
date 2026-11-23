package com.diaplus.app.domain

import java.time.LocalDate
import java.time.YearMonth

// los dias que se dibujan en la cuadricula de un mes. La semana empieza el lunes, y los huecos
// antes del dia 1 y despues del ultimo dia se llenan con dias del mes anterior y del siguiente
fun diasDeLaCuadricula(mes: YearMonth): List<LocalDate> {
    val primero = mes.atDay(1)
    // lunes = 0 ... domingo = 6
    val desfase = primero.dayOfWeek.value - 1
    val semanas = (desfase + mes.lengthOfMonth() + 6) / 7
    val inicio = primero.minusDays(desfase.toLong())
    return List(semanas * 7) { inicio.plusDays(it.toLong()) }
}

// al cambiar de mes se intenta conservar el numero del dia; si no existe (31 de enero -> febrero)
// queda el ultimo dia del mes
fun diaAlCambiarDeMes(actual: LocalDate, nuevoMes: YearMonth): LocalDate {
    return nuevoMes.atDay(minOf(actual.dayOfMonth, nuevoMes.lengthOfMonth()))
}
