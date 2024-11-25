package com.example.devkots.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bio_reports")
data class BioReportEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val formId: Long,
    val date: String,
    val status: Boolean,
    val biomonitor_id: String,
    val type: String
)