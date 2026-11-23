package com.diaplus.app.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.diaplus.app.R
import com.diaplus.app.ui.theme.Colores
import com.diaplus.app.ui.theme.ColoresClaros

@Composable
fun CampoAnotar(c: Colores, alEnviar: (String) -> Unit = {}) {
    var texto by remember { mutableStateOf("") }

    // avisa a la pantalla y deja el campo limpio para la siguiente
    fun enviar() {
        if (texto.isNotBlank()) {
            alEnviar(texto)
            texto = ""
        }
    }

    Column {
        OutlinedTextField(
            value = texto,
            onValueChange = { texto = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text(stringResource(R.string.anotar_placeholder), fontSize = 14.sp)
            },
            leadingIcon = {
                Icon(
                    painter = painterResource(R.drawable.ic_mas),
                    contentDescription = null,
                    tint = c.textoClaro,
                    modifier = Modifier.size(16.dp)
                )
            },
            // el boton de enviar solo sale cuando hay algo escrito
            trailingIcon = {
                if (texto.isNotBlank()) {
                    Icon(
                        painter = painterResource(R.drawable.ic_check),
                        contentDescription = stringResource(R.string.anotar),
                        tint = c.naranja,
                        modifier = Modifier
                            .size(16.dp)
                            .clickable { enviar() }
                    )
                }
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { enviar() }),
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = c.tarjeta,
                unfocusedContainerColor = c.tarjeta,
                focusedBorderColor = c.naranja,
                unfocusedBorderColor = c.borde,
                focusedTextColor = c.texto,
                unfocusedTextColor = c.texto,
                focusedPlaceholderColor = c.textoClaro,
                unfocusedPlaceholderColor = c.textoClaro,
                cursorColor = c.naranja,
            )
        )
    }
}

@Preview(name = "Campo anotar", showBackground = true, backgroundColor = 0xFFFFF8F2, widthDp = 390)
@Composable
fun PreviewCampoAnotar() {
    Column(modifier = Modifier.padding(16.dp)) {
        CampoAnotar(ColoresClaros)
    }
}
