package com.example.devkots.data.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.devkots.model.LocalEntities.CamarasTrampaReportEntity
import com.example.devkots.model.LocalEntities.FaunaPuntoConteoReportEntity

@Dao
interface CamarasTrampaReportDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCamarasTrampaReport(report: CamarasTrampaReportEntity)

    @Query("SELECT * FROM camaras_trampa_reports WHERE status = :status")
    suspend fun getCamarasTrampaReportsByStatus(status: Boolean): List<CamarasTrampaReportEntity>

    @Query("SELECT * FROM camaras_trampa_reports")
    suspend fun getAllCamarasTrampaReports(): List<CamarasTrampaReportEntity>
}