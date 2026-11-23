package com.diaplus.app.domain

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import java.time.LocalDateTime

class LeerTextoNaturalTest {

    // miercoles 4 de noviembre de 2026, 10:00
    private val ahora = LocalDateTime.of(2026, 11, 4, 10, 0)

    private fun leer(texto: String) = leerTextoNatural(texto, ahora)

    @Test
    fun mananaConHoraPm() {
        val leido = leer("entregar ensayo mañana 3pm")
        assertEquals("Entregar ensayo", leido.titulo)
        assertEquals(LocalDateTime.of(2026, 11, 5, 15, 0), leido.fecha)
    }

    @Test
    fun sinFechaNiHora() {
        val leido = leer("comprar 2 panes")
        assertEquals("Comprar 2 panes", leido.titulo)
        assertNull(leido.fecha)
    }

    @Test
    fun soloElDiaEsALasNueve() {
        val leido = leer("llamar a mamá mañana")
        assertEquals("Llamar a mamá", leido.titulo)
        assertEquals(LocalDateTime.of(2026, 11, 5, 9, 0), leido.fecha)
    }

    @Test
    fun soloLaHoraEsHoySiTodaviaNoPasa() {
        assertEquals(LocalDateTime.of(2026, 11, 4, 20, 30), leer("gimnasio a las 20:30").fecha)
    }

    @Test
    fun soloLaHoraEsMananaSiYaPaso() {
        val leido = leer("desayunar 8am")
        assertEquals("Desayunar", leido.titulo)
        assertEquals(LocalDateTime.of(2026, 11, 5, 8, 0), leido.fecha)
    }

    @Test
    fun hoyConMinutos() {
        val leido = leer("reunión hoy 3:30 pm")
        assertEquals("Reunión", leido.titulo)
        assertEquals(LocalDateTime.of(2026, 11, 4, 15, 30), leido.fecha)
    }

    @Test
    fun pasadoManana() {
        val leido = leer("pagar renta pasado mañana")
        assertEquals("Pagar renta", leido.titulo)
        assertEquals(LocalDateTime.of(2026, 11, 6, 9, 0), leido.fecha)
    }

    @Test
    fun diaDeLaSemana() {
        val leido = leer("parcial de cálculo el viernes a las 8")
        assertEquals("Parcial de cálculo", leido.titulo)
        assertEquals(LocalDateTime.of(2026, 11, 6, 8, 0), leido.fecha)
    }

    @Test
    fun elMismoDiaDeLaSemanaEsLaOtraSemana() {
        assertEquals(LocalDateTime.of(2026, 11, 11, 9, 0), leer("médico miercoles").fecha)
    }

    @Test
    fun deLaNocheSumaDoce() {
        val leido = leer("cine sábado a las 8 de la noche")
        assertEquals("Cine", leido.titulo)
        assertEquals(LocalDateTime.of(2026, 11, 7, 20, 0), leido.fecha)
    }

    @Test
    fun porLaMananaNoEsElDiaDeManana() {
        val leido = leer("correr por la mañana")
        assertEquals("Correr por la mañana", leido.titulo)
        assertNull(leido.fecha)
    }

    @Test
    fun mananaPorLaManana() {
        val leido = leer("correr mañana a las 7 de la mañana")
        assertEquals("Correr", leido.titulo)
        assertEquals(LocalDateTime.of(2026, 11, 5, 7, 0), leido.fecha)
    }

    @Test
    fun quitaElPara() {
        assertEquals("Entregar informe", leer("entregar informe para el lunes").titulo)
    }

    @Test
    fun docePmEsMediodia() {
        assertEquals(LocalDateTime.of(2026, 11, 4, 12, 0), leer("almuerzo 12pm").fecha)
    }

    @Test
    fun horaImposibleSeQuedaEnElTitulo() {
        val leido = leer("ver capítulo 25:99")
        assertEquals("Ver capítulo 25:99", leido.titulo)
        assertNull(leido.fecha)
    }

    @Test
    fun soloFechaUsaElTextoComoTitulo() {
        assertEquals("Mañana 3pm", leer("mañana 3pm").titulo)
    }

    @Test
    fun noEncuentraHoyDentroDeOtraPalabra() {
        val leido = leer("tapar el hoyo")
        assertEquals("Tapar el hoyo", leido.titulo)
        assertNull(leido.fecha)
    }
}
