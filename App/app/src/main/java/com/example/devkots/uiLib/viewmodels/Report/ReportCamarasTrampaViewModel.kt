package com.example.devkots.uiLib.viewmodels.Report

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.devkots.data.BioReportService
import com.example.devkots.model.CamarasTrampaReport
import com.example.devkots.model.FaunaBusquedaReport
import kotlinx.coroutines.launch

class ReportCamarasTrampaViewModel(private val bioReportService: BioReportService) : ViewModel() {
    var report by mutableStateOf<CamarasTrampaReport?>(null)
    var isEditable by mutableStateOf(false)
    var loading by mutableStateOf(true)
    var errorMessage by mutableStateOf<String?>(null)

    fun loadReport(reportId: Int) {
        viewModelScope.launch {
            val response = bioReportService.getCamarasTrampaReport(reportId)
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

    fun updateReport(reportId: Int, updatedReport: CamarasTrampaReport) {
        viewModelScope.launch {
            val response = bioReportService.updateCamarasTrampaReport(reportId, updatedReport)
            if (response.isSuccessful) {
                report = updatedReport
            } else {
                errorMessage = "Failed to update report."
            }
        }
    }
}