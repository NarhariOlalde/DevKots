package com.example.devkots.data.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.devkots.model.LocalEntities.FaunaTransectoReportEntity
import com.example.devkots.model.LocalEntities.ParcelaVegetacionReportEntity

@Dao
interface ParcelaVegetacionReportDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertParcelaVegetacionReport(report: ParcelaVegetacionReportEntity): Long

    @Query("SELECT * FROM parcela_vegetacion_reports WHERE status = :status")
    suspend fun getParcelaVegetacionReportsByStatus(status: Boolean): List<ParcelaVegetacionReportEntity>

    @Query("SELECT * FROM parcela_vegetacion_reports")
    suspend fun getAllParcelaVegetacionReports(): List<ParcelaVegetacionReportEntity>

    @Query("SELECT * FROM parcela_vegetacion_reports WHERE id = :id LIMIT 1")
    suspend fun getParcelaVegetacionReportById(id: Long): ParcelaVegetacionReportEntity?

    @Query("DELETE FROM parcela_vegetacion_reports WHERE id = :id")
    suspend fun deleteParcelaVegetacionReportById(id: Long)

    @Update
    suspend fun updateParcelaVegetacionReport(report: ParcelaVegetacionReportEntity)
}