package com.example.devkots.data.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.devkots.model.LocalEntities.FaunaPuntoConteoReportEntity
import com.example.devkots.model.LocalEntities.VariablesClimaticasReportEntity

@Dao
interface VariablesClimaticasReportDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVariablesClimaticasReport(report: VariablesClimaticasReportEntity)

    @Query("SELECT * FROM variables_climaticas_reports WHERE status = :status")
    suspend fun getVariablesClimaticasReportsByStatus(status: Boolean): List<VariablesClimaticasReportEntity>

    @Query("SELECT * FROM variables_climaticas_reports")
    suspend fun getAllVariablesClimaticasReports(): List<VariablesClimaticasReportEntity>
}