package com.example.devkots.data.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.devkots.model.BioReportEntity

@Dao
interface BioReportDao {
    @Query("SELECT * FROM bio_reports")
    suspend fun getAllReports(): List<BioReportEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReport(report: BioReportEntity)

    @Delete
    suspend fun deleteReport(report: BioReportEntity)
}
