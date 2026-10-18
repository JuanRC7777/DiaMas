package com.diaplus.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.diaplus.app.R
import com.diaplus.app.ui.theme.Colores
import com.diaplus.app.ui.theme.ColoresClaros

@Composable
fun FormularioTarea(c: Colores, alGuardar: (String) -> Unit) {
    var titulo by remember { mutableStateOf("") }
    var avisar by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
            .background(c.tarjeta)
            .navigationBarsPadding()
            .padding(horizontal = 20.dp)
            .padding(bottom = 24.dp)
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 12.dp, bottom = 12.dp)
                .width(36.dp)
                .height(4.dp)
                .clip(CircleShape)
                .background(c.borde)
        )

        Text(
            text = "Nueva tarea",
            color = c.texto,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = titulo,
            onValueChange = { titulo = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("¿Qué tienes que hacer?") },
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = c.fondo,
                unfocusedContainerColor = c.fondo,
                focusedBorderColor = c.naranja,
                unfocusedBorderColor = c.borde,
                focusedTextColor = c.texto,
                unfocusedTextColor = c.texto,
                focusedPlaceholderColor = c.textoClaro,
                unfocusedPlaceholderColor = c.textoClaro,
                cursorColor = c.naranja,
            )
        )
        Spacer(modifier = Modifier.height(12.dp))

        // todavia no abren el calendario ni el reloj
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            CajitaFechaHora(c, R.drawable.ic_calendario, "dd/mm/aaaa", Modifier.weight(1f))
            CajitaFechaHora(c, R.drawable.ic_reloj, "--:--", Modifier.weight(1f))
        }
        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(c.fondo)
                .border(1.dp, c.borde, RoundedCornerShape(12.dp))
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Switch(
                checked = avisar,
                onCheckedChange = { avisar = it },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = c.naranja,
                    uncheckedThumbColor = Color.White,
                    uncheckedTrackColor = c.borde,
                    uncheckedBorderColor = c.borde,
                )
            )
            Column(modifier = Modifier.padding(start = 12.dp)) {
                Text(
                    text = "Avisar también 30 min antes",
                    color = c.texto,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(text = "Para tener margen de reacción real", color = c.textoClaro, fontSize = 10.sp)
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { alGuardar(titulo) },
            enabled = titulo.isNotBlank(),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = c.naranja,
                contentColor = Color.White,
                disabledContainerColor = c.naranja.copy(alpha = 0.4f),
                disabledContentColor = Color.White,
            )
        ) {
            Text(text = "Ya quedó", fontSize = 14.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun CajitaFechaHora(c: Colores, icono: Int, texto: String, modifier: Modifier) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(c.fondo)
            .border(1.dp, c.borde, RoundedCornerShape(12.dp))
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(icono),
            contentDescription = null,
            tint = c.textoClaro,
            modifier = Modifier.size(14.dp)
        )
        Text(
            text = texto,
            color = c.textoClaro,
            fontSize = 12.sp,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}

@Preview(name = "Formulario", showBackground = true, backgroundColor = 0xFF999999, widthDp = 390)
@Composable
fun PreviewFormulario() {
    FormularioTarea(ColoresClaros, alGuardar = {})
}

@Preview(name = "Cajita de fecha", showBackground = true, backgroundColor = 0xFFFFFFFF, widthDp = 180)
@Composable
fun PreviewCajitaFecha() {
    Box(modifier = Modifier.padding(8.dp)) {
        CajitaFechaHora(ColoresClaros, R.drawable.ic_calendario, "dd/mm/aaaa", Modifier)
    }
}
