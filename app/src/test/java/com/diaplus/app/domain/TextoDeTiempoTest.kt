package com.diaplus.app.domain

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalDateTime

class TextoDeTiempoTest {

    private val ahora = LocalDateTime.of(2026, 11, 4, 17, 0)

    @Test
    fun sinFecha() {
        assertEquals("sin fecha", textoDeTiempo(null, ahora))
    }

    @Test
    fun faltanMinutos() {
        assertEquals("en 25min · 17:25", textoDeTiempo(ahora.plusMinutes(25), ahora))
    }

    @Test
    fun faltanHorasElMismoDia() {
        assertEquals("en 2h · 19:30", textoDeTiempo(ahora.plusMinutes(150), ahora))
    }

    @Test
    fun manana() {
        assertEquals("mañana · 20:28", textoDeTiempo(LocalDateTime.of(2026, 11, 5, 20, 28), ahora))
    }

    @Test
    fun faltanDias() {
        assertEquals("en 2d · 20:00", textoDeTiempo(LocalDateTime.of(2026, 11, 6, 20, 0), ahora))
    }

    @Test
    fun pasoHaceMinutos() {
        assertEquals("hace 10min · 16:50", textoDeTiempo(ahora.minusMinutes(10), ahora))
    }

    @Test
    fun pasoHaceHoras() {
        assertEquals("hace 3h · 14:00", textoDeTiempo(ahora.minusHours(3), ahora))
    }

    @Test
    fun pasoHaceDias() {
        assertEquals("hace 2d · 11:00", textoDeTiempo(LocalDateTime.of(2026, 11, 2, 11, 0), ahora))
    }

    @Test
    fun vencidaSoloSiLaFechaYaPaso() {
        assertTrue(estaVencida(ahora.minusMinutes(1), ahora))
        assertFalse(estaVencida(ahora.plusMinutes(1), ahora))
        assertFalse(estaVencida(null, ahora))
    }
}
