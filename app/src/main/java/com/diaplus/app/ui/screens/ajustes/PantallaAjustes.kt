package com.diaplus.app.ui.screens.ajustes

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.diaplus.app.R
import com.diaplus.app.ui.theme.Colores

@Composable
fun PantallaAjustes(
    c: Colores,
    estado: AjustesEstado,
    alVolver: () -> Unit,
    alCambiarOscuro: (Boolean) -> Unit,
    alCambiarSonido: (Boolean) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(c.fondo)
            .statusBarsPadding()
            .padding(horizontal = 16.dp)
    ) {
        Row(
            modifier = Modifier.padding(top = 12.dp, bottom = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(c.tarjeta2)
                    .clickable { alVolver() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_flecha_abajo),
                    contentDescription = stringResource(R.string.volver),
                    tint = c.textoMedio,
                    modifier = Modifier
                        .size(14.dp)
                        .rotate(90f)
                )
            }
            Text(
                text = stringResource(R.string.ajustes),
                color = c.texto,
                fontSize = 23.sp,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier.padding(start = 12.dp)
            )
        }

        FilaAjuste(
            c, stringResource(R.string.ajuste_oscuro), stringResource(R.string.ajuste_oscuro_detalle),
            estado.oscuro, alCambiarOscuro
        )
        FilaAjuste(
            c, stringResource(R.string.ajuste_sonido), stringResource(R.string.ajuste_sonido_detalle),
            estado.sonido, alCambiarSonido
        )
    }
}

// una tarjeta con título, explicación y un interruptor
@Composable
private fun FilaAjuste(
    c: Colores,
    titulo: String,
    detalle: String,
    activado: Boolean,
    alCambiar: (Boolean) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(c.tarjeta)
            .clickable { alCambiar(!activado) }
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = titulo, color = c.texto, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
            Text(text = detalle, color = c.textoClaro, fontSize = 12.sp)
        }
        Switch(
            checked = activado,
            onCheckedChange = alCambiar,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = c.naranja,
                uncheckedThumbColor = Color.White,
                uncheckedTrackColor = c.borde,
                uncheckedBorderColor = c.borde,
            )
        )
    }
}
