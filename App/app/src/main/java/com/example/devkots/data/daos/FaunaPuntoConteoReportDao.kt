package com.example.devkots.data.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.devkots.model.FaunaPuntoConteoReport
import com.example.devkots.model.LocalEntities.FaunaPuntoConteoReportEntity
import com.example.devkots.model.LocalEntities.FaunaTransectoReportEntity

@Dao
interface FaunaPuntoConteoReportDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFaunaPuntoConteoReport(report: FaunaPuntoConteoReportEntity)

    @Query("SELECT * FROM fauna_puntoconteo_reports WHERE status = :status")
    suspend fun getFaunaPuntoConteoReportsByStatus(status: Boolean): List<FaunaPuntoConteoReportEntity>

    @Query("SELECT * FROM fauna_puntoconteo_reports")
    suspend fun getAllFaunaPuntoConteoReports(): List<FaunaPuntoConteoReportEntity>
}