package com.example.devkots.uiLib.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.devkots.data.BioReportService

class ReportViewModelFactory(
    private val reportService: Any
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(ReportFaunaTransectoViewModel::class.java) -> {
                ReportFaunaTransectoViewModel(reportService as BioReportService) as T
            }
            modelClass.isAssignableFrom(ReportFaunaPuntoConteoViewModel::class.java) -> {
                ReportFaunaPuntoConteoViewModel(reportService as BioReportService) as T
            }
            modelClass.isAssignableFrom(ReportFaunaBusquedaLibreViewModel::class.java) -> {
                ReportFaunaBusquedaLibreViewModel(reportService as BioReportService) as T
            }
            modelClass.isAssignableFrom(ReportValidacionCoberturaViewModel::class.java) -> {
                ReportValidacionCoberturaViewModel(reportService as BioReportService) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}

