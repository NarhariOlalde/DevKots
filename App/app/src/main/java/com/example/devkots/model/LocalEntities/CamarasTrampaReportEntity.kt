package com.example.devkots.model.LocalEntities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "camaras_trampa_reports")
data class CamarasTrampaReportEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val type: String = "Camaras Trampa",
    val code: String,
    val zona: String,
    val nombrecamara: String,
    val placacamara: String,
    val placaguaya: String,
    val anchocamino: Int,
    val fechainstalacion: String,
    val distancia: Int,
    val altura: Int,
    val listachequeo: List<String>? = null,
    val photoPaths: List<String>? = null,
    val observations: String,
    val date: String,
    val time: String,
    val gpsLocation: String,
    val weather: String,
    val status: Boolean = false,
    val biomonitor_id: String,
    val season: String
)