package com.example.devkots.uiLib.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.devkots.data.BioReportService
import com.example.devkots.uiLib.viewmodels.Report.ReportCamarasTrampaViewModel
import com.example.devkots.uiLib.viewmodels.Report.ReportFaunaBusquedaLibreViewModel
import com.example.devkots.uiLib.viewmodels.Report.ReportFaunaPuntoConteoViewModel
import com.example.devkots.uiLib.viewmodels.Report.ReportFaunaTransectoViewModel
import com.example.devkots.uiLib.viewmodels.Report.ReportParcelaVegetacionViewModel
import com.example.devkots.uiLib.viewmodels.Report.ReportValidacionCoberturaViewModel
import com.example.devkots.uiLib.viewmodels.Report.ReportVariablesClimaticasViewModel

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
            modelClass.isAssignableFrom(ReportParcelaVegetacionViewModel::class.java) -> {
                ReportParcelaVegetacionViewModel(reportService as BioReportService) as T
            }
            modelClass.isAssignableFrom(ReportCamarasTrampaViewModel::class.java) -> {
                ReportCamarasTrampaViewModel(reportService as BioReportService) as T
            }
            modelClass.isAssignableFrom(ReportVariablesClimaticasViewModel::class.java) -> {
                ReportVariablesClimaticasViewModel(reportService as BioReportService) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}

