package com.diaplus.app.ui.screens.calendario

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.diaplus.app.R
import com.diaplus.app.data.model.Tarea
import com.diaplus.app.data.model.fecha
import com.diaplus.app.domain.diasDeLaCuadricula
import com.diaplus.app.domain.estaVencida
import com.diaplus.app.ui.components.BotonMas
import com.diaplus.app.ui.components.FormularioTarea
import com.diaplus.app.ui.components.TarjetaTarea
import com.diaplus.app.ui.theme.Colores
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.YearMonth

// un mes completo con puntos en los dias que tienen tareas, y debajo las tareas del dia elegido.
// solo dibuja; el ViewModel decide que mes y que dia se ven
@Composable
fun PantallaCalendario(
    c: Colores,
    estado: CalendarioEstado,
    ahora: LocalDateTime = LocalDateTime.now(),
    alVolver: () -> Unit,
    alCambiarMes: (Int) -> Unit,
    alElegirDia: (LocalDate) -> Unit,
    alIrAHoy: () -> Unit,
    alMarcar: (Tarea) -> Unit,
    alAbrir: (Tarea) -> Unit,
    alGuardarNueva: (String, LocalDateTime?, Boolean) -> Unit,
    abrirFormulario: Boolean = false,
) {
    var verFormulario by remember { mutableStateOf(abrirFormulario) }

    val hoy = ahora.toLocalDate()
    val meses = stringArrayResource(R.array.meses)
    val diasSemana = stringArrayResource(R.array.dias_semana)
    val letras = stringArrayResource(R.array.dias_letras)
    val elegido = estado.seleccionado
    val delDia = estado.tareasPorDia[elegido].orEmpty()

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
            Row(
                modifier = Modifier.padding(top = 12.dp, bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                BotonRedondo(c, R.drawable.ic_flecha_abajo, stringResource(R.string.volver), 90f, alVolver)
                Text(
                    text = stringResource(R.string.calendario),
                    color = c.texto,
                    fontSize = 23.sp,
                    fontWeight = FontWeight.ExtraBold,
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 12.dp)
                )
                Text(
                    text = stringResource(R.string.hoy),
                    color = c.naranjaOscuro,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(c.tarjeta)
                        .border(0.5.dp, c.borde, RoundedCornerShape(12.dp))
                        .clickable { alIrAHoy() }
                        .padding(horizontal = 14.dp, vertical = 8.dp)
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                BotonRedondo(c, R.drawable.ic_flecha_abajo, stringResource(R.string.mes_anterior), 90f) { alCambiarMes(-1) }
                Text(
                    text = stringResource(R.string.mes_titulo, meses[estado.mes.monthValue - 1], estado.mes.year),
                    color = c.texto,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1f)
                )
                BotonRedondo(c, R.drawable.ic_flecha_abajo, stringResource(R.string.mes_siguiente), -90f) { alCambiarMes(1) }
            }
            Spacer(modifier = Modifier.height(12.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                for (letra in letras) {
                    Text(
                        text = letra,
                        color = c.textoClaro,
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .weight(1f)
                            .padding(vertical = 4.dp)
                    )
                }
            }

            val dias = remember(estado.mes) { diasDeLaCuadricula(estado.mes) }
            for (semana in dias.chunked(7)) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    for (dia in semana) {
                        CeldaDia(
                            c = c,
                            dia = dia,
                            tareas = estado.tareasPorDia[dia].orEmpty(),
                            ahora = ahora,
                            fueraDelMes = YearMonth.from(dia) != estado.mes,
                            seleccionado = dia == elegido,
                            esHoy = dia == hoy,
                            alTocar = { alElegirDia(dia) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(2.dp))
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Leyenda(c, c.naranja, stringResource(R.string.leyenda_pendiente))
                Leyenda(c, c.naranjaOscuro, stringResource(R.string.leyenda_vencida), hueco = true)
                Leyenda(c, c.verde, stringResource(R.string.leyenda_hecha))
            }

            Box(
                modifier = Modifier
                    .padding(vertical = 14.dp)
                    .fillMaxWidth()
                    .height(0.5.dp)
                    .background(c.borde)
            )

            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    text = stringResource(
                        R.string.dia_titulo,
                        diasSemana[elegido.dayOfWeek.value - 1],
                        elegido.dayOfMonth,
                        meses[elegido.monthValue - 1].lowercase()
                    ),
                    color = c.texto,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = pluralStringResource(R.plurals.actividades, delDia.size, delDia.size),
                    color = c.textoMedio,
                    fontSize = 12.sp
                )
            }
            Spacer(modifier = Modifier.height(10.dp))

            if (delDia.isEmpty()) {
                DiaLibre(c)
            }
            for (tarea in delDia) {
                TarjetaTarea(tarea, c, ahora, alMarcar = { alMarcar(tarea) }, alTocar = { alAbrir(tarea) })
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
            // el boton atras cierra la hoja en vez de volver a la lista
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
                // la hoja abre con el dia elegido ya puesto
                FormularioTarea(c, diaInicial = elegido, alGuardar = { titulo, fecha, aviso30 ->
                    alGuardarNueva(titulo, fecha, aviso30)
                    verFormulario = false
                })
            }
        }
    }
}

