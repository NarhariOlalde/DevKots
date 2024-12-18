package com.example.devkots.uiLib.screens.DetailScreen

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.RadioButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.devkots.R
import com.example.devkots.data.AppDatabase
import com.example.devkots.data.BioReportService
import com.example.devkots.uiLib.components.EditableField
import com.example.devkots.uiLib.components.EditableFieldNumeric
import com.example.devkots.uiLib.viewmodels.Report.ReportVariablesClimaticasViewModel
import com.example.devkots.uiLib.viewmodels.ReportViewModelFactory

@Composable
fun ReportVariablesClimaticasDetailScreen(
    navController: NavController,
    status: Boolean,
    reportId: Int,
    bioReportService: BioReportService
) {
    val context = LocalContext.current
    val database = remember { AppDatabase.getInstance(context) }
    val variablesClimaticasReportDao = database.variablesClimaticasReportDao()

    val viewModel: ReportVariablesClimaticasViewModel = viewModel(
        factory = ReportViewModelFactory(
            bioReportService = bioReportService,
            variablesClimaticasReportDao = variablesClimaticasReportDao
        )
    )

    var submissionResult by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(reportId, status) {
        viewModel.loadReport(reportId, status)
    }

    // Función para verificar si todos los campos obligatorios están completos
    fun isFormValid(): Boolean {
        val report = viewModel.report
        return report != null &&
                report.zona.isNotEmpty() &&
                report.pluviosidad > 0 &&
                report.tempmax > 0 &&
                report.tempmin > 0 &&
                report.humedadmax > 0 &&
                report.nivelquebrada > 0 &&
                report.weather.isNotEmpty() &&
                report.season.isNotEmpty()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalles del Formulario") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                modifier = Modifier.height(100.dp),
                backgroundColor = Color(0xFF4E7029),
                contentColor = Color.White
            )
        }
    ) { paddingValues ->
        if (viewModel.loading) {
            CircularProgressIndicator(modifier = Modifier.fillMaxSize())
        } else if (viewModel.report != null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text("#FM$reportId", fontSize = 32.sp, color = Color(0xFF4E7029))

                Text(
                    text = "Zona",
                    fontSize = 24.sp
                )

                Column {
                    val zoneTypes = listOf("Bosque", "Arreglo Forestal", "Cultivos Transitorios", "Cultivos Permanentes")
                    zoneTypes.forEach { type ->
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            RadioButton(
                                selected = viewModel.report!!.zona == type,
                                onClick = {
                                    viewModel.report = viewModel.report?.copy(zona = type)
                                },
                                enabled = viewModel.isEditable
                            )
                            Text(
                                text = type,
                            )
                        }
                    }
                }

                EditableFieldNumeric("Pluviosidad en mm", viewModel.report!!.pluviosidad, viewModel.isEditable) {
                    viewModel.report = viewModel.report?.copy(pluviosidad = it)
                }

                EditableFieldNumeric("Temperatura máxima", viewModel.report!!.tempmax, viewModel.isEditable) {
                    viewModel.report = viewModel.report?.copy(tempmax = it)
                }

                EditableFieldNumeric("Humedad máxima", viewModel.report!!.humedadmax, viewModel.isEditable) {
                    viewModel.report = viewModel.report?.copy(humedadmax = it)
                }

                EditableFieldNumeric("Temperatura mínima", viewModel.report!!.tempmin, viewModel.isEditable) {
                    viewModel.report = viewModel.report?.copy(tempmin = it)
                }

                EditableFieldNumeric("Nivel de Quebrada en mts", viewModel.report!!.nivelquebrada, viewModel.isEditable) {
                    viewModel.report = viewModel.report?.copy(nivelquebrada = it)
                }

                Spacer(modifier = Modifier.height(16.dp))

                if (viewModel.isEditable) {
                    Button(
                        onClick = {
                            viewModel.updateReport(reportId, viewModel.report!!)
                            navController.popBackStack() // Go back after successful edit
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF4E7029)),
                        enabled = isFormValid() && viewModel.isEditable
                    ) {
                        Text("Save Changes", color = Color.White, fontSize = 18.sp)
                    }
                }

                viewModel.errorMessage?.let {
                    Text(it, color = MaterialTheme.colors.error)
                }
            }
        }
    }
}