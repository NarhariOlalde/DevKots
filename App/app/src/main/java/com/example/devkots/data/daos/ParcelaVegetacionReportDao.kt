package com.example.devkots.data.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.devkots.model.LocalEntities.ParcelaVegetacionReportEntity

@Dao
interface ParcelaVegetacionReportDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertParcelaVegetacionReport(report: ParcelaVegetacionReportEntity)

    @Query("SELECT * FROM parcela_vegetacion_reports WHERE status = :status")
    suspend fun getParcelaVegetacionReportsByStatus(status: Boolean): List<ParcelaVegetacionReportEntity>

    @Query("SELECT * FROM parcela_vegetacion_reports")
    suspend fun getAllParcelaVegetacionReports(): List<ParcelaVegetacionReportEntity>
}