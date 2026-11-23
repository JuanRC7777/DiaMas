package com.diaplus.app.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import com.diaplus.app.data.model.Tarea

// exportSchema guarda en app/schemas/ una copia de la tabla por cada version: sirve para escribir y probar migraciones
@Database(entities = [Tarea::class], version = 1, exportSchema = true)
abstract class BaseDeDatos : RoomDatabase() {

    abstract fun tareaDao(): TareaDao

    companion object {
        // cuando se cambie la tabla (una columna nueva, por ejemplo): subir "version" y agregar aqui una
        // Migration que lleve las tareas guardadas de la version vieja a la nueva.
        // no usar fallbackToDestructiveMigration: borraria las tareas de quien ya usa la app
        private val MIGRACIONES: Array<Migration> = emptyArray()

        fun crear(contexto: Context): BaseDeDatos {
            return Room.databaseBuilder(contexto, BaseDeDatos::class.java, "diaplus.db")
                .addMigrations(*MIGRACIONES)
                .build()
        }
    }
}
