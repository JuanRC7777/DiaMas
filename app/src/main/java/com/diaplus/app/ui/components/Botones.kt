package com.diaplus.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
fun BotonMas(c: Colores, alTocar: () -> Unit, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .size(56.dp)
            .clip(CircleShape)
            .background(c.naranja)
            .clickable { alTocar() },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_mas),
            contentDescription = "nueva tarea",
            tint = Color.White,
            modifier = Modifier.size(24.dp)
        )
    }
}

@Composable
fun TituloCompletadas(c: Colores, cantidad: Int, alTocar: () -> Unit) {
    Row(
        modifier = Modifier
            .padding(top = 12.dp, bottom = 10.dp)
            .clickable { alTocar() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "YA QUEDÓ · $cantidad",
            color = c.textoClaro,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.5.sp
        )
        Icon(
            painter = painterResource(R.drawable.ic_flecha_abajo),
            contentDescription = null,
            tint = c.textoClaro,
            modifier = Modifier
                .padding(start = 6.dp)
                .size(10.dp)
        )
    }
}

@Preview(name = "Boton +", showBackground = true, backgroundColor = 0xFFFFF8F2)
@Composable
fun PreviewBotonMas() {
    Box(modifier = Modifier.padding(16.dp)) {
        BotonMas(ColoresClaros, alTocar = {})
    }
}

@Preview(name = "Titulo Ya quedo", showBackground = true, backgroundColor = 0xFFFFF8F2)
@Composable
fun PreviewTituloCompletadas() {
    Box(modifier = Modifier.padding(horizontal = 16.dp)) {
        TituloCompletadas(ColoresClaros, cantidad = 1, alTocar = {})
    }
}
