package com.diaplus.app.domain

import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDate

class CalcularRachaTest {

    private val hoy = LocalDate.of(2026, 11, 4)

    @Test
    fun sinTareasCompletadas() {
        assertEquals(0, calcularRacha(emptyList(), hoy))
    }

    @Test
    fun tresDiasSeguidosHastaHoy() {
        val dias = listOf(hoy, hoy.minusDays(1), hoy.minusDays(2))
        assertEquals(3, calcularRacha(dias, hoy))
    }

    @Test
    fun variasTareasElMismoDiaCuentanUnaVez() {
        assertEquals(1, calcularRacha(listOf(hoy, hoy, hoy), hoy))
    }

    @Test
    fun siHoyNoHayNadaSigueLaDeAyer() {
        val dias = listOf(hoy.minusDays(1), hoy.minusDays(2))
        assertEquals(2, calcularRacha(dias, hoy))
    }

    @Test
    fun unDiaSinCompletarLaCorta() {
        val dias = listOf(hoy, hoy.minusDays(2), hoy.minusDays(3))
        assertEquals(1, calcularRacha(dias, hoy))
    }

    @Test
    fun seRompeSiPasaronDosDias() {
        assertEquals(0, calcularRacha(listOf(hoy.minusDays(2)), hoy))
    }
}
