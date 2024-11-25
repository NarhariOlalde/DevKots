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
import com.example.devkots.data.daos.ValidacionCoberturaReportDao
import com.example.devkots.model.ValidacionCoberturaReport
import com.example.devkots.uiLib.components.copyUriToExternalStorage
import com.example.devkots.uiLib.components.toEntity
import com.example.devkots.uiLib.components.toValidacionCoberturaReport
import kotlinx.coroutines.launch

class ReportValidacionCoberturaViewModel(
    private val bioReportService: BioReportService,
    private val ValidacionCoberturaReportDao: ValidacionCoberturaReportDao? = null
) : ViewModel() {

    var report by mutableStateOf<ValidacionCoberturaReport?>(null)
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
                val response = bioReportService.getValidacionCoberturaReport(reportId)
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
                ValidacionCoberturaReportDao?.getValidacionCoberturaReportById(reportId.toLong())?.let {
                    report = it.toValidacionCoberturaReport()
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

    fun updateReport(reportId: Int, updatedReport: ValidacionCoberturaReport) {
        viewModelScope.launch {
            loading = true
            try {
                Log.d("Update", "Iniciando actualización en la base de datos local")
                val entity = updatedReport.toEntity(reportId)
                ValidacionCoberturaReportDao?.updateValidacionCoberturaReport(entity)
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

    fun removePhotoAt(index: Int) {
        report = report?.let { currentReport ->
            val updatedPhotoPaths = currentReport.photoPaths?.toMutableList() ?: mutableListOf()
            if (index in updatedPhotoPaths.indices) {
                updatedPhotoPaths.removeAt(index)
            }
            currentReport.copy(photoPaths = updatedPhotoPaths)
        }
    }

    fun addPhoto(context: Context, uri: Uri) {
        Log.d("AddPhoto", "URI recibida: $uri")
        val newUri = copyUriToExternalStorage(context, uri)
        if (newUri != null) {
            Log.d("AddPhoto", "URI copiada al almacenamiento externo: $newUri")
            val currentPhotoPaths = report?.photoPaths?.toMutableList() ?: mutableListOf()
            Log.d("AddPhoto", "Rutas actuales de fotos: $currentPhotoPaths")
            if (currentPhotoPaths.size < 6) {
                currentPhotoPaths.add(newUri.toString())
                report = report?.copy(photoPaths = currentPhotoPaths)
                Log.d("AddPhoto", "Nueva lista de fotos: $currentPhotoPaths")
            } else {
                Toast.makeText(context, "Máximo de 5 imágenes alcanzado", Toast.LENGTH_SHORT).show()
            }
        } else {
            Log.e("AddPhoto", "Error al copiar la foto al almacenamiento externo")
            Toast.makeText(context, "Error al agregar la foto", Toast.LENGTH_SHORT).show()
        }
    }
}