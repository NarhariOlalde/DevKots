package com.example.devkots.uiLib.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.devkots.uiLib.components.MainLayout
import com.example.devkots.uiLib.theme.ObjectGreen2

@Composable
fun ReportSelectionScreen(navController: NavController) {
    // State variables for dropdown selections
    var selectedWeather by remember { mutableStateOf("") }
    var selectedSeason by remember { mutableStateOf("") }
    var selectedType by remember { mutableStateOf("") }

    // Options for each selection
    val weatherOptions = listOf("Soleado", "Nublado", "Lluvioso")
    val seasonOptions = listOf("Verano", "Invierno", "Otoño", "Primavera")
    val typeOptions = listOf(
        "Fauna en Transecto",
        "Fauna en Punto de Conteo",
        "Fauna Busqueda Libre",
        "Validacion de Cobertura",
        "Parcela de Vegetacion",
        "Camaras Trampa",
        "Variables Climaticas"
    )

    MainLayout(navController = navController) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Report Selection", fontSize = 24.sp)

            // Weather Dropdown
            DropdownMenuSelector(
                label = "Select Weather",
                options = weatherOptions,
                selectedOption = selectedWeather,
                onOptionSelected = { selectedWeather = it }
            )

            // Season Dropdown
            DropdownMenuSelector(
                label = "Select Season",
                options = seasonOptions,
                selectedOption = selectedSeason,
                onOptionSelected = { selectedSeason = it }
            )

            // Type Dropdown
            DropdownMenuSelector(
                label = "Select Type",
                options = typeOptions,
                selectedOption = selectedType,
                onOptionSelected = { selectedType = it }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Next Button
            Button(
                onClick = {
                    when (selectedType) {
                        "Fauna en Transecto" -> navController.navigate("fauna_transect_form")
                        "Fauna en Punto de Conteo" -> navController.navigate("fauna_point_count_form")
                        "Fauna Busqueda Libre" -> navController.navigate("fauna_free_search_form")
                        "Validacion de Cobertura" -> navController.navigate("coverage_validation_form")
                        "Parcela de Vegetacion" -> navController.navigate("vegetation_plot_form")
                        "Camaras Trampa" -> navController.navigate("trap_cameras_form")
                        "Variables Climaticas" -> navController.navigate("climatic_variables_form")
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = selectedWeather.isNotEmpty() && selectedSeason.isNotEmpty() && selectedType.isNotEmpty(),
                colors = ButtonDefaults.buttonColors(backgroundColor = ObjectGreen2)
            ) {
                Text("Next", color = MaterialTheme.colors.onPrimary)
            }
        }
    }
}

@Composable
fun DropdownMenuSelector(
    label: String,
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) } // Controls the dropdown visibility

    Box(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = selectedOption,
            onValueChange = {}, // No direct change needed here since it's controlled via `onOptionSelected`
            label = { Text(label) },
            readOnly = true,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = true } // Expand dropdown when clicked
        )

        // DropdownMenu shown when `expanded` is true
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }, // Collapse the dropdown when dismissed
            modifier = Modifier.fillMaxWidth()
        ) {
            options.forEach { option ->
                DropdownMenuItem(onClick = {
                    onOptionSelected(option) // Update selected option
                    expanded = false // Collapse dropdown after selection
                }) {
                    Text(option)
                }
            }
        }
    }
}