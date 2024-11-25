package com.example.devkots.model.LocalEntities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "variables_climaticas_reports")
data class VariablesClimaticasReportEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val type: String = "Variables Climáticas",
    val zona: String,
    val pluviosidad: Int,
    val tempmax: Int,
    val humedadmax: Int,
    val tempmin: Int,
    val nivelquebrada: Int,
    val date: String,
    val time: String,
    val gpsLocation: String,
    val weather: String,
    val status: Boolean = false,
    val biomonitor_id: String,
    val season: String
)