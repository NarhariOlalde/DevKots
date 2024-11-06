package com.example.devkots.uiLib.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.devkots.data.BioReportService
import com.example.devkots.data.RetrofitInstanceBioReport
import com.example.devkots.model.BioReport
import com.example.devkots.uiLib.components.MainLayout
import com.example.devkots.uiLib.viewmodels.BioReportViewModel
import com.example.devkots.uiLib.theme.ObjectGreen2
import com.example.devkots.uiLib.theme.ObjectGreen1
import com.example.devkots.uiLib.theme.ObjectGreen5
import kotlinx.coroutines.launch

@Composable
fun SearchScreen(
    navController: NavController,
    bioReportService: BioReportService,
    biomonitorId: String // Current user's biomonitor ID
) {
    val coroutineScope = rememberCoroutineScope()
    var reports by remember { mutableStateOf<List<BioReport>>(emptyList()) }
    var filteredReports by remember { mutableStateOf<List<BioReport>>(emptyList()) }
    var selectedTab by remember { mutableStateOf(0) }
    var loading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var successMessage by remember { mutableStateOf<String?>(null) }

    // Define filterReportsByTab as a lambda or function within SearchScreen
    val filterReportsByTab: (Int) -> Unit = { tabIndex ->
        filteredReports = when (tabIndex) {
            0 -> reports // Todos: Show all reports
            1 -> reports.filter { !it.status } // Guardados: Show only reports with status == false
            2 -> reports.filter { it.status } // Subidos: Show only reports with status == true
            else -> reports
        }
    }

    // Fetch reports on launch
    LaunchedEffect(Unit) {
        coroutineScope.launch {
            val response = bioReportService.getAllReportsBiomonitorID(biomonitorId)
            println("Biomonitor ID: $biomonitorId")
            println("Response: $response")
            if (response.isSuccessful) {
                reports = response.body() ?: emptyList()
                println("Reports: $reports")
                filterReportsByTab(selectedTab)
                loading = false
            } else {
                errorMessage = "Failed to load reports."
                loading = false
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Search Reports") },
                actions = {
                    IconButton(onClick = {
                        coroutineScope.launch {
                            val reportsToUpdate = reports.filter { !it.status && it.biomonitor_id == biomonitorId }
                            var allSuccessful = true

                            // Update each report individually
                            for (report in reportsToUpdate) {
                                val response = bioReportService.updateReportStatusById(
                                    id = report.id,
                                    statusUpdate = mapOf("status" to true)
                                )
                                if (!response.isSuccessful) {
                                    allSuccessful = false
                                    errorMessage = "Failed to update report with ID: ${report.id}"
                                    break
                                }
                            }

                            // Refresh reports if all updates succeeded
                            if (allSuccessful) {
                                val refreshedResponse = bioReportService.getAllReportsBiomonitorID(biomonitorId)
                                if (refreshedResponse.isSuccessful) {
                                    reports = refreshedResponse.body() ?: emptyList()
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
                        Text("Upload All", color = MaterialTheme.colors.onPrimary)
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
                Column(modifier = Modifier
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
                ) {
                    filteredReports.forEach { report ->

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                                .clickable {
                                    navController.navigate("report_detail/${report.id}")
                                },
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text("Report ID: ${report.id}", fontSize = 16.sp)
                                Text("Type: ${report.type}", fontSize = 14.sp)
                                Text("Date: ${report.date}", fontSize = 12.sp)
                                Text("Status: ${if (report.status) "Uploaded" else "Pending"}", fontSize = 12.sp)
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