package com.example.devkots.uiLib.screens

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.devkots.R
import com.example.devkots.data.AppDatabase
import com.example.devkots.data.BioReportService
import com.example.devkots.model.BioReport
import com.example.devkots.model.BioReportEntity
import com.example.devkots.model.LocalEntities.CamarasTrampaReportEntity
import com.example.devkots.model.LocalEntities.FaunaBusquedaReportEntity
import com.example.devkots.model.LocalEntities.FaunaPuntoConteoReportEntity
import com.example.devkots.model.LocalEntities.FaunaTransectoReportEntity
import com.example.devkots.model.LocalEntities.ParcelaVegetacionReportEntity
import com.example.devkots.model.LocalEntities.ValidacionCoberturaReportEntity
import com.example.devkots.model.LocalEntities.VariablesClimaticasReportEntity
import com.example.devkots.uiLib.components.ReportRepository
import com.example.devkots.uiLib.components.toCamarasTrampaReport
import com.example.devkots.uiLib.components.toFaunaBusquedaReport
import com.example.devkots.uiLib.components.toFaunaPuntoConteoReport
import com.example.devkots.uiLib.components.toFaunaTransectoReport
import com.example.devkots.uiLib.components.toParcelaVegetacionReport
import com.example.devkots.uiLib.components.toValidacionCoberturaReport
import com.example.devkots.uiLib.components.toVariablesClimaticasReport
import kotlinx.coroutines.launch

