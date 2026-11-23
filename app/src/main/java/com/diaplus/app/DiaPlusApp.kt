package com.diaplus.app

import android.app.Application
import com.diaplus.app.data.local.BaseDeDatos
import com.diaplus.app.data.local.Preferencias
import com.diaplus.app.data.repository.TareaRepositorio
import com.diaplus.app.notifications.CanalNotificaciones
import com.diaplus.app.notifications.ProgramadorAvisos
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

// se crea una sola vez al abrir la app (o cuando llega un aviso con la app cerrada)
class DiaPlusApp : Application() {

    lateinit var repositorio: TareaRepositorio
        private set

    lateinit var preferencias: Preferencias
        private set

    override fun onCreate() {
        super.onCreate()
        CanalNotificaciones.crear(this)
        preferencias = Preferencias(this)
        val baseDeDatos = BaseDeDatos.crear(this)
        repositorio = TareaRepositorio(baseDeDatos.tareaDao(), ProgramadorAvisos(this))

        // las alarmas se pueden perder (el telefono se reinicio, o el sistema cerro la app por completo).
        // cada vez que arranca la app se vuelven a poner; poner la misma alarma dos veces no la duplica
        CoroutineScope(Dispatchers.IO).launch { repositorio.reprogramarAvisos() }
    }
}
