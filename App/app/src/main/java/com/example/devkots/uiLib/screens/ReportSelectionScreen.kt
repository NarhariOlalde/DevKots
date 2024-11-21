package com.example.devkots.uiLib.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.devkots.R
import com.example.devkots.uiLib.components.FormLayout
import com.example.devkots.uiLib.components.MainLayout
import com.example.devkots.uiLib.theme.IntroGreen
import com.example.devkots.uiLib.theme.ObjectGreen2

@Composable
fun ReportSelectionScreen(navController: NavController,
                          modifier: Modifier = Modifier) {
    // State variables for dropdown selections
    var selectedWeather by remember { mutableStateOf("") }
    var selectedSeason by remember { mutableStateOf("") }
    var selectedType by remember { mutableStateOf("") }

    // Options for each selection
    val typeOptions = listOf(
        "Fauna en Transecto",
        "Fauna en Punto de Conteo",
        "Fauna Búsqueda Libre",
        "Validación de Cobertura",
        "Parcela de Vegetación",
        "Cámaras Trampa",
        "Variables Climáticas"
    )
    FormLayout(navController = navController) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(IntroGreen)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Text(
                    text = "Estado del tiempo:",
                    fontSize = 35.sp,
                    color = colorResource(id = R.color.black)
                )
            }

            Spacer(modifier = Modifier.height(15.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Image(
                    painter = painterResource(id = R.drawable.sunny),
                    contentDescription = "Soleado",
                    modifier = Modifier
                        .size(150.dp)
                        .background(
                            if (selectedWeather == "Soleado") Color(0xFF99CC66) else Color.Transparent,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .clickable { selectedWeather = "Soleado" }
                        .padding(8.dp)
                )

                Image(
                    painter = painterResource(id = R.drawable.cloudy),
                    contentDescription = "Nublado",
                    modifier = Modifier
                        .size(150.dp)
                        .background(
                            if (selectedWeather == "Nublado") Color(0xFF99CC66) else Color.Transparent,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .clickable { selectedWeather = "Nublado" }
                        .padding(8.dp)
                )

                Image(
                    painter = painterResource(id = R.drawable.rainy),
                    contentDescription = "Lluvioso",
                    modifier = Modifier
                        .size(150.dp)
                        .background(
                            if (selectedWeather == "Lluvioso") Color(0xFF99CC66) else Color.Transparent,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .clickable { selectedWeather = "Lluvioso" }
                        .padding(8.dp)
                )
            }

            Spacer(modifier = Modifier.height(45.dp))

            Text(
                text = "Selecciona la estación:",
                fontSize = 35.sp,
                modifier = Modifier.padding(start = 16.dp),
                color = colorResource(id = R.color.black)
            )

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = selectedSeason == "Verano-Seco",
                        onClick = { selectedSeason = "Verano-Seco" }
                    )
                    Text(
                        text = "Verano/Seco",
                        modifier = Modifier.padding(start = 8.dp),
                        fontSize = 28.sp
                    )
                }

                Spacer(modifier = Modifier.width(32.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = selectedSeason == "Invierno-Lluviosa",
                        onClick = { selectedSeason = "Invierno-Lluviosa" }
                    )
                    Text(
                        text = "Invierno/Lluviosa",
                        modifier = Modifier.padding(start = 8.dp),
                        fontSize = 28.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(45.dp))

            Text(
                text = "Tipo de registro:",
                fontSize = 35.sp,
                modifier = Modifier.padding(start = 16.dp),
                color = colorResource(id = R.color.black)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                typeOptions.forEach { type ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = selectedType == type,
                            onClick = { selectedType = type }
                        )
                        Text(
                            text = type,
                            fontSize = 28.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }

            Spacer(modifier = Modifier.height(25.dp))

            // Next Button
            Button(
                onClick = {
                    when (selectedType) {
                        "Fauna en Transecto" -> navController.navigate("fauna_transect_form/$selectedWeather/$selectedSeason")
                        "Fauna en Punto de Conteo" -> navController.navigate("fauna_point_count_form/$selectedWeather/$selectedSeason")
                        "Fauna Búsqueda Libre" -> navController.navigate("fauna_free_search_form/$selectedWeather/$selectedSeason")
                        "Validación de Cobertura" -> navController.navigate("coverage_validation_form/$selectedWeather/$selectedSeason")
                        "Parcela de Vegetación" -> navController.navigate("vegetation_plot_form/$selectedWeather/$selectedSeason")
                        "Cámaras Trampa" -> navController.navigate("trap_cameras_form/$selectedWeather/$selectedSeason")
                        "Variables Climáticas" -> navController.navigate("climatic_variables_form/$selectedWeather/$selectedSeason")
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .height(60.dp),
                enabled = selectedWeather.isNotEmpty() && selectedSeason.isNotEmpty() && selectedType.isNotEmpty(),
                colors = ButtonDefaults.buttonColors(backgroundColor = ObjectGreen2)
            ) {
                Text(
                    text = "Siguiente",
                    fontSize = 28.sp,
                    color = Color.White
                )
            }
            Spacer(modifier = Modifier.height(25.dp))
        }
    }

}
