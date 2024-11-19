package com.example.devkots.uiLib.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.devkots.data.BioReportService
import com.example.devkots.model.FaunaBusquedaReport
import com.example.devkots.model.ParcelaVegetacionReport
import kotlinx.coroutines.launch

class ReportParcelaVegetacionViewModel(private val bioReportService: BioReportService) : ViewModel() {
    var report by mutableStateOf<ParcelaVegetacionReport?>(null)
    var isEditable by mutableStateOf(false)
    var loading by mutableStateOf(true)
    var errorMessage by mutableStateOf<String?>(null)

    fun loadReport(reportId: Int) {
        viewModelScope.launch {
            val response = bioReportService.getParcelaVegetacionReport(reportId)
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

    fun updateReport(reportId: Int, updatedReport: ParcelaVegetacionReport) {
        viewModelScope.launch {
            val response = bioReportService.updateParcelaVegetacionReport(reportId, updatedReport)
            if (response.isSuccessful) {
                report = updatedReport
            } else {
                errorMessage = "Failed to update report."
            }
        }
    }
}