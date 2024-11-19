package com.example.devkots.uiLib.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.devkots.data.BioReportService
import com.example.devkots.model.FaunaBusquedaReport
import com.example.devkots.model.ValidacionCoberturaReport
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
}