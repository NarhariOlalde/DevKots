package com.example.devkots.model

data class CamarasTrampaReport(
    val type: String = "Camaras Trampa",
    val code: String,
    val zona: String,
    val nombrecamara: String,
    val placacamara: String,
    val placaguaya: String,
    val anchocamino: String,
    val fechainstalacion: String,
    val distancia: String,
    val altura: String,
    val listachequeo: String,
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