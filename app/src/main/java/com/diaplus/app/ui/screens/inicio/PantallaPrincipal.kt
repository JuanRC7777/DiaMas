package com.diaplus.app.ui.screens.inicio

import androidx.activity.compose.BackHandler
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
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.diaplus.app.R
import com.diaplus.app.data.model.Tarea
import com.diaplus.app.ui.components.AvisoDeshacer
import com.diaplus.app.ui.components.BotonMas
import com.diaplus.app.ui.components.CampoAnotar
import com.diaplus.app.ui.components.Encabezado
import com.diaplus.app.ui.components.FormularioTarea
import com.diaplus.app.ui.components.TarjetaTarea
import com.diaplus.app.ui.components.TituloCompletadas
import com.diaplus.app.ui.theme.Colores
import com.diaplus.app.ui.theme.ColoresClaros
import java.time.LocalDateTime

// solo dibuja el estado que le llega y avisa de lo que hace el usuario;
// las tareas las maneja InicioViewModel.
// los dos ultimos parametros son para los previews
@Composable
fun PantallaPrincipal(
    c: Colores,
    oscuro: Boolean,
    cambiarTema: () -> Unit,
    estado: InicioEstado,
    ahora: LocalDateTime = LocalDateTime.now(),
    alAnotar: (String) -> Unit = {},
    alGuardarNueva: (String, LocalDateTime?, Boolean) -> Unit = { _, _, _ -> },
    alMarcar: (Tarea) -> Unit = {},
    alBorrar: (Tarea) -> Unit = {},
    alAbrir: (Tarea) -> Unit = {},
    alDeshacer: () -> Unit = {},
    alAbrirAjustes: () -> Unit = {},
    alAbrirCalendario: () -> Unit = {},
    abrirCompletadas: Boolean = false,
    abrirFormulario: Boolean = false,
) {
    var verCompletadas by remember { mutableStateOf(abrirCompletadas) }
    var verFormulario by remember { mutableStateOf(abrirFormulario) }

    val pendientes = estado.pendientes
    val completadas = estado.completadas

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
            Encabezado(c, oscuro, pendientes.size, cambiarTema, racha = estado.racha, abrirAjustes = alAbrirAjustes, abrirCalendario = alAbrirCalendario)

            CampoAnotar(c, alEnviar = alAnotar)
            Spacer(modifier = Modifier.height(12.dp))

            if (pendientes.isEmpty() && completadas.isEmpty() && !estado.cargando) {
                ListaVacia(c)
            }

            for (tarea in pendientes) {
                TarjetaDeslizable(tarea, c, ahora, alMarcar, alAbrir, alBorrar)
            }

            if (completadas.isNotEmpty()) {
                TituloCompletadas(c, completadas.size, alTocar = { verCompletadas = !verCompletadas })
                if (verCompletadas) {
                    for (tarea in completadas) {
                        TarjetaDeslizable(tarea, c, ahora, alMarcar, alAbrir, alBorrar)
                    }
                }
            }

            // para que el boton + no tape la ultima tarea
            Spacer(modifier = Modifier.height(100.dp))
        }

        val aviso = estado.avisoDeshacer
        if (aviso != null) {
            // el margen derecho es para no quedar debajo del boton +
            AvisoDeshacer(
                c,
                stringResource(if (aviso == TipoAviso.COMPLETADA) R.string.aviso_completada else R.string.aviso_borrada),
                alDeshacer,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .navigationBarsPadding()
                    .padding(start = 16.dp, end = 92.dp, bottom = 30.dp)
            )
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
            // el boton atras cierra la hoja en vez de salir de la app
            BackHandler { verFormulario = false }

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
                FormularioTarea(c, alGuardar = { titulo, fecha, aviso30 ->
                    alGuardarNueva(titulo, fecha, aviso30)
                    verFormulario = false
                })
            }
        }
    }
}

// la tarjeta de siempre, pero deslizarla hacia la izquierda la borra
@Composable
fun TarjetaDeslizable(
    tarea: Tarea,
    c: Colores,
    ahora: LocalDateTime,
    alMarcar: (Tarea) -> Unit,
    alAbrir: (Tarea) -> Unit,
    alBorrar: (Tarea) -> Unit,
) {
    // key: cada tarea recuerda su propio deslizamiento aunque cambie el orden de la lista
    key(tarea.id) {
        val deslizar = rememberSwipeToDismissBoxState()

        SwipeToDismissBox(
            state = deslizar,
            enableDismissFromStartToEnd = false,
            onDismiss = { alBorrar(tarea) },
            backgroundContent = {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 8.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(c.naranja)
                        .padding(end = 20.dp),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    Text(text = stringResource(R.string.borrar), color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        ) {
            TarjetaTarea(tarea, c, ahora, alMarcar = { alMarcar(tarea) }, alTocar = { alAbrir(tarea) })
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
        Text(text = stringResource(R.string.lista_vacia_titulo), color = c.texto, fontSize = 14.sp, fontWeight = FontWeight.ExtraBold)
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(R.string.lista_vacia_texto),
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
