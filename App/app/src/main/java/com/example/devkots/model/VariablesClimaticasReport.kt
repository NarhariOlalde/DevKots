package com.example.devkots.model

data class VariablesClimaticasReport(
    val type: String = "Variables Climaticas",
    val zona: String,
    val pluviosidad: String,
    val tempmax: String,
    val humedadmax: String,
    val tempmin: String,
    val nivelquebrada: String,
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