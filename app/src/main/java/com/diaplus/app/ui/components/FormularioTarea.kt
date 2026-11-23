package com.diaplus.app.ui.components

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.diaplus.app.R
import com.diaplus.app.data.model.Tarea
import com.diaplus.app.data.model.fecha
import com.diaplus.app.domain.HORA_POR_DEFECTO
import com.diaplus.app.domain.juntarFechaHora
import com.diaplus.app.domain.tituloLimpio
import com.diaplus.app.ui.theme.Colores
import com.diaplus.app.ui.theme.ColoresClaros
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

// la hoja "Nueva tarea" que sube desde abajo.
// alGuardar recibe el titulo, la fecha (o null) y si se avisa 30 min antes
@Composable
fun FormularioTarea(
    c: Colores,
    alGuardar: (String, LocalDateTime?, Boolean) -> Unit,
    diaInicial: LocalDate? = null,
) {
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
            text = stringResource(R.string.nueva_tarea),
            color = c.texto,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))

        CamposTarea(
            c, inicial = null, textoBoton = stringResource(R.string.ya_quedo),
            alGuardar = alGuardar, diaInicial = diaInicial
        )
    }
}

// los campos se comparten entre "Nueva tarea" y la pantalla de editar.
// con "inicial" arrancan llenos con los datos de esa tarea
@Composable
fun CamposTarea(
    c: Colores,
    inicial: Tarea?,
    textoBoton: String,
    alGuardar: (String, LocalDateTime?, Boolean) -> Unit,
    diaInicial: LocalDate? = null,
) {
    val contexto = LocalContext.current

    var titulo by remember { mutableStateOf(inicial?.titulo ?: "") }
    var dia by remember { mutableStateOf(inicial?.fecha()?.toLocalDate() ?: diaInicial) }
    // con un dia ya puesto, la hora arranca a las 9:00 para que lo que se ve sea lo que se guarda
    var hora by remember { mutableStateOf(inicial?.fecha()?.toLocalTime() ?: diaInicial?.let { HORA_POR_DEFECTO }) }
    var avisar by remember { mutableStateOf(inicial?.aviso30 ?: false) }

    // se usan el calendario y el reloj del telefono
    fun elegirDia() {
        val base = dia ?: LocalDate.now()
        DatePickerDialog(
            contexto,
            // los meses del calendario de Android empiezan en 0
            { _, anio, mes, diaDelMes -> dia = LocalDate.of(anio, mes + 1, diaDelMes) },
            base.year, base.monthValue - 1, base.dayOfMonth
        ).show()
    }

    fun elegirHora() {
        val base = hora ?: LocalTime.of(9, 0)
        TimePickerDialog(
            contexto,
            { _, h, m -> hora = LocalTime.of(h, m) },
            base.hour, base.minute, true
        ).show()
    }

    OutlinedTextField(
        value = titulo,
        onValueChange = { titulo = it },
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text(stringResource(R.string.que_tienes_que_hacer)) },
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

    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        val textoDia = dia?.let { "%02d/%02d/%d".format(it.dayOfMonth, it.monthValue, it.year) }
        val textoHora = hora?.let { "%02d:%02d".format(it.hour, it.minute) }

        CajitaFechaHora(
            c, R.drawable.ic_calendario, textoDia ?: stringResource(R.string.fecha_vacia), Modifier.weight(1f),
            elegido = dia != null, alTocar = { elegirDia() }
        )
        CajitaFechaHora(
            c, R.drawable.ic_reloj, textoHora ?: stringResource(R.string.hora_vacia), Modifier.weight(1f),
            elegido = hora != null, alTocar = { elegirHora() }
        )
    }

    if (dia != null || hora != null) {
        Text(
            text = stringResource(R.string.quitar_fecha),
            color = c.textoMedio,
            fontSize = 11.sp,
            modifier = Modifier
                .padding(top = 6.dp)
                .clickable {
                    dia = null
                    hora = null
                }
        )
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
                text = stringResource(R.string.avisar_antes),
                color = c.texto,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold
            )
            Text(text = stringResource(R.string.avisar_antes_detalle), color = c.textoClaro, fontSize = 10.sp)
        }
    }
    Spacer(modifier = Modifier.height(16.dp))

    Button(
        onClick = {
            // solo el dia = a las 9:00; solo la hora = hoy (o mañana si ya paso)
            val fecha = juntarFechaHora(dia, hora, LocalDateTime.now())
            // sin fecha no hay nada que avisar
            alGuardar(tituloLimpio(titulo), fecha, avisar && fecha != null)
        },
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
        Text(text = textoBoton, fontSize = 14.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun CajitaFechaHora(
    c: Colores,
    icono: Int,
    texto: String,
    modifier: Modifier,
    elegido: Boolean = false,
    alTocar: () -> Unit = {},
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(c.fondo)
            .border(1.dp, c.borde, RoundedCornerShape(12.dp))
            .clickable { alTocar() }
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(icono),
            contentDescription = null,
            tint = if (elegido) c.naranja else c.textoClaro,
            modifier = Modifier.size(14.dp)
        )
        Text(
            text = texto,
            color = if (elegido) c.texto else c.textoClaro,
            fontSize = 12.sp,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}

@Preview(name = "Formulario", showBackground = true, backgroundColor = 0xFF999999, widthDp = 390)
@Composable
fun PreviewFormulario() {
    FormularioTarea(ColoresClaros, alGuardar = { _, _, _ -> })
}

@Preview(name = "Cajita de fecha", showBackground = true, backgroundColor = 0xFFFFFFFF, widthDp = 180)
@Composable
fun PreviewCajitaFecha() {
    Box(modifier = Modifier.padding(8.dp)) {
        CajitaFechaHora(ColoresClaros, R.drawable.ic_calendario, "dd/mm/aaaa", Modifier)
    }
}
