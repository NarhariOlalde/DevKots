package com.example.devkots.uiLib.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.devkots.data.RetrofitInstanceBioReport
import com.example.devkots.model.BioReport
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class BioReportViewModel : ViewModel() {
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
                val response = RetrofitInstanceBioReport.api.getAllReports()
                if (response.isSuccessful) {
                    val reports = response.body()?.filter { it.biomonitor_id == biomonitorId } ?: emptyList()
                    _bioReports.value = reports
                    _totalReports.value = reports.size
                    _trueReportsCount.value = reports.count { it.status }
                    _falseReportsCount.value = reports.size - _trueReportsCount.value
                    _trueStatusPercentage.value = if (reports.isNotEmpty()) {
                        (_trueReportsCount.value.toDouble() / reports.size) * 100
                    } else {
                        0.0
                    }
                } else {
                    _errorMessage.value = "Failed to fetch reports: ${response.message()}"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error: ${e.message}"
            }
        }
    }
}