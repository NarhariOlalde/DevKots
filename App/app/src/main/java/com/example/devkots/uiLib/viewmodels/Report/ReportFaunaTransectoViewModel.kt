package com.example.devkots.uiLib.viewmodels.Report

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.compose.runtime.*
import com.example.devkots.data.BioReportService
import com.example.devkots.model.FaunaTransectoReport
import kotlinx.coroutines.launch

class ReportFaunaTransectoViewModel(private val bioReportService: BioReportService) : ViewModel() {
    var report by mutableStateOf<FaunaTransectoReport?>(null)
    var isEditable by mutableStateOf(false)
    var loading by mutableStateOf(true)
    var errorMessage by mutableStateOf<String?>(null)

    fun loadReport(reportId: Int) {
        viewModelScope.launch {
            val response = bioReportService.getFaunaTransectoReport(reportId)
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

    fun updateReport(reportId: Int, updatedReport: FaunaTransectoReport) {
        viewModelScope.launch {
            val response = bioReportService.updateFaunaTransectoReport(reportId, updatedReport)
            if (response.isSuccessful) {
                report = updatedReport
            } else {
                errorMessage = "Failed to update report."
            }
        }
    }
}

