package com.diaplus.app.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.diaplus.app.data.model.fecha
import com.diaplus.app.data.model.tareasDeEjemplo
import com.diaplus.app.ui.screens.Bienvenida
import com.diaplus.app.ui.screens.ajustes.AjustesEstado
import com.diaplus.app.ui.screens.ajustes.PantallaAjustes
import com.diaplus.app.ui.screens.calendario.CalendarioEstado
import com.diaplus.app.ui.screens.calendario.PantallaCalendario
import com.diaplus.app.ui.screens.inicio.InicioEstado
import com.diaplus.app.ui.screens.inicio.PantallaPrincipal
import com.diaplus.app.ui.screens.inicio.TipoAviso
import com.diaplus.app.ui.screens.tarea.PantallaTarea
import com.diaplus.app.ui.theme.ColoresClaros
import com.diaplus.app.ui.theme.ColoresOscuros
import java.time.LocalDate
import java.time.YearMonth

// mismo tamaño del diseño de figma (390 x 844)

// los previews no usan la base de datos: se les pasa un estado armado a mano
private fun estadoCalendario(): CalendarioEstado {
    val porDia = tareasDeEjemplo()
        .mapNotNull { tarea -> tarea.fecha()?.let { it.toLocalDate() to tarea } }
        .groupBy({ it.first }, { it.second })
    return CalendarioEstado(YearMonth.now(), LocalDate.now(), porDia)
}

private fun estadoDeEjemplo(): InicioEstado {
    val tareas = tareasDeEjemplo()
    return InicioEstado(
        pendientes = tareas.filter { !it.completada },
        completadas = tareas.filter { it.completada },
        racha = 1,
    )
}

@Preview(name = "1 - Pantalla principal", showSystemUi = true, device = "spec:width=390dp,height=844dp")
@Composable
fun PreviewPrincipal() {
    PantallaPrincipal(ColoresClaros, oscuro = false, cambiarTema = {}, estado = estadoDeEjemplo())
}

@Preview(name = "2 - Modo oscuro", showSystemUi = true, device = "spec:width=390dp,height=844dp")
@Composable
fun PreviewOscuro() {
    PantallaPrincipal(ColoresOscuros, oscuro = true, cambiarTema = {}, estado = estadoDeEjemplo())
}

@Preview(name = "3 - Ya quedo abierto", showSystemUi = true, device = "spec:width=390dp,height=844dp")
@Composable
fun PreviewCompletadas() {
    PantallaPrincipal(
        ColoresClaros, oscuro = false, cambiarTema = {}, estado = estadoDeEjemplo(),
        abrirCompletadas = true
    )
}

@Preview(name = "4 - Nueva tarea", showSystemUi = true, device = "spec:width=390dp,height=844dp")
@Composable
fun PreviewFormulario() {
    PantallaPrincipal(
        ColoresClaros, oscuro = false, cambiarTema = {}, estado = estadoDeEjemplo(),
        abrirFormulario = true
    )
}

@Preview(name = "5 - Bienvenida", showSystemUi = true, device = "spec:width=390dp,height=844dp")
@Composable
fun PreviewBienvenida() {
    Bienvenida(ColoresClaros)
}

@Preview(name = "6 - Lista vacia", showSystemUi = true, device = "spec:width=390dp,height=844dp")
@Composable
fun PreviewVacia() {
    PantallaPrincipal(ColoresClaros, oscuro = false, cambiarTema = {}, estado = InicioEstado())
}

@Preview(name = "7 - Deshacer", showSystemUi = true, device = "spec:width=390dp,height=844dp")
@Composable
fun PreviewDeshacer() {
    PantallaPrincipal(
        ColoresClaros, oscuro = false, cambiarTema = {},
        estado = estadoDeEjemplo().copy(avisoDeshacer = TipoAviso.COMPLETADA)
    )
}

@Preview(name = "8 - Editar tarea", showSystemUi = true, device = "spec:width=390dp,height=844dp")
@Composable
fun PreviewEditarTarea() {
    PantallaTarea(ColoresClaros, tareasDeEjemplo()[1], alVolver = {}, alGuardar = { _, _, _ -> }, alBorrar = {})
}

@Preview(name = "9 - Ajustes", showSystemUi = true, device = "spec:width=390dp,height=844dp")
@Composable
fun PreviewAjustes() {
    PantallaAjustes(
        ColoresClaros, AjustesEstado(oscuro = false, sonido = true),
        alVolver = {}, alCambiarOscuro = {}, alCambiarSonido = {}
    )
}

@Preview(name = "10 - Ajustes oscuro", showSystemUi = true, device = "spec:width=390dp,height=844dp")
@Composable
fun PreviewAjustesOscuro() {
    PantallaAjustes(
        ColoresOscuros, AjustesEstado(oscuro = true, sonido = false),
        alVolver = {}, alCambiarOscuro = {}, alCambiarSonido = {}
    )
}

@Preview(name = "11 - Calendario", showSystemUi = true, device = "spec:width=390dp,height=844dp")
@Composable
fun PreviewCalendario() {
    PantallaCalendario(
        ColoresClaros, estadoCalendario(),
        alVolver = {}, alCambiarMes = {}, alElegirDia = {}, alIrAHoy = {},
        alMarcar = {}, alAbrir = {}, alGuardarNueva = { _, _, _ -> }
    )
}

@Preview(name = "12 - Calendario oscuro", showSystemUi = true, device = "spec:width=390dp,height=844dp")
@Composable
fun PreviewCalendarioOscuro() {
    PantallaCalendario(
        ColoresOscuros, estadoCalendario(),
        alVolver = {}, alCambiarMes = {}, alElegirDia = {}, alIrAHoy = {},
        alMarcar = {}, alAbrir = {}, alGuardarNueva = { _, _, _ -> }
    )
}

@Preview(name = "13 - Calendario nueva tarea", showSystemUi = true, device = "spec:width=390dp,height=844dp")
@Composable
fun PreviewCalendarioNuevaTarea() {
    PantallaCalendario(
        ColoresClaros, estadoCalendario(),
        alVolver = {}, alCambiarMes = {}, alElegirDia = {}, alIrAHoy = {},
        alMarcar = {}, alAbrir = {}, alGuardarNueva = { _, _, _ -> },
        abrirFormulario = true
    )
}
