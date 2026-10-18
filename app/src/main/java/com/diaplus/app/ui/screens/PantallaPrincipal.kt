package com.diaplus.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.diaplus.app.R
import com.diaplus.app.data.Tarea
import com.diaplus.app.data.tareasDeEjemplo
import com.diaplus.app.ui.components.BotonMas
import com.diaplus.app.ui.components.CampoAnotar
import com.diaplus.app.ui.components.Encabezado
import com.diaplus.app.ui.components.FormularioTarea
import com.diaplus.app.ui.components.TarjetaTarea
import com.diaplus.app.ui.components.TituloCompletadas
import com.diaplus.app.ui.theme.Colores
import com.diaplus.app.ui.theme.ColoresClaros

// los ultimos parametros son para los previews
@Composable
fun PantallaPrincipal(
    c: Colores,
    oscuro: Boolean,
    cambiarTema: () -> Unit,
    tareasIniciales: List<Tarea> = tareasDeEjemplo,
    abrirCompletadas: Boolean = false,
    abrirFormulario: Boolean = false,
) {
    val tareas = remember { tareasIniciales.toMutableStateList() }
    var verCompletadas by remember { mutableStateOf(abrirCompletadas) }
    var verFormulario by remember { mutableStateOf(abrirFormulario) }

    val pendientes = tareas.filter { !it.completada }
    val completadas = tareas.filter { it.completada }

    fun marcar(tarea: Tarea) {
        val posicion = tareas.indexOf(tarea)
        tareas[posicion] = tarea.copy(completada = !tarea.completada)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(c.fondo)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
        ) {
            Encabezado(c, oscuro, pendientes.size, cambiarTema)

            CampoAnotar(c)
            Spacer(modifier = Modifier.height(12.dp))

            if (tareas.isEmpty()) {
                ListaVacia(c)
            }

            for (tarea in pendientes) {
                TarjetaTarea(tarea, c, alMarcar = { marcar(tarea) })
            }

            if (completadas.isNotEmpty()) {
                TituloCompletadas(c, completadas.size, alTocar = { verCompletadas = !verCompletadas })
                if (verCompletadas) {
                    for (tarea in completadas) {
                        TarjetaTarea(tarea, c, alMarcar = { marcar(tarea) })
                    }
                }
            }

            // para que el boton + no tape la ultima tarea
            Spacer(modifier = Modifier.height(100.dp))
        }

        BotonMas(
            c,
            alTocar = { verFormulario = true },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .navigationBarsPadding()
                .padding(end = 20.dp, bottom = 24.dp)
        )

        if (verFormulario) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.4f))
                    .clickable { verFormulario = false }
            )
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .imePadding()
            ) {
                FormularioTarea(c, alGuardar = { titulo ->
                    tareas.add(0, Tarea(titulo, "sin fecha"))
                    verFormulario = false
                })
            }
        }
    }
}

@Composable
fun ListaVacia(c: Colores) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp, vertical = 64.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(c.naranjaClaro),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_sol),
                contentDescription = null,
                tint = c.naranja,
                modifier = Modifier.size(36.dp)
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
        Text(text = "La lista está limpia", color = c.texto, fontSize = 14.sp, fontWeight = FontWeight.ExtraBold)
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Escribe algo arriba para anotar tu primer pendiente. Sin pendientes, sin ruido.",
            color = c.textoClaro,
            fontSize = 12.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Preview(name = "Lista vacia", showBackground = true, backgroundColor = 0xFFFFF8F2)
@Composable
fun PreviewListaVacia() {
    ListaVacia(ColoresClaros)
}
