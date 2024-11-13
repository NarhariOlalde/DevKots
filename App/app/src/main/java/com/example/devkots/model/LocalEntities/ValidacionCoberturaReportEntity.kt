package com.example.devkots.model.LocalEntities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "validacion_cobertura_reports")
data class ValidacionCoberturaReportEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val type: String = "Validación de Cobertura",
    val code: String,
    val seguimiento: String,
    val cambio: String,
    val cobertura: String,
    val tiposCultivo: String,
    val disturbio: String,
    val photoPaths: List<String>? = emptyList(),
    val observations: String,
    val date: String,
    val time: String,
    val gpsLocation: String,
    val weather: String,
    val status: Boolean = false,
    val biomonitor_id: String,
    val season: String
)