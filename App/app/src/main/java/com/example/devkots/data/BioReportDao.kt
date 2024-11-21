package com.example.devkots.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.devkots.model.BioReport
import com.example.devkots.model.BioReportEntity

@Dao
interface BioReportDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReport(report: BioReportEntity)

    @Query("SELECT * FROM bio_reports")
    suspend fun getAllReports(): List<BioReportEntity>

    @Query("SELECT * FROM bio_reports WHERE status = :status")
    suspend fun getReportsByStatus(status: Boolean): List<BioReportEntity>

}
