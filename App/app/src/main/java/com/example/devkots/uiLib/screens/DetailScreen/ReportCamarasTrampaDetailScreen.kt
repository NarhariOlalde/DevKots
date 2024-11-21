package com.example.devkots.uiLib.screens.DetailScreen

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Checkbox
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
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.devkots.R
import com.example.devkots.data.BioReportService
import com.example.devkots.uiLib.components.EditableField
import com.example.devkots.uiLib.components.EditableFieldNumeric
import com.example.devkots.uiLib.viewmodels.Report.ReportCamarasTrampaViewModel
import com.example.devkots.uiLib.viewmodels.ReportViewModelFactory

@Composable
fun ReportCamarasTrampaDetailScreen(
    navController: NavController,
    reportId: Int,
    bioReportService: BioReportService
) {
    val viewModel: ReportCamarasTrampaViewModel = viewModel(
        factory = ReportViewModelFactory(bioReportService)
    )
    val listachequeo = listOf("Programada", "Memoria", "Prueba de gateo", "Instalada", "Letrero de cámara", "Prendida")

    // Estado para los elementos seleccionados
    val selectedItems = remember { mutableStateListOf<String>() }

    // Cargar datos del reporte al inicio
    LaunchedEffect(Unit) {
        viewModel.loadReport(reportId)
    }

    // Sincronizar selectedItems con los valores del reporte cuando cambie
    LaunchedEffect(viewModel.report) {
        viewModel.report?.listachequeo?.let { items ->
            selectedItems.clear()
            selectedItems.addAll(items)
        }
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

                EditableField("Código", viewModel.report!!.code, viewModel.isEditable) {
                    viewModel.report = viewModel.report?.copy(code = it)
                }

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
                                }
                            )
                            Text(
                                text = type,
                            )
                        }
                    }
                }

                EditableField("Nombre Cámara", viewModel.report!!.nombrecamara, viewModel.isEditable) {
                    viewModel.report = viewModel.report?.copy(nombrecamara = it)
                }

                EditableField("Placa Cámara", viewModel.report!!.placacamara, viewModel.isEditable) {
                    viewModel.report = viewModel.report?.copy(placacamara = it)
                }

                EditableField("Placa Guaya", viewModel.report!!.placaguaya, viewModel.isEditable) {
                    viewModel.report = viewModel.report?.copy(placaguaya = it)
                }

                EditableFieldNumeric("Ancho del Camino en mts", viewModel.report!!.anchocamino.toString(), viewModel.isEditable) {
                    viewModel.report = viewModel.report?.copy(anchocamino = it.toIntOrNull() ?: viewModel.report!!.anchocamino)
                }

                EditableField("Fecha de Instalación", viewModel.report!!.fechainstalacion, viewModel.isEditable) {
                    viewModel.report = viewModel.report?.copy(fechainstalacion = it)
                }

                EditableFieldNumeric("Distancia al objetivo en mts", viewModel.report!!.distancia.toString(), viewModel.isEditable) {
                    viewModel.report = viewModel.report?.copy(distancia = it.toIntOrNull() ?: viewModel.report!!.distancia)
                }

                EditableFieldNumeric("Altura del lente en mts", viewModel.report!!.altura.toString(), viewModel.isEditable) {
                    viewModel.report = viewModel.report?.copy(altura = it.toIntOrNull() ?: viewModel.report!!.altura)
                }

                Text(
                    text = "Lista de chequeo",
                    fontSize = 24.sp,
                )
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    listachequeo.forEach { item ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                        ) {
                            Checkbox(
                                checked = selectedItems.contains(item),
                                onCheckedChange = { isChecked ->
                                    if (isChecked) {
                                        if (!selectedItems.contains(item)) {
                                            selectedItems.add(item)
                                        }
                                    } else {
                                        selectedItems.remove(item)
                                    }
                                }
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = item,
                                color = Color.Black
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                if (viewModel.isEditable) {
                    Button(
                        onClick = {
                            viewModel.report = viewModel.report?.copy(listachequeo = selectedItems)
                            viewModel.updateReport(reportId, viewModel.report!!)
                            navController.popBackStack()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF4E7029))
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