package com.example.devkots.data.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.devkots.model.LocalEntities.FaunaTransectoReportEntity

@Dao
interface FaunaTransectoReportDao {
    // Métodos para los FaunaTransectoReportEntity
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFaunaTransectoReport(report: FaunaTransectoReportEntity): Long

    @Query("SELECT * FROM fauna_transecto_reports WHERE status = :status")
    suspend fun getFaunaTransectoReportsByStatus(status: Boolean): List<FaunaTransectoReportEntity>

    @Query("SELECT * FROM fauna_transecto_reports")
    suspend fun getAllFaunaTransectoReports(): List<FaunaTransectoReportEntity>

    @Query("SELECT * FROM fauna_transecto_reports WHERE id = :id LIMIT 1")
    suspend fun getFaunaTransectoReportById(id: Long): FaunaTransectoReportEntity?

    @Query("DELETE FROM fauna_transecto_reports WHERE id = :id")
    suspend fun deleteFaunaTransectoReportById(id: Long)

    @Update
    suspend fun updateFaunaTransectoReport(report: FaunaTransectoReportEntity)
}