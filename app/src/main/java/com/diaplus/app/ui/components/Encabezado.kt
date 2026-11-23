package com.diaplus.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
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
fun Encabezado(
    c: Colores,
    oscuro: Boolean,
    pendientes: Int,
    cambiarTema: () -> Unit,
    racha: Int = 0,
    abrirAjustes: () -> Unit = {},
    abrirCalendario: () -> Unit = {},
) {
    val hora = LocalTime.now().hour
    val saludo = stringResource(
        if (hora < 13) R.string.saludo_dia else if (hora < 20) R.string.saludo_tarde else R.string.saludo_noche
    )

    val hoy = stringArrayResource(R.array.dias_semana)[LocalDate.now().dayOfWeek.value - 1]

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 8.dp, top = 12.dp, bottom = 12.dp)
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = saludo, color = c.textoClaro, fontSize = 12.sp, fontWeight = FontWeight.Medium)
            Text(text = hoy, color = c.texto, fontSize = 23.sp, fontWeight = FontWeight.ExtraBold)
            Row {
                if (pendientes == 0) {
                    Text(text = stringResource(R.string.sin_pendientes), color = c.verde, fontSize = 12.sp)
                } else {
                    Text(
                        text = pluralStringResource(R.plurals.pendientes, pendientes, pendientes),
                        color = c.textoMedio,
                        fontSize = 12.sp
                    )
                }
                // dias seguidos completando al menos una tarea
                if (racha > 0) {
                    Text(
                        text = " · " + pluralStringResource(R.plurals.racha, racha, racha),
                        color = c.naranja,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }

        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(c.tarjeta2)
                .clickable { abrirCalendario() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_calendario),
                contentDescription = stringResource(R.string.calendario),
                tint = c.textoMedio,
                modifier = Modifier.size(15.dp)
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(c.tarjeta2)
                .clickable { abrirAjustes() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_ajustes),
                contentDescription = stringResource(R.string.abrir_ajustes),
                tint = c.textoMedio,
                modifier = Modifier.size(15.dp)
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(c.tarjeta2)
                .clickable { cambiarTema() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(if (oscuro) R.drawable.ic_sol else R.drawable.ic_luna),
                contentDescription = stringResource(R.string.cambiar_tema),
                tint = c.textoMedio,
                modifier = Modifier.size(15.dp)
            )
        }
    }
}

@Preview(name = "Encabezado", showBackground = true, backgroundColor = 0xFFFFF8F2, widthDp = 390)
@Composable
fun PreviewEncabezado() {
    Encabezado(ColoresClaros, oscuro = false, pendientes = 3, cambiarTema = {}, racha = 2)
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
