package com.example.devkots.model.LocalEntities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "parcela_vegetacion_reports")
data class ParcelaVegetacionReportEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val type: String = "Parcela de Vegetacion",
    val code: String,
    val cuadrante: String,
    val subcuadrante: String,
    val habitocrecimiento: String,
    val nombrecomun: String,
    val nombrecientifico: String? = null,
    val placa: String,
    val circunferencia: Int,
    val distancia: Int,
    val estaturabio: Int,
    val altura: Int,
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