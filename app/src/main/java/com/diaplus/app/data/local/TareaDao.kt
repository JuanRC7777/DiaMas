package com.diaplus.app.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.diaplus.app.data.model.Tarea
import kotlinx.coroutines.flow.Flow

@Dao
interface TareaDao {

    // Flow: avisa solo cada vez que la tabla cambia
    @Query("SELECT * FROM tareas")
    fun observarTodas(): Flow<List<Tarea>>

    @Query("SELECT * FROM tareas WHERE id = :id")
    suspend fun buscar(id: Long): Tarea?

    @Query("SELECT * FROM tareas WHERE completada = 0 AND fechaMillis IS NOT NULL")
    suspend fun pendientesConFecha(): List<Tarea>

    // sirve para crear y para editar: si el id ya existe, la reemplaza
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun guardar(tarea: Tarea): Long

    @Delete
    suspend fun borrar(tarea: Tarea)
}
