package com.diaplus.app.data

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.diaplus.app.data.local.BaseDeDatos
import com.diaplus.app.data.local.TareaDao
import com.diaplus.app.data.model.Tarea
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

// prueba la base de datos real (Room + SQLite) dentro del emulador o telefono.
// la base va en memoria: no toca las tareas de la app
@RunWith(AndroidJUnit4::class)
class TareaDaoTest {

    private lateinit var baseDeDatos: BaseDeDatos
    private lateinit var dao: TareaDao

    @Before
    fun crearBase() {
        val contexto = ApplicationProvider.getApplicationContext<Context>()
        baseDeDatos = Room.inMemoryDatabaseBuilder(contexto, BaseDeDatos::class.java).build()
        dao = baseDeDatos.tareaDao()
    }

    @After
    fun cerrarBase() {
        baseDeDatos.close()
    }

    @Test
    fun guardarDevuelveUnIdYLaTareaSeEncuentra() = runBlocking {
        val id = dao.guardar(Tarea(titulo = "Pagar renta", fechaMillis = 1000L, aviso30 = true))

        val leida = dao.buscar(id)
        assertNotNull(leida)
        assertEquals("Pagar renta", leida!!.titulo)
        assertEquals(1000L, leida.fechaMillis)
        assertTrue(leida.aviso30)
    }

    @Test
    fun guardarConUnIdQueYaExisteEditaEnVezDeDuplicar() = runBlocking {
        val id = dao.guardar(Tarea(titulo = "Borrador"))
        dao.guardar(Tarea(id = id, titulo = "Versión final"))

        val todas = dao.observarTodas().first()
        assertEquals(1, todas.size)
        assertEquals("Versión final", todas[0].titulo)
    }

    @Test
    fun borrarQuitaLaTarea() = runBlocking {
        val id = dao.guardar(Tarea(titulo = "Temporal"))
        dao.borrar(dao.buscar(id)!!)

        assertNull(dao.buscar(id))
        assertTrue(dao.observarTodas().first().isEmpty())
    }

    @Test
    fun borrarYGuardarConElMismoIdLaDevuelveTalComoEstaba() = runBlocking {
        // asi funciona «Deshacer» despues de borrar
        val id = dao.guardar(Tarea(titulo = "Ensayo", fechaMillis = 5000L))
        val original = dao.buscar(id)!!
        dao.borrar(original)
        dao.guardar(original)

        assertEquals(original, dao.buscar(id))
    }

    @Test
    fun pendientesConFechaIgnoraCompletadasYSinFecha() = runBlocking {
        dao.guardar(Tarea(titulo = "Con fecha", fechaMillis = 1000L))
        dao.guardar(Tarea(titulo = "Sin fecha"))
        dao.guardar(Tarea(titulo = "Completada", fechaMillis = 2000L, completada = true))

        val pendientes = dao.pendientesConFecha()
        assertEquals(listOf("Con fecha"), pendientes.map { it.titulo })
    }
}
