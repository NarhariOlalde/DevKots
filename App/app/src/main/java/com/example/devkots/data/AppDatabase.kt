package com.example.devkots.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.devkots.data.daos.BioReportDao
import com.example.devkots.data.daos.CamarasTrampaReportDao
import com.example.devkots.data.daos.FaunaBusquedaReportDao
import com.example.devkots.data.daos.FaunaPuntoConteoReportDao
import com.example.devkots.data.daos.FaunaTransectoReportDao
import com.example.devkots.data.daos.ParcelaVegetacionReportDao
import com.example.devkots.data.daos.ValidacionCoberturaReportDao
import com.example.devkots.data.daos.VariablesClimaticasReportDao
import com.example.devkots.model.BioReportEntity
import com.example.devkots.model.LocalEntities.CamarasTrampaReportEntity
import com.example.devkots.model.LocalEntities.FaunaBusquedaReportEntity
import com.example.devkots.model.LocalEntities.FaunaPuntoConteoReportEntity
import com.example.devkots.model.LocalEntities.FaunaTransectoReportEntity
import com.example.devkots.model.LocalEntities.ParcelaVegetacionReportEntity
import com.example.devkots.model.LocalEntities.ValidacionCoberturaReportEntity
import com.example.devkots.model.LocalEntities.VariablesClimaticasReportEntity

@Database(entities = [FaunaTransectoReportEntity::class, FaunaPuntoConteoReportEntity::class, FaunaBusquedaReportEntity::class, ValidacionCoberturaReportEntity::class, ParcelaVegetacionReportEntity::class, CamarasTrampaReportEntity::class, VariablesClimaticasReportEntity::class, BioReportEntity::class], version = 2)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun faunaTransectoReportDao(): FaunaTransectoReportDao
    abstract fun faunaPuntoConteoReportDao(): FaunaPuntoConteoReportDao
    abstract fun faunaBusquedaReportDao(): FaunaBusquedaReportDao
    abstract fun validacionCoberturaReportDao(): ValidacionCoberturaReportDao
    abstract fun parcelaVegetacionReportDao(): ParcelaVegetacionReportDao
    abstract fun camarasTrampaReportDao(): CamarasTrampaReportDao
    abstract fun variablesClimaticasReportDao(): VariablesClimaticasReportDao
    abstract fun bioReportDao(): BioReportDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}

