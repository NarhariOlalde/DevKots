package com.example.devkots.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "fauna_transecto_reports")
data class FaunaTransectoReportEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val type: String = "Fauna en Transecto",
    val transectoNumber: Int,
    val animalType: String,
    val commonName: String,
    val scientificName: String? = null,
    val individualCount: Int,
    val observationType: String,
    val photoPath: String? = null,
    val observations: String,
    val date: String,
    val time: String,
    val gpsLocation: String,
    val weather: String,
    val status: Boolean = false,
    val biomonitor_id: String,
    val season: String
)
