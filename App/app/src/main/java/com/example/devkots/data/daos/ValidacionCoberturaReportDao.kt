package com.example.devkots.data.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.devkots.model.LocalEntities.ValidacionCoberturaReportEntity

@Dao
interface ValidacionCoberturaReportDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertValidacionCoberturaReport(report: ValidacionCoberturaReportEntity)

    @Query("SELECT * FROM validacion_cobertura_reports WHERE status = :status")
    suspend fun getValidacionCoberturaReportsByStatus(status: Boolean): List<ValidacionCoberturaReportEntity>

    @Query("SELECT * FROM validacion_cobertura_reports")
    suspend fun getAllValidacionCoberturaReports(): List<ValidacionCoberturaReportEntity>
}