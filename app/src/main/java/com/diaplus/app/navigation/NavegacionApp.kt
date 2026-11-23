package com.diaplus.app.navigation

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.diaplus.app.notifications.CanalNotificaciones
import com.diaplus.app.ui.screens.ajustes.AjustesViewModel
import com.diaplus.app.ui.screens.ajustes.PantallaAjustes
import com.diaplus.app.ui.screens.calendario.CalendarioViewModel
import com.diaplus.app.ui.screens.calendario.PantallaCalendario
import com.diaplus.app.ui.screens.inicio.InicioViewModel
import com.diaplus.app.ui.screens.inicio.PantallaPrincipal
import com.diaplus.app.ui.screens.tarea.PantallaTarea
import com.diaplus.app.ui.screens.tarea.TareaViewModel
import com.diaplus.app.ui.theme.Colores
import kotlinx.coroutines.delay
import java.time.LocalDateTime

@Composable
fun NavegacionApp(c: Colores, oscuro: Boolean, cambiarTema: () -> Unit) {
    val navegacion = rememberNavController()

    // esta afuera del NavHost para que las dos pantallas usen el mismo
    val inicio: InicioViewModel = viewModel(factory = InicioViewModel.Fabrica)

    PedirPermisoDeNotificaciones()

    NavHost(navController = navegacion, startDestination = Rutas.INICIO) {

        composable(Rutas.INICIO) {
            val estado by inicio.estado.collectAsState()

            val ahora by rememberAhora()

            PantallaPrincipal(
                c = c,
                oscuro = oscuro,
                cambiarTema = cambiarTema,
                estado = estado,
                ahora = ahora,
                alAnotar = { inicio.anotar(it) },
                alGuardarNueva = { titulo, fecha, aviso30 -> inicio.guardarNueva(titulo, fecha, aviso30) },
                alMarcar = { inicio.marcar(it) },
                alBorrar = { inicio.borrar(it) },
                alAbrir = { navegacion.navigate(Rutas.tarea(it.id)) },
                alDeshacer = { inicio.deshacer() },
                alAbrirAjustes = { navegacion.navigate(Rutas.AJUSTES) },
                alAbrirCalendario = { navegacion.navigate(Rutas.CALENDARIO) },
            )
        }

        composable(Rutas.CALENDARIO) {
            val calendario: CalendarioViewModel = viewModel(factory = CalendarioViewModel.Fabrica)
            val estado by calendario.estado.collectAsState()
            val ahora by rememberAhora()

            PantallaCalendario(
                c = c,
                estado = estado,
                ahora = ahora,
                alVolver = { navegacion.popBackStack() },
                alCambiarMes = { calendario.cambiarMes(it) },
                alElegirDia = { calendario.elegirDia(it) },
                alIrAHoy = { calendario.irAHoy() },
                alMarcar = { calendario.marcar(it) },
                alAbrir = { navegacion.navigate(Rutas.tarea(it.id)) },
                alGuardarNueva = { titulo, fecha, aviso30 -> calendario.guardarNueva(titulo, fecha, aviso30) },
            )
        }

        composable(Rutas.AJUSTES) {
            val ajustes: AjustesViewModel = viewModel(factory = AjustesViewModel.Fabrica)
            val estado by ajustes.estado.collectAsState()

            PantallaAjustes(
                c = c,
                estado = estado,
                alVolver = { navegacion.popBackStack() },
                alCambiarOscuro = { ajustes.cambiarOscuro(it) },
                alCambiarSonido = { ajustes.cambiarSonido(it) },
            )
        }

        composable(Rutas.TAREA, arguments = listOf(navArgument("id") { type = NavType.LongType })) { entrada ->
            val id = entrada.arguments?.getLong("id") ?: 0L
            val modelo: TareaViewModel = viewModel(factory = TareaViewModel.fabrica(id))
            val tarea by modelo.tarea.collectAsState()

            PantallaTarea(
                c = c,
                tarea = tarea,
                alVolver = { navegacion.popBackStack() },
                alGuardar = { titulo, fecha, aviso30 ->
                    modelo.guardar(titulo, fecha, aviso30, alTerminar = { navegacion.popBackStack() })
                },
                alBorrar = {
                    // la borra el ViewModel de inicio para que alla salga el "Deshacer"
                    tarea?.let { inicio.borrar(it) }
                    navegacion.popBackStack()
                },
            )
        }
    }
}

// la hora de ahora, renovada cada medio minuto para que "en 2h" o "vencida" no se queden viejos
@Composable
fun rememberAhora(): State<LocalDateTime> {
    return produceState(LocalDateTime.now()) {
        while (true) {
            delay(30_000)
            value = LocalDateTime.now()
        }
    }
}

// desde Android 13 hay que pedir permiso para mostrar notificaciones
@Composable
fun PedirPermisoDeNotificaciones() {
    val contexto = LocalContext.current
    val pedir = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) {}

    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && !CanalNotificaciones.tienePermiso(contexto)) {
            pedir.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }
}
