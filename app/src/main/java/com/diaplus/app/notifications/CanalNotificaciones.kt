package com.diaplus.app.notifications

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.diaplus.app.MainActivity
import com.diaplus.app.R

object CanalNotificaciones {

    private const val ID_CANAL = "recordatorios"

    // el canal es obligatorio desde Android 8. Crearlo dos veces no hace nada
    fun crear(contexto: Context) {
        val canal = NotificationChannel(ID_CANAL, contexto.getString(R.string.canal_nombre), NotificationManager.IMPORTANCE_HIGH)
        canal.description = contexto.getString(R.string.canal_descripcion)
        contexto.getSystemService(NotificationManager::class.java).createNotificationChannel(canal)
    }

    fun tienePermiso(contexto: Context): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) return true
        val permiso = ContextCompat.checkSelfPermission(contexto, Manifest.permission.POST_NOTIFICATIONS)
        return permiso == PackageManager.PERMISSION_GRANTED
    }

    fun mostrar(contexto: Context, id: Long, titulo: String, antes: Boolean, sonido: Boolean = true) {
        if (!tienePermiso(contexto)) return

        // tocar la notificacion abre la app
        val abrirApp = PendingIntent.getActivity(
            contexto, 0, Intent(contexto, MainActivity::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // el boton "Ya quedó" la completa sin abrir la app
        val completar = PendingIntent.getBroadcast(
            contexto, id.toInt(),
            Intent(contexto, ReceptorAviso::class.java)
                .setAction(ReceptorAviso.ACCION_COMPLETAR)
                .putExtra(ReceptorAviso.EXTRA_ID, id),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notificacion = NotificationCompat.Builder(contexto, ID_CANAL)
            .setSmallIcon(R.drawable.ic_campana)
            .setContentTitle(titulo)
            .setContentText(contexto.getString(if (antes) R.string.aviso_en_30 else R.string.aviso_ahora))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_REMINDER)
            .setContentIntent(abrirApp)
            .setAutoCancel(true)
            // sin sonido ni vibracion si el usuario lo apago en ajustes
            .setSilent(!sonido)
            .addAction(0, contexto.getString(R.string.ya_quedo), completar)
            .build()

        // mismo id para los dos avisos: el de la hora reemplaza al de 30 min antes
        NotificationManagerCompat.from(contexto).notify(id.toInt(), notificacion)
    }

    fun quitar(contexto: Context, id: Long) {
        NotificationManagerCompat.from(contexto).cancel(id.toInt())
    }
}