@Composable
private fun CeldaDia(
    c: Colores,
    dia: LocalDate,
    tareas: List<Tarea>,
    ahora: LocalDateTime,
    fueraDelMes: Boolean,
    seleccionado: Boolean,
    esHoy: Boolean,
    alTocar: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val forma = RoundedCornerShape(12.dp)
    val meses = stringArrayResource(R.array.meses)
    val cuenta = pluralStringResource(R.plurals.actividades, tareas.size, tareas.size)
    val descripcion = stringResource(R.string.dia_descripcion, dia.dayOfMonth, meses[dia.monthValue - 1].lowercase(), cuenta)

    var colorNumero = c.texto
    if (seleccionado) {
        colorNumero = Color.White
    } else if (fueraDelMes) {
        colorNumero = c.textoClaro
    } else if (esHoy) {
        colorNumero = c.naranjaOscuro
    }

    Column(
        modifier = modifier
            .height(46.dp)
            .clip(forma)
            .background(if (seleccionado) c.naranja else Color.Transparent)
            .then(if (esHoy && !seleccionado) Modifier.border(1.5.dp, c.naranja, forma) else Modifier)
            .clickable { alTocar() }
            .semantics(mergeDescendants = true) { contentDescription = descripcion },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = dia.dayOfMonth.toString(),
            color = colorNumero,
            fontSize = 14.sp,
            fontWeight = if (esHoy || seleccionado) FontWeight.Bold else FontWeight.Normal
        )
        Spacer(modifier = Modifier.height(4.dp))
        // hasta tres puntos; el detalle esta en la lista de abajo
        Row(horizontalArrangement = Arrangement.spacedBy(3.dp), modifier = Modifier.height(6.dp)) {
            for (tarea in tareas.take(3)) {
                PuntoDeTarea(
                    color = if (seleccionado) Color.White else colorDeTarea(c, tarea, ahora),
                    hueco = esVencida(tarea, ahora)
                )
            }
        }
    }
}

private fun esVencida(tarea: Tarea, ahora: LocalDateTime): Boolean {
    return !tarea.completada && estaVencida(tarea.fecha(), ahora)
}

// verde = ya quedo, naranja oscuro = vencida, naranja = pendiente
private fun colorDeTarea(c: Colores, tarea: Tarea, ahora: LocalDateTime): Color {
    if (tarea.completada) return c.verde
    if (esVencida(tarea, ahora)) return c.naranjaOscuro
    return c.naranja
}

// punto lleno = pendiente o ya quedo; anillo = vencida (se distingue sin depender del color)
@Composable
private fun PuntoDeTarea(color: Color, hueco: Boolean) {
    Box(
        modifier = Modifier
            .size(6.dp)
            .clip(CircleShape)
            .then(if (hueco) Modifier.border(1.5.dp, color, CircleShape) else Modifier.background(color))
    )
}

@Composable
private fun BotonRedondo(c: Colores, icono: Int, descripcion: String, giro: Float, alTocar: () -> Unit) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(c.tarjeta2)
            .clickable { alTocar() },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(icono),
            contentDescription = descripcion,
            tint = c.textoMedio,
            modifier = Modifier
                .size(14.dp)
                .rotate(giro)
        )
    }
}

@Composable
private fun Leyenda(c: Colores, color: Color, texto: String, hueco: Boolean = false) {
    Row(
        modifier = Modifier.padding(horizontal = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        PuntoDeTarea(color, hueco)
        Spacer(modifier = Modifier.width(4.dp))
        Text(text = texto, color = c.textoMedio, fontSize = 11.sp)
    }
}

@Composable
private fun DiaLibre(c: Colores) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 28.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(RoundedCornerShape(18.dp))
                .background(c.naranjaClaro),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_sol),
                contentDescription = null,
                tint = c.naranja,
                modifier = Modifier.size(26.dp)
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = stringResource(R.string.dia_libre_titulo),
            color = c.texto,
            fontSize = 14.sp,
            fontWeight = FontWeight.ExtraBold
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = stringResource(R.string.dia_libre_texto),
            color = c.textoClaro,
            fontSize = 12.sp,
            textAlign = TextAlign.Center
        )
    }
}
