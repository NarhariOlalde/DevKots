package com.example.devkots.data.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.devkots.model.LocalEntities.FaunaBusquedaReportEntity

@Dao
interface FaunaBusquedaReportDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFaunaBusquedaReport(report: FaunaBusquedaReportEntity)

    @Query("SELECT * FROM fauna_busqueda_reports WHERE status = :status")
    suspend fun getFaunaBusquedaReportsByStatus(status: Boolean): List<FaunaBusquedaReportEntity>

    @Query("SELECT * FROM fauna_busqueda_reports")
    suspend fun getAllFaunaBusquedaReports(): List<FaunaBusquedaReportEntity>
}