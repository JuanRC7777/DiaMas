package com.diaplus.app.domain

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

// lo que se entiende de un texto como "entregar ensayo mañana 3pm"
data class TextoLeido(val titulo: String, val fecha: LocalDateTime?)

// si se da el dia pero no la hora, el aviso es a las 9 de la mañana
val HORA_POR_DEFECTO: LocalTime = LocalTime.of(9, 0)

// para que "hoy" no se encuentre dentro de otra palabra
private const val INICIO = "(?<![\\p{L}\\d])"
private const val FIN = "(?![\\p{L}\\d])"
private const val A_LAS = "(?:a\\s+las?\\s+)?"

private fun patron(texto: String) = Regex(texto, RegexOption.IGNORE_CASE)

// "3pm", "3:30 pm", "a las 3 p.m."
private val horaConAmPm = patron("$INICIO$A_LAS(\\d{1,2})(?::(\\d{2}))?\\s*(am|pm|a\\.\\s?m\\.?|p\\.\\s?m\\.?)$FIN")

// "15:00", "a las 8:30"
private val horaConPuntos = patron("$INICIO$A_LAS(\\d{1,2}):(\\d{2})$FIN")

// "a las 8"
private val horaConALas = patron("${INICIO}a\\s+las?\\s+(\\d{1,2})$FIN")

private val deLaTarde = patron("$INICIO(?:de|por|en)\\s+la\\s+(?:tarde|noche)$FIN")
private val deLaManana = patron("$INICIO(?:de|por|en)\\s+la\\s+mañana$FIN")

private const val ANTES_DEL_DIA = "(?:para\\s+)?(?:el\\s+|este\\s+)?"
private val pasadoManana = patron("$INICIO${ANTES_DEL_DIA}pasado\\s+mañana$FIN")
private val manana = patron("$INICIO${ANTES_DEL_DIA}mañana$FIN")
private val hoy = patron("$INICIO${ANTES_DEL_DIA}hoy$FIN")

// en el mismo orden que DayOfWeek: lunes = 1 ... domingo = 7
private val diasDeLaSemana = listOf("lunes", "martes", "mi[ée]rcoles", "jueves", "viernes", "s[áa]bado", "domingo")
    .map { patron("$INICIO$ANTES_DEL_DIA$it$FIN") }

fun leerTextoNatural(texto: String, ahora: LocalDateTime): TextoLeido {
    var resto = texto
    var hora: LocalTime? = null
    var dia: LocalDate? = null

    // 1. la hora
    val conAmPm = horaConAmPm.find(resto)
    val conPuntos = horaConPuntos.find(resto)
    val conALas = horaConALas.find(resto)

    if (conAmPm != null) {
        val h = conAmPm.groupValues[1].toInt()
        val m = conAmPm.groupValues[2].ifEmpty { "0" }.toInt()
        val esPm = conAmPm.groupValues[3].startsWith("p", ignoreCase = true)
        if (h in 1..12 && m <= 59) {
            hora = LocalTime.of(h % 12 + if (esPm) 12 else 0, m)
            resto = resto.removeRange(conAmPm.range)
        }
    } else if (conPuntos != null) {
        val h = conPuntos.groupValues[1].toInt()
        val m = conPuntos.groupValues[2].toInt()
        if (h <= 23 && m <= 59) {
            hora = LocalTime.of(h, m)
            resto = resto.removeRange(conPuntos.range)
        }
    } else if (conALas != null) {
        val h = conALas.groupValues[1].toInt()
        if (h <= 23) {
            hora = LocalTime.of(h, 0)
            resto = resto.removeRange(conALas.range)
        }
    }

    // "a las 8 de la noche" son las 20:00.
    // solo se quita si hubo hora, y antes de buscar el "mañana" de dia siguiente
    if (hora != null) {
        if (deLaTarde.containsMatchIn(resto)) {
            if (hora.hour < 12) hora = hora.plusHours(12)
            resto = deLaTarde.replaceFirst(resto, "")
        } else if (deLaManana.containsMatchIn(resto)) {
            resto = deLaManana.replaceFirst(resto, "")
        }
    }

    // 2. el dia. "por la mañana" no cuenta como el dia de mañana
    val sinDeLaManana = deLaManana.replace(resto) { " ".repeat(it.value.length) }
    val hoyDia = ahora.toLocalDate()

    val encontrado = pasadoManana.find(sinDeLaManana)?.also { dia = hoyDia.plusDays(2) }
        ?: manana.find(sinDeLaManana)?.also { dia = hoyDia.plusDays(1) }
        ?: hoy.find(sinDeLaManana)?.also { dia = hoyDia }
        ?: diasDeLaSemana.firstNotNullOfOrNull { it.find(sinDeLaManana) }?.also { match ->
            val numero = diasDeLaSemana.indexOfFirst { it.matches(match.value) } + 1
            // el proximo dia con ese nombre; si es hoy, el de la otra semana
            var faltan = (numero - hoyDia.dayOfWeek.value + 7) % 7
            if (faltan == 0) faltan = 7
            dia = hoyDia.plusDays(faltan.toLong())
        }
    if (encontrado != null) {
        resto = resto.removeRange(encontrado.range)
    }

    // 3. el titulo es lo que queda
    var titulo = resto.replace(Regex("\\s+"), " ").trim().trim(',', '.', '-', ' ')
    if (titulo.isEmpty()) titulo = texto.trim()
    titulo = titulo.replaceFirstChar { it.uppercase() }

    return TextoLeido(titulo, juntarFechaHora(dia, hora, ahora))
}

// arma la fecha completa cuando falta el dia o la hora
fun juntarFechaHora(dia: LocalDate?, hora: LocalTime?, ahora: LocalDateTime): LocalDateTime? {
    if (hora == null) return dia?.atTime(HORA_POR_DEFECTO)
    if (dia != null) return dia.atTime(hora)

    // solo la hora: es hoy, o mañana si esa hora ya paso
    val hoyALaHora = ahora.toLocalDate().atTime(hora)
    return if (hoyALaHora.isAfter(ahora)) hoyALaHora else hoyALaHora.plusDays(1)
}
