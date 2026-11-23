package com.diaplus.app.data.local

import android.content.Context
import android.content.res.Configuration
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

// archivo "ajustes.preferences_pb" dentro de la app
private val Context.almacen: DataStore<Preferences> by preferencesDataStore(name = "ajustes")

// ajustes pequeños (modo oscuro, sonido de los avisos). Para listas de datos se usa Room;
// para unos pocos si/no, DataStore
class Preferencias(private val contexto: Context) {

    private val datos: Flow<Preferences> = contexto.almacen.data.catch { error ->
        // si el archivo no se puede leer se usan los valores por defecto
        if (error is IOException) emit(emptyPreferences()) else throw error
    }

    // la primera vez, el modo oscuro sigue al del telefono
    val oscuro: Flow<Boolean> = datos.map { it[OSCURO] ?: sistemaEnModoOscuro() }

    val sonido: Flow<Boolean> = datos.map { it[SONIDO] ?: true }

    suspend fun guardarOscuro(valor: Boolean) {
        contexto.almacen.edit { it[OSCURO] = valor }
    }

    suspend fun guardarSonido(valor: Boolean) {
        contexto.almacen.edit { it[SONIDO] = valor }
    }

    private fun sistemaEnModoOscuro(): Boolean {
        val modo = contexto.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        return modo == Configuration.UI_MODE_NIGHT_YES
    }

    private companion object {
        val OSCURO = booleanPreferencesKey("oscuro")
        val SONIDO = booleanPreferencesKey("sonido")
    }
}
