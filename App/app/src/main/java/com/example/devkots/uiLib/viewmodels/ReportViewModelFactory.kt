package com.example.devkots.uiLib.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.devkots.data.BioReportService
import com.example.devkots.data.daos.CamarasTrampaReportDao
import com.example.devkots.data.daos.FaunaBusquedaReportDao
import com.example.devkots.data.daos.FaunaPuntoConteoReportDao
import com.example.devkots.data.daos.FaunaTransectoReportDao
import com.example.devkots.data.daos.ParcelaVegetacionReportDao
import com.example.devkots.data.daos.ValidacionCoberturaReportDao
import com.example.devkots.data.daos.VariablesClimaticasReportDao
import com.example.devkots.uiLib.viewmodels.Report.ReportCamarasTrampaViewModel
import com.example.devkots.uiLib.viewmodels.Report.ReportFaunaBusquedaLibreViewModel
import com.example.devkots.uiLib.viewmodels.Report.ReportFaunaPuntoConteoViewModel
import com.example.devkots.uiLib.viewmodels.Report.ReportFaunaTransectoViewModel
import com.example.devkots.uiLib.viewmodels.Report.ReportParcelaVegetacionViewModel
import com.example.devkots.uiLib.viewmodels.Report.ReportValidacionCoberturaViewModel
import com.example.devkots.uiLib.viewmodels.Report.ReportVariablesClimaticasViewModel

class ReportViewModelFactory(
    private val bioReportService: BioReportService,
    private val faunaTransectoReportDao: FaunaTransectoReportDao? = null,
    private val faunaPuntoConteoReportDao: FaunaPuntoConteoReportDao? = null,
    private val faunaBusquedaLibreReportDao: FaunaBusquedaReportDao? = null,
    private val validacionCoberturaReportDao: ValidacionCoberturaReportDao? = null,
    private val parcelaVegetacionReportDao: ParcelaVegetacionReportDao? = null,
    private val camarasTrampaReportDao: CamarasTrampaReportDao? = null,
    private val variablesClimaticasReportDao: VariablesClimaticasReportDao? = null
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(ReportFaunaTransectoViewModel::class.java) -> {
                ReportFaunaTransectoViewModel(
                    bioReportService,
                    faunaTransectoReportDao
                ) as T
            }
            modelClass.isAssignableFrom(ReportFaunaPuntoConteoViewModel::class.java) -> {
                ReportFaunaPuntoConteoViewModel(
                    bioReportService,
                    faunaPuntoConteoReportDao
                ) as T
            }
            modelClass.isAssignableFrom(ReportFaunaBusquedaLibreViewModel::class.java) -> {
                ReportFaunaBusquedaLibreViewModel(
                    bioReportService,
                    faunaBusquedaLibreReportDao
                ) as T
            }
            modelClass.isAssignableFrom(ReportValidacionCoberturaViewModel::class.java) -> {
                ReportValidacionCoberturaViewModel(
                    bioReportService,
                    validacionCoberturaReportDao
                ) as T
            }
            modelClass.isAssignableFrom(ReportParcelaVegetacionViewModel::class.java) -> {
                ReportParcelaVegetacionViewModel(
                    bioReportService,
                    parcelaVegetacionReportDao
                ) as T
            }
            modelClass.isAssignableFrom(ReportCamarasTrampaViewModel::class.java) -> {
                ReportCamarasTrampaViewModel(
                    bioReportService,
                    camarasTrampaReportDao
                ) as T
            }
            modelClass.isAssignableFrom(ReportVariablesClimaticasViewModel::class.java) -> {
                ReportVariablesClimaticasViewModel(
                    bioReportService,
                    variablesClimaticasReportDao
                ) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}


