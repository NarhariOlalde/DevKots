package com.example.devkots.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.devkots.model.BioReportEntity
import com.example.devkots.model.FaunaTransectoReportEntity

@Database(entities = [FaunaTransectoReportEntity::class, BioReportEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun faunaTransectoReportDao(): FaunaTransectoReportDao
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

