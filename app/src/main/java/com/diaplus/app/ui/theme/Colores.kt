package com.diaplus.app.ui.theme

import androidx.compose.ui.graphics.Color

data class Colores(
    val fondo: Color,
    val tarjeta: Color,
    val tarjeta2: Color,
    val texto: Color,
    val textoMedio: Color,
    val textoClaro: Color,
    val borde: Color,
    val naranja: Color,
    val naranjaOscuro: Color,
    val naranjaClaro: Color,
    val verde: Color,
    val verdeClaro: Color,
)

// modo claro
val ColoresClaros = Colores(
    fondo = Color(0xFFFFF8F2),
    tarjeta = Color(0xFFFFFFFF),
    tarjeta2 = Color(0xFFFFF0E8),
    texto = Color(0xFF2C2018),
    textoMedio = Color(0xFF7A6558),
    textoClaro = Color(0xFFB8A090),
    borde = Color(0xFFEDE0D4),
    naranja = Color(0xFFFF8A5B),
    naranjaOscuro = Color(0xFFE8723F),
    naranjaClaro = Color(0xFFFFF0EB),
    verde = Color(0xFF3DBDB5),
    verdeClaro = Color(0xFFE4F8F7),
)

// modo oscuro
val ColoresOscuros = Colores(
    fondo = Color(0xFF121826),
    tarjeta = Color(0xFF1C2535),
    tarjeta2 = Color(0xFF232F44),
    texto = Color(0xFFEAE6DF),
    textoMedio = Color(0xFF8A96AA),
    textoClaro = Color(0xFF4A5672),
    borde = Color(0xFF2A3850),
    naranja = Color(0xFFFF9B6B),
    naranjaOscuro = Color(0xFFFF8A5B),
    naranjaClaro = Color(0xFF281A12),
    verde = Color(0xFF4ECDC4),
    verdeClaro = Color(0xFF122624),
)
