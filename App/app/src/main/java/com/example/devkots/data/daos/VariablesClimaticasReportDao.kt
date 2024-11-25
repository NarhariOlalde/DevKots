package com.example.devkots.data.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.devkots.model.LocalEntities.FaunaPuntoConteoReportEntity
import com.example.devkots.model.LocalEntities.FaunaTransectoReportEntity
import com.example.devkots.model.LocalEntities.VariablesClimaticasReportEntity

@Dao
interface VariablesClimaticasReportDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVariablesClimaticasReport(report: VariablesClimaticasReportEntity): Long

    @Query("SELECT * FROM variables_climaticas_reports WHERE status = :status")
    suspend fun getVariablesClimaticasReportsByStatus(status: Boolean): List<VariablesClimaticasReportEntity>

    @Query("SELECT * FROM variables_climaticas_reports")
    suspend fun getAllVariablesClimaticasReports(): List<VariablesClimaticasReportEntity>

    @Query("SELECT * FROM variables_climaticas_reports WHERE id = :id LIMIT 1")
    suspend fun getVariablesClimaticasReportById(id: Long): VariablesClimaticasReportEntity?

    @Query("DELETE FROM variables_climaticas_reports WHERE id = :id")
    suspend fun deleteVariablesClimaticasReportById(id: Long)

    @Update
    suspend fun updateVariablesClimaticasReport(report: VariablesClimaticasReportEntity)
}