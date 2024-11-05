package com.example.devkots.uiLib.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.devkots.data.BioReportService
import com.example.devkots.model.BioReport
import com.example.devkots.model.FaunaTransectoReport
import kotlinx.coroutines.launch

@Composable
fun ReportDetailScreen(
    navController: NavController,
    reportId: Int,
    bioReportService: BioReportService
) {
    val coroutineScope = rememberCoroutineScope()
    var report by remember { mutableStateOf<FaunaTransectoReport?>(null) }
    var isEditable by remember { mutableStateOf(false) }
    var loading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Fetch report details
    LaunchedEffect(Unit) {
        coroutineScope.launch {
            val response = bioReportService.getFaunaTransectoReport(reportId)
            if (response.isSuccessful) {
                report = response.body()
                isEditable = report?.status == false // Allow editing if status is false
                loading = false
            } else {
                errorMessage = "Failed to load report details."
                loading = false
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Report Details") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                backgroundColor = Color(0xFF4E7029),
                contentColor = Color.White
            )
        }
    ) { paddingValues ->
        if (loading) {
            CircularProgressIndicator(modifier = Modifier.fillMaxSize())
        } else if (report != null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
                    .background(Color(0xFFD6E5CC)) // Background color for detail screen
                    .verticalScroll(rememberScrollState()), // Enable scrolling
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text("Report ID: $reportId", fontSize = 24.sp, color = Color(0xFF4E7029))
                Spacer(modifier = Modifier.height(8.dp))

                EditableField("Transecto Number", report!!.transectoNumber.toString(), isEditable) {
                    report = report?.copy(transectoNumber = it.toIntOrNull() ?: report!!.transectoNumber)
                }

                EditableField("Animal Type", report!!.animalType, isEditable) {
                    report = report?.copy(animalType = it)
                }

                EditableField("Common Name", report!!.commonName, isEditable) {
                    report = report?.copy(commonName = it)
                }

                EditableField("Scientific Name", report!!.scientificName ?: "", isEditable) {
                    report = report?.copy(scientificName = it.takeIf { it.isNotEmpty() })
                }

                EditableField("Individual Count", report!!.individualCount.toString(), isEditable) {
                    report = report?.copy(individualCount = it.toIntOrNull() ?: report!!.individualCount)
                }

                EditableField("Observation Type", report!!.observationType, isEditable) {
                    report = report?.copy(observationType = it)
                }

                EditableField("Weather", report!!.weather, isEditable) {
                    report = report?.copy(weather = it)
                }

                EditableField("Season", report!!.season, isEditable) {
                    report = report?.copy(season = it)
                }

                EditableField("GPS Location", report!!.gpsLocation, isEditable) {
                    report = report?.copy(gpsLocation = it)
                }

                EditableField("Observations", report!!.observations, isEditable) {
                    report = report?.copy(observations = it)
                }

                Spacer(modifier = Modifier.height(16.dp))

                if (isEditable) {
                    Button(
                        onClick = {
                            coroutineScope.launch {
                                val updateResponse = bioReportService.updateFaunaTransectoReport(reportId, report!!)
                                if (updateResponse.isSuccessful) {
                                    navController.popBackStack() // Go back after successful edit
                                } else {
                                    errorMessage = "Failed to update report."
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF4E7029))
                    ) {
                        Text("Save Changes", color = Color.White, fontSize = 18.sp)
                    }
                }

                errorMessage?.let {
                    Text(it, color = MaterialTheme.colors.error)
                }
            }
        }
    }
}

@Composable
fun EditableField(
    label: String,
    value: String,
    isEditable: Boolean,
    onValueChange: (String) -> Unit
) {
    Column {
        Text(label, color = Color(0xFF4E7029), fontSize = 16.sp)
        if (isEditable) {
            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color(0xFFA5BE00),
                    unfocusedBorderColor = Color(0xFF6A994E)
                )
            )
        } else {
            Text(
                text = value,
                fontSize = 16.sp,
                color = Color(0xFF6A994E),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            )
        }
    }
}