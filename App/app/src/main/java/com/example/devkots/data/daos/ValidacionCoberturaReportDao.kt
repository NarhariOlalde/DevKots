package com.example.devkots.data.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.devkots.model.LocalEntities.FaunaTransectoReportEntity
import com.example.devkots.model.LocalEntities.ValidacionCoberturaReportEntity

@Dao
interface ValidacionCoberturaReportDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertValidacionCoberturaReport(report: ValidacionCoberturaReportEntity): Long

    @Query("SELECT * FROM validacion_cobertura_reports WHERE status = :status")
    suspend fun getValidacionCoberturaReportsByStatus(status: Boolean): List<ValidacionCoberturaReportEntity>

    @Query("SELECT * FROM validacion_cobertura_reports")
    suspend fun getAllValidacionCoberturaReports(): List<ValidacionCoberturaReportEntity>

    @Query("SELECT * FROM validacion_cobertura_reports WHERE id = :id LIMIT 1")
    suspend fun getValidacionCoberturaReportById(id: Long): ValidacionCoberturaReportEntity?

    @Query("DELETE FROM validacion_cobertura_reports WHERE id = :id")
    suspend fun deleteValidacionCoberturaReportById(id: Long)

    @Update
    suspend fun updateValidacionCoberturaReport(report: ValidacionCoberturaReportEntity)
}