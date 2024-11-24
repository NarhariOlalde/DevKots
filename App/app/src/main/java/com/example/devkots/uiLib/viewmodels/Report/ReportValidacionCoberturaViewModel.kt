package com.example.devkots.uiLib.viewmodels.Report

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.devkots.data.BioReportService
import com.example.devkots.model.ValidacionCoberturaReport
import com.example.devkots.uiLib.components.copyUriToExternalStorage
import kotlinx.coroutines.launch

class ReportValidacionCoberturaViewModel(private val bioReportService: BioReportService) : ViewModel() {
    var report by mutableStateOf<ValidacionCoberturaReport?>(null)
    var isEditable by mutableStateOf(false)
    var loading by mutableStateOf(true)
    var errorMessage by mutableStateOf<String?>(null)

    fun loadReport(reportId: Int) {
        viewModelScope.launch {
            val response = bioReportService.getValidacionCoberturaReport(reportId)
            if (response.isSuccessful) {
                report = response.body()
                isEditable = report?.status == false
                loading = false
            } else {
                errorMessage = "Failed to load report details."
                loading = false
            }
        }
    }

    fun updateReport(reportId: Int, updatedReport: ValidacionCoberturaReport) {
        viewModelScope.launch {
            val response = bioReportService.updateValidacionCoberturaReport(reportId, updatedReport)
            if (response.isSuccessful) {
                report = updatedReport
            } else {
                errorMessage = "Failed to update report."
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

    // Agregar una nueva foto con control de límite
    fun addPhoto(context: Context, uri: Uri) {
        val newUri = copyUriToExternalStorage(context, uri)  // Copia la URI al almacenamiento externo
        if (newUri != null) {
            val currentPhotoPaths = report?.photoPaths?.toMutableList() ?: mutableListOf()
            if (currentPhotoPaths.size < 5) {  // Verifica si hay espacio para más fotos
                currentPhotoPaths.add(newUri.toString())  // Agrega la nueva URI a la lista
                report = report?.copy(photoPaths = currentPhotoPaths)
            } else {
                // Limite alcanzado
                Toast.makeText(context, "Máximo de 5 imágenes alcanzado", Toast.LENGTH_SHORT).show()
            }
        } else {
            // Error al copiar la foto al almacenamiento externo
            Toast.makeText(context, "Error al agregar la foto", Toast.LENGTH_SHORT).show()
        }
    }
}