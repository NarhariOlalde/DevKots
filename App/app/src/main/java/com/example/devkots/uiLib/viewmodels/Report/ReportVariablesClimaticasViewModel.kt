package com.example.devkots.uiLib.viewmodels.Report

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.devkots.data.BioReportService
import com.example.devkots.data.daos.VariablesClimaticasReportDao
import com.example.devkots.model.VariablesClimaticasReport
import com.example.devkots.uiLib.components.copyUriToExternalStorage
import com.example.devkots.uiLib.components.toEntity
import com.example.devkots.uiLib.components.toVariablesClimaticasReport
import kotlinx.coroutines.launch

class ReportVariablesClimaticasViewModel(
    private val bioReportService: BioReportService,
    private val VariablesClimaticasReportDao: VariablesClimaticasReportDao? = null
) : ViewModel() {

    var report by mutableStateOf<VariablesClimaticasReport?>(null)
    var isEditable by mutableStateOf(false)
    var loading by mutableStateOf(true)
    var errorMessage by mutableStateOf<String?>(null)

    // Función para cargar el reporte
    fun loadReport(reportId: Int, status: Boolean) {
        Log.d("loadReport", "Cargando reporte con ID: $reportId y status: $status")
        if (status) {
            fetchReportFromApi(reportId)
        } else {
            fetchReportFromDatabase(reportId)
        }
    }

    private fun fetchReportFromApi(reportId: Int) {
        viewModelScope.launch {
            loading = true
            try {
                val response = bioReportService.getVariablesClimaticasReport(reportId)
                if (response.isSuccessful) {
                    response.body()?.let { fetchedReport ->
                        report = fetchedReport
                        isEditable = report?.status == false
                    } ?: run {
                        Log.e("Report", "La respuesta de la API no contiene cuerpo.")
                        errorMessage = "Error: Respuesta vacía del servidor."
                    }
                } else {
                    Log.e("Report", "Error en la respuesta de la API: ${response.message()}")
                    errorMessage = "Error al obtener reporte desde la API: ${response.message()}"
                }
            } catch (e: Exception) {
                Log.e("Report", "Error al obtener reporte desde la API", e)
                errorMessage = "Error al obtener reporte desde la API: ${e.message}"
            } finally {
                loading = false
            }
        }
    }

    private fun fetchReportFromDatabase(reportId: Int) {
        viewModelScope.launch {
            loading = true
            try {
                VariablesClimaticasReportDao?.getVariablesClimaticasReportById(reportId.toLong())?.let {
                    report = it.toVariablesClimaticasReport()
                }
                isEditable = report?.status == true
            } catch (e: Exception) {
                Log.e("Report", "Error al obtener reporte desde la base de datos", e)
                errorMessage = "Error al cargar reporte desde la base de datos."
            } finally {
                loading = false
            }
        }
    }

    fun updateReport(reportId: Int, updatedReport: VariablesClimaticasReport) {
        viewModelScope.launch {
            loading = true
            try {
                Log.d("Update", "Iniciando actualización en la base de datos local")
                val entity = updatedReport.toEntity(reportId)
                VariablesClimaticasReportDao?.updateVariablesClimaticasReport(entity)
                Log.d("Update", "Reporte actualizado localmente")
                report = updatedReport
                Log.d("Update", "Estado en memoria actualizado")
            } catch (e: Exception) {
                Log.e("Report", "Error al actualizar reporte localmente", e)
                errorMessage = "Error al actualizar reporte local: ${e.message}"
            } finally {
                loading = false
            }
        }
    }
}