package com.diaplus.app.domain

import org.junit.Assert.assertEquals
import org.junit.Test

class TituloTest {

    @Test
    fun poneLaPrimeraLetraEnMayuscula() {
        assertEquals("Prueba calendario", tituloLimpio("prueba calendario"))
    }

    @Test
    fun quitaEspaciosAlPrincipioYAlFinal() {
        assertEquals("Pagar renta", tituloLimpio("   pagar renta  "))
    }

    @Test
    fun juntaLosEspaciosRepetidos() {
        assertEquals("Llamar a mamá", tituloLimpio("llamar   a  mamá"))
    }

    @Test
    fun noCambiaElRestoDeLasMayusculas() {
        assertEquals("Entregar informe de PCR", tituloLimpio("entregar informe de PCR"))
    }

    @Test
    fun unTituloVacioSigueVacio() {
        assertEquals("", tituloLimpio("   "))
    }

    @Test
    fun conAcentoEnLaPrimeraLetra() {
        assertEquals("Álgebra lineal", tituloLimpio("álgebra lineal"))
    }
}
