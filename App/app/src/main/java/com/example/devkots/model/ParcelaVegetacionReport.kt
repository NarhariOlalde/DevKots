package com.example.devkots.model

data class ParcelaVegetacionReport(
    val type: String = "Parcela de Vegetacion",
    val code: String,
    val cuadrante: String,
    val subcuadrante: String,
    val habitocrecimiento: String,
    val nombrecomun: String,
    val nombrecientifico: String? = null,
    val placa: String? = null,
    val circunferencia: String? = null,
    val distancia: String? = null,
    val estaturabio: String? = null,
    val altura: String? = null,
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