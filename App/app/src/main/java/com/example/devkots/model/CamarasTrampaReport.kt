package com.example.devkots.model

data class CamarasTrampaReport(
    val type: String = "Cámaras Trampa",
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