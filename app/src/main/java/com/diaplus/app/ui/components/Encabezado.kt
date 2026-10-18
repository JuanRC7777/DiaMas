package com.diaplus.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.diaplus.app.R
import com.diaplus.app.ui.theme.Colores
import com.diaplus.app.ui.theme.ColoresClaros
import com.diaplus.app.ui.theme.ColoresOscuros
import java.time.LocalDate
import java.time.LocalTime

@Composable
fun Encabezado(c: Colores, oscuro: Boolean, pendientes: Int, cambiarTema: () -> Unit) {
    val hora = LocalTime.now().hour
    var saludo = "Buenas noches"
    if (hora < 13) {
        saludo = "Buenos días"
    } else if (hora < 20) {
        saludo = "Buenas tardes"
    }

    val dias = listOf("Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo")
    val hoy = dias[LocalDate.now().dayOfWeek.value - 1]

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 8.dp, top = 12.dp, bottom = 12.dp)
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = saludo, color = c.textoClaro, fontSize = 12.sp, fontWeight = FontWeight.Medium)
            Text(text = hoy, color = c.texto, fontSize = 23.sp, fontWeight = FontWeight.ExtraBold)
            if (pendientes == 0) {
                Text(text = "Un día menos con pendientes", color = c.verde, fontSize = 12.sp)
            } else if (pendientes == 1) {
                Text(text = "1 pendiente", color = c.textoMedio, fontSize = 12.sp)
            } else {
                Text(text = "$pendientes pendientes", color = c.textoMedio, fontSize = 12.sp)
            }
        }

        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(c.tarjeta2)
                .clickable { cambiarTema() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(if (oscuro) R.drawable.ic_sol else R.drawable.ic_luna),
                contentDescription = "cambiar tema",
                tint = c.textoMedio,
                modifier = Modifier.size(15.dp)
            )
        }
    }
}

@Preview(name = "Encabezado", showBackground = true, backgroundColor = 0xFFFFF8F2, widthDp = 390)
@Composable
fun PreviewEncabezado() {
    Encabezado(ColoresClaros, oscuro = false, pendientes = 3, cambiarTema = {})
}

@Preview(name = "Encabezado oscuro", showBackground = true, backgroundColor = 0xFF121826, widthDp = 390)
@Composable
fun PreviewEncabezadoOscuro() {
    Encabezado(ColoresOscuros, oscuro = true, pendientes = 3, cambiarTema = {})
}

@Preview(name = "Encabezado sin pendientes", showBackground = true, backgroundColor = 0xFFFFF8F2, widthDp = 390)
@Composable
fun PreviewEncabezadoSinPendientes() {
    Encabezado(ColoresClaros, oscuro = false, pendientes = 0, cambiarTema = {})
}
