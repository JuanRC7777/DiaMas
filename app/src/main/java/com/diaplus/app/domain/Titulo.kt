package com.diaplus.app.domain

// el titulo de una tarea siempre se guarda igual, lo cree donde lo cree el usuario:
// sin espacios de mas, y con la primera letra en mayuscula
fun tituloLimpio(titulo: String): String {
    return titulo.trim()
        .replace(Regex("\\s+"), " ")
        .replaceFirstChar { it.uppercase() }
}
