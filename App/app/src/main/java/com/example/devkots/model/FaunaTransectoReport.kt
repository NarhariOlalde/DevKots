package com.example.devkots.model

data class FaunaTransectoReport(
    val type: String = "Fauna en Transecto",     // Updated to "type" instead of "formType"
    val transectoNumber: Int,                // Numero de Transecto
    val animalType: String,                  // Tipo de Animal
    val commonName: String,                  // Nombre Comun
    val scientificName: String? = null,      // Nombre Cientifico (optional)
    val individualCount: Int,                // Numero de Individuos
    val observationType: String,             // Tipo de Observacion
    val photoPaths: List<String>? = null,
    val observations: String,                // Observaciones
    val date: String,                        // Current date
    val time: String,                        // Current time
    val gpsLocation: String,                 // GPS coordinates
    val weather: String,                     // Weather
    val status: Boolean = false,             // Status (always false for new entries)
    val biomonitor_id: String,                 // ID of the logged-in user
    val season: String                       // Season
)