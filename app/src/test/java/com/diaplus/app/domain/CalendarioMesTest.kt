package com.diaplus.app.domain

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth

class CalendarioMesTest {

    @Test
    fun noviembre2026EmpiezaEnDomingoYNecesitaSeisSemanas() {
        val dias = diasDeLaCuadricula(YearMonth.of(2026, 11))

        assertEquals(42, dias.size)
        // el 1 de noviembre es domingo: la cuadricula arranca el lunes 26 de octubre
        assertEquals(LocalDate.of(2026, 10, 26), dias.first())
        assertEquals(LocalDate.of(2026, 12, 6), dias.last())
    }

    @Test
    fun febrero2027EmpiezaEnLunesYCabeEnCuatroSemanas() {
        val dias = diasDeLaCuadricula(YearMonth.of(2027, 2))

        assertEquals(28, dias.size)
        assertEquals(LocalDate.of(2027, 2, 1), dias.first())
        assertEquals(LocalDate.of(2027, 2, 28), dias.last())
    }

    @Test
    fun laCuadriculaSiempreEmpiezaEnLunesYSonSemanasCompletas() {
        for (mes in 1..12) {
            val dias = diasDeLaCuadricula(YearMonth.of(2026, mes))
            assertEquals(DayOfWeek.MONDAY, dias.first().dayOfWeek)
            assertEquals(0, dias.size % 7)
        }
    }

    @Test
    fun todosLosDiasDelMesEstanYSonSeguidos() {
        val mes = YearMonth.of(2028, 2)
        val dias = diasDeLaCuadricula(mes)

        // 2028 es bisiesto: febrero tiene 29 dias
        assertEquals(29, dias.count { YearMonth.from(it) == mes })
        for (i in 1 until dias.size) {
            assertEquals(dias[i - 1].plusDays(1), dias[i])
        }
    }

    @Test
    fun alCambiarDeMesSeConservaElNumeroDelDia() {
        val nuevo = diaAlCambiarDeMes(LocalDate.of(2026, 11, 15), YearMonth.of(2026, 12))
        assertEquals(LocalDate.of(2026, 12, 15), nuevo)
    }

    @Test
    fun si31NoExisteQuedaElUltimoDiaDelMes() {
        assertEquals(
            LocalDate.of(2027, 2, 28),
            diaAlCambiarDeMes(LocalDate.of(2027, 1, 31), YearMonth.of(2027, 2))
        )
        assertEquals(
            LocalDate.of(2026, 11, 30),
            diaAlCambiarDeMes(LocalDate.of(2026, 12, 31), YearMonth.of(2026, 11))
        )
    }

    @Test
    fun cambiarDeMesNuncaSaleDelMesNuevo() {
        val nuevo = YearMonth.of(2026, 4)
        val dia = diaAlCambiarDeMes(LocalDate.of(2026, 3, 31), nuevo)
        assertTrue(YearMonth.from(dia) == nuevo)
    }
}
