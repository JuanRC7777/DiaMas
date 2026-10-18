package com.diaplus.app.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.diaplus.app.ui.screens.Bienvenida
import com.diaplus.app.ui.screens.PantallaPrincipal
import com.diaplus.app.ui.theme.ColoresClaros
import com.diaplus.app.ui.theme.ColoresOscuros

// mismo tamaño del diseño de figma (390 x 844)

@Preview(name = "1 - Pantalla principal", showSystemUi = true, device = "spec:width=390dp,height=844dp")
@Composable
fun PreviewPrincipal() {
    PantallaPrincipal(ColoresClaros, oscuro = false, cambiarTema = {})
}

@Preview(name = "2 - Modo oscuro", showSystemUi = true, device = "spec:width=390dp,height=844dp")
@Composable
fun PreviewOscuro() {
    PantallaPrincipal(ColoresOscuros, oscuro = true, cambiarTema = {})
}

@Preview(name = "3 - Ya quedo abierto", showSystemUi = true, device = "spec:width=390dp,height=844dp")
@Composable
fun PreviewCompletadas() {
    PantallaPrincipal(ColoresClaros, oscuro = false, cambiarTema = {}, abrirCompletadas = true)
}

@Preview(name = "4 - Nueva tarea", showSystemUi = true, device = "spec:width=390dp,height=844dp")
@Composable
fun PreviewFormulario() {
    PantallaPrincipal(ColoresClaros, oscuro = false, cambiarTema = {}, abrirFormulario = true)
}

@Preview(name = "5 - Bienvenida", showSystemUi = true, device = "spec:width=390dp,height=844dp")
@Composable
fun PreviewBienvenida() {
    Bienvenida(ColoresClaros)
}

@Preview(name = "6 - Lista vacia", showSystemUi = true, device = "spec:width=390dp,height=844dp")
@Composable
fun PreviewVacia() {
    PantallaPrincipal(ColoresClaros, oscuro = false, cambiarTema = {}, tareasIniciales = emptyList())
}
