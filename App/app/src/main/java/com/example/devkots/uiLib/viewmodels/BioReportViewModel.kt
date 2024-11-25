package com.example.devkots.uiLib.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.devkots.data.RetrofitInstanceBioReport
import com.example.devkots.model.BioReport
import com.example.devkots.uiLib.components.ReportRepository
import com.example.devkots.uiLib.components.toBioReport
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class BioReportViewModel(
    private val bioReportRepository: ReportRepository
) : ViewModel() {

    private val _bioReports = MutableStateFlow<List<BioReport>>(emptyList())
    val bioReports: StateFlow<List<BioReport>> = _bioReports

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _totalReports = MutableStateFlow(0)
    val totalReports: StateFlow<Int> = _totalReports

    private val _trueReportsCount = MutableStateFlow(0)
    val trueReportsCount: StateFlow<Int> = _trueReportsCount

    private val _falseReportsCount = MutableStateFlow(0)
    val falseReportsCount: StateFlow<Int> = _falseReportsCount

    private val _trueStatusPercentage = MutableStateFlow(0.0)
    val trueStatusPercentage: StateFlow<Double> = _trueStatusPercentage

    fun fetchReportsForUser(biomonitorId: String) {
        viewModelScope.launch {
            try {
                // Llama a la API para obtener los reportes remotos
                val remoteResponse = RetrofitInstanceBioReport.api.getAllReports()
                val remoteReports = if (remoteResponse.isSuccessful) {
                    remoteResponse.body()?.filter { it.biomonitor_id == biomonitorId } ?: emptyList()
                } else {
                    _errorMessage.value = "Failed to fetch remote reports: ${remoteResponse.message()}"
                    emptyList()
                }

                // Llama al repositorio para obtener los reportes locales
                val localReports = bioReportRepository.getAllLocalReports()
                    .filter { it.biomonitor_id == biomonitorId }

                // Combina los reportes locales y remotos
                val combinedReports = remoteReports + localReports.map { it.toBioReport() }

                // Actualiza las estadísticas
                _bioReports.value = combinedReports
                _totalReports.value = combinedReports.size
                _trueReportsCount.value = combinedReports.count { it.status }
                _falseReportsCount.value = combinedReports.size - _trueReportsCount.value
                _trueStatusPercentage.value = if (combinedReports.isNotEmpty()) {
                    (_trueReportsCount.value.toDouble() / combinedReports.size) * 100
                } else {
                    0.0
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error: ${e.message}"
            }
        }
    }
}
