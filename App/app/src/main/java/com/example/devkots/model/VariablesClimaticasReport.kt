package com.example.devkots.model

data class VariablesClimaticasReport(
    val type: String = "Variables Climaticas",
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