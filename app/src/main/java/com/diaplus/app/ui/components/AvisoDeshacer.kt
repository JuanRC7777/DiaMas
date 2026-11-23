package com.diaplus.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.diaplus.app.R
import com.diaplus.app.ui.theme.Colores
import com.diaplus.app.ui.theme.ColoresClaros

// aviso que sale abajo despues de completar o borrar una tarea.
// va con los colores al reves (fondo oscuro en modo claro) para que resalte
@Composable
fun AvisoDeshacer(c: Colores, texto: String, alDeshacer: () -> Unit, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(c.texto)
            .padding(start = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = texto, color = c.fondo, fontSize = 12.sp, fontWeight = FontWeight.Medium)
        Text(
            text = stringResource(R.string.deshacer),
            color = c.naranja,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .clickable { alDeshacer() }
                .padding(horizontal = 16.dp, vertical = 14.dp)
        )
    }
}

@Preview(name = "Aviso deshacer", showBackground = true, backgroundColor = 0xFFFFF8F2)
@Composable
fun PreviewAvisoDeshacer() {
    Box(modifier = Modifier.padding(16.dp)) {
        AvisoDeshacer(ColoresClaros, "Tarea completada", alDeshacer = {})
    }
}
