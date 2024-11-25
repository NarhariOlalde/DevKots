package com.example.devkots.data.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.devkots.model.LocalEntities.FaunaBusquedaReportEntity
import com.example.devkots.model.LocalEntities.FaunaTransectoReportEntity

@Dao
interface FaunaBusquedaReportDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFaunaBusquedaReport(report: FaunaBusquedaReportEntity): Long

    @Query("SELECT * FROM fauna_busqueda_reports WHERE status = :status")
    suspend fun getFaunaBusquedaReportsByStatus(status: Boolean): List<FaunaBusquedaReportEntity>

    @Query("SELECT * FROM fauna_busqueda_reports")
    suspend fun getAllFaunaBusquedaReports(): List<FaunaBusquedaReportEntity>

    @Query("SELECT * FROM fauna_busqueda_reports WHERE id = :id LIMIT 1")
    suspend fun getFaunaBusquedaReportById(id: Long): FaunaBusquedaReportEntity?

    @Query("DELETE FROM fauna_busqueda_reports WHERE id = :id")
    suspend fun deleteFaunaBusquedaReportById(id: Long)

    @Update
    suspend fun updateFaunaBusquedaReport(report: FaunaBusquedaReportEntity)
}