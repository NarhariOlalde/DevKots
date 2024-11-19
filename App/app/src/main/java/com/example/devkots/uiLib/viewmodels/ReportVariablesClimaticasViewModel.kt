package com.example.devkots.uiLib.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.devkots.data.BioReportService
import com.example.devkots.model.FaunaBusquedaReport
import com.example.devkots.model.VariablesClimaticasReport
import kotlinx.coroutines.launch

class ReportVariablesClimaticasViewModel(private val bioReportService: BioReportService) : ViewModel() {
    var report by mutableStateOf<VariablesClimaticasReport?>(null)
    var isEditable by mutableStateOf(false)
    var loading by mutableStateOf(true)
    var errorMessage by mutableStateOf<String?>(null)

    fun loadReport(reportId: Int) {
        viewModelScope.launch {
            val response = bioReportService.getVariablesClimaticasReport(reportId)
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

    fun updateReport(reportId: Int, updatedReport: VariablesClimaticasReport) {
        viewModelScope.launch {
            val response = bioReportService.updateVariablesClimaticasReport(reportId, updatedReport)
            if (response.isSuccessful) {
                report = updatedReport
            } else {
                errorMessage = "Failed to update report."
            }
        }
    }
}