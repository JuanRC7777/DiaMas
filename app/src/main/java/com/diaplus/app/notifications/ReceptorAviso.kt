package com.diaplus.app.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.diaplus.app.DiaPlusApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

// Android lo despierta aunque la app este cerrada
class ReceptorAviso : BroadcastReceiver() {

    override fun onReceive(contexto: Context, intent: Intent) {
        val id = intent.getLongExtra(EXTRA_ID, 0)

        when (intent.action) {
            ACCION_AVISAR -> {
                val titulo = intent.getStringExtra(EXTRA_TITULO) ?: return
                val antes = intent.getBooleanExtra(EXTRA_ANTES, false)
                enSegundoPlano(contexto) { app ->
                    // el sonido se elige en ajustes
                    CanalNotificaciones.mostrar(contexto, id, titulo, antes, app.preferencias.sonido.first())
                }
            }

            ACCION_COMPLETAR -> {
                CanalNotificaciones.quitar(contexto, id)
                enSegundoPlano(contexto) { app ->
                    val tarea = app.repositorio.buscar(id)
                    if (tarea != null && !tarea.completada) {
                        app.repositorio.marcar(tarea, true)
                    }
                }
            }

            Intent.ACTION_BOOT_COMPLETED -> {
                enSegundoPlano(contexto) { app -> app.repositorio.reprogramarAvisos() }
            }
        }
    }

    // la base de datos no se puede usar en el hilo principal.
    // goAsync le pide a Android que espere a que termine
    private fun enSegundoPlano(contexto: Context, trabajo: suspend (DiaPlusApp) -> Unit) {
        val app = contexto.applicationContext as DiaPlusApp
        val pendiente = goAsync()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                trabajo(app)
            } finally {
                pendiente.finish()
            }
        }
    }

    companion object {
        const val ACCION_AVISAR = "com.diaplus.app.AVISAR"
        const val ACCION_COMPLETAR = "com.diaplus.app.COMPLETAR"
        const val EXTRA_ID = "id"
        const val EXTRA_TITULO = "titulo"
        const val EXTRA_ANTES = "antes"
    }
}
