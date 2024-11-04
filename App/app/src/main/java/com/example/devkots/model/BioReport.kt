package com.example.devkots.model

data class BioReport(
    val id: Int,
    val date: String,
    val status: Boolean,
    val biomonitor_id: String,
    val type: String
)