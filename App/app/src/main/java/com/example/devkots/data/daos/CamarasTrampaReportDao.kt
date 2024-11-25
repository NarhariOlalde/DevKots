package com.example.devkots.data.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.devkots.model.LocalEntities.CamarasTrampaReportEntity
import com.example.devkots.model.LocalEntities.FaunaPuntoConteoReportEntity
import com.example.devkots.model.LocalEntities.FaunaTransectoReportEntity

@Dao
interface CamarasTrampaReportDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCamarasTrampaReport(report: CamarasTrampaReportEntity): Long

    @Query("SELECT * FROM camaras_trampa_reports WHERE status = :status")
    suspend fun getCamarasTrampaReportsByStatus(status: Boolean): List<CamarasTrampaReportEntity>

    @Query("SELECT * FROM camaras_trampa_reports")
    suspend fun getAllCamarasTrampaReports(): List<CamarasTrampaReportEntity>

    @Query("SELECT * FROM camaras_trampa_reports WHERE id = :id LIMIT 1")
    suspend fun getCamarasTrampaReportById(id: Long): CamarasTrampaReportEntity?

    @Query("DELETE FROM camaras_trampa_reports WHERE id = :id")
    suspend fun deleteCamarasTrampaReportById(id: Long)

    @Update
    suspend fun updateCamarasTrampaReport(report: CamarasTrampaReportEntity)
}