@Composable
fun SearchScreen(
    navController: NavController,
    bioReportService: BioReportService,
    biomonitorId: String, // Current user's biomonitor ID
    reportRepository: ReportRepository
) {
    val context = LocalContext.current
    val database = remember { AppDatabase.getInstance(context) } // Instancia de la base de datos
    val bioReportDao = remember { database.bioReportDao() } // Instancia del DAO

    val coroutineScope = rememberCoroutineScope()

    var reports by remember { mutableStateOf<List<BioReport>>(emptyList()) }
    var localReports by remember { mutableStateOf<List<BioReportEntity>>(emptyList()) }
    var filteredReports by remember { mutableStateOf<List<BioReport>>(emptyList()) }
    var combinedReports by remember { mutableStateOf<List<BioReport>>(emptyList()) }

    var selectedTab by remember { mutableIntStateOf(0) }
    var loading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var successMessage by remember { mutableStateOf<String?>(null) }

    // Define filterReportsByTab as a lambda or function within SearchScreen
    val filterReportsByTab: (Int) -> Unit = { tabIndex ->
        filteredReports = when (tabIndex) {
            0 -> combinedReports // Todos: Show all reports
            1 -> combinedReports.filter { !it.status } // Guardados: Show only reports with status == false
            2 -> combinedReports.filter { it.status } // Subidos: Show only reports with status == true
            else -> reports
        }
    }

    // Fetch reports on launch
    LaunchedEffect(Unit) {
        coroutineScope.launch {
            loading = true
            try {
                // Obtener reportes de la API
                val apiResponse = bioReportService.getAllReportsBiomonitorID(biomonitorId)
                if (apiResponse.isSuccessful) {
                    reports = apiResponse.body() ?: emptyList()
                } else {
                    errorMessage = "Error al obtener reportes de la API."
                }

                // Obtener reportes locales
                localReports = bioReportDao.getReportsByBiomonitorId(biomonitorId)

                // Combinar reportes
                combinedReports = reports + localReports.map { localReport ->
                    BioReport(
                        id = localReport.formId.toInt(),
                        biomonitor_id = localReport.biomonitor_id,
                        status = localReport.status,
                        date = localReport.date,
                        type = localReport.type
                    )
                }

                // Filtrar por pestaña seleccionada
                filterReportsByTab(selectedTab)
            } catch (e: Exception) {
                errorMessage = "Error: ${e.message}"
            } finally {
                loading = false
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Búsqueda", fontSize = 24.sp, modifier = Modifier.padding(start = 40.dp))},
                backgroundColor = Color(0xFFB4D68F),
                modifier = Modifier.height(80.dp),
                actions = {
                    IconButton(onClick = {
                        coroutineScope.launch {
                            val reportsToUpdate = localReports.filter { !it.status && it.biomonitor_id == biomonitorId }
                            var allSuccessful = true

                            // Update each report individually
                            for (report in reportsToUpdate) {
                                val fullReport = reportRepository.getReportByIdAndType(report.formId, report.type)
                                val reportForSubmission = when (report.type) {
                                    "Fauna en Transecto" -> (fullReport as? FaunaTransectoReportEntity)?.toFaunaTransectoReport()
                                    "Fauna en Punto de Conteo" -> (fullReport as? FaunaPuntoConteoReportEntity)?.toFaunaPuntoConteoReport()
                                    "Fauna Búsqueda Libre" -> (fullReport as? FaunaBusquedaReportEntity)?.toFaunaBusquedaReport()
                                    "Validación de Cobertura" -> (fullReport as? ValidacionCoberturaReportEntity)?.toValidacionCoberturaReport()
                                    "Parcela de Vegetación" -> (fullReport as? ParcelaVegetacionReportEntity)?.toParcelaVegetacionReport()
                                    "Cámaras Trampa" -> (fullReport as? CamarasTrampaReportEntity)?.toCamarasTrampaReport()
                                    "Variables Climáticas" -> (fullReport as? VariablesClimaticasReportEntity)?.toVariablesClimaticasReport()
                                    else -> {
                                        allSuccessful = false
                                        errorMessage = "Unknown form type for report with ID: ${report.id}"
                                        break
                                    }
                                }

                                if (reportForSubmission != null) {
                                    // Enviar el reporte a la API
                                    val submitResponse = when (report.type) {
                                        "Fauna en Transecto" -> bioReportService.submitFaunaTransectoReport(reportForSubmission)
                                        "Fauna en Punto de Conteo" -> bioReportService.submitFaunaPuntoConteoReport(reportForSubmission)
                                        "Fauna Búsqueda Libre" -> bioReportService.submitFaunaBusquedaReport(reportForSubmission)
                                        "Validación de Cobertura" -> bioReportService.submitValidacionCoberturaReport(reportForSubmission)
                                        "Parcela de Vegetación" -> bioReportService.submitParcelaVegetacionReport(reportForSubmission)
                                        "Cámaras Trampa" -> bioReportService.submitCamarasTrampaReport(reportForSubmission)
                                        "Variables Climáticas" -> bioReportService.submitVariablesClimaticasReport(reportForSubmission)
                                        else -> {
                                            allSuccessful = false
                                            errorMessage = "Unknown form type for report with ID: ${report.id}"
                                            break
                                        }
                                    }

                                    reportRepository.deleteReportByIdAndType(report.formId, report.type)
                                    reportRepository.deleteReport(report.id)

                                } else {
                                    allSuccessful = false
                                    errorMessage = "Failed to convert report with ID: ${report.id} to appropriate type"
                                    break
                                }
                            }
                            // Refresh reports if all updates succeeded
                            if (allSuccessful) {
                                val refreshedResponse = bioReportService.getAllReportsBiomonitorID(biomonitorId)
                                if (refreshedResponse.isSuccessful) {
                                    reports = refreshedResponse.body() ?: emptyList()
                                    localReports = bioReportDao.getReportsByBiomonitorId(biomonitorId)

                                    combinedReports = reports + localReports.map { localReport ->
                                        BioReport(
                                            id = localReport.formId.toInt(),
                                            biomonitor_id = localReport.biomonitor_id,
                                            status = localReport.status,
                                            date = localReport.date,
                                            type = localReport.type
                                        )
                                    }

                                    filterReportsByTab(selectedTab)
                                    errorMessage = null
                                } else {
                                    errorMessage = "Failed to refresh reports after upload."
                                }
                            } else {
                                errorMessage = "Some reports failed to upload."
                            }
                        }
                    }) {
                        Text("Subir todos", color = Color.Black, modifier = Modifier.padding(end = 20.dp), fontSize = 20.sp)
                    }
                }
            )
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Tabs for filtering
            TabRow(
                selectedTabIndex = selectedTab,
                backgroundColor = Color.White,
                contentColor = Color(0xFF4E7029),
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = {
                        selectedTab = 0
                        filterReportsByTab(0)
                    },
                    selectedContentColor = Color(0xFF4E7029), // Verde si está seleccionada
                    unselectedContentColor = Color.Gray, // Gris si no está seleccionada
                ) {
                    Text(
                        "Todos",
                        modifier = Modifier.padding(16.dp),
                        fontSize = 18.sp // Tamaño de letra más grande
                    )
                }
                Tab(
                    selected = selectedTab == 1,
                    onClick = {
                        selectedTab = 1
                        filterReportsByTab(1)
                    },
                    selectedContentColor = Color(0xFF4E7029), // Verde si está seleccionada
                    unselectedContentColor = Color.Gray, // Gris si no está seleccionada
                ) {
                    Text(
                        "Guardados",
                        modifier = Modifier.padding(16.dp),
                        fontSize = 18.sp // Tamaño de letra más grande
                    )
                }
                Tab(
                    selected = selectedTab == 2,
                    onClick = {
                        selectedTab = 2
                        filterReportsByTab(2)
                    },
                    selectedContentColor = Color(0xFF4E7029), // Verde si está seleccionada
                    unselectedContentColor = Color.Gray, // Gris si no está seleccionada
                ) {
                    Text(
                        "Subidos",
                        modifier = Modifier.padding(16.dp),
                        fontSize = 18.sp // Tamaño de letra más grande
                    )
                }
            }

            if (loading) {
                CircularProgressIndicator(modifier = Modifier.fillMaxSize())
            } else if (filteredReports.isNotEmpty()) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    filteredReports.forEach { report ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                                .clickable {
                                    navController.navigate("report_detail/${report.id}/${report.type}/${report.status.toString()}")
                                },
                            backgroundColor = Color.White,
                            elevation = 4.dp,
                            border = BorderStroke(1.dp, Color.LightGray)
                        ) {
                            Row(
                                modifier = Modifier.padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Image(
                                    painter = painterResource(
                                        id = if (report.status) R.drawable.subido else R.drawable.guardado
                                    ),
                                    contentDescription = null,
                                    modifier = Modifier.size(75.dp)
                                )
                                Spacer(modifier = Modifier.width(30.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    if (report.status) {
                                        Text("#FM${report.id}", fontSize = 30.sp, fontWeight = FontWeight.Bold)
                                        Text("${report.type}", fontSize = 25.sp)
                                        Text("Date: ${report.date}", fontSize = 20.sp)
                                    } else {
                                        Text("#FMLocal", fontSize = 30.sp, fontWeight = FontWeight.Bold)
                                        Text("${report.type}", fontSize = 25.sp)
                                        Text("Date: ${report.date}", fontSize = 20.sp)
                                    }
                                }

                                if(!report.status) {
                                    var expanded by remember { mutableStateOf(false) }
                                    Box {
                                        // Botón de los tres puntos
                                        IconButton(onClick = { expanded = true }) {
                                            Icon(
                                                imageVector = Icons.Default.MoreVert,
                                                contentDescription = "Opciones",
                                                modifier = Modifier.size(24.dp)
                                            )
                                        }

                                        // Menú desplegable
                                        DropdownMenu(
                                            expanded = expanded,
                                            onDismissRequest = { expanded = false },
                                            modifier = Modifier.wrapContentSize()
                                        ) {
                                            DropdownMenuItem(
                                                onClick = {
                                                    expanded = false
                                                    coroutineScope.launch {
                                                        reportRepository.deleteReportByIdAndType(
                                                            report.id.toLong(),
                                                            report.type
                                                        )
                                                        reportRepository.deleteReportByFormIdAndType(
                                                            report.id.toLong(),
                                                            report.type
                                                        )
                                                        val refreshedResponse =
                                                            bioReportService.getAllReportsBiomonitorID(
                                                                biomonitorId
                                                            )
                                                        if (refreshedResponse.isSuccessful) {
                                                            reports = refreshedResponse.body()
                                                                ?: emptyList()
                                                            localReports =
                                                                bioReportDao.getReportsByBiomonitorId(
                                                                    biomonitorId
                                                                )
                                                            combinedReports =
                                                                reports + localReports.map { localReport ->
                                                                    BioReport(
                                                                        id = localReport.formId.toInt(),
                                                                        biomonitor_id = localReport.biomonitor_id,
                                                                        status = localReport.status,
                                                                        date = localReport.date,
                                                                        type = localReport.type
                                                                    )
                                                                }
                                                            filterReportsByTab(selectedTab)
                                                        }
                                                    }
                                                }
                                            ) {
                                                Row(verticalAlignment = Alignment.CenterVertically) {
                                                    Icon(
                                                        imageVector = Icons.Default.Delete, // Usamos el ícono predeterminado de basura
                                                        contentDescription = "Borrar",
                                                        modifier = Modifier.size(20.dp)
                                                    )
                                                    Spacer(modifier = Modifier.width(8.dp))
                                                    Text("Borrar")
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                Text("No reports found.", modifier = Modifier.align(Alignment.CenterHorizontally))
            }

            errorMessage?.let {
                Text(it, color = MaterialTheme.colors.error, modifier = Modifier.padding(16.dp))
            }

            successMessage?.let {
                Text(it, color = MaterialTheme.colors.primary, modifier = Modifier.padding(16.dp))
            }
        }
    }
}


