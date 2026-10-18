package com.diaplus.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.diaplus.app.R
import com.diaplus.app.data.Tarea
import com.diaplus.app.ui.theme.Colores
import com.diaplus.app.ui.theme.ColoresClaros
import com.diaplus.app.ui.theme.ColoresOscuros

@Composable
fun TarjetaTarea(tarea: Tarea, c: Colores, alMarcar: () -> Unit) {
    var colorRaya = c.borde
    if (tarea.completada) {
        colorRaya = c.verde
    } else if (tarea.vencida) {
        colorRaya = c.naranja
    }

    var colorCuando = c.textoClaro
    if (tarea.completada) {
        colorCuando = c.verde
    } else if (tarea.vencida) {
        colorCuando = c.naranja
    }

    var fondo = c.tarjeta
    if (tarea.completada) {
        fondo = c.verdeClaro
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
            .height(IntrinsicSize.Min)
            .clip(RoundedCornerShape(16.dp))
            .background(fondo),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .width(4.dp)
                .fillMaxHeight()
                .background(colorRaya)
        )

        Box(
            modifier = Modifier
                .padding(start = 14.dp)
                .size(24.dp)
                .clip(CircleShape)
                .background(if (tarea.completada) c.verde else Color.Transparent)
                .border(2.dp, if (tarea.completada) c.verde else c.naranja, CircleShape)
                .clickable { alMarcar() },
            contentAlignment = Alignment.Center
        ) {
            if (tarea.completada) {
                Icon(
                    painter = painterResource(R.drawable.ic_check),
                    contentDescription = "completada",
                    tint = Color.White,
                    modifier = Modifier.size(12.dp)
                )
            }
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 14.dp, vertical = 14.dp)
        ) {
            Text(
                text = tarea.titulo,
                color = if (tarea.completada) c.textoClaro else c.texto,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                textDecoration = if (tarea.completada) TextDecoration.LineThrough else null
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (tarea.vencida && !tarea.completada) {
                    Text(
                        text = "vencida · ",
                        color = c.naranja,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                Text(text = tarea.cuando, color = colorCuando, fontSize = 12.sp)
                if (tarea.aviso30 && !tarea.completada) {
                    Text(
                        text = "+30m",
                        color = c.textoClaro,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier
                            .padding(start = 6.dp)
                            .clip(CircleShape)
                            .background(c.tarjeta2)
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }
        }

        if (!tarea.completada) {
            Icon(
                painter = painterResource(R.drawable.ic_campana),
                contentDescription = "recordatorio",
                tint = c.textoClaro,
                modifier = Modifier
                    .padding(end = 16.dp)
                    .size(16.dp)
            )
        }
    }
}

@Preview(name = "Tarjeta normal", showBackground = true, backgroundColor = 0xFFFFF8F2, widthDp = 360)
@Composable
fun PreviewTarjetaNormal() {
    Column(modifier = Modifier.padding(16.dp)) {
        TarjetaTarea(Tarea("Estudiar para parcial de Cálculo", "en 2d · 20:00"), ColoresClaros, alMarcar = {})
    }
}

@Preview(name = "Tarjeta vencida", showBackground = true, backgroundColor = 0xFFFFF8F2, widthDp = 360)
@Composable
fun PreviewTarjetaVencida() {
    Column(modifier = Modifier.padding(16.dp)) {
        TarjetaTarea(Tarea("Pagar renta", "hace 3h · 14:00", vencida = true), ColoresClaros, alMarcar = {})
    }
}

@Preview(name = "Tarjeta con aviso +30m", showBackground = true, backgroundColor = 0xFFFFF8F2, widthDp = 360)
@Composable
fun PreviewTarjetaAviso() {
    Column(modifier = Modifier.padding(16.dp)) {
        TarjetaTarea(Tarea("Entregar ensayo de Comunicación", "mañana · 20:28", aviso30 = true), ColoresClaros, alMarcar = {})
    }
}

@Preview(name = "Tarjeta completada", showBackground = true, backgroundColor = 0xFFFFF8F2, widthDp = 360)
@Composable
fun PreviewTarjetaCompletada() {
    Column(modifier = Modifier.padding(16.dp)) {
        TarjetaTarea(Tarea("Mandar correo al profesor", "hace 2d · 11:00", completada = true), ColoresClaros, alMarcar = {})
    }
}

@Preview(name = "Tarjeta modo oscuro", showBackground = true, backgroundColor = 0xFF121826, widthDp = 360)
@Composable
fun PreviewTarjetaOscura() {
    Column(modifier = Modifier.padding(16.dp)) {
        TarjetaTarea(Tarea("Pagar renta", "hace 3h · 14:00", vencida = true), ColoresOscuros, alMarcar = {})
    }
}
