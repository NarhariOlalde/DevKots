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
import com.example.devkots.uiLib.viewmodels.Report.ReportFaunaBusquedaLibreViewModel
import com.example.devkots.uiLib.viewmodels.ReportViewModelFactory

@Composable
fun ReportFaunaBusquedaLibreDetailScreen(
    navController: NavController,
    reportId: Int,
    bioReportService: BioReportService
) {
    val viewModel: ReportFaunaBusquedaLibreViewModel = viewModel(
        factory = ReportViewModelFactory(bioReportService)
    )

    LaunchedEffect(Unit) {
        viewModel.loadReport(reportId)
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
                                selected = viewModel.report!!.zone == type,
                                onClick = {
                                    viewModel.report = viewModel.report?.copy(zone = type)
                                }
                            )
                            Text(
                                text = type,
                            )
                        }
                    }
                }

                Text(
                    text = "Tipo de Animal",
                    fontSize = 24.sp,
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    val animals = listOf(
                        R.drawable.mamifero to "Mamífero",
                        R.drawable.ave to "Ave",
                        R.drawable.reptil to "Reptil",
                        R.drawable.anfibio to "Anfibio",
                        R.drawable.insecto to "Insecto"
                    )

                    animals.forEachIndexed { index, animal ->
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.clickable {
                                viewModel.report = viewModel.report?.copy(animalType = animal.second)
                            }
                        ) {
                            Image(
                                painter = painterResource(id = animal.first),
                                contentDescription = animal.second,
                                modifier = Modifier
                                    .size(90.dp)
                                    .background(
                                        if (viewModel.report!!.animalType == animal.second) Color(0xFF99CC66) else Color.Transparent,
                                        shape = RoundedCornerShape(8.dp)
                                    )
                                    .padding(8.dp)
                            )
                            Text(
                                text = animal.second,
                                fontSize = 18.sp,
                                color = Color.Black
                            )
                        }
                    }
                }


                EditableField("Nombre Común", viewModel.report!!.commonName, viewModel.isEditable) {
                    viewModel.report = viewModel.report?.copy(commonName = it)
                }

                EditableField("Nombre Científico", viewModel.report!!.scientificName ?: "", viewModel.isEditable) {
                    viewModel.report = viewModel.report?.copy(scientificName = it.takeIf { it.isNotEmpty() })
                }

                EditableFieldNumeric("Número de Individuos", viewModel.report!!.individualCount.toString(), viewModel.isEditable) {
                    viewModel.report = viewModel.report?.copy(individualCount = it.toIntOrNull() ?: viewModel.report!!.individualCount)
                }
                Text(
                    text = "Tipo de Registro",
                    fontSize = 24.sp,
                )
                Column {
                    val observationTypes = listOf("La Vió", "Huella", "Rastro", "Cacería", "Le dijeron")
                    observationTypes.forEach { type ->
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            RadioButton(
                                selected = viewModel.report!!.observationType == type,
                                onClick = {
                                    viewModel.report = viewModel.report?.copy(observationType = type)
                                }
                            )
                            Text(
                                text = type,
                            )
                        }
                    }
                }

                Text(
                    text = "Altura de Observación",
                    fontSize = 24.sp,
                )

                Column {
                    val heightTypes = listOf("Baja <1mt", "Media 1-3mt", "Alta >3mt")
                    heightTypes.forEach { type ->
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            RadioButton(
                                selected = viewModel.report!!.observationHeight == type,
                                onClick = {
                                    viewModel.report = viewModel.report?.copy(observationHeight = type)
                                }
                            )
                            Text(
                                text = type,
                            )
                        }
                    }
                }

                Text(
                    text = "Estado del Tiempo",
                    fontSize = 24.sp,
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    val weatherTypes = listOf(
                        "Soleado" to R.drawable.sunny,
                        "Nublado" to R.drawable.cloudy,
                        "Lluvioso" to R.drawable.rainy
                    )

                    weatherTypes.forEach { (weatherType, iconResId) ->
                        Image(
                            painter = painterResource(id = iconResId),
                            contentDescription = weatherType,
                            modifier = Modifier
                                .size(90.dp)
                                .background(
                                    if (viewModel.report!!.weather == weatherType) Color(0xFF99CC66) else Color.Transparent,
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .clickable {
                                    viewModel.report = viewModel.report?.copy(weather = weatherType)
                                }
                                .padding(8.dp)
                        )
                    }
                }
                Text(
                    text = "Estación",
                    fontSize = 24.sp,
                )
                Column {
                    val seasons = listOf("Verano-Seco", "Invierno-Lluviosa")

                    seasons.forEach { season ->
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            RadioButton(
                                selected = viewModel.report!!.season == season,
                                onClick = {
                                    viewModel.report = viewModel.report?.copy(season = season)
                                }
                            )
                            Text(
                                text = season
                            )
                        }
                    }
                }


                EditableField("Observaciones", viewModel.report!!.observations, viewModel.isEditable) {
                    viewModel.report = viewModel.report?.copy(observations = it)
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