package com.diaplus.app.ui.screens.tarea

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.diaplus.app.R
import com.diaplus.app.data.model.Tarea
import com.diaplus.app.ui.components.CamposTarea
import com.diaplus.app.ui.theme.Colores
import java.time.LocalDateTime

// editar una tarea que ya existe. "tarea" es null mientras se lee de la base de datos
@Composable
fun PantallaTarea(
    c: Colores,
    tarea: Tarea?,
    alVolver: () -> Unit,
    alGuardar: (String, LocalDateTime?, Boolean) -> Unit,
    alBorrar: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(c.fondo)
            .statusBarsPadding()
            .imePadding()
            .verticalScroll(rememberScrollState())
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
                // la misma flecha de "Ya quedó", girada para que apunte hacia atras
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
                text = stringResource(R.string.editar_tarea),
                color = c.texto,
                fontSize = 23.sp,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier.padding(start = 12.dp)
            )
        }

        if (tarea != null) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .background(c.tarjeta)
                    .padding(20.dp)
            ) {
                CamposTarea(c, inicial = tarea, textoBoton = stringResource(R.string.guardar_cambios), alGuardar = alGuardar)
            }

            Text(
                text = stringResource(R.string.borrar_tarea),
                color = c.naranjaOscuro,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 8.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .clickable { alBorrar() }
                    .padding(horizontal = 20.dp, vertical = 14.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}
