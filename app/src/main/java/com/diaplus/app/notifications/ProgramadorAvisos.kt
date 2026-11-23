package com.diaplus.app.notifications

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.diaplus.app.data.model.Tarea

// programa y cancela las alarmas de cada tarea
class ProgramadorAvisos(private val contexto: Context) {

    private val alarmas = contexto.getSystemService(AlarmManager::class.java)

    fun programar(tarea: Tarea) {
        val fecha = tarea.fechaMillis ?: return
        if (tarea.completada) return

        val ahora = System.currentTimeMillis()
        if (fecha > ahora) {
            poner(fecha, intento(tarea.id, tarea.titulo, antes = false))
        }

        val mediaHoraAntes = fecha - 30 * 60 * 1000
        if (tarea.aviso30 && mediaHoraAntes > ahora) {
            poner(mediaHoraAntes, intento(tarea.id, tarea.titulo, antes = true))
        }
    }

    // cancela las dos alarmas y quita de la barra la notificacion de esa tarea, si ya habia llegado.
    // asi, completar o borrar la tarea dentro de la app no deja un aviso viejo
    fun cancelar(id: Long) {
        alarmas.cancel(intento(id, "", antes = false))
        alarmas.cancel(intento(id, "", antes = true))
        CanalNotificaciones.quitar(contexto, id)
    }

    private fun poner(cuando: Long, intento: PendingIntent) {
        // desde Android 12 la alarma exacta necesita un permiso que el usuario puede quitar.
        // sin el permiso se usa una normal, que puede llegar unos minutos tarde
        val puedeExacta = Build.VERSION.SDK_INT < Build.VERSION_CODES.S || alarmas.canScheduleExactAlarms()
        if (puedeExacta) {
            alarmas.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, cuando, intento)
        } else {
            alarmas.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, cuando, intento)
        }
    }

    // cada tarea tiene dos alarmas: a la hora (id * 2) y 30 min antes (id * 2 + 1)
    private fun intento(id: Long, titulo: String, antes: Boolean): PendingIntent {
        val intent = Intent(contexto, ReceptorAviso::class.java)
            .setAction(ReceptorAviso.ACCION_AVISAR)
            .putExtra(ReceptorAviso.EXTRA_ID, id)
            .putExtra(ReceptorAviso.EXTRA_TITULO, titulo)
            .putExtra(ReceptorAviso.EXTRA_ANTES, antes)
        val codigo = (id * 2 + if (antes) 1 else 0).toInt()
        return PendingIntent.getBroadcast(
            contexto, codigo, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }
}
