package com.example.devkots.uiLib.components

import com.example.devkots.data.BioReportService
import com.example.devkots.data.daos.BioReportDao
import com.example.devkots.data.daos.CamarasTrampaReportDao
import com.example.devkots.data.daos.FaunaBusquedaReportDao
import com.example.devkots.data.daos.FaunaPuntoConteoReportDao
import com.example.devkots.data.daos.FaunaTransectoReportDao
import com.example.devkots.data.daos.ParcelaVegetacionReportDao
import com.example.devkots.data.daos.ValidacionCoberturaReportDao
import com.example.devkots.data.daos.VariablesClimaticasReportDao
import com.example.devkots.model.BioReportEntity
import com.example.devkots.model.FaunaTransectoReport

class ReportRepository(private val bioReportDao: BioReportDao ,
                       private val faunaTransectoDao: FaunaTransectoReportDao,
                       private val faunaPuntoConteoDao: FaunaPuntoConteoReportDao,
                       private val faunaBusquedaDao: FaunaBusquedaReportDao,
                       private val validacionCoberturaDao: ValidacionCoberturaReportDao,
                       private val parcelaVegetacionDao: ParcelaVegetacionReportDao,
                       private val camarasTrampaDao: CamarasTrampaReportDao,
                       private val variablesClimaticasDao: VariablesClimaticasReportDao, ) {

    suspend fun getReportByIdAndType(id: Long, formType: String): Any? {
        return when (formType) {
            "Fauna en Transecto" -> faunaTransectoDao.getFaunaTransectoReportById(id)
            "Fauna en Punto de Conteo" -> faunaPuntoConteoDao.getFaunaPuntoConteoReportById(id)
            "Fauna Búsqueda Libre" -> faunaBusquedaDao.getFaunaBusquedaReportById(id)
            "Validación de Cobertura" -> validacionCoberturaDao.getValidacionCoberturaReportById(id)
            "Parcela de Vegetación" -> parcelaVegetacionDao.getParcelaVegetacionReportById(id)
            "Cámaras Trampa" -> camarasTrampaDao.getCamarasTrampaReportById(id)
            "Variables Climáticas" -> variablesClimaticasDao.getVariablesClimaticasReportById(id)
            else -> null
        }
    }
    suspend fun deleteReportByIdAndType(id: Long, formType: String) {
        when (formType) {
            "Fauna en Transecto" -> faunaTransectoDao.deleteFaunaTransectoReportById(id)
            "Fauna en Punto de Conteo" -> faunaPuntoConteoDao.deleteFaunaPuntoConteoReportById(id)
            "Fauna Búsqueda Libre" -> faunaBusquedaDao.deleteFaunaBusquedaReportById(id)
            "Validación de Cobertura" -> validacionCoberturaDao.deleteValidacionCoberturaReportById(id)
            "Parcela de Vegetación" -> parcelaVegetacionDao.deleteParcelaVegetacionReportById(id)
            "Cámaras Trampa" -> camarasTrampaDao.deleteCamarasTrampaReportById(id)
            "Variables Climáticas" -> variablesClimaticasDao.deleteVariablesClimaticasReportById(id)
            else -> throw IllegalArgumentException("Unknown form type: $formType")
        }
    }
    suspend fun deleteReport(id: Int) {
        bioReportDao.deleteReportById(id)
    }

    suspend fun getAllLocalReports(): List<BioReportEntity> {
        return bioReportDao.getAllReports()
    }

    suspend fun deleteReportByFormIdAndType(id: Long, type: String) {
        return bioReportDao.deleteReportByFormIdAndType(id, type)
    }
}

