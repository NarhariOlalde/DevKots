package com.example.devkots.model

data class VariablesClimaticasReport(
    val type: String = "Camaras Trampa",
    val zona: String,
    val pluvosidad: String,
